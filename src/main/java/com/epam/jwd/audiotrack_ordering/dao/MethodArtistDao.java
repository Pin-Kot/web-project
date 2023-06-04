package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public final class MethodArtistDao extends CommonDao<Artist> implements ArtistDao {

    private static final Logger LOG = LogManager.getLogger(MethodArtistDao.class);

    private static final String ARTIST_TABLE_NAME = "artist";
    private static final String ID_FIELD_NAME = "id";
    private static final String NAME_FIELD_NAME = "name";
    private static final String QUERY = " = ? ";
    private static final String VALUES = "values (?)";

    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, NAME_FIELD_NAME);

    private final String selectByNameExpression;

    private MethodArtistDao(ConnectionPool pool) {
        super(pool, LOG);
        this.selectByNameExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName()
                + SPACE + format(WHERE_FIELD, NAME_FIELD_NAME);
    }

    @Override
    protected String getTableName() {
        return ARTIST_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected List<String> getInsertFields() {
        return Collections.singletonList(NAME_FIELD_NAME);
    }

    @Override
    protected String getValues() {
        return VALUES;
    }

    @Override
    protected String getDelimiter() {
        return QUERY;
    }

    @Override
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    @Override
    protected Artist extractResult(ResultSet rs) throws SQLException {
        return new Artist(rs.getLong(ID_FIELD_NAME),
                rs.getString(NAME_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Artist artist) throws SQLException {
        statement.setLong(1, artist.getId());
        statement.setString(2, artist.getName());
    }

    @Override
    protected void fillInsertingEntity(PreparedStatement statement, Artist artist) throws SQLException {
        statement.setString(1, artist.getName());
    }

    @Override
    protected void fillUpdatingEntity(PreparedStatement statement, Artist artist) throws SQLException {
        fillEntity(statement, artist);
        statement.setLong(3, artist.getId());
    }

    @Override
    public Optional<Artist> findByName(String name) {
        try {
            return executePreparedForGenericEntity(selectByNameExpression,
                    this::extractResultCatchingException, st -> st.setString(1, name));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    static ArtistDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ArtistDao INSTANCE = new MethodArtistDao(ConnectionPool.transactionalInstance());
    }
}
