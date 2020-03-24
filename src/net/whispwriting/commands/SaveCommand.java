package net.whispwriting.commands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.tasks.SaveTask;

import javax.annotation.Nonnull;
import java.util.Timer;

public class SaveCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.equals("b!save")){
            Timer timer = new Timer();
            timer.schedule(new SaveTask(), 0);
            event.getChannel().sendMessage("Saving complete.").queue();
        }
    }
}
