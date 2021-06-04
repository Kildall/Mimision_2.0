package com.kildall.mimision2;

import com.kildall.mimision2.commands.AFKCommand;
import com.kildall.mimision2.commands.Mimir;
import com.kildall.mimision2.commands.NoMimir;
import com.kildall.mimision2.files.ConfigFile;
import com.kildall.mimision2.files.MessagesFile;
import com.kildall.mimision2.listeners.*;
import com.kildall.mimision2.utils.AFK;
import com.kildall.mimision2.utils.VotingUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mimision2 extends JavaPlugin {

    private static Mimision2 instance;

    public static Mimision2 getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        instance = this;
        ConfigFile.createConfigFile();
        MessagesFile.createMessagesConfigFile();

        getCommand("mimir").setExecutor(new Mimir());
        getCommand("nomimir").setExecutor(new NoMimir());
        getCommand("afk").setExecutor(new AFKCommand());

        getServer().getPluginManager().registerEvents(new onChat(), this);
        getServer().getPluginManager().registerEvents(new onInteraction(), this);
        getServer().getPluginManager().registerEvents(new onJoin(), this);
        getServer().getPluginManager().registerEvents(new onMovement(), this);
        getServer().getPluginManager().registerEvents(new onPlayerEnterBed(), this);
        getServer().getPluginManager().registerEvents(new onLeave(),this);

        AFK.checkIfPlayerAFK();
        VotingUtils.checkAvaiablePlayers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
