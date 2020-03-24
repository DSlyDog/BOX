package net.whispwriting.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class HelpCommand extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.equals("b!help")){
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("BOX - Help");
            builder.addField("General Information", "Between each part of a command, make sure you include " +
                    "two spaces. Box size is not currently limited. If storage becomes an issue, it will be.", false);
            builder.addField("b!help", "Shows the help dialog.", false);
            builder.addField("b!create  <name>  <size>  <owner>", "Create a new box. To create a box only you " +
                    "can access, type \"me\" for the owner. Otherwise, type the name of a role you want to grant access to.", false);
            builder.addField("b!add  <box>  <item>  <amount>", "Add the given amount of items to the box.", false);
            builder.addField("b!retrieve  <box>  <item>  <amount>", "Retrieve the given amount of items from " +
                    "the box.", false);
            builder.addField("b!open  <box>", "Open and view the contents of the given box.", false);
            event.getChannel().sendMessage(builder.build()).queue();
        }
    }
}
