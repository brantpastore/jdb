package com.github.brantpastore;

import com.github.brantpastore.util.Messages;
import net.dv8tion.jda.client.events.group.GroupUserJoinEvent;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.Arrays;

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
        guild = event.getGuild();

        String[] initInput = message.getContentRaw().split(" ");
        String lowered = Arrays.toString(initInput);
        lowered = lowered.toLowerCase();
        lowered = lowered.replaceAll("[^ a-zA-Z0-9!:/.-]",""); // TODO: regex can make this a lot slower than it needs to be. Re-implement this at a later date.
        String[] command = lowered.split(" ");

        /**
         * We ignore any message not sent from a user.
         * TODO:
         *  PM commands to sadd to ban list
         *  Access level requirement
         *  Admin level commands aka kicking, banning, etc
         *  new user welcoming message (and options to customize it per name basis)
         */
        if (!author.isBot() && guild != null) {
            if ("!play".equals(command[0]) && command.length == 2) {
                musicMgr.getInstance().loadAndPlay(event.getTextChannel(), command[1]);
            } else if ("!queue".equals(command[0]) && command.length == 2) {
                sendMessage(channel, "Not implemented");
            } else if ("!skip".equals(command[0])) {
                musicMgr.getInstance().skipTrack(event.getTextChannel());
            } else if ("!volume".equals(command[0])) {
                int vol = Integer.parseInt(command[1]);
                sendMessage(channel, "Setting volume to " + command[1]);
                musicMgr.getInstance().getGuildAudioPlayer(guild).player.setVolume(vol);
            } else if ("whos".equals(command[0]) && "your".equals(command[1]) && "daddy".equals(command[2])) {
                sendMessage(channel, Messages.OWNER.toString());
            } else if ("who".equals(command[0]) && "am".equals(command[1]) && "i".equals(command[2])) {
                sendMessage(channel, "Your name is " + author.getName());
            } else if ("!help".equals(command[0])) {
                sendMessage(channel, "[Wowees List of commands]");
                sendMessage(channel, "WhosYourDaddy - returns who the owner of this bot is.");
                sendMessage(channel, "WhoAmI - returns your username.");
                sendMessage(channel, "!play - plays the video URL to the audio stream");
                sendMessage(channel, "[NOT IMPLEMENTED] !queue - Adds an audiostream to the current queue.");
                sendMessage(channel, "!skip - stops the current audistream and starts the next one in queue.");
                sendMessage(channel, "[NOT IMPLEMENTED] !volume - sets the volume of the audiostream for all users in the channel.");
            }
        }
        super.onMessageReceived(event);
    }

    public void sendMessage(MessageChannel channel, String message) {
        channel.sendTyping().queue();
        channel.sendMessage(message).queue();
    }

    @Override
    public void onGroupUserJoin​(GroupUserJoinEvent event) {
        //TODO
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) {
            return;
        }
        PrivateChannel channel = event.getChannel();
        Message message = event.getMessage();

        String[] initInput = message.getContentRaw().split(" ");
        String lowered = Arrays.toString(initInput);
        lowered = lowered.toLowerCase();
        lowered = lowered.replaceAll("[^ a-zA-Z]","");
        String[] command = lowered.split(" ");

        if (Array.getLength(command) == 1) {
            if ("login".equals(command[0])) {
                sendPrivateMessage(channel, "Not yet implemented");
                // TODO
            } else if ("level".equals(command[0])) {
                sendPrivateMessage(channel, "Not yet implemented");
            } else if ("help".equals(command[0])) {
                sendPrivateMessage(channel, "[Wowees List of commands]");
                sendPrivateMessage(channel, "Commands are case-insensitive");
                sendPrivateMessage(channel, "Whos Your Daddy - returns who the owner of this bot is.");
                sendPrivateMessage(channel, "Who Am I - returns your username.");
                sendPrivateMessage(channel, "get level - returns the level for the user");
                sendPrivateMessage(channel, "[NOT IMPLEMENTED] level - Grants the requested level to the chosen user.");
                sendPrivateMessage(channel, "[NOT IMPLEMENTED] login - Logs you in and applies your access level to this bot.");
            }
        } else if (Array.getLength(command) == 2) {
            if ("game".equals(command[0])) {
                StringBuilder str = new StringBuilder();
                for (int index = 1; index < command.length; index++) { // index of 1 so we dont include the command part of the string
                    str.append(command[index]);
                    str.append(" ");
                }
                DiscordMasterBot.getJDA().getPresence().setGame(Game.playing(str.toString()));
            }
        } else if (Array.getLength(command) == 3) {
            if ("get".equals(command[0]) && "level".equals(command[1])) {
                try {
                    if (command[2] != null) {
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
            } else if ("whos".equals(command[0]) && "your".equals(command[1]) && "daddy".equals(command[2])) {
                sendMessage(channel, Messages.OWNER.toString());
            } else if ("who".equals(command[0]) && "am".equals(command[1]) && "i".equals(command[2])) {
                sendMessage(channel, "Your name is " + author.getName());
            }
        }
    }

    public void sendPrivateMessage(PrivateChannel channel, String message) {
        channel.sendTyping().queue();
        channel.sendMessage(message).queue();
        System.out.println(message);
    }
}
