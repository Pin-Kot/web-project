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
    private static final String QUERY_AND_COMMA = " = ?, ";
    private static final String VALUES = "values (?, ?, ?)";

    private static final List<String> FIELDS = Arrays.asList("id", "title", "year", "type");
    private static final List<String> INSERT_FIELDS = Arrays.asList(TITLE_FIELD_NAME, YEAR_FIELD_NAME,
            TYPE_FIELD_NAME);

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
    protected List<String> getInsertFields() {
        return INSERT_FIELDS;
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
    protected Album extractResult(ResultSet rs) throws SQLException {
        return new Album(rs.getLong(ID_FIELD_NAME), rs.getString(TITLE_FIELD_NAME), rs.getInt(YEAR_FIELD_NAME),
                Album.typeOf(rs.getString(TYPE_FIELD_NAME)));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Album album) throws SQLException {
        statement.setLong(1, album.getId());
        statement.setString(2, album.getTitle());
        statement.setInt(3, album.getYear());
        statement.setString(4, String.valueOf(album.getType()));
    }

    @Override
    protected void fillInsertingEntity(PreparedStatement statement, Album album) throws SQLException {
        statement.setString(1, album.getTitle());
        statement.setInt(2, album.getYear());
        statement.setString(3, String.valueOf(album.getType()));
    }

    @Override
    protected void fillUpdatingEntity(PreparedStatement statement, Album album) throws SQLException {
        fillEntity(statement, album);
        statement.setLong(5, album.getId());
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
