package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.FileUtils;
import com.mischiefsmp.perms.MischiefPerms;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

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

    //Example: getCMDUsage("group.create");
    public static String getCMDUsage(String cmdKey) {
        return CMD_INFO.getString(String.format("commands.%s.usage", cmdKey));
    }

    //Example: getCMDPerm("group.create");
    public static String getCMDPerm(String cmdKey) {
        return CMD_INFO.getString(String.format("commands.%s.permission", cmdKey));
    }
}
