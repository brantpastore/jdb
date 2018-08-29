/**
 *     Copyright 2015-2016 Austin Keener
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.brantpastore.util;
import com.github.brantpastore.AccessLevels;

import java.sql.*;
import java.util.HashMap;

public class Database
{
    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String db = "wowee";
    private static final String user = "root";
    private static final String password = "Ninja2rk3wl!";
    private static final String dbString = "[Database][MySQL]: ";

    public enum dbInfo
    {
        driver("com.mysql.cj.jdbc.Driver"),
        host("jdbc:mysql://localhost:3306/"),
        db("wowee"),
        user("root"),
        password("Ninja2rk3wl!");

        private final String info;

        dbInfo(final String info) {
            this.info = info;
        }

        @Override
        public String toString() {
            return info;
        }
    }

    private static Database instance;

    private static Connection conn;
    private HashMap<String, PreparedStatement> preparedStatements;

    public static Database getInstance() {
            if (instance == null)
                instance = new Database();
        return instance;
    }

    public static Connection getConnection()
    {
        try {
            if (conn.isClosed())
                conn = setupConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static Connection setupConnection()
    {
        try {
            Class.forName(dbInfo.driver.toString());
            conn = DriverManager.getConnection(dbInfo.host.toString() + dbInfo.db.toString(), dbInfo.user.toString(), dbInfo.password.toString());
            System.out.println(dbString + "Connection Successful!");
        } catch (ClassNotFoundException e) {
            System.out.println(dbString + "Error establishing connection!");
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * TODO:
     * Store playlists users create in a database
     * store certain users who have certain access levels to the bot
     */
    private Database()
    {
        preparedStatements = new HashMap<String, PreparedStatement>();
        try {
            setupConnection();

            Statement statement = conn.createStatement();
            statement.setQueryTimeout(30);
            //statement.execute("PRAGMA foreign_keys = ON");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                    "Users(" +
                    "UID INT(11) NOT NULL AUTO_INCREMENT," +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (UID)," +
                    "UNIQUE INDEX `UID` (UID)" +
                    ")");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                    "AccessLevels(" +
                    "UID INT(11) NOT NULL AUTO_INCREMENT," +
                    "username VARCHAR(50) NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "PRIMARY KEY (UID)," +
                    "UNIQUE INDEX `UID` (UID)" +
                    ")");

            // Different Levels mean you can access certain commands depending on what level you are
            preparedStatements.put(AccessLevels.GET_LEVEL, conn.prepareStatement("SELECT level FROM access_levels WHERE UID = ?"));
            preparedStatements.put(AccessLevels.GET_UID, conn.prepareStatement("SELECT UID from users WHERE username = ?"));
            /* preparedStatements.put(AccessLevels.accCommands.GRANT_LEVEL, conn.prepareStatement("REPLACE INTO access_levels (uid) VALUES (?) "));
             preparedStatements.put(AccessLevels.accCommands.REMOVE_LEVEL, conn.prepareStatement("REPLACE level FROM access_levels WHERE UID = ?"));

            preparedStatements.put(Permissions.ADD_OP, conn.prepareStatement("REPLACE INTO Ops (id) VALUES (?)"));
            preparedStatements.put(Permissions.GET_OPS, conn.prepareStatement("SELECT id FROM Ops"));
            preparedStatements.put(Permissions.REMOVE_OPS, conn.prepareStatement("DELETE FROM Ops WHERE id = ?"));

            //TodoCommand
            preparedStatements.put(TodoCommand.ADD_TODO_LIST, conn.prepareStatement("INSERT INTO TodoLists (label, owner, locked) VALUES (?, ?, ?)"));
            preparedStatements.put(TodoCommand.ADD_TODO_ENTRY, conn.prepareStatement("INSERT INTO TodoEntries (listId, content, checked) VALUES (?, ?, ?)"));

             */
        }
        catch (SQLException e) {
            System.out.println(dbString + "SQL Exception!");
            System.out.println(e.getMessage());
        }
    }

    public PreparedStatement getStatement(String statementName)
    {
        if (!preparedStatements.containsKey(statementName))
            throw new RuntimeException(dbString + "The statement: '" + statementName + "' does not exist.");
        return preparedStatements.get(statementName);
    }

    public static int getAutoIncrement(PreparedStatement executedStatement, int col) throws SQLException
    {
        ResultSet autoIncrements = executedStatement.getGeneratedKeys();
        autoIncrements.next();
        return autoIncrements.getInt(col);
    }
}