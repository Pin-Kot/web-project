package com.epam.jwd.audiotrack_ordering.dao;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.db.ResultSetExtractor;
import com.epam.jwd.audiotrack_ordering.db.StatementPreparer;
import com.epam.jwd.audiotrack_ordering.entity.Entity;
import com.epam.jwd.audiotrack_ordering.exception.EntityExtractionFailedException;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public abstract class CommonDao<T extends Entity> implements EntityDao<T> {

    protected static final String SELECT_ALL_FROM = "select %s from ";
    protected static final String WHERE_FIELD = "where %s = ?";
    protected static final String SPACE = " ";
    protected static final String COMMA = ", ";

    protected static final String INSERT_INTO = "insert into %s (%s)";
    protected static final String SET = "set %s";
    protected static final String UPDATE = "update %s";
    protected static final String DELETE_FROM = "delete from %s";

    protected static final String QUERY = " = ? ";
    protected static final String QUERY_AND_COMMA = " = ?, ";
    protected static final String DOT = ".";
    protected static final String JOIN_ON = "join %s on %s = %s";
    protected static final String AND = "and %s = ?";

    protected final ConnectionPool pool;
    private final Logger logger;

    private final String selectAllExpression;
    private final String selectByIdExpression;
    private final String insertSql;
    private final String updateAllByEntityIdExpression;
    private final String deleteSql;

    protected CommonDao(ConnectionPool pool, Logger logger) {
        this.pool = pool;
        this.logger = logger;
        this.insertSql = format(INSERT_INTO, getTableName(), String.join(COMMA, getInsertFields())) + SPACE
                + getValues();
        this.selectAllExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName();
        this.selectByIdExpression = selectAllExpression + SPACE + format(WHERE_FIELD, getIdFieldName());
        this.updateAllByEntityIdExpression = format(UPDATE, getTableName()) + SPACE
                + format(SET, String.join(getDelimiter(), getFields())) + QUERY
                + format(WHERE_FIELD, getIdFieldName());
        this.deleteSql = format(DELETE_FROM, getTableName()) + SPACE + format(WHERE_FIELD, getIdFieldName());
    }

    @Override
    public void create(T entity) {
        try {
            final int rowsUpdated = executePreparedUpdate(insertSql, st -> fillInsertingEntity(st, entity));
            if (rowsUpdated > 0) {
                logger.info("Added successfully. New entity {}", entity);
            } else {
                logger.error("Add sql error occurred");
            }
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return executeStatement(selectAllExpression, this::extractResultCatchingException);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<T> find(Long id) {
        try {
            return executePreparedForGenericEntity(selectByIdExpression,
                    this::extractResultCatchingException, st -> st.setLong(1, id));
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }

    @Override
    public void update(T entity) {
        try {
            final int rowsUpdated = executePreparedUpdate(updateAllByEntityIdExpression,
                    st -> fillUpdatingEntity(st, entity));
            if (rowsUpdated > 0) {
                logger.info("Updated successfully. New user data {}", entity);
            } else {
                logger.error("Update error occurred");
            }
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public boolean delete(Long id) {
        try {
            final int rowsUpdated = executePreparedUpdate(deleteSql, st -> st.setLong(1, id));
            if (rowsUpdated > 0) {
                logger.info("Entity with id= {} deleted successfully", id);
                return true;
            } else {
                logger.error("Delete error occurred");
                return false;
            }
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    protected List<String> getTableFields() {
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < getFields().size(); i++) {
            newList.add(getTableName() + DOT + getFields().get(i));
        }
        return newList;
    }

    protected String getTableField(String tableName, String fieldName) {
        return tableName + DOT + fieldName;
    }

    protected List<T> executeStatement(String sql, ResultSetExtractor<T> extractor) throws InterruptedException {
        try (final Connection connection = pool.takeConnection();
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(sql)) {
            return extractor.extractAll(resultSet);
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
            logger.debug("sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            logger.error("could not extract entity", e);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Collections.emptyList();
    }

    protected T executeStatementForEntity(String sql, ResultSetExtractor<T> extractor) throws InterruptedException {
        try (final Connection connection = pool.takeConnection();
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery(sql)) {
            return extractor.extract(resultSet);
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
            logger.debug("sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            logger.error("could not extract entity", e);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return null;
    }

    protected List<T> executePreparedForEntities(String sql,
                                                 ResultSetExtractor<T> extractor,
                                                 StatementPreparer statementPreparation) throws InterruptedException {
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            if (statementPreparation != null) {
                statementPreparation.accept(statement);
            }
            final ResultSet resultSet = statement.executeQuery();
            return extractor.extractAll(resultSet);
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
            logger.debug("sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            logger.error("could not extract entity", e);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Collections.emptyList();
    }

    protected <G> Optional<G> executePreparedForGenericEntity(String sql, ResultSetExtractor<G> extractor,
                                                              StatementPreparer statementPreparation)
            throws InterruptedException {
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            if (statementPreparation != null) {
                statementPreparation.accept(statement);
            }
            final ResultSet resultSet = statement.executeQuery();
            return resultSet.next()
                    ? Optional.ofNullable(extractor.extract(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
            logger.debug("sql: {}", sql);
        } catch (EntityExtractionFailedException e) {
            logger.error("could not extract entity", e);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Optional.empty();
    }

    protected int executePreparedUpdate(String sql,
                                        StatementPreparer statementPreparation) throws InterruptedException {
        try (final Connection connection = pool.takeConnection();
             final PreparedStatement statement = connection.prepareStatement(sql)) {
            if (statementPreparation != null) {
                statementPreparation.accept(statement);
            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
            logger.debug("sql: {}", sql);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return 0;
    }

    protected boolean executeCompoundUpdate(String setSql, StatementPreparer setStatementPreparation,
                                            StatementPreparer insertStatementPreparation)
            throws InterruptedException {
        try {
            final Connection connection = pool.takeConnection();

            final PreparedStatement setStatement = connection.prepareStatement(setSql);
            if (setStatementPreparation != null) {
                setStatementPreparation.accept(setStatement);
            }
            setStatement.executeUpdate();

            final PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            if (insertStatementPreparation != null) {
                insertStatementPreparation.accept(insertStatement);
            }
            insertStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
            logger.debug("sql: {}", setSql);
            logger.debug("sql: {}", insertSql);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return false;
    }

    protected boolean executeListUpdate(String insertLinkSql, List<StatementPreparer> setStatementsPreparation,
                                        StatementPreparer insertStatementPreparation)
            throws InterruptedException {
        try {
            final Connection connection = pool.takeConnection();

            final PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            if (insertStatementPreparation != null) {
                insertStatementPreparation.accept(insertStatement);
            }
            insertStatement.executeUpdate();

            for (StatementPreparer setStatementPreparation : setStatementsPreparation) {
                final PreparedStatement setStatement = connection.prepareStatement(insertLinkSql);
                if (setStatementPreparation != null) {
                    setStatementPreparation.accept(setStatement);
                }
                setStatement.executeUpdate();
            }

            return true;
        } catch (SQLException e) {
            logger.error("sql exception occurred", e);
            logger.debug("sql: {}", insertLinkSql);
            logger.debug("sql: {}", insertSql);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return false;
    }

    protected T extractResultCatchingException(ResultSet rs) throws EntityExtractionFailedException {
        try {
            return extractResult(rs);
        } catch (SQLException e) {
            logger.error("sql exception occurred extracting entity from ResultSet", e);
            throw new EntityExtractionFailedException("could not extract entity", e);
        }
    }

    protected abstract String getTableName();

    protected abstract List<String> getFields();

    protected abstract List<String> getInsertFields();

    protected abstract String getValues();

    protected abstract String getIdFieldName();

    protected abstract String getDelimiter();

    protected abstract T extractResult(ResultSet rs) throws SQLException;

    protected abstract void fillEntity(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void fillInsertingEntity(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void fillUpdatingEntity(PreparedStatement statement, T entity) throws SQLException;
}
