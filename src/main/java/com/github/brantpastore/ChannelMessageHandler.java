package com.github.brantpastore;

import com.github.brantpastore.util.Messages;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class ChannelMessageHandler extends ListenerAdapter {
    public static MusicManager musicMgr = new MusicManager();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();                //The user that sent the message
        Message message = event.getMessage();           //The message that was received.
        MessageChannel channel = event.getChannel();    //This is the MessageChannel that the message was sent to.
        String[] command = message.getContentRaw().split(" ", 2);
        Guild guild = event.getGuild();

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
            if ("WhosYourDaddy".equals(command[0])) {
                channel.sendMessage(Messages.OWNER.toString()).queue();
            } else if ("WhoAmI".equals(command[0])) {
                channel.sendMessage("Your name is " + author.getName()).queue();
            }else if ("!get_level".equals(command[0])){
                try {
                    if (!command[1].isEmpty()) {
                        String res = AccessLevels.getAccessLevel(command[1]);
                        switch (res) {
                            case "0": // USER_LEVEL
                                channel.sendMessage(res + " is a user").queue();
                                break;

                            case "1": // MOD_LEVEL
                                channel.sendMessage(res + " is a moderator").queue();
                                break;

                            case "2": // ADMIN_LEVEL
                                channel.sendMessage(res + " is an administrator").queue();
                                break;
                        }
                    } else {
                        channel.sendMessage("That user does not have permissions! [You can add an account by using the command !add]").queue();
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                    channel.sendMessage(e.getMessage()).queue();
                }
            } else if ("!grant_level".equals(command[0])) {
                // TODO
            }else if ("!login".equals(command[0])) {
                // TODO
            } else if ("!play".equals(command[0]) && command.length == 2) {
                musicMgr.getInstance().loadAndPlay(event.getTextChannel(), command[1]);
            } else if ("!queue".equals(command[0]) && command.length == 2) {
                // TODO
            } else if ("!skip".equals(command[0])) {
                musicMgr.getInstance().skipTrack(event.getTextChannel());
            } else if ("!volume".equals(command[0])) {
                // TODO
            } else if ("!list".equals(command[0])) {
                channel.sendMessage("[Wowees List of commands]").queue();
                channel.sendMessage("WhosYourDaddy - returns who the owner of this bot is.").queue();
                channel.sendMessage("WhoAmI - returns your username.").queue();
                channel.sendMessage("!play - plays the video URL to the audio stream").queue();
                channel.sendMessage("[NOT IMPLEMENTED] !queue - Adds an audiostream to the current queue.").queue();
                channel.sendMessage("!skip - stops the current audistream and starts the next one in queue.").queue();
                channel.sendMessage("[NOT IMPLEMENTED] !volume - sets the volume of the audiostream for all users in the channel.").queue();
                channel.sendMessage("[NOT IMPLEMENTED] !level - Grants the requested level to the chosen user.").queue();
                channel.sendMessage("[NOT IMPLEMENTED] !login - Logs you in and applies your access level to this bot.").queue();
            }
        }
        super.onMessageReceived(event);
    }
}
