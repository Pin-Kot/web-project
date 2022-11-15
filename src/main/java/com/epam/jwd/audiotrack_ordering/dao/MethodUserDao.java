package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.User;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private static final List<String> FIELDS = Arrays.asList("id", "first_name", "last_name", "email", "birthday",
            "discount", "account_id");

    private MethodUserDao(ConnectionPool pool) {
        super(pool, LOG);
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
    protected User extractResult(ResultSet rs) throws SQLException {
        return new User(rs.getLong(ID_FIELD_NAME), rs.getString(FIRST_NAME_FIELD_NAME),
                rs.getString(LAST_NAME_FIELD_NAME), rs.getString(EMAIL_FIELD_NAME),
                rs.getDate(BIRTHDAY_FIELD_NAME), rs.getBigDecimal(DISCOUNT_FIELD_NAME),
                rs.getLong(ACCOUNT_ID_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, User entity) throws SQLException {
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getFirstName());
        statement.setString(3, entity.getLastName());
        statement.setString(4, entity.getEmail());
        statement.setDate(5, (Date) entity.getBirthday());
        statement.setBigDecimal(6, entity.getDiscount());
        statement.setLong(7, entity.getAccId());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    static UserDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final UserDao INSTANCE = new MethodUserDao(ConnectionPool.transactionalInstance());
    }
}
