package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.utils.FileUtils;
import com.mischiefsmp.perms.MischiefPerms;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.HashMap;

public class LangManager {
    private static final HashMap<String, FileConfiguration> langMaps = new HashMap<>();

    public static void init() {
        for(String lang : PluginConfig.getLanguages()) {
            langMaps.put(lang, FileUtils.loadConfig(String.format("lang/%s.yml", lang)));
        }
    }

    public static String getString(CommandSender sender, String key, Object... args) {
        String language = PluginConfig.getDefaultLanguage();
        if(sender instanceof ConsoleCommandSender) {
            language = PluginConfig.getConsoleLanguage();
        }
        //TODO: Add per user language

        return getString(language, key, args);
    }

    public static String getString(String key, Object... args) {
        return getString(PluginConfig.getDefaultLanguage(), key, args);
    }

    //TODO: Make sure that we check if the key exists, if not check for the default language and if that doesnt exist when send no key msg
    public static String getString(String language, String key, Object... args) {
        String msg = langMaps.get(language).getString(String.format("messages.%s", key));
        if(args.length > 0 && msg != null)
            msg = String.format(msg, args);
        if(msg == null)
            msg = String.format("No String for key <%s>", key);
        return msg;
    }
}
