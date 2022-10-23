package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.User;
import com.epam.jwd.audiotrack_ordering.exception.EntityExtractionFailedException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MethodUserDao extends CommonDao<User> implements UserDao {

    private static final Logger LOG = LogManager.getLogger(MethodUserDao.class);

    private static final String USER_TABLE_NAME = "user";
    private static final String ID_FIELD_NAME = "id";
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String LAST_NAME_FIELD_NAME = "last_name";
    private static final String EMAIL_FIELD_NAME = "email";
    private static final String BIRTHDAY_FIELD_NAME = "birthday";
    private static final String DISCOUNT_FIELD_NAME = "discount";
    private static final String ACCOUNT_ID_FIELD_NAME = "account_id";

    private MethodUserDao(ConnectionPool pool) {
        super(pool, LOG);
    }

    @Override
    protected String getTableName() {
        return USER_TABLE_NAME;
    }

    @Override
    protected User extractResult(ResultSet rs) throws EntityExtractionFailedException {
        try {
            return new User(rs.getLong(ID_FIELD_NAME), rs.getString(FIRST_NAME_FIELD_NAME),
                    rs.getString(LAST_NAME_FIELD_NAME), rs.getString(EMAIL_FIELD_NAME),
                    rs.getDate(BIRTHDAY_FIELD_NAME), rs.getBigDecimal(DISCOUNT_FIELD_NAME),
                    rs.getLong(ACCOUNT_ID_FIELD_NAME));
        } catch (SQLException e) {
            LOG.error("sql exception occurred extracting user from ResultSet", e);
            throw new EntityExtractionFailedException("");
        }
    }

    @Override
    public User create(User entity) {
        return null;
    }

    @Override
    public Optional<User> read(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    static UserDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final UserDao INSTANCE = new MethodUserDao(ConnectionPool.locking());
    }
}
