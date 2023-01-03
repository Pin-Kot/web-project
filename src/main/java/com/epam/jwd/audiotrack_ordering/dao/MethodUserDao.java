package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.sql.Date.valueOf;

public final class MethodUserDao extends CommonDao<User> implements UserDao {

    private static final Logger LOG = LogManager.getLogger(MethodUserDao.class);

    private static final String USER_TABLE_NAME = "user";
    private static final String ID_FIELD_NAME = "id";
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String LAST_NAME_FIELD_NAME = "last_name";
    private static final String EMAIL_FIELD_NAME = "email";
    private static final String BIRTHDAY_FIELD_NAME = "birthday";
    private static final String DISCOUNT_FIELD_NAME = "discount";
    private static final String ACCOUNT_ID_FIELD_NAME = "account_id";
    private static final String UPDATE = "update %s";
    private static final String SET = "set %s";
    private static final String QUERY_AND_COMMA = " = ?, ";
    private static final String QUERY = " = ? ";

    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, FIRST_NAME_FIELD_NAME, LAST_NAME_FIELD_NAME,
            EMAIL_FIELD_NAME, BIRTHDAY_FIELD_NAME, DISCOUNT_FIELD_NAME, ACCOUNT_ID_FIELD_NAME);


    private final String selectByAccountIdExpression;
    private final String updateSql;


    private MethodUserDao(ConnectionPool pool) {
        super(pool, LOG);
        this.selectByAccountIdExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName()
                + SPACE + format(WHERE_FIELD, ACCOUNT_ID_FIELD_NAME);
        this.updateSql = format(UPDATE, getTableName()) + SPACE
                + format(SET, String.join(QUERY_AND_COMMA, getFields())) + QUERY + format(WHERE_FIELD, ID_FIELD_NAME);
    }

    @Override
    protected String getTableName() {
        return USER_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    @Override
    protected User extractResult(ResultSet rs) throws SQLException {
        return new User(rs.getLong(ID_FIELD_NAME), rs.getString(FIRST_NAME_FIELD_NAME),
                rs.getString(LAST_NAME_FIELD_NAME), rs.getString(EMAIL_FIELD_NAME),
                rs.getDate(BIRTHDAY_FIELD_NAME).toLocalDate(), rs.getBigDecimal(DISCOUNT_FIELD_NAME),
                rs.getLong(ACCOUNT_ID_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, User user) throws SQLException {
        statement.setLong(1, user.getId());
        statement.setString(2, user.getFirstName());
        statement.setString(3, user.getLastName());
        statement.setString(4, user.getEmail());
        statement.setDate(5, valueOf(user.getBirthday()));
        statement.setBigDecimal(6, user.getDiscount());
        statement.setLong(7, user.getAccId());
    }

    protected void fillEntityWithId(PreparedStatement statement, User user) throws SQLException {
        fillEntity(statement, user);
        statement.setLong(8, user.getId());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    public Optional<User> findUserByAccountId(Long accountId) {
        try {
            return executePreparedForGenericEntity(selectByAccountIdExpression,
                    this::extractResultCatchingException, st -> st.setLong(1, accountId));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();        }

    }

    @Override
    public User update(User entity) {
        try {
            final int rowsUpdated = executePreparedUpdate(updateSql, st -> fillEntityWithId(st, entity));
            if (rowsUpdated > 0) {
                LOG.info("Updated successfully. New user data {}", entity);
            } else {
                LOG.error("Update error occurred");
            }
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return null;
        }
        return null;
    }

    static UserDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final UserDao INSTANCE = new MethodUserDao(ConnectionPool.transactionalInstance());
    }
}
