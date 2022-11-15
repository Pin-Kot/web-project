package com.epam.jwd.audiotrack_ordering.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadLocalTransactionManager implements TransactionManager {

    private static final Logger LOG = LogManager.getLogger(ThreadLocalTransactionManager.class);

    private static final ReentrantLock LOCK = new ReentrantLock();

    private ThreadLocalTransactionManager() {
    }

    private static final ThreadLocal<TransactionId> THREAD_CONNECTION = ThreadLocal.withInitial(() -> {
        try {
            return new SimpleTransactionId(new ProxyConnection(ConnectionPool.lockingInstance().takeConnection(),
                    ConnectionPool.lockingInstance()));
        } catch (InterruptedException e) {
            LOG.warn("Thread was interrupted", e);
            Thread.currentThread().interrupt();
            return null;
        }
    });

    @Override
    public void initTransaction() {
        LOCK.lock();
        try {
            final Connection connection = THREAD_CONNECTION.get().getConnection();
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            LOG.error("SQL exc occurred trying to initialize transaction", e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public void commitTransaction() {
        LOCK.lock();
        try {
            final Connection connection = THREAD_CONNECTION.get().getConnection();
            if (!connection.getAutoCommit()) {
                connection.commit();
                connection.setAutoCommit(true);
            }
            THREAD_CONNECTION.remove();
            connection.close();
        } catch (SQLException e) {
            LOG.error("SQL exc occurred committing transaction", e);
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public boolean isTransactionActive() {
        LOCK.lock();
        try {
            final Connection connection = THREAD_CONNECTION.get().getConnection();
            final boolean transactionActive = !connection.getAutoCommit();
            if (!transactionActive) {
                THREAD_CONNECTION.remove();
                connection.close();
            }
            return transactionActive;
        } catch (SQLException e) {
            LOG.error("SQL exc occurred trying to check transaction status", e);
            return false;
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public Optional<TransactionId> getTransactionId() {
        LOCK.lock();
        try {
            final TransactionId transactionId = THREAD_CONNECTION.get();
            if (transactionId.getConnection().getAutoCommit()) {
                THREAD_CONNECTION.remove();
                ((ProxyConnection) transactionId.getConnection()).getConnection().close();
                return Optional.empty();
            }
            return Optional.of(transactionId);
        } catch (SQLException e) {
            LOG.error("SQL exc occurred trying to get transaction id", e);
            return Optional.empty();
        } finally {
            LOCK.unlock();
        }
    }

    static TransactionManager getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final TransactionManager INSTANCE = new ThreadLocalTransactionManager();
    }
}
