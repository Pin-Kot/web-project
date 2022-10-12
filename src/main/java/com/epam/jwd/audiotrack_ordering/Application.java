package com.epam.jwd.audiotrack_ordering;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.db.ResultSetExtractor;
import com.epam.jwd.audiotrack_ordering.db.StatementPreparer;
import com.epam.jwd.audiotrack_ordering.entity.Entity;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.exception.EntityExtractionFailedException;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {

    private static final Logger LOG = LogManager.getLogger(Application.class);

    private static final String SELECT_ALL_USR_SQL = "select id as id, first_name as f_name, last_name as l_name," +
            "email as email, birthday as birthday, discount as discount, account_id as account_id from user";
    private static final String FIND_USR_OLDER_THAN_SQL = "select id as id, first_name as f_name, last_name as l_name," +
            "email as email, birthday as birthday, discount as discount, account_id as account_id from user " +
            "where birthday < ?";
    private static final String ID_COLUMN_NAME = "id";
    private static final String FIRST_NAME_COLUMN_NAME = "f_name";
    private static final String LAST_NAME_COLUMN_NAME = "l_name";
    private static final String EMAIL_COLUMN_NAME = "email";
    private static final String BIRTHDAY_COLUMN_NAME = "birthday";
    private static final String DISCOUNT_COLUMN_NAME = "discount";
    private static final String ACCOUNT_ID_COLUMN_NAME = "account_id";

    private static final String NOT_FOUND_MSG = "could not extract User";

    public static void main(String[] args) {

        LOG.trace("Program start");

        Thread thread1 = new JThread("Thread1");
        Thread thread2 = new JThread("Thread2");
        Thread thread3 = new JThread("Thread3");
        Thread thread4 = new JThread("Thread4");
        Thread thread5 = new JThread("Thread5");
        Thread thread6 = new JThread("Thread6");
        Thread thread7 = new JThread("Thread7");
        Thread thread8 = new JThread("Thread8");
        Thread thread9 = new JThread("Thread9");
        Thread thread10 = new JThread("Thread10");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();
        thread10.start();

//        final ConnectionPool cp = ConnectionPool.locking();
//        cp.init();
//        final List<User> users;
//        final LocalDate date = LocalDate.of(1990, 6, 1);
//        try {
////            users = fetchUsersFromDb();
//            users = executePrepared(
//                    FIND_USR_OLDER_THAN_SQL,
//                    Application::extractUser,
//                    st -> st.setDate(1, Date.valueOf(date)));
//        } catch (InterruptedException e) {
//            LOG.info("interrupted fetching users, closing pool");
//            ConnectionPool.locking().shutDown();
//            return;
//        }
//        users.forEach(user -> LOG.info("found user {}", user));
//        cp.shutDown();

        LOG.trace("Program end");
    }

    private static List<User> fetchUsersOlderThan(LocalDate date) throws InterruptedException {
        return executePrepared(
                FIND_USR_OLDER_THAN_SQL,
                Application::extractUser,
                st -> st.setDate(1, Date.valueOf(date)));
    }

    private static List<User> fetchUsersFromDb() throws InterruptedException {
        return executeStatement(SELECT_ALL_USR_SQL, Application::extractUser);
    }

    //    Prepared Statement
    private static <T extends Entity> List<T> executePrepared(String sql, ResultSetExtractor<T> extractor,
                                                              StatementPreparer statementPreparation) throws InterruptedException {
        try (final Connection connection = ConnectionPool.locking()
                .takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            if (statementPreparation != null) {
                statementPreparation.accept(statement);
            }
            final ResultSet resultSet = statement.executeQuery();
            return extractor.extractAll(resultSet);
        } catch (SQLException e) {
            LOG.error("sql exception occurred", e);
            LOG.debug("sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            LOG.error("could not extract entity", e);
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Collections.emptyList();
    }

    private static <T extends Entity> T executeSinglePrepared(String sql, ResultSetExtractor<T> extractor,
                                                              StatementPreparer statementPreparation) throws InterruptedException {
        try (final Connection connection = ConnectionPool.locking().takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            if (statementPreparation != null) {
                statementPreparation.accept(statement);
            }
            final ResultSet resultSet = statement.executeQuery();
            return extractor.extract(resultSet);
        } catch (SQLException e) {
            LOG.error("sql exception occurred", e);
            LOG.debug("sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            LOG.error("could not extract entity", e);
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return null;
    }

    //    Custom Statement
    private static <T extends Entity> List<T> executeStatement(String sql, ResultSetExtractor<T> extractor) throws InterruptedException {
        try (final Connection connection = ConnectionPool.locking().takeConnection();
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(sql)) {
            return extractor.extractAll(resultSet);
        } catch (SQLException e) {
            LOG.error("sql exception occurred", e);
            LOG.debug("sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            LOG.error("could not extract entity", e);
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Collections.emptyList();
    }

    private static User extractUser(ResultSet resultSet) throws EntityExtractionFailedException {
        try {
            return new User(resultSet.getLong(ID_COLUMN_NAME), resultSet.getString(FIRST_NAME_COLUMN_NAME),
                    resultSet.getString(LAST_NAME_COLUMN_NAME), resultSet.getString(EMAIL_COLUMN_NAME),
                    resultSet.getDate(BIRTHDAY_COLUMN_NAME), resultSet.getBigDecimal(DISCOUNT_COLUMN_NAME),
                    resultSet.getLong(ACCOUNT_ID_COLUMN_NAME));
        } catch (SQLException e) {
            LOG.error("could not extract value from result set", e);
            throw new EntityExtractionFailedException("failed to extract user");
        }
    }

    static class JThread extends Thread {

        private final Logger LOG = LogManager.getLogger(JThread.class);
        private final ConnectionPool CONNECTION_POOL = ConnectionPool.locking();

        JThread(String name) {
            super(name);
        }

        public void run() {
            System.out.printf("%s started... \n", Thread.currentThread().getName());
            CONNECTION_POOL.init();
            final List<User> users;
            try {
                users = Application.fetchUsersFromDb();

            } catch (InterruptedException e) {
                LOG.info("interrupted fetching users. closing pool");
                CONNECTION_POOL.shutDown();
                return;
            }
            users.forEach(user -> LOG.info("{} Found {}", Thread.currentThread().getName(), user));
//        try {
//            wait(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
            CONNECTION_POOL.shutDown();
            System.out.printf("%s fiished... \n", Thread.currentThread().getName());
        }
    }
}
