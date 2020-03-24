package net.whispwriting;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.whispwriting.commands.*;
import net.whispwriting.servers.BotLoader;
import net.whispwriting.servers.OnServerJoin;
import net.whispwriting.servers.Server;
import net.whispwriting.tasks.SaveTask;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BOX {

    public static JDA jda;
    public static Map<String, Server> servers = new HashMap<>();

    public static void main(String[] args) throws LoginException {
        jda = new JDABuilder(AccountType.BOT).setToken("").build();
        jda.getPresence().setActivity(Activity.playing("When Robots Ruled the World"));
        jda.addEventListener(new OnServerJoin());
        jda.addEventListener(new AddCommand());
        jda.addEventListener(new CreateCommand());
        jda.addEventListener(new OpenCommand(jda));
        jda.addEventListener(new RetrieveCommand());
        jda.addEventListener(new SaveCommand());
        jda.addEventListener(new HelpCommand());

        Timer load = new Timer();
        load.schedule(new TimerTask() {
            @Override
            public void run() {
                BotLoader botLoader = new BotLoader();
                botLoader.start();
                System.out.println("Servers: " + servers.size());
            }
        }, 2000);

        Timer timer = new Timer();
        timer.schedule(new SaveTask(), 300000, 300000);
    }

}
