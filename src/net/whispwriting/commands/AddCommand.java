package net.whispwriting.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.BOX;
import net.whispwriting.servers.Server;

import javax.annotation.Nonnull;
import java.util.List;

public class AddCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String[] message = event.getMessage().getContentRaw().split(" {2}");
        if (message[0].equals("b!add") && !event.getAuthor().isBot()){
            if (message.length < 4){
                event.getChannel().sendMessage("Not enough arguments.").queue();
                event.getChannel().sendMessage("b!add  <box>  <item>  <amount>").queue();
                return;
            }
            String serverStr = event.getGuild().getId();
            Server server = BOX.servers.get(serverStr);
            User user = event.getAuthor();
            Member member = event.getGuild().getMember(user);
            server.add(message[1], message[2], Integer.parseInt(message[3]), event.getChannel(), member);
        }
    }
}
