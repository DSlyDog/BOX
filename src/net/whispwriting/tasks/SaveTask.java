package net.whispwriting.tasks;

import net.whispwriting.BOX;
import net.whispwriting.servers.Server;

import java.util.Map;
import java.util.TimerTask;

public class SaveTask extends TimerTask {
    @Override
    public void run() {
        for (Map.Entry<String, Server> serverMap : BOX.servers.entrySet()){
            serverMap.getValue().save();
            System.out.println("Saving...");
        }
    }
}
