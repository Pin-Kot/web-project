package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public final class MethodTrackDao extends CommonDao<Track> implements TrackDao {

    private static final Logger LOG = LogManager.getLogger(MethodTrackDao.class);
    private static final List<String> FIELDS = Arrays.asList("id", "title", "year", "price");
    private static final String TRACK_TABLE_NAME = "track";
    private static final String ID_FIELD_NAME = "id";
    private static final String TITLE_FIELD_NAME = "title";
    private static final String YEAR_FIELD_NAME = "year";
    private static final String PRICE_FIELD_NAME = "price";
    private static final String DELIMITER = ", ";

    private final String selectByTitleExpression;

    private MethodTrackDao(ConnectionPool pool) {
        super(pool, LOG);
        this.selectByTitleExpression = format(SELECT_ALL_FROM, String.join(DELIMITER, getFields())) + getTableName()
                + SPACE + format(WHERE_FIELD, TITLE_FIELD_NAME);
    }

    @Override
    protected String getTableName() {
        return TRACK_TABLE_NAME;
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
    protected Track extractResult(ResultSet rs) throws SQLException {
        return new Track(rs.getLong(ID_FIELD_NAME), rs.getString(TITLE_FIELD_NAME),
                rs.getInt(YEAR_FIELD_NAME), rs.getBigDecimal(PRICE_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Track entity) throws SQLException {
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getTitle());
        statement.setInt(3, entity.getYear());
        statement.setBigDecimal(4, entity.getPrice());
    }

    @Override
    public Optional<Track> findByTitle(String title) {
        try {
            return executePreparedForGenericEntity(selectByTitleExpression,
                    this::extractResultCatchingException, st -> st.setString(1, title));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    static TrackDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final TrackDao INSTANCE = new MethodTrackDao(ConnectionPool.transactionalInstance());
    }
}
