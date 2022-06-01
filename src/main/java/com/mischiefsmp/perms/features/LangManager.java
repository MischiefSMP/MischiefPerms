package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.FileUtils;
import com.mischiefsmp.perms.MischiefPerms;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.HashMap;

public class LangManager {
    private static MischiefPerms plugin;
    private static final HashMap<String, FileConfiguration> langMaps = new HashMap<>();

    public static void init(MischiefPerms plugin) {
        LangManager.plugin = plugin;
        for(String lang : PluginConfig.getLanguages()) {
            langMaps.put(lang, FileUtils.loadConfig(String.format("lang/%s.yml", lang)));
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
        return msg;
    }
}
