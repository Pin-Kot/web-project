package com.epam.jwd.audiotrack_ordering.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
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

    private static final int INITIAL_CONNECTIONS_AMOUNT = 8;
    private final Queue<ProxyConnection> availableConnections = new ArrayDeque<>();
    private final List<ProxyConnection> givenAwayConnections = new ArrayList<>();

    private boolean initialized = false;

    private static LockingConnectionPool instance = null;
    private static final ReentrantLock Lock = new ReentrantLock();

    private LockingConnectionPool(){}

    public static LockingConnectionPool getInstance() {
        if (instance == null) {
            try {
                Lock.lock();
                    if (instance == null) {
                        instance = new LockingConnectionPool();
                    }
                } finally {
                    Lock.unlock();
                }
            }
        return instance;
    }

    @Override
    public boolean isInitialized() {
        Lock.lock();
        try {
            return initialized;
        } finally {
            Lock.unlock();
        }
    }

    @Override
    public boolean init() {
        Lock.lock();
        try {
            if (!initialized) {
                registerDrivers();
                initializedConnections(INITIAL_CONNECTIONS_AMOUNT, true);
                initialized = true;
                return true;
            }
        } finally {
            Lock.unlock();
        }
        return false;
    }

    @Override
    public boolean shutDown() {
        Lock.lock();
        try {
            if (initialized) {
                closeConnections();
                deregisterDrivers();
                initialized = false;
                return true;
            }
        } finally {
            Lock.unlock();
        }
        return false;
    }

    @Override
    public Connection takeConnection() throws InterruptedException {
        Lock.lock();
        try {
            while (availableConnections.isEmpty()) {
                this.wait();
            }
            final ProxyConnection connection = availableConnections.poll();
            givenAwayConnections.add(connection);
            return connection;
        } finally {
            Lock.unlock();
        }
    }

    @Override
    @SuppressWarnings("SuspiciousMethodCalls")
    public void returnConnection(Connection connection) {
        try {
            Lock.lock();
            if (givenAwayConnections.remove(connection)) {
                availableConnections.add((ProxyConnection) connection);
            } else {
                LOG.warn("Attempt to add unknown connection to Connection Pool. Connection {}", connection);
            }
        } finally {
            Lock.unlock();
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

    private Properties receiveProperties()  {
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
