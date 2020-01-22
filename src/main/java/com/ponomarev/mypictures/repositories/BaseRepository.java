package com.ponomarev.mypictures.repositories;

import com.ponomarev.mypictures.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class BaseRepository {

    protected Statement statement;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;
    protected Connection connection;
    protected ResourceBundle bundle;

    public BaseRepository() {
    }

    public BaseRepository(String filename) {
        createBundle(filename);
    }

    public void executeWithResult(String query) {
        try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void execute(String query) {
        try {
            connection = Database.getConnection();
            statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        connection = Database.getConnection();
        return connection;
    }

    public void closeConnection() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createBundle(String file) {
        bundle = ResourceBundle.getBundle(file);
    }
}
