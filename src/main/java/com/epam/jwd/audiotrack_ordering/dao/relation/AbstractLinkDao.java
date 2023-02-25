package com.epam.jwd.audiotrack_ordering.dao.relation;

import com.epam.jwd.audiotrack_ordering.db.ConnectionPool;
import com.epam.jwd.audiotrack_ordering.db.ResultSetExtractor;
import com.epam.jwd.audiotrack_ordering.db.StatementPreparer;
import com.epam.jwd.audiotrack_ordering.entity.dto.Link;
import com.epam.jwd.audiotrack_ordering.exception.EntityExtractionFailedException;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public abstract class AbstractLinkDao<T extends Link> implements LinkDao<T> {

    protected final ConnectionPool pool;
    private final Logger logger;

    private final String insertLinkSql;
    ///
    private final String insertLinkByIdSql;
    private final String selectLinkByIdExpression;

    public AbstractLinkDao(ConnectionPool pool, Logger logger) {
        this.pool = pool;
        this.logger = logger;

        ///
        this.insertLinkByIdSql = "insert into album_artist_link (album_id, artist_id) values (LAST_INSERT_ID(), ?)";
        this.insertLinkSql = "insert into album_artist_link (album_id, artist_id) values (?, ?)";
        this.selectLinkByIdExpression = "select album_id, artist_id from album_artist_link where album_id = ? and " +
                "artist_id = ?";
    }

    @Override
    public void create(T link) {
        try {
            final int rowsUpdated = executePreparedUpdate(insertLinkSql, st -> fillLink(st, link));
            if (rowsUpdated > 0) {
                logger.info("Added successfully. New link {}", link);
            } else {
                logger.error("Update sql error occurred");
            }
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    ///
    public void createById(Long rightId) {
        try {
            final int rowsUpdated = executePreparedUpdate(insertLinkByIdSql, st -> fillLinkById(st, rightId));
            if (rowsUpdated > 0) {
                logger.info("Added successfully. By id {}", rightId);
            } else {
                logger.error("Update sql error occurred");
            }
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Optional<T> findLink(Long leftEntityId, Long rightEntityId) {
        try {
            return executePreparedForGenericLink(selectLinkByIdExpression,
                    this::extractResultCatchingException, st -> {
                        st.setLong(1, leftEntityId);
                        st.setLong(2, rightEntityId);
                    });
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
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

    protected <G> Optional<G> executePreparedForGenericLink(String sql, ResultSetExtractor<G> extractor,
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
            logger.error("could not extract link", e);
        } catch (InterruptedException e) {
            logger.info("takeConnection interrupted", e);
            Thread.currentThread().interrupt();
            throw e;
        }
        return Optional.empty();
    }

    protected int executePreparedUpdate(String sql,
                                        StatementPreparer statementPreparation) throws InterruptedException {
        try (final Connection con = pool.takeConnection();
             final PreparedStatement statement = con.prepareStatement(sql)) {
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
            return extractResultLink(rs);
        } catch (SQLException e) {
            logger.error("sql exception occurred extracting link from ResultSet", e);
            throw new EntityExtractionFailedException("could not extract link", e);
        }
    }

    protected abstract String getTableName();

    protected abstract List<String> getFields();

    protected abstract T extractResultLink(ResultSet rs) throws SQLException;

    protected abstract void fillLink(PreparedStatement statement, T link) throws SQLException;

    protected abstract void fillLinkById(PreparedStatement statement, Long id) throws SQLException;
}
