package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.MischiefPerms;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class LangManager {
    private static MischiefPerms plugin;
    private static final HashMap<String, FileConfiguration> langMaps = new HashMap<>();

    public static void init(MischiefPerms plugin) {
        LangManager.plugin = plugin;
        for(String lang : PluginConfig.getLanguages()) {
            String location = String.format("lang/%s.yml", lang);
            File intendedPath = new File(plugin.getDataFolder(), location);
            if(!intendedPath.exists())
                plugin.saveResource(location, false);
            YamlConfiguration cfg = new YamlConfiguration();
            try {
                cfg.load(intendedPath);
            } catch (IOException | InvalidConfigurationException exception) {
                MischiefPerms.log("Error loading language file <%s>!", Level.SEVERE, lang);
                exception.printStackTrace();
            }
            langMaps.put(lang, cfg);
        }
    }

    public static String getString(CommandSender sender, String key, Object... args) {
        //TODO: Run checks to see if we got per-user language
        return getString(key, args);
    }

    public static String getString(String key, Object... args) {
        return getString(PluginConfig.getDefaultLanguage(), key, args);
    }

    public static String getString(String language, String key, Object... args) {
        String msg = langMaps.get(language).getString(String.format("messages.%s", key));
        if(args.length > 0 && msg != null)
            msg = String.format(msg, args);
        return String.format("[%s] %s", plugin.getName(), msg);
    }
}
