package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Album;
import com.epam.jwd.audiotrack_ordering.entity.Image;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

public class MethodImageDao extends CommonDao<Image> implements ImageDao {

    private static final Logger LOG = LogManager.getLogger(MethodImageDao.class);

    private static final String IMAGE_TABLE_NAME = "image";
    private static final String ID_FIELD_NAME = "id";
    private static final String IMAGE_FIELD_NAME = "image";

    private static final String ALBUM_IMAGE_LINK_TABLE_NAME = "album_image_link";
    private static final String ALBUM_IMAGE_LINK_IMAGE_ID_FIELD_NAME = "image_id";
    private static final String ALBUM_IMAGE_LINK_ALBUM_ID_FIELD_NAME = "album_id";

    private static final String ALBUM_TABLE_NAME = "album";
    private static final String ALBUM_TABLE_ID_FIELD_NAME = "id";
    private static final String ALBUM_TABLE_TITLE_FIELD_NAME = "title";

    private static final String VALUES = "values (?)";
    private static final String QUERY = " = ? ";

    private static final String VARIABLE_ALBUM_ID_NAME = "@'album_id'";

    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, IMAGE_FIELD_NAME);

    private final String setVariableExpression;
    private final String selectByAlbumExpression;

//    private static final String selectByAlbumExpression = "select image.id, image.image from image "+
//            "join album_image_link on image.id = album_image_link.image_id " +
//            "join album on album_image_link.album_id = album.id and album.title = ?";

    public MethodImageDao(ConnectionPool pool) {
        super(pool, LOG);
        String selectAllFromTableExpression = format(SELECT_ALL_FROM, String.join(COMMA, getTableFields()))
                + getTableName();
        this.selectByAlbumExpression = selectAllFromTableExpression + SPACE
                + format(JOIN_ON, ALBUM_IMAGE_LINK_TABLE_NAME,
                getTableField(getTableName(), ID_FIELD_NAME),
                getTableField(ALBUM_IMAGE_LINK_TABLE_NAME, ALBUM_IMAGE_LINK_IMAGE_ID_FIELD_NAME))
                + SPACE
                + format(JOIN_ON, ALBUM_TABLE_NAME,
                getTableField(ALBUM_IMAGE_LINK_TABLE_NAME, ALBUM_IMAGE_LINK_ALBUM_ID_FIELD_NAME),
                getTableField(ALBUM_TABLE_NAME, ALBUM_TABLE_ID_FIELD_NAME))
                + SPACE
                + format(AND, getTableField(ALBUM_TABLE_NAME, ALBUM_TABLE_TITLE_FIELD_NAME));
        this.setVariableExpression = format(SET, getVariableName().concat(QUERY));
    }

    @Override
    protected String getTableName() {
        return IMAGE_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected List<String> getInsertFields() {
        return Collections.singletonList(IMAGE_FIELD_NAME);
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
        return QUERY;
    }

    protected String getVariableName() {
        return VARIABLE_ALBUM_ID_NAME;
    }

    @Override
    protected Image extractResult(ResultSet rs) throws SQLException {
        return new Image(rs.getLong(ID_FIELD_NAME),
                rs.getString(IMAGE_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Image image) throws SQLException {
        statement.setLong(1, image.getId());
        statement.setBlob(2, new ByteArrayInputStream(image.getImage().getBytes()));
    }

    @Override
    protected void fillInsertingEntity(PreparedStatement statement, Image image) throws SQLException {
        statement.setBlob(1, new ByteArrayInputStream(image.getImage().getBytes()));
    }

    @Override
    protected void fillUpdatingEntity(PreparedStatement statement, Image image) throws SQLException {
        fillEntity(statement, image);
        statement.setLong(3, image.getId());
    }

    private void fillInsertingImage(PreparedStatement statement, InputStream image) throws SQLException, IOException {
        String imageString = convertStreamToBase64DataString(image);
        statement.setString(1, imageString);
    }

    protected void fillVariable(PreparedStatement statement, Album album) throws SQLException {
        statement.setLong(1, album.getId());
    }

    public String convertStreamToBase64DataString(InputStream image) throws IOException {
        byte[] byteImage = IOUtils.toByteArray(image);
        byte[] encodeBase64 = Base64.getEncoder().encode(byteImage);
        return new String(encodeBase64, StandardCharsets.UTF_8);
    }

    @Override
    public List<Image> findAlbumImages(String albumTitle) {
        try {
            return executePreparedForEntities(selectByAlbumExpression,
                    this::extractResultCatchingException, st -> st.setString(1, albumTitle));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public void createImage(Album album, InputStream image) {
        try {
            final boolean isCreated = executeCompoundUpdate(setVariableExpression, st -> fillVariable(st, album),
                    st -> {
                        try {
                            fillInsertingImage(st, image);
                        } catch (IOException e) {
                            LOG.info("Can't convert image");
                        }
                    });
            if (isCreated) {
                LOG.info("Image added successfully");
            } else {
                LOG.error("Update sql error occurred");
            }
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    static ImageDao getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        public static final ImageDao INSTANCE = new MethodImageDao(ConnectionPool.transactionalInstance());
    }
}
