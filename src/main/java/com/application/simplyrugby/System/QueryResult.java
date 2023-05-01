package com.application.simplyrugby.System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class was created to solve a SQLIte 'Database Class is locked' issue<br>
 * most of the methods of DBTools use local Connections and prepared statements that are then closed locally<br>
 * usually with a try-catch with resources<br>
 * but some part of the program need to manipulate a resulSet that need the connections to be closed remotely.<br>
 * After some research, this solution is tried. The class will encapsulate the resultSet, statement and connection<br>
 * and send it to the calling class. This will allow the calling function to make sure the connections are closed.
 * By implementing the AutoClosable interface and overriding the close() method, this will also allows<br>
 * the QueryResult object to be automatically closed in a Try with resources.
 */
public class QueryResult implements AutoCloseable{

    // declaring the 3 needed objects.
    private ResultSet resultSet;
    private Connection connection;
    private PreparedStatement statement;

    public QueryResult(ResultSet resultSet,Connection connection,PreparedStatement statement){
        this.resultSet=resultSet;
        this.connection=connection;
        this.statement=statement;
    }

    /**
     * Send back the requested resultSet.
     * @return The requested resultSet
     * @throws ValidationException Error if the resultSet is empty.
     */
    public ResultSet getResultSet() throws ValidationException{
        // test if empty
        if (resultSet!=null)
            return resultSet;
        else
            throw new ValidationException("No results returned from the database");
    }

    /**
     * overriding the close() method that is called to free the resources
     * @throws SQLException Error if anu issues.
     */
    @Override
    public void close() throws SQLException {
        try{
            if (resultSet!=null)
                resultSet.close();
            if (statement!=null)
                statement.close();
            if (connection!=null)
                connection.close();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
