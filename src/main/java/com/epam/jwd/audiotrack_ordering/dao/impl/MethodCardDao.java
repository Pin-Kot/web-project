package com.epam.jwd.audiotrack_ordering.dao.impl;

import com.epam.jwd.audiotrack_ordering.dao.CardDao;
import com.epam.jwd.audiotrack_ordering.dao.CommonDao;
import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.entity.Card;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class MethodCardDao extends CommonDao<Card> implements CardDao {

    private static final Logger LOG = LogManager.getLogger(MethodCardDao.class);

    private static final String CARD_TABLE_NAME = "card";
    private static final String ID_FIELD_NAME = "id";
    private static final String HOLDER_NAME_FIELD_NAME = "holder_name";
    private static final String NUMBER_FIELD_NAME = "number";
    private static final String AMOUNT_FIELD_NAME = "amount";
    private static final String VALUES = "values (?, ?, ?)";

    private static final List<String> FIELDS = Arrays.asList(ID_FIELD_NAME, HOLDER_NAME_FIELD_NAME, NUMBER_FIELD_NAME,
            AMOUNT_FIELD_NAME);
    private static final List<String> INSERT_FIELDS = Arrays.asList(HOLDER_NAME_FIELD_NAME, NUMBER_FIELD_NAME,
            AMOUNT_FIELD_NAME);

    private final String selectByNumberExpression;
    private final String updateByNumberExpression;

    private MethodCardDao(ConnectionPool pool) {
        super(pool, LOG);
        this.selectByNumberExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName()
                + SPACE + format(WHERE_FIELD, getNumberFieldName());
        this.updateByNumberExpression = format(UPDATE, getTableName()) + SPACE + format(SET, getAmountFieldName())
                + QUERY + format(WHERE_FIELD, getNumberFieldName());
    }

    @Override
    public void create(Card entity) {

    }

    @Override
    public Optional<Card> find(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Card> findAll() {
        return null;
    }

    @Override
    public Optional<Card> findCardByNumber(String number) {
        try {
            return executePreparedForGenericEntity(selectByNumberExpression,
                    this::extractResultCatchingException, st -> st.setString(1, number));
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    @Override
    public void update(Card entity) {

    }

    @Override
    public boolean updateCardRecipientAmount(BigDecimal amount, String cardRecipientNumber) {
        try {
            if (executePreparedUpdate(updateByNumberExpression,
                    st -> fillCardRecipientParameters(st, amount, cardRecipientNumber)) > 0) {
                LOG.info("Updated successfully. New amount for card {} is {}", cardRecipientNumber, amount);
                return true;
            } else {
                LOG.error("Update error occurred");
                return false;
            }
        } catch (InterruptedException e) {
            LOG.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    protected String getTableName() {
        return CARD_TABLE_NAME;
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
    protected String getIdFieldName() {
        return ID_FIELD_NAME;
    }

    @Override
    protected String getDelimiter() {
        return QUERY_AND_COMMA;
    }

    private String getAmountFieldName() {
        return AMOUNT_FIELD_NAME;
    }

    private String getNumberFieldName() {
        return NUMBER_FIELD_NAME;
    }

    @Override
    protected Card extractResult(ResultSet rs) throws SQLException {
        return new Card(rs.getLong(ID_FIELD_NAME), rs.getString(HOLDER_NAME_FIELD_NAME),
                rs.getString(NUMBER_FIELD_NAME), rs.getBigDecimal(AMOUNT_FIELD_NAME));
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Card entity) throws SQLException {

    }

    @Override
    protected void fillInsertingEntity(PreparedStatement statement, Card entity) throws SQLException {

    }

    @Override
    protected void fillUpdatingEntity(PreparedStatement statement, Card entity) throws SQLException {

    }

    private void fillCardRecipientParameters(PreparedStatement statement, BigDecimal amount, String recipientCardNumber)
            throws SQLException {
        statement.setBigDecimal(1, amount);
        statement.setString(2, recipientCardNumber);
    }

    public static CardDao getInstance() {
        return MethodCardDao.Holder.INSTANCE;
    }

    private static class Holder {
        public static final CardDao INSTANCE = new MethodCardDao(ConnectionPool.transactionalInstance());
    }
}
