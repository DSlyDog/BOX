package net.whispwriting.servers;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;
import net.whispwriting.BOX;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BotLoader {

    public void start(){
        try {
            ThunderFile serverList = Thunderbolt.load("server-list", System.getProperty("user.dir"));
            List<String> serverStrLs = serverList.getStringList("servers");
            for (String id : serverStrLs) {
                Server server = new Server(id);
                server.load();
                BOX.servers.put(id, server);
            }
        } catch (FileLoadException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

}
