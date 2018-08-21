package com.github.brantpastore;

import com.github.brantpastore.util.Messages;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageHandler extends ListenerAdapter {
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
         */
        if (!author.isBot() && guild != null) {
            if ("WhosYourDaddy".equals(command[0])) {
            	channel.sendMessage(Messages.OWNER.toString()).queue();
            } else if ("WhoAmI".equals(command[0])) {
            	channel.sendMessage("Your name is " + author.getName()).queue();    
            } else if ("!youtube".equals(command[0]) && command.length == 2) {
            	 musicMgr.getInstance().loadAndPlay(event.getTextChannel(), command[1]);
            } else if ("!queue".equals(command[0]) && command.length == 2) {
            	
            } else if ("!skip".equals(command[0])) {
            	musicMgr.getInstance().skipTrack(event.getTextChannel());
            }
        }
        super.onMessageReceived(event);
    }
}
