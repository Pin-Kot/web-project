package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class MethodArtistDao extends CommonDao<Artist> implements ArtistDao {

    private static final Logger LOG = LogManager.getLogger(MethodArtistDao.class);

    private static final String ARTIST_TABLE_NAME = "artist";
    private static final String ID_FIELD_NAME = "id";
    private static final String NAME_FIELD_NAME = "name";
    public static final List<String> FIELDS = Arrays.asList("id", "name");

    private MethodArtistDao(ConnectionPool pool) {
        super(pool, LOG);
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
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    @Override
    protected Artist extractResult(ResultSet rs) throws SQLException {
        return new Artist(rs.getLong(ID_FIELD_NAME),
                rs.getString(NAME_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Artist entity) throws SQLException {

    }

    @Override
    public Optional<Artist> findByName(String name) {
        return Optional.empty();
    }

    static ArtistDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ArtistDao INSTANCE = new MethodArtistDao(ConnectionPool.transactionalInstance());
    }
}
