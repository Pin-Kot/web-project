package com.epam.jwd.audiotrack_ordering.dao.impl;

import com.epam.jwd.audiotrack_ordering.dao.CommonDao;
import com.epam.jwd.audiotrack_ordering.dao.TrackDao;
import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Artist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public final class MethodTrackDao extends CommonDao<Track> implements TrackDao {

    private static final Logger LOG = LogManager.getLogger(MethodTrackDao.class);

    private static final String TRACK_TABLE_NAME = "track";
    private static final String ID_FIELD_NAME = "id";
    private static final String TITLE_FIELD_NAME = "title";
    private static final String YEAR_FIELD_NAME = "year";
    private static final String PRICE_FIELD_NAME = "price";

    private static final String TRACK_ARTIST_LINK_TABLE_NAME = "track_artist_link";
    private static final String T_ARTIST_LINK_TRACK_ID_FIELD_NAME = "track_id";
    private static final String T_ARTIST_LINK_ARTIST_ID_FIELD_NAME = "artist_id";

    private static final String TRACK_ALBUM_LINK_TABLE_NAME = "track_album_link";
    private static final String TRACK_ALBUM_LINK_TRACK_ID_FIELD_NAME = "track_id";
    private static final String TRACK_ALBUM_LINK_ALBUM_ID_FIELD_NAME = "album_id";

    private static final String ORDER_TRACK_LINK_TABLE_NAME = "order_track_link";
    private static final String ORDER_TRACK_LINK_ORDER_ID_FIELD_NAME = "order_id";
    private static final String ORDER_TRACK_LINK_TRACK_ID_FIELD_NAME = "track_id";

    private static final String ARTIST_TABLE_NAME = "artist";
    private static final String ARTIST_TABLE_ID_FIELD_NAME = "id";
    private static final String ARTIST_TABLE_NAME_FIELD_NAME = "name";

    private static final String ALBUM_TABLE_NAME = "album";
    private static final String ALBUM_TABLE_ID_FIELD_NAME = "id";
    private static final String ALBUM_TABLE_TITLE_FIELD_NAME = "title";

    private static final String ORDER_TABLE_NAME = "`order`";
    private static final String ORDER_ID_FIELD_NAME = "id";

    private static final String VALUES = "values (?, ?, ?)";

    private static final String ARTIST_ID_VARIABLE_NAME = "@'artist_id'";
    private static final String ALBUM_ID_VARIABLE_NAME = "@'album_id'";

//    private static final String selectByAlbumExpression =
//            "select track.id, track.title, track.year, track.price from track " +
//            "join track_album_link on track.id = track_album_link.track_id " +
//            "join album on track_album_link.album_id = album.id and album.title = ?";

//    private static final String selectByArtistExpression =
//            "select track.id, track.title, track.year, track.price from track " +
//            "join track_artist_link on track.id = track_artist_link.track_id " +
//            "join artist on track_artist_link.artist_id = artist.id and artist.name = ?";

//    private static final String selectByOrderExpression =
//        "select track.id, track.title, track.year, track.price from track " +
//                "join order_track_link on track.id = order_track_link.track_id " +
//                "join `order` on order_track_link.order_id = `order`.id and `order`.id = ?";

    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, TITLE_FIELD_NAME, YEAR_FIELD_NAME,
            PRICE_FIELD_NAME);
    private static final List<String> INSERT_FIELDS = Arrays.asList(TITLE_FIELD_NAME, YEAR_FIELD_NAME,
            PRICE_FIELD_NAME);
    private static final List<String> VARIABLES = Arrays.asList(ALBUM_ID_VARIABLE_NAME, ARTIST_ID_VARIABLE_NAME);

    private final String selectByTitleExpression;
    private final String selectByArtistExpression;
    private final String selectByAlbumExpression;
    private final String selectByTitleByYearByPriceExpression;
    private final String setVariablesExpression;
    private final String selectByOrderExpression;

    private MethodTrackDao(ConnectionPool pool) {
        super(pool, LOG);
        this.selectByTitleExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName()
                + SPACE + format(WHERE_FIELD, TITLE_FIELD_NAME);
        String selectAllFromTableExpression = format(SELECT_ALL_FROM, String.join(COMMA, getTableFields()))
                + getTableName();
        this.selectByArtistExpression = selectAllFromTableExpression + SPACE
                + format(JOIN_ON, TRACK_ARTIST_LINK_TABLE_NAME,
                getTableField(getTableName(), ID_FIELD_NAME),
                getTableField(TRACK_ARTIST_LINK_TABLE_NAME, T_ARTIST_LINK_TRACK_ID_FIELD_NAME))
                + SPACE
                + format(JOIN_ON, ARTIST_TABLE_NAME,
                getTableField(TRACK_ARTIST_LINK_TABLE_NAME, T_ARTIST_LINK_ARTIST_ID_FIELD_NAME),
                getTableField(ARTIST_TABLE_NAME, ARTIST_TABLE_ID_FIELD_NAME))
                + SPACE
                + format(AND, getTableField(ARTIST_TABLE_NAME, ARTIST_TABLE_NAME_FIELD_NAME));
        this.selectByAlbumExpression = selectAllFromTableExpression + SPACE
                + format(JOIN_ON, TRACK_ALBUM_LINK_TABLE_NAME,
                getTableField(getTableName(), ID_FIELD_NAME),
                getTableField(TRACK_ALBUM_LINK_TABLE_NAME, TRACK_ALBUM_LINK_TRACK_ID_FIELD_NAME))
                + SPACE
                + format(JOIN_ON, ALBUM_TABLE_NAME,
                getTableField(TRACK_ALBUM_LINK_TABLE_NAME, TRACK_ALBUM_LINK_ALBUM_ID_FIELD_NAME),
                getTableField(ALBUM_TABLE_NAME, ALBUM_TABLE_ID_FIELD_NAME))
                + SPACE
                + format(AND, getTableField(ALBUM_TABLE_NAME, ALBUM_TABLE_TITLE_FIELD_NAME));
        this.selectByTitleByYearByPriceExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields()))
                + getTableName() + SPACE + format(WHERE_FIELD, TITLE_FIELD_NAME) + SPACE + format(AND, YEAR_FIELD_NAME)
                + SPACE + format(AND, PRICE_FIELD_NAME);
        this.selectByOrderExpression = selectAllFromTableExpression + SPACE
                +format(JOIN_ON, ORDER_TRACK_LINK_TABLE_NAME,
                getTableField(getTableName(), ID_FIELD_NAME),
                getTableField(ORDER_TRACK_LINK_TABLE_NAME, ORDER_TRACK_LINK_TRACK_ID_FIELD_NAME)) + SPACE
                +format(JOIN_ON, ORDER_TABLE_NAME,
                getTableField(ORDER_TRACK_LINK_TABLE_NAME, ORDER_TRACK_LINK_ORDER_ID_FIELD_NAME),
                getTableField(ORDER_TABLE_NAME, ORDER_ID_FIELD_NAME)) + SPACE
                +format(AND, getTableField(ORDER_TABLE_NAME, ORDER_ID_FIELD_NAME));
        this.setVariablesExpression = format(SET, String.join(getDelimiter(), getVariables()).concat(QUERY));
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

    private List<String> getVariables() {
        return VARIABLES;
    }

    @Override
    protected Track extractResult(ResultSet rs) throws SQLException {
        return new Track(rs.getLong(ID_FIELD_NAME), rs.getString(TITLE_FIELD_NAME),
                rs.getInt(YEAR_FIELD_NAME), rs.getBigDecimal(PRICE_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Track track) throws SQLException {
        statement.setLong(1, track.getId());
        statement.setString(2, track.getTitle());
        statement.setInt(3, track.getYear());
        statement.setBigDecimal(4, track.getPrice());
    }

    @Override
    protected void fillInsertingEntity(PreparedStatement statement, Track track) throws SQLException {
        statement.setString(1, track.getTitle());
        statement.setInt(2, track.getYear());
        statement.setBigDecimal(3, track.getPrice());
    }

    protected void fillVariables(PreparedStatement statement, Album album, Artist artist) throws SQLException {
        statement.setLong(1, album.getId());
        statement.setLong(2, artist.getId());
    }

    @Override
    protected void fillUpdatingEntity(PreparedStatement statement, Track track) throws SQLException {
        fillEntity(statement, track);
        statement.setLong(5, track.getId());
    }

    @Override
    public void createTrack(Track track, Album album, Artist artist) {
        try {
            final boolean isCreated = executeCompoundUpdate(setVariablesExpression,
                    st -> fillVariables(st, album, artist), st -> fillInsertingEntity(st, track));
            if (isCreated) {
                LOG.info("Added successfully. New track {}", track);
            } else {
                LOG.error("Update sql error occurred");
            }
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
        }
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

    @Override
    public Optional<Track> findByTitleByYearByPrice(String title, int year, BigDecimal price) {
        try {
            return executePreparedForGenericEntity(selectByTitleByYearByPriceExpression,
                    this::extractResultCatchingException, st -> {
                        st.setString(1, title);
                        st.setInt(2, year);
                        st.setBigDecimal(3, price);
                    });
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    @Override
    public List<Track> findTracksByArtistName(String artistName) {
        try {
            return executePreparedForEntities(selectByArtistExpression,
                    this::extractResultCatchingException, st -> st.setString(1, artistName));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Track> findTracksByAlbumTitle(String title) {
        try {
            return executePreparedForEntities(selectByAlbumExpression,
                    this::extractResultCatchingException, st -> st.setString(1, title));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Track> findTracksByOrderId(Long id) {
        try {
            return executePreparedForEntities(selectByOrderExpression,
                    this::extractResultCatchingException, st -> st.setLong(1, id));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public boolean deleteTrackFromAlbum(Long id) {
        return false;
    }

    public static TrackDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final TrackDao INSTANCE = new MethodTrackDao(ConnectionPool.transactionalInstance());
    }
}
