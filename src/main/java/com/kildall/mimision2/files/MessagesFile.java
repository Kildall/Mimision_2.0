package com.kildall.mimision2.files;


import com.kildall.mimision2.Mimision2;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class MessagesFile {

    private static final Mimision2 plugin = Mimision2.getInstance();
    private static final Logger logger = plugin.getLogger();
    private static FileConfiguration messagesConfig;

    public static void createMessagesConfigFile(){
        logger.info("Checking Messages.yml file!");
        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if(!messagesFile.exists()){
            logger.info("Creating Messages.yml file...");
            plugin.saveResource("messages.yml", false);
            logger.info("Done!");
        } else {
            logger.info("Messages file already exists!");
        }

        messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }

    }

    public static FileConfiguration getMessages(){
        return messagesConfig;
    }
}
