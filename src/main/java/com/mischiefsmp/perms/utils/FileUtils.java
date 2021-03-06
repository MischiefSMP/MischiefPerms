package com.mischiefsmp.perms.utils;

import com.mischiefsmp.perms.MischiefPerms;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;

public class FileUtils {
    private static MischiefPerms plugin;

    public static void init(MischiefPerms plugin) {
        FileUtils.plugin = plugin;
    }

    public static FileConfiguration loadConfig(String name) {
        try {
            File cfgFinalFile = new File(plugin.getDataFolder(), name);
            if(!cfgFinalFile.exists())
                plugin.saveResource(name, false);

            YamlConfiguration cfg = new YamlConfiguration();
            cfg.load(cfgFinalFile);
            return cfg;
        } catch(Exception exception) {
            MischiefPerms.log("Error loading config for file <%s>", Level.SEVERE, name);
            exception.printStackTrace();
            return null;
        }
    }

    public static void deleteFile(File file) {
        if(!file.delete())
            MischiefPerms.log("File %s could not be deleted.", Level.WARNING, file.getAbsolutePath());
    }
}
