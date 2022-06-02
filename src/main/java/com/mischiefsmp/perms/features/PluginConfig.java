package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.MischiefPerms;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.List;

public class PluginConfig {
    private static FileConfiguration config;

    public static void init(MischiefPerms plugin) {
        config = plugin.getConfig();
        if(!new File(plugin.getDataFolder(), "config.yml").exists())
            plugin.saveResource("config.yml", false);
    }

    public static String getDefaultLanguage() {
        return config.getString("language");
    }

    public static String getConsoleLanguage() {
        return config.getString("console-language");
    }

    public static List<String> getLanguages() {
        return config.getStringList("languages");
    }
}
