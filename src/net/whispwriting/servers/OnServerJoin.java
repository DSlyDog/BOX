package net.whispwriting.servers;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.whispwriting.BOX;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OnServerJoin extends ListenerAdapter {

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        String id = event.getGuild().getId();
        Server server = new Server(id);
        BOX.servers.put(id, server);
        ThunderFile serverList = new ThunderFile("server-list", System.getProperty("user.dir"));
        try {
            try {
                List<String> serverStrLs = serverList.getStringList("servers");
                serverStrLs.add(id);
                serverList.set("servers", serverStrLs);
                serverList.save();
            } catch (JSONException e) {
                List<String> serverStrLs = new ArrayList<>();
                serverStrLs.add(id);
                serverList.set("servers", serverStrLs);
                serverList.save();
            }
        }catch(IOException f){
            f.printStackTrace();
        }
    }
}
