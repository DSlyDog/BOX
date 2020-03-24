package net.whispwriting.containers;

import io.bluecube.thunderbolt.Thunderbolt;
import io.bluecube.thunderbolt.exceptions.FileLoadException;
import io.bluecube.thunderbolt.io.ThunderFile;
import io.bluecube.thunderbolt.org.json.JSONException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bag {

    private int slots;
    private String owner;
    private String name;
    private Map<String, Integer> items = new HashMap<>();

    public Bag(int slots, String owner, String name){
        this.slots = slots;
        this.owner = owner;
        this.name = name;
    }

    public void add(String item, int amount, TextChannel channel, Member member){
        if (member.getUser().getDiscriminator().equals(owner)) {
            add(item, amount, channel);
        }else{
            for (Role role : member.getRoles()){
                if (role.getName().equals(owner)) {
                    add(item, amount, channel);
                    return;
                }
            }
            channel.sendMessage("You are not allowed to open that bag.").queue();
        }
    }

    private void add(String item, int amount, TextChannel channel){
        if (items.containsKey(item)) {
            int current = items.get(item);
            current += amount;
            items.replace(item, current);
            channel.sendMessage("Items added.").queue();
        } else {
            if (items.size() <= slots) {
                items.put(item, amount);
                channel.sendMessage("Items added.").queue();
            }else
                channel.sendMessage("Sorry, that bag is full.").queue();
        }
    }

    public void retrieve(String item, int amount, TextChannel channel, Member member){
        if (member.getUser().getDiscriminator().equals(owner)) {
            retrieve(item, amount, channel);
        }else{
            for (Role role : member.getRoles()){
                if (role.getName().equals(owner)) {
                    retrieve(item, amount, channel);
                    return;
                }
            }
            channel.sendMessage("You are not allowed to open that bag.").queue();
        }
    }

    private void retrieve(String item, int amount, TextChannel channel){
        System.out.println(items.size());
        if (items.containsKey(item)) {
            int currentAmount = items.get(item);
            int finalAmount = currentAmount - amount;
            if (finalAmount < 0){
                channel.sendMessage("You don't have enough of " + item + " to retrieve that many. You currently have "
                + currentAmount + ".").queue();
            }else if (finalAmount == 0){
                items.remove(item);
                channel.sendMessage("Items retrieved. You have no more " + item + " left.").queue();
            }else{
                items.replace(item, finalAmount);
                channel.sendMessage("Items retrieved. You have " + finalAmount + " more " + item + " left.").queue();
            }
        } else {
            channel.sendMessage("You do not have any " + item + " in that bag.").queue();
        }
    }

    public List<EmbedBuilder> open(Member member, TextChannel channel){
        if (member.getUser().getDiscriminator().equals(owner)) {
            return open();
        }else{
            for (Role role : member.getRoles()){
                if (role.getName().equals(owner)) {
                    return open();
                }
            }
            channel.sendMessage("You are not allowed to open that bag.").queue();
            return null;
        }
    }

    private List<EmbedBuilder> open(){
        List<EmbedBuilder> builders = new ArrayList<>();
        int i=1;
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(name);
        System.out.println("Items Size: " + items.size());
        for (Map.Entry<String, Integer> entry : items.entrySet()){
            System.out.println(i);
            System.out.print(entry.getKey() + " ");
            System.out.println(entry.getValue());
            builder.addField(Integer.toString(i), entry.getKey() + " x" + entry.getValue(), false);
            i++;
            if (i == 26){
                builders.add(builder);
                builder = new EmbedBuilder();
                i = 1;
            }
        }
        builders.add(builder);
        return builders;
    }

    public void save(String id) throws IOException, FileLoadException, JSONException {
        ThunderFile file = Thunderbolt.load(name, System.getProperty("user.dir") + "/" + id);
        file.set("owner", owner);
        file.set("slots", slots);
        file.save();
        int i = 0;
        for (Map.Entry<String, Integer> item : items.entrySet()){
            String itemName = item.getKey();
            int amount = item.getValue();
            String toStore = itemName + "  " + amount;
            file.set(Integer.toString(i), toStore);
            i++;
        }
        Thunderbolt.unload(name);
    }

    public void load(String id) throws JSONException, IOException, FileLoadException {
        ThunderFile file = Thunderbolt.load(name, System.getProperty("user.dir") + "/" + id);
        int i=0;
        boolean loadLoop = true;
        while (loadLoop){
            try {
                String itemLine = file.getString(Integer.toString(i));
                System.out.println(itemLine);
                String[] itemLineSplit = itemLine.split(" {2}");
                items.put(itemLineSplit[0], Integer.parseInt(itemLineSplit[1]));
                i++;
            }catch(JSONException e){
                loadLoop = false;
            }
        }
        Thunderbolt.unload(name);
    }

}
