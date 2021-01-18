package test.test.database;

import test.test.Test;

import java.sql.*;
import java.util.Date;

public class mySQLData {
    private Test plugin;
    public mySQLData(Test plugin) {
        this.plugin = plugin;
    }

    public void createData(String nickName, String ocelotName, Date date) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = plugin.SQL.getConnection().prepareStatement("INSERT INTO test1 (nickname, ocelotname, time) VALUE (?, ?, ?)");
            preparedStatement.setString(1, nickName);
            preparedStatement.setString(2, ocelotName);
            preparedStatement.setString(3, date.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
