package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Album;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class MethodAlbumDao extends CommonDao<Album> implements AlbumDao {

    private static final Logger LOG = LogManager.getLogger(MethodAlbumDao.class);

    private static final String ALBUM_TABLE_NAME = "album";
    private static final String ID_FIELD_NAME = "id";
    private static final String TITLE_FIELD_NAME = "title";
    private static final String YEAR_FIELD_NAME = "year";
    private static final String TYPE_FIELD_NAME = "type";
    private static final List<String> FIELDS = Arrays.asList("id", "title", "year", "type");

    private MethodAlbumDao(ConnectionPool pool) {
        super(pool, LOG);
    }

    @Override
    protected String getTableName() {
        return ALBUM_TABLE_NAME;
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
    protected Album extractResult(ResultSet rs) throws SQLException {
        return new Album(rs.getLong(ID_FIELD_NAME), rs.getString(TITLE_FIELD_NAME), rs.getInt(YEAR_FIELD_NAME),
                Album.typeOf(rs.getString(TYPE_FIELD_NAME)));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Album entity) throws SQLException {
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getTitle());
        statement.setInt(3, entity.getYear());
        statement.setString(4, String.valueOf(entity.getType()));
    }

    @Override
    public Optional<Album> findByTitle(String title) {
        return Optional.empty();
    }

    static AlbumDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final AlbumDao INSTANCE = new MethodAlbumDao(ConnectionPool.transactionalInstance());
    }
}
