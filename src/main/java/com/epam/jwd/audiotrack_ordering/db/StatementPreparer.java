package com.epam.jwd.audiotrack_ordering.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementPreparer {

    void accept(PreparedStatement statement) throws SQLException;
}
