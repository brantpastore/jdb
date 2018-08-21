/**
 * A Discord bot written using
 * https://docs.javacord.org/api/v/3.0.0/overview-summary.html
 * Author:
 * Brant Pastore
 *
 */
package com.github.brantpastore;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import com.github.brantpastore.util.Database;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.sql.SQLOutput;

public class DiscordMasterBot {
    private static JDA jda = null;
    private static JDABuilder jdaBuilder = null;
    private static Database database = null;
    private static Settings settings = new Settings();
    private static MessageHandler msgHandler = new MessageHandler();

    public static void login() {
        try {
            jda = new JDABuilder(AccountType.BOT).setToken(settings.getToken()).buildBlocking();
            Database.getInstance();
            System.out.println("Logged in!");
            System.out.println("You can add this bot to your server with the url " + Settings.getAuthURL());
        } catch (LoginException e) {
            e.printStackTrace();
            e.getMessage();
        } catch (InterruptedException e) {
            e.printStackTrace();
            e.getMessage();
        }
    }
    public static void main(String[] args) {
        login();
        jda.addEventListener(msgHandler);
    }
}
