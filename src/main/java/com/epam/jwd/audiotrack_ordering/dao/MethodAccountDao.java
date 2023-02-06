package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Account;
import com.epam.jwd.audiotrack_ordering.entity.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public final class MethodAccountDao extends CommonDao<Account> implements AccountDao {

    private static final Logger LOG = LogManager.getLogger(MethodAccountDao.class);

    private static final String ACCOUNT_TABLE_NAME = "account";
    private static final String ID_FIELD_NAME = "id";
    private static final String LOGIN_FIELD_NAME = "login";
    private static final String PASSWORD_FIELD_NAME = "acc_password";
    private static final String ROLE_FIELD_NAME = "role";
    private static final String QUERY_AND_COMMA = " = ?, ";
    private static final String VALUES = "values (?, ?, ?)";

    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, LOGIN_FIELD_NAME, PASSWORD_FIELD_NAME,
            ROLE_FIELD_NAME);

    private static final List<String> INSERTING_FIELDS = Arrays.asList(LOGIN_FIELD_NAME, PASSWORD_FIELD_NAME,
            ROLE_FIELD_NAME);

    private final String selectByLoginExpression;

    private MethodAccountDao(ConnectionPool pool) {
        super(pool, LOG);
        this.selectByLoginExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName()
                + SPACE + format(WHERE_FIELD, LOGIN_FIELD_NAME);
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
    protected List<String> getInsertFields() {
        return INSERTING_FIELDS;
    }

    @Override
    protected String getValues() {
        return VALUES;
    }

    @Override
    protected String getInsertRequest() {
        return QUERY_AND_COMMA;
    }

    @Override
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    @Override
    protected Account extractResult(ResultSet rs) throws SQLException {
        return new Account(rs.getLong(ID_FIELD_NAME), rs.getString(LOGIN_FIELD_NAME),
                rs.getString(PASSWORD_FIELD_NAME), Role.of(rs.getString(ROLE_FIELD_NAME)));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Account account) throws SQLException {
        statement.setLong(1, account.getId());
        statement.setString(2, account.getLogin());
        statement.setString(3, account.getPassword());
        statement.setString(4, String.valueOf(account.getRole()));
    }

    @Override
    protected void fillInsertingEntity(PreparedStatement statement, Account account) throws SQLException {
        statement.setString(1, account.getLogin());
        statement.setString(2, account.getPassword());
        statement.setString(3, String.valueOf(account.getRole()));
    }

    @Override
    protected void fillUpdatingEntity(PreparedStatement statement, Account account) throws SQLException {
        fillEntity(statement, account);
        statement.setLong(5, account.getId());
    }

    @Override
    public Optional<Account> findAccountByLogin(String login) {
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
