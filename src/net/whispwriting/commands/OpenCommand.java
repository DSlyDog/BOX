package net.whispwriting.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.BOX;
import net.whispwriting.servers.Server;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OpenCommand extends ListenerAdapter {

    private JDA bot;

    public OpenCommand(JDA bot){
        this.bot = bot;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()){
            String[] message = event.getMessage().getContentRaw().split(" {2}");
            if (message[0].equals("b!open")) {
                if (message.length < 2) {
                    event.getChannel().sendMessage("Not enough arguments.").queue();
                    event.getChannel().sendMessage("b!open  <box>").queue();
                    return;
                }
                Server server = BOX.servers.get(event.getGuild().getId());
                User user = event.getAuthor();
                Member member = event.getGuild().getMember(user);
                List<EmbedBuilder> builders = server.open(message[1], member, event.getChannel());
                for (EmbedBuilder builder : builders){
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setEmbed(builder.build());
                event.getChannel().sendMessage(messageBuilder.build()).queue();
                }
            }
        }
    }
}
