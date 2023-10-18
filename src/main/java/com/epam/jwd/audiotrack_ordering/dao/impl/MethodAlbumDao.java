package com.epam.jwd.audiotrack_ordering.dao.impl;

import com.epam.jwd.audiotrack_ordering.dao.AlbumDao;
import com.epam.jwd.audiotrack_ordering.dao.CommonDao;
import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Album;
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

public final class MethodAlbumDao extends CommonDao<Album> implements AlbumDao {

    private static final Logger LOG = LogManager.getLogger(MethodAlbumDao.class);

    private static final String ALBUM_TABLE_NAME = "album";
    private static final String ID_FIELD_NAME = "id";
    private static final String TITLE_FIELD_NAME = "title";
    private static final String YEAR_FIELD_NAME = "year";
    private static final String TYPE_FIELD_NAME = "type";

    private static final String VALUES = "values (?, ?, ?)";

    private static final String ARTIST_TABLE_NAME = "artist";
    private static final String ARTIST_TABLE_ID_FIELD_NAME = "id";
    private static final String ARTIST_TABLE_NAME_FIELD_NAME = "name";

    private static final String ALBUM_ARTIST_LINK_TABLE_NAME = "album_artist_link";
    private static final String ALBUM_ARTIST_LINK_ALBUM_ID_FIELD_NAME = "album_id";
    private static final String ALBUM_ARTIST_LINK_ARTIST_ID_FIELD_NAME = "artist_id";

    private static final String VARIABLE_ARTIST_ID_NAME = "@'artist_id'";

    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, TITLE_FIELD_NAME, YEAR_FIELD_NAME,
            TYPE_FIELD_NAME);
    private static final List<String> INSERT_FIELDS = Arrays.asList(TITLE_FIELD_NAME, YEAR_FIELD_NAME,
            TYPE_FIELD_NAME);

//    private static final String selectByArtistExpression = "select album.id, album.title, album.year, album.type from album " +
//            "join album_artist_link on album.id = album_artist_link.album_id " +
//            "join artist on album_artist_link.artist_id = artist.id and artist.name = ?";

    private final String selectByArtistExpression;
    private final String selectByTitleByYearExpression;
    private final String setVariableExpression;

    private MethodAlbumDao(ConnectionPool pool) {
        super(pool, LOG);
        String selectAllFromTableExpression = format(SELECT_ALL_FROM, String.join(COMMA, getTableFields()))
                + getTableName();
        this.selectByTitleByYearExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName()
                + SPACE + format(WHERE_FIELD, TITLE_FIELD_NAME) + SPACE + format(AND, YEAR_FIELD_NAME);
        this.selectByArtistExpression = selectAllFromTableExpression + SPACE
                + format(JOIN_ON, ALBUM_ARTIST_LINK_TABLE_NAME, getTableField(getTableName(), ID_FIELD_NAME),
                getTableField(ALBUM_ARTIST_LINK_TABLE_NAME, ALBUM_ARTIST_LINK_ALBUM_ID_FIELD_NAME)) + SPACE
                + format(JOIN_ON, ARTIST_TABLE_NAME,
                getTableField(ALBUM_ARTIST_LINK_TABLE_NAME, ALBUM_ARTIST_LINK_ARTIST_ID_FIELD_NAME),
                getTableField(ARTIST_TABLE_NAME, ARTIST_TABLE_ID_FIELD_NAME)) + SPACE
                + format(AND, getTableField(ARTIST_TABLE_NAME, ARTIST_TABLE_NAME_FIELD_NAME));
        this.setVariableExpression = format(SET, getVariableName().concat(QUERY));
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
    protected String getDelimiter() {
        return QUERY_AND_COMMA;
    }

    @Override
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    protected String getVariableName() {
        return VARIABLE_ARTIST_ID_NAME;
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

    protected void fillVariable(PreparedStatement statement, Artist artist) throws SQLException {
        statement.setLong(1, artist.getId());
    }

    @Override
    public void createAlbum(Album album, Artist artist) {
        try {
            final boolean isCreated = executeCompoundUpdate(setVariableExpression, st -> fillVariable(st, artist),
                    st -> fillInsertingEntity(st, album));
            if (isCreated) {
                LOG.info("Created successfully. New album {}", album);
            } else {
                LOG.error("Update sql error occurred");
            }
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Optional<Album> findByTitleByYear(String title, int year) {
        try {
            return executePreparedForGenericEntity(selectByTitleByYearExpression,
                    this::extractResultCatchingException, st -> {
                        st.setString(1, title);
                        st.setInt(2, year);
                    });
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    @Override
    public List<Album> findAlbumsByArtistName(String artistName) {
        try {
            return executePreparedForEntities(selectByArtistExpression,
                    this::extractResultCatchingException, st -> st.setString(1, artistName));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    public static AlbumDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final AlbumDao INSTANCE = new MethodAlbumDao(ConnectionPool.transactionalInstance());
    }
}
