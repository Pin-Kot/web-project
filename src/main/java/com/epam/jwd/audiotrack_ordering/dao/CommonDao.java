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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.lang.String.join;

public abstract class CommonDao<T extends Entity> implements EntityDao<T> {

    protected static final String SELECT_ALL_FROM = "select %s from ";
    protected static final String WHERE_FIELD = "where %s = ?";
    protected static final String SPACE = " ";
    protected static final String COMMA = ", ";
    private static final String INSERT_INTO = "insert into %s (%s)";

    protected final ConnectionPool pool;
    private final String selectAllExpression;
    private final String selectByIdExpression;
    private final String insertSql;

    private final Logger logger;

    protected CommonDao(ConnectionPool pool, Logger logger) {
        this.pool = pool;
        this.logger = logger;
        this.selectAllExpression = format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName();
        this.selectByIdExpression = selectAllExpression + SPACE + format(WHERE_FIELD, getIdFieldName());
        this.insertSql = format(INSERT_INTO, getTableName(), join(COMMA, getFields()));

    }

    @Override
    public T create(T entity) {
        try {
            final int rowsUpdated = executePreparedUpdate(insertSql, st -> fillEntity(st, entity));
            if (rowsUpdated > 0) {
                return null;
            }
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return null;
        }
        return null;
    }

    @Override
    public List<T> read() {
        try {
            return executeStatement(selectAllExpression, this::extractResultCatchingException);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<T> read(Long id) {
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
    public T update(T entity) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
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

    protected abstract String getIdFieldName();

    protected abstract T extractResult(ResultSet rs) throws SQLException;

    protected abstract void fillEntity(PreparedStatement statement, T entity) throws SQLException;
}
