package com.kildall.mimision2;

import com.kildall.mimision2.commands.Mimir;
import com.kildall.mimision2.utils.Messages;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mimision2 extends JavaPlugin {

    private static Mimision2 instance;

    public static Mimision2 getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;

        if(!Messages.messagesFileExists()){
            Messages.createDefaultFile();
        }
        getCommand("mimir").setExecutor(new Mimir());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
