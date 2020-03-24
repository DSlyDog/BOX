package net.whispwriting.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.BOX;
import net.whispwriting.servers.Server;

import javax.annotation.Nonnull;

public class CreateCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()){
            String[] message = event.getMessage().getContentRaw().split(" {2}");
            if (message[0].equals("b!create")){
                if (message.length < 4){
                    event.getChannel().sendMessage("Not enough arguments.").queue();
                    event.getChannel().sendMessage("b!create  <name>  <size>  <owner>").queue();
                    return;
                }
                String serverStr = event.getGuild().getId();
                Server server = BOX.servers.get(serverStr);
                User user = event.getAuthor();
                if (message[3].equals("me"))
                    if (server.create(message[1], user.getDiscriminator(), Integer.parseInt(message[2])))
                        event.getChannel().sendMessage("Bag created successfully.").queue();
                    else
                        event.getChannel().sendMessage("A bag by that name already exists.").queue();
                else
                    if (server.create(message[1], message[3], Integer.parseInt(message[2])))
                        event.getChannel().sendMessage("Bag created successfully.").queue();
                    else
                        event.getChannel().sendMessage("A bag by that name already exists.").queue();
            }
        }
    }
}
