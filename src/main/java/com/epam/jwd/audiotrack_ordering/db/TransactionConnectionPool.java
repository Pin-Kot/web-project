package com.epam.jwd.audiotrack_ordering.db;

import com.epam.jwd.audiotrack_ordering.exception.CouldNotInitializeConnectionPoolError;

import java.sql.Connection;
import java.util.Optional;

public final class TransactionConnectionPool implements ConnectionPool {

    private final ConnectionPool connectionPool;

    private final TransactionManager transactionManager;

    private TransactionConnectionPool(ConnectionPool connectionPool, TransactionManager transactionManager) {
        this.connectionPool = connectionPool;
        this.transactionManager = transactionManager;
    }

    @Override
    public boolean isInitialized() {
        return connectionPool.isInitialized();
    }

    @Override
    public boolean init() throws CouldNotInitializeConnectionPoolError {
        return connectionPool.init();
    }

    @Override
    public boolean shutDown() {
        return connectionPool.shutDown();
    }

    @Override
    public Connection takeConnection() throws InterruptedException {
        final Optional<TransactionId> transactionId = transactionManager.getTransactionId();
        return transactionId.isPresent()
                ? transactionId.get().getConnection()
                : new ProxyConnection(connectionPool.takeConnection(), this);
    }

    @Override
    public void returnConnection(Connection connection) {
        if (!transactionManager.getTransactionId().isPresent()) {
             connectionPool.returnConnection(((ProxyConnection) connection).getConnection());
        }
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    static ConnectionPool getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ConnectionPool INSTANCE =
                new TransactionConnectionPool(LockingConnectionPool.getInstance(),
                TransactionManager.getInstance());
    }
}
