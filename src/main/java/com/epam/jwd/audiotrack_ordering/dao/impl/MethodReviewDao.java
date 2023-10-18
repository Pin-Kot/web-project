package com.epam.jwd.audiotrack_ordering.dao.impl;

import com.epam.jwd.audiotrack_ordering.dao.CommonDao;
import com.epam.jwd.audiotrack_ordering.dao.ReviewDao;
import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Review;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static java.sql.Date.valueOf;

public class MethodReviewDao extends CommonDao<Review> implements ReviewDao {

    private static final Logger LOG = LogManager.getLogger(MethodReviewDao.class);

    private static final String REVIEW_TABLE_NAME = "review";
    private static final String ID_FIELD_NAME = "id";
    private static final String DATE_FIELD_NAME = "date";
    private static final String TEXT_FIELD_NAME = "text";
    private static final String ACCOUNT_LOGIN_FIELD_NAME = "account_login";
    private static final String TRACK_ID_FIELD_NAME = "track_id";
    private static final String VALUES = "values (?, ?, ?, ?)";

    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, DATE_FIELD_NAME, TEXT_FIELD_NAME,
            ACCOUNT_LOGIN_FIELD_NAME, TRACK_ID_FIELD_NAME);
    private static final List<String> INSERT_FIELDS = Arrays.asList(DATE_FIELD_NAME, TEXT_FIELD_NAME,
            ACCOUNT_LOGIN_FIELD_NAME, TRACK_ID_FIELD_NAME);

    private final String selectByTrackIdExpression;

    public MethodReviewDao(ConnectionPool pool) {
        super(pool, LOG);
        this.selectByTrackIdExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName()
                + SPACE + format(WHERE_FIELD, TRACK_ID_FIELD_NAME);
    }

    @Override
    protected String getTableName() {
        return REVIEW_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected List<String> getInsertFields() {
        return INSERT_FIELDS;
    }

    @Override
    protected String getValues() {
        return VALUES;
    }

    @Override
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    @Override
    protected String getDelimiter() {
        return QUERY_AND_COMMA;
    }

    @Override
    protected Review extractResult(ResultSet rs) throws SQLException {
        return new Review(rs.getLong(ID_FIELD_NAME), rs.getDate(DATE_FIELD_NAME).toLocalDate(),
                rs.getString(TEXT_FIELD_NAME), rs.getString(ACCOUNT_LOGIN_FIELD_NAME), rs.getLong(TRACK_ID_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Review review) throws SQLException {
        statement.setLong(1, review.getId());
        statement.setDate(2, valueOf(review.getDate()));
        statement.setString(3, review.getText());
        statement.setString(4, review.getAccountLogin());
        statement.setLong(5, review.getTrackId());
    }

    @Override
    protected void fillInsertingEntity(PreparedStatement statement, Review review) throws SQLException {
        statement.setDate(1, valueOf(review.getDate()));
        statement.setString(2, review.getText());
        statement.setString(3, review.getAccountLogin());
        statement.setLong(4, review.getTrackId());
    }

    @Override
    protected void fillUpdatingEntity(PreparedStatement statement, Review review) throws SQLException {
        fillEntity(statement, review);
        statement.setLong(6, review.getId());
    }

    @Override
    public List<Review> findReviewsByTrackId(Long trackId) {
        try {
            return executePreparedForEntities(selectByTrackIdExpression,
                    this::extractResultCatchingException, st -> st.setLong(1, trackId));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    public static ReviewDao getInstance() {
        return MethodReviewDao.Holder.INSTANCE;
    }

    private static class Holder {
        public static final ReviewDao INSTANCE = new MethodReviewDao(ConnectionPool.transactionalInstance());
    }
}
