package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class MethodAccountDao extends CommonDao<Account> implements AccountDao {

    private static final String ACCOUNT_TABLE_NAME = "account";

    private static final Logger LOG = LogManager.getLogger(MethodAccountDao.class);
    private static final List<String> FIELDS = Arrays.asList("id", "login", "acc_password", "role");
    private static final String LOGIN_FIELD_NAME = "login";
    private final String selectByLoginExpression;

    private MethodAccountDao(ConnectionPool pool) {
        super(pool, LOG);
        this.selectByLoginExpression = SELECT_ALL_FROM + getTableName() + SPACE + format(WHERE_FIELD, LOGIN_FIELD_NAME);
    }

    @Override
    protected String getTableName() {
        return ACCOUNT_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected Account extractResult(ResultSet rs) throws SQLException {
        return new Account(rs.getLong("id"), rs.getString("login"),
                rs.getString("acc_password"), Account.roleOf(rs.getString("role")));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Account entity) throws SQLException {
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getLogin());
        statement.setString(3, entity.getLogin());
        statement.setString(4, String.valueOf(entity.getRole()));
    }


    @Override
    public Optional<Account> readAccountByLogin(String login) {
        try {
            return executePreparedForGenericEntity(selectByLoginExpression,
                    this::extractResultCatchingException, st -> st.setString(1, login));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    static AccountDao getInstance() {
        return MethodAccountDao.Holder.INSTANCE;
    }

    private static class Holder {
        public static final AccountDao INSTANCE = new MethodAccountDao(ConnectionPool.transactionalInstance());
    }
}
