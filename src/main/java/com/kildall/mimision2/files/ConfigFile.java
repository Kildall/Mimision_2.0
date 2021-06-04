package com.kildall.mimision2.files;

import com.kildall.mimision2.Mimision2;
import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class ConfigFile {
    private static final Mimision2 plugin = Mimision2.getInstance();
    private static final Logger logger = plugin.getLogger();

    public static void createConfigFile(){
        logger.info("Checking config.yml file!");
        if(!plugin.getDataFolder().exists()){
            logger.info("Creating config.yml file...");
            plugin.saveDefaultConfig();
            logger.info("Done!");
        } else {
            logger.info("Config files already exist.");
        }
    }

    public static FileConfiguration getConfigFile(){
        return plugin.getConfig();
    }
}
