package net.whispwriting.servers;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.whispwriting.containers.Bag;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    private String id;
    private Map<String, Bag> bags = new HashMap<>();

    public Server(String id){
        this.id = id;
    }

    public boolean create(String name, String owner, int size){
        if (bags.containsKey(name))
            return false;

        Bag bag = new Bag(size, owner, name);
        bags.put(name, bag);
        return true;
    }

    public void add(String bagName, String item, int amount, TextChannel channel, Member member){
        if (!bags.containsKey(bagName)){
            channel.sendMessage("No bag exists by that name.").queue();
            return;
        }
        bags.get(bagName).add(item, amount, channel, member);
    }

    public void retrieve(String bagName, String item, int amount, TextChannel channel, Member member){
        if (!bags.containsKey(bagName)){
            channel.sendMessage("No bag exists by that name.").queue();
            return;
        }
        bags.get(bagName).retrieve(item, amount, channel, member);
    }

    public List<EmbedBuilder> open(String name, Member member, TextChannel channel){
        return bags.get(name).open(member, channel);
    }

    public void save(){
        for (Map.Entry<String, Bag> bagEntry : bags.entrySet()){
            Bag bag = bagEntry.getValue();
            File path = new File(System.getProperty("user.dir") + "/" + id);
            if (!path.exists())
                path.mkdir();
            try {
                bag.save(id);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FileLoadException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void load(){
        File serverDir = new File(System.getProperty("user.dir") + "/" + id);
        if (serverDir.isDirectory()){
            File[] files = serverDir.listFiles();
            for (File file : files){
                String nameRaw = file.getName();
                String name = nameRaw.substring(0, nameRaw.indexOf("."));
                ThunderFile bagFile = null;
                try {
                    bagFile = Thunderbolt.load(name, System.getProperty("user.dir") + "/" + id);
                } catch (FileLoadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String owner = bagFile.getString("owner");
                int slots = bagFile.getInt("slots");
                Bag bag = new Bag(slots, owner, name);
                Thunderbolt.unload(name);
                try {
                    bag.load(id);
                }catch(JSONException e){
                    e.printStackTrace();
                } catch (FileLoadException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bags.put(name, bag);
            }
        }
    }

}
