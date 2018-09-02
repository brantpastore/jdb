package com.github.brantpastore;

import com.github.brantpastore.util.Messages;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.SQLException;

public class ChannelMessageHandler extends ListenerAdapter {
    private static MusicManager musicMgr;
    private User author;
    private Message message;
    private Guild guild;
    private MessageChannel channel;
    private String[] command;

    public ChannelMessageHandler() {
        musicMgr = new MusicManager();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        author = event.getAuthor();                //The user that sent the message
        message = event.getMessage();           //The message that was received.
        channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
        command = message.getContentRaw().split(" ", 2);
        guild = event.getGuild();

        /**
         * We ignore any message not sent from a user.
         * TODO:
         *  volume command for music/audio streams
         *  PM commands to set Access levels, add to ban list
         *  Access level requirement
         *  Admin level commands aka kicking, banning, etc
         *  new user welcoming message (and options to customize it per name basis)
         */
        if (!author.isBot() && guild != null) {
            if ("Whos Your Daddy".equals(command[0])) {
                sendMessage(channel, Messages.OWNER.toString());
            } else if ("Who Am I".equals(command[0])) {
                sendMessage(channel, "Your name is " + author.getName());
            } else if ("!play".equals(command[0]) && command.length == 2) {
                musicMgr.getInstance().loadAndPlay(event.getTextChannel(), command[1]);
            } else if ("!queue".equals(command[0]) && command.length == 2) {
                sendMessage(channel, "Not implemented");
            } else if ("!skip".equals(command[0])) {
                musicMgr.getInstance().skipTrack(event.getTextChannel());
            } else if ("!volume".equals(command[0])) {
                int vol = Integer.parseInt(command[1]);
                sendMessage(channel, "Setting volume to " + command[1]);
                musicMgr.getInstance().getGuildAudioPlayer(guild).player.setVolume(vol);
            } else if ("!list".equals(command[0])) {
                sendMessage(channel,"[Wowees List of commands]");
                sendMessage(channel,"WhosYourDaddy - returns who the owner of this bot is.");
                sendMessage(channel,"WhoAmI - returns your username.");
                sendMessage(channel,"!play - plays the video URL to the audio stream");
                sendMessage(channel,"[NOT IMPLEMENTED] !queue - Adds an audiostream to the current queue.");
                sendMessage(channel, "!skip - stops the current audistream and starts the next one in queue.");
                sendMessage(channel, "[NOT IMPLEMENTED] !volume - sets the volume of the audiostream for all users in the channel.");
            }
        }
        super.onMessageReceived(event);
    }

    public void sendMessage(MessageChannel channel, String message)
    {
        channel.sendTyping().queue();
        channel.sendMessage(message).queue();
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) {
            return;
        }

        PrivateChannel channel = event.getChannel();
        Message message = event.getMessage();
        String[] command = message.getContentRaw().split(" ", 2);
        System.out.println("Private message recieved: " + command[0]);
        if ("login".equals(command[0])) {
            sendPrivateMessage(channel, "Not yet implemented");
            // TODO
        } else if ("Whos Your Daddy".equals(command[0])) {
                sendMessage(channel, Messages.OWNER.toString());
            } else if ("Who Am I".equals(command[0])) {
                sendMessage(channel, "Your name is " + author.getName());
        } else if ("get_level".equals(command[0])){
            try {
                if (!command[1].isEmpty()) {
                    String res = AccessLevels.getAccessLevel(command[1]);
                    switch (res) {
                        case "0": // USER_LEVEL
                            sendPrivateMessage(channel, res + " is a user");
                            break;

                        case "1": // MOD_LEVEL
                            sendPrivateMessage(channel, res + " is a moderator");
                            break;

                        case "2": // ADMIN_LEVEL
                            sendPrivateMessage(channel, res + " is an administrator");
                            break;
                    }
                } else {
                    sendPrivateMessage(channel, "That user does not have permissions! [You can add an account by using the command !add]");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
                channel.sendMessage(e.getMessage()).queue();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else if ("level".equals(command[0])) {
            sendPrivateMessage(channel, "Not yet implemented");
        } else if ("help".equals(command[0])) {
            sendPrivateMessage(channel, "[Wowees List of commands]");
            sendPrivateMessage(channel, "WhosYourDaddy - returns who the owner of this bot is.");
            sendPrivateMessage(channel, "WhoAmI - returns your username.");
            sendPrivateMessage(channel, "get_level - returns the level for the user");
            sendPrivateMessage(channel, "[NOT IMPLEMENTED] level - Grants the requested level to the chosen user.");
            sendPrivateMessage(channel, "[NOT IMPLEMENTED] login - Logs you in and applies your access level to this bot.");
        }
    }

    public void sendPrivateMessage(PrivateChannel channel, String message) {
        channel.sendTyping().queue();
        channel.sendMessage(message).queue();
        System.out.println(message);
    }
}
