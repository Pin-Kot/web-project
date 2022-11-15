package com.epam.jwd.audiotrack_ordering.db;

import java.util.Optional;

public interface TransactionManager {

    void initTransaction();

    void commitTransaction();

    boolean isTransactionActive();

    Optional<TransactionId> getTransactionId();

    static TransactionManager getInstance() {
        return ThreadLocalTransactionManager.getInstance();
    }
}
