package com.epam.jwd.audiotrack_ordering.dao.impl;

import com.epam.jwd.audiotrack_ordering.dao.CommonDao;
import com.epam.jwd.audiotrack_ordering.dao.OrderDao;
import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.db.StatementPreparer;
import com.epam.jwd.audiotrack_ordering.entity.Order;
import com.epam.jwd.audiotrack_ordering.entity.Track;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

public class MethodOrderDao extends CommonDao<Order> implements OrderDao {

    private static final Logger LOG = LogManager.getLogger(MethodOrderDao.class);

    private static final String ORDER_TABLE_NAME = "`order`";
    private static final String ID_FIELD_NAME = "id";
    private static final String DATE_FIELD_NAME = "date";
    private static final String USER_ID_FIELD_NAME = "user_id";
    private static final String STATUS_FIELD_NAME = "status";
    private static final String VALUE_FIELD_NAME = "value";

    private static final String VALUES = "values (?, ?, ?, ?)";
    private static final String LINK_VALUES = "values (LAST_INSERT_ID(), ?)";

    private static final String ORDER_TRACK_LINK_TABLE_NAME = "order_track_link";
    private static final String ORDER_TRACK_LINK_ORDER_ID_FIELD_NAME = "order_id";
    private static final String ORDER_TRACK_LINK_TRACK_ID_FIELD_NAME = "track_id";

    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, DATE_FIELD_NAME, USER_ID_FIELD_NAME,
            STATUS_FIELD_NAME, VALUE_FIELD_NAME);
    private static final List<String> INSERT_FIELDS = Arrays.asList(DATE_FIELD_NAME, USER_ID_FIELD_NAME,
            STATUS_FIELD_NAME, VALUE_FIELD_NAME);

    private static final List<String> LINK_FIELDS = Arrays.asList(ORDER_TRACK_LINK_ORDER_ID_FIELD_NAME,
            ORDER_TRACK_LINK_TRACK_ID_FIELD_NAME);

    private final String setLinkExpression;
    private final String selectByUserIdExpression;

    private MethodOrderDao(ConnectionPool pool) {
        super(pool, LOG);
        this.setLinkExpression = format(INSERT_INTO, getLinkTableName(), String.join(COMMA, getLinkInsertFields()))
                + SPACE + getLinkValues();
        this.selectByUserIdExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName()
                + SPACE + format(WHERE_FIELD, USER_ID_FIELD_NAME);
    }

    @Override
    protected String getTableName() {
        return ORDER_TABLE_NAME;
    }

    private String getLinkTableName() {
        return ORDER_TRACK_LINK_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return FIELDS;
    }

    @Override
    protected List<String> getInsertFields() {
        return INSERT_FIELDS;
    }

    private List<String> getLinkInsertFields() {
        return LINK_FIELDS;
    }

    @Override
    protected String getValues() {
        return VALUES;
    }

    private String getLinkValues() {
        return LINK_VALUES;
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
    protected Order extractResult(ResultSet rs) throws SQLException {
        return new Order(rs.getLong(ID_FIELD_NAME), rs.getDate(DATE_FIELD_NAME).toLocalDate(),
                rs.getLong(USER_ID_FIELD_NAME), Order.typeOf(rs.getString(STATUS_FIELD_NAME)),
                rs.getBigDecimal(VALUE_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Order order) throws SQLException {
        statement.setLong(1, order.getId());
        statement.setDate(2, Date.valueOf(order.getDate()));
        statement.setLong(3, order.getUserId());
        statement.setString(4, String.valueOf(order.getStatus()));
        statement.setBigDecimal(5, order.getValue());
    }

    @Override
    protected void fillInsertingEntity(PreparedStatement statement, Order order) throws SQLException {
        statement.setDate(1, Date.valueOf(order.getDate()));
        statement.setLong(2, order.getUserId());
        statement.setString(3, String.valueOf(order.getStatus()));
        statement.setBigDecimal(4, order.getValue());
    }

    @Override
    protected void fillUpdatingEntity(PreparedStatement statement, Order order) throws SQLException {
        fillEntity(statement, order);
        statement.setLong(6, order.getId());
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        try {
            return executePreparedForEntities(selectByUserIdExpression,
                    this::extractResultCatchingException, st -> st.setLong(1, userId));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    protected void fillVariable(PreparedStatement statement, Track track) throws SQLException {
        statement.setLong(1, track.getId());
    }

    @Override
    public void createOrder(Order order, List<Track> tracks) {
        try {
            final boolean isCreated = executeListUpdate(setLinkExpression, prepareListOfStatements(tracks),
                    st -> fillInsertingEntity(st, order));
            if (isCreated) {
                LOG.info("Created successfully. New order {}", order);
            } else {
                LOG.error("Update sql error occurred");
            }
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private List<StatementPreparer> prepareListOfStatements(List<Track> tracks) {
        List<StatementPreparer> preparedStatements = new ArrayList<>();
        for (Track t : tracks) {
            preparedStatements.add(st -> fillVariable(st, t));
        }
        return preparedStatements;
    }

    public static OrderDao getInstance() {
        return MethodOrderDao.Holder.INSTANCE;
    }

    private static class Holder {
        public static final OrderDao INSTANCE = new MethodOrderDao(ConnectionPool.transactionalInstance());
    }
}
