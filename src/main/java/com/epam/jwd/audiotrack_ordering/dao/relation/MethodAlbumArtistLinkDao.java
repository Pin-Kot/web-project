package com.epam.jwd.audiotrack_ordering.dao.relation;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.dto.AlbumArtistLink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MethodAlbumArtistLinkDao extends AbstractLinkDao<AlbumArtistLink> implements LinkDao<AlbumArtistLink> {

    private static final Logger LOG = LogManager.getLogger(MethodAlbumArtistLinkDao.class);

    private static final String ALBUM_ARTIST_LINK_TABLE_NAME = "album_artist_link";
    private static final String ALBUM_ID_FIELD_NAME = "album_id";
    private static final String ARTIST_ID_FIELD_NAME = "artist_id";

    private static final List<String> FIELDS = Arrays.asList(ALBUM_ID_FIELD_NAME, ARTIST_ID_FIELD_NAME);

    private MethodAlbumArtistLinkDao(ConnectionPool pool) {
        super(pool, LOG);
    }

    @Override
    protected String getTableName() {
        return ALBUM_ARTIST_LINK_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected AlbumArtistLink extractResultLink(ResultSet rs) throws SQLException {
        return new AlbumArtistLink(rs.getLong(ALBUM_ID_FIELD_NAME), rs.getLong(ARTIST_ID_FIELD_NAME));
    }

    @Override
    protected void fillLink(PreparedStatement statement, AlbumArtistLink link) throws SQLException {
        statement.setLong(1, link.getLeftEntityId());
        statement.setLong(2, link.getRightEntityId());
    }

    @Override
    public void delete(AlbumArtistLink entity) {

    }

    @Override
    protected void fillLinkById(PreparedStatement statement, Long id) throws SQLException {
        statement.setLong(1, id);
    }

    static MethodAlbumArtistLinkDao getInstance() {
        return MethodAlbumArtistLinkDao.Holder.INSTANCE;
    }

    private static class Holder {
        public static final MethodAlbumArtistLinkDao INSTANCE = new MethodAlbumArtistLinkDao(ConnectionPool.transactionalInstance());
    }
}
