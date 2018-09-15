package com.github.brantpastore;

// TODO:
/*
*   Move this into the FileManager class to load these values from the config file.
*   Move the class functions to the DiscordMasterBot class.
 */
public class Settings {
    private static String token = "NDc5ODgwODE4ODM2MzczNTA0.Dly5Lw.sI6EZKrkpid7CyfNcNlGND_4Cr0";
    private static String authURL = "https://discordapp.com/api/oauth2/authorize?client_id=479880818836373504&scope=bot";

    public String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Settings.token = token;
    }

    public static String getAuthURL() {
        return authURL;
    }
}
