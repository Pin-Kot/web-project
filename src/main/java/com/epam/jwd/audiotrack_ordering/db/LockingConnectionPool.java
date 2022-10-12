package com.epam.jwd.audiotrack_ordering.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import com.epam.jwd.audiotrack_ordering.exception.CouldNotInitializeConnectionPool;
import com.epam.jwd.audiotrack_ordering.exception.ResourceLoadingFailedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LockingConnectionPool implements ConnectionPool {

    private static final Logger LOG = LogManager.getLogger(LockingConnectionPool.class);

    public static final String PROPERTY_FILE_NAME = "connection.properties";
    public static final String DB_URL = "db.url";
    public static final String DB_USER = "db.user";
    public static final String DB_PASSWORD = "db.password";
    public static final String DB_INITIAL_POOL_SIZE = "db.initialPoolSize";
    public static final double THRESHOLD = 0.25;

    private final Queue<ProxyConnection> availableConnections = new ArrayDeque<>();
    private final List<ProxyConnection> givenAwayConnections = new ArrayList<>();

    private boolean initialized = false;

    private static LockingConnectionPool instance = null;
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Condition CONDITION = LOCK.newCondition();

    private LockingConnectionPool() {
    }

    public static LockingConnectionPool getInstance() {
        if (instance == null) {
            try {
                LOCK.lock();
                if (instance == null) {
                    instance = new LockingConnectionPool();
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }

    @Override
    public boolean isInitialized() {
        LOCK.lock();
        try {
            return initialized;
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public boolean init() {
        LOCK.lock();
        try {
            if (!initialized) {
                registerDrivers();
                initializedConnections(Integer.parseInt(paramFromProperties(receiveProperties(), DB_INITIAL_POOL_SIZE)),
                        true);
                initialized = true;
                return true;
            }
        } finally {
            LOCK.unlock();
        }
        return false;
    }

    @Override
    public boolean shutDown() {
        LOCK.lock();
        try {
            if (initialized) {
                closeConnections();
                deregisterDrivers();
                initialized = false;
                return true;
            }
        } finally {
            LOCK.unlock();
        }
        return false;
    }

    @Override
    public Connection takeConnection() throws InterruptedException {
        LOCK.lock();
        try {
            while (availableConnections.isEmpty()) {
                CONDITION.await();
            }
            final ProxyConnection connection = availableConnections.poll();
            LOG.trace("{} take connection...", Thread.currentThread().getName());
            LOG.trace("available connection size {}", availableConnections.size());
            givenAwayConnections.add(connection);
            LOG.trace("given away connection size {}", givenAwayConnections.size());
            LOG.trace("current size {}", obtainCurrentPoolSize());
            if (availableConnections.size() < THRESHOLD * obtainCurrentPoolSize()) {
                LOG.warn("need to initialize new connections");
                initializedConnections(Integer.parseInt(paramFromProperties(receiveProperties(),
                        DB_INITIAL_POOL_SIZE)), true);
            }
            return connection;
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public void returnConnection(Connection connection) {
        try {
            LOCK.lock();
            if (givenAwayConnections.remove(connection)) {
                availableConnections.add((ProxyConnection) connection);
                LOG.trace("{} return connection...", Thread.currentThread().getName());
                LOG.trace("current size {}", obtainCurrentPoolSize());
                if (availableConnections.size() > Integer.parseInt(paramFromProperties(receiveProperties(),
                        DB_INITIAL_POOL_SIZE))) {
                    LOG.warn("free connections found, extra connection will remove");
                    availableConnections.remove(connection);
                    LOG.trace("new current size {}", obtainCurrentPoolSize());
                }
            } else {
                LOG.warn("Attempt to add unknown connection to Connection Pool. Connection {}", connection);
            }
        } finally {
            LOCK.unlock();
        }
    }

    private void initializedConnections(int amount, boolean failOnConnectionException) {
        try {
            for (int i = 0; i < amount; i++) {
                final Connection connection = DriverManager.getConnection(paramFromProperties(receiveProperties(),
                        DB_URL),
                        paramFromProperties(receiveProperties(), DB_USER),
                        paramFromProperties(receiveProperties(), DB_PASSWORD));
                LOG.info("initialized connection {}", connection);
                final ProxyConnection proxyConnection = new ProxyConnection(connection, this);
                availableConnections.add(proxyConnection);
            }
        } catch (SQLException e) {
            LOG.error("Error occurred creating connection");
            if (failOnConnectionException) {
                throw new CouldNotInitializeConnectionPool("Failed to create connection", e);
            }
        }
    }

    private int obtainCurrentPoolSize() {
        return availableConnections.size() + givenAwayConnections.size();
    }

    private void closeConnections() {
        closeConnections(this.availableConnections);
        closeConnections(this.givenAwayConnections);
    }

    private void closeConnections(Collection<ProxyConnection> connections) {
        for (ProxyConnection connect : connections) {
            closeConnection(connect);
        }
    }

    private void closeConnection(ProxyConnection connection) {
        try {
            connection.realClose();
            LOG.info("closed connection {}", connection);
        } catch (SQLException e) {
            LOG.error("Could not close connection", e);
        }
    }

    private void registerDrivers() {
        LOG.trace("registering sql drivers");
        try {
            DriverManager.registerDriver(DriverManager.getDriver(paramFromProperties(receiveProperties(), DB_URL)));
        } catch (SQLException e) {
            LOG.error("could not register drivers", e);
            throw new CouldNotInitializeConnectionPool("Unsuccessful db driver registration attempt", e);
        }
    }

    private static void deregisterDrivers() {
        LOG.trace("unregistering sql drivers");
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                LOG.error("Could not deregister driver", e);
            }
        }
    }

    private Properties receiveProperties() {
        Properties prop = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
            prop.load(inputStream);
        } catch (IOException e) {
            LOG.error("property file '" + PROPERTY_FILE_NAME +
                    "' not found in the classpath");
            throw new ResourceLoadingFailedException("could not load the configurable parameters", e);
        }
        return prop;
    }

    private String paramFromProperties(Properties prop, String param) {
        return prop.getProperty(param);
    }

}
