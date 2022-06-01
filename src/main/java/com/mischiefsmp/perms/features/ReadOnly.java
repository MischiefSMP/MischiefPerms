package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.FileUtils;
import com.mischiefsmp.perms.MischiefPerms;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;

public class ReadOnly {
    private static FileConfiguration CMD_INFO;

    public static void init(MischiefPerms plugin) {
        CMD_INFO = FileUtils.loadConfig("readonly/cmdinfo.yml");

        //This folder is called readonly, if we for some reason cannot delete it it doesnt matter too much.
        //Make sure to put a "WARNING - DONT EDIT" message into every .yml file in there
        final File df = plugin.getDataFolder();
        new File(df, "readonly/cmdinfo.yml").delete();
        new File(df, "readonly").delete();
    }

    //Example: getCMDUsage("perms.group-create");
    public static String getCMDUsage(String cmdKey) {
        return CMD_INFO.getString(String.format("commands.%s.usage", cmdKey));
    }

    //Example: getCMDPerm("perms.group-create");
    public static String getCMDPerm(String cmdKey) {
        return CMD_INFO.getString(String.format("commands.%s.permission", cmdKey));
    }

    //Example: getCMDPerm("perms.group-create");
    public static String getCMDExec(String cmdKey) {
        return CMD_INFO.getString(String.format("commands.%s.exec", cmdKey));
    }

    //Example: getCMDHelp(sender, "perms");
    public static ArrayList<String> getCMDHelp(CommandSender sender, String cmdKey) {
        ArrayList<String> result = new ArrayList<>();
        ConfigurationSection section = CMD_INFO.getConfigurationSection(String.format("commands.%s", cmdKey));
        if(section == null)
            return result;

        for(String key : section.getKeys(false)) {
            String permission = CMD_INFO.getString(String.format("commands.%s.%s.permission", cmdKey, key));
            if(permission != null && sender.hasPermission(permission)) {
                String usage = CMD_INFO.getString(String.format("commands.%s.%s.usage", cmdKey, key));
                result.add(usage);
            }
        }

        return result;
    }
}
