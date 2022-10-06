package com.epam.jwd.audiotrack_ordering.db;

import com.epam.jwd.audiotrack_ordering.exception.CouldNotInitializeConnectionPool;

import java.sql.Connection;

public interface ConnectionPool {

    boolean isInitialized() throws CouldNotInitializeConnectionPool;

    boolean init();

    boolean shutDown();

    Connection takeConnection() throws InterruptedException;

    void returnConnection(Connection connection);

    static ConnectionPool locking() {
        return LockingConnectionPool.getInstance();
    }
}
