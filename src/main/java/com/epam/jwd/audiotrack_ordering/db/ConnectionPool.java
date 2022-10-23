package com.epam.jwd.audiotrack_ordering.db;

import com.epam.jwd.audiotrack_ordering.exception.CouldNotInitializeConnectionPoolError;

import java.sql.Connection;

public interface ConnectionPool {

    boolean isInitialized();

    boolean init() throws CouldNotInitializeConnectionPoolError;

    boolean shutDown();

    Connection takeConnection() throws InterruptedException;

    void returnConnection(Connection connection);

    static ConnectionPool locking() {
        return LockingConnectionPool.getInstance();
    }
}
