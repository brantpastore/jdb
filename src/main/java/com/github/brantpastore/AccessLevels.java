package com.github.brantpastore;

import com.github.brantpastore.util.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.*;

/**
 * This class handles all permission-level commands.
 * ex. Add Admin, Remove Admin, Add User, Remove User, Login
 * Different users have different access levels
 * EX.
 *
 */

public class AccessLevels {
    /**
     * Definitions for the different user roles.
     * USER_LEVEL: can play music
     * MOD_LEVEL: can skip, pause, set the volume for the music stream, and kick users from the server
     * ADMIN_LEVEL: can do all of the above aswell as ban users from the server.
     *
     * TODO:
     *  Add more if necessary/
     */
    public enum accLevel {
        USER_LEVEL,  // 0
        MOD_LEVEL,   // 1
        ADMIN_LEVEL; // 2
    }

    public static final String UID_ERROR = "0"; // 0 - UID does not exist
    public static final String USERNAME_ERROR = "1"; // 1 - Username does not exist
    public static final String SUCCESS = "2"; // 2 - No error occured


    public static final String GET_LEVEL = "getLevel";
    public static final String GET_UID = "uid";
    //        GRANT_LEVEL("grant"),
//        REMOVE_LEVEL("remove"),
//        GET_LEVEL("get"),
//        GET_UID("uid");
//
//        private final String cmd;
//
//        private accCommands(String s) {
//            cmd = s;
//        }
//    }
    private static AccessLevels accessLevels;
    private static ArrayList<String> commands; // Maybe make this a map? User could be the key and the value can be the access level.
    private static final String dbString = "[Database][AccessLevels]: ";

    private static final String INVALID_LEVEL = "[AccessLevel]: You do not have the authority to run that command.";

    /**
     * Used to retrieve the UID from the users table
     * @param u
     *      This is a string containing the username of the account we need
     * @return
     */
    public static String getUID(String u) {
        ResultSet res = null;
        try (PreparedStatement stmt = Database.getInstance().getStatement(GET_UID)) {
            stmt.setString(1, u);
            System.out.println(dbString + " Fetching UID for user " + u);
            res = stmt.executeQuery();
            if(res.next()) {
                String uid = res.getString("UID");
                System.out.println(dbString + "Their UID is " + uid);
                return uid;
            } else {
                return UID_ERROR;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return UID_ERROR;
    }

    /**
     * Returns the value in the level field of the access_level table
     * @param u
     * @return
     */
    public static String getAccessLevel(String u) {
        ResultSet r = null;
        try {
            PreparedStatement stmt = Database.getInstance().getStatement(GET_LEVEL);
            String res = getUID(u);
            if (res != UID_ERROR) {
                stmt.setString(1, res);
                r = stmt.executeQuery();
            } else {
                System.out.println(dbString + "The user " + u + " does not have any permissions.");
                return dbString + "That user does not have permissions.";
            }
            if (r.next()) {
                String level = r.getString("level");
                System.out.println(dbString + "Successfully retrieved " + u + " access level " + level);
                return level;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbString + "Error retrieving Access Level!";
    }

    /**
     * This function is used to change the level of the specified user.
     * @param u
     *      The user were adding
     * @return
     * @throws SQLException
     */
    public static boolean Grant(String u) throws SQLException
    {
        if (commands.contains(u)) {
            return false;
        }
        return false;
    }
}
