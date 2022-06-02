package com.mischiefsmp.perms.features;

import com.mischiefsmp.perms.utils.FileUtils;
import com.mischiefsmp.perms.MischiefPerms;
import com.mischiefsmp.perms.utils.CmdInfo;
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
    //This returns the usage if no exec is set
    public static String getCMDExec(String cmdKey) {
        String path = String.format("commands.%s.exec", cmdKey);
        if(CMD_INFO.contains(path))
            return CMD_INFO.getString(path);
        return getCMDUsage(cmdKey);
    }

    public static String getCMDSuggestion(String cmdKey) {
        String path = String.format("commands.%s.suggest", cmdKey);
        if(CMD_INFO.contains(path))
            return CMD_INFO.getString(path);
        return getCMDUsage(cmdKey);
    }

    //Example: getCMDHelp(sender, "perms");
    //Returns a list of CmdInfo which contains usage, permission, execution and a translated description
    public static ArrayList<CmdInfo> getCMDHelp(CommandSender sender, String cmdKey) {
        ArrayList<CmdInfo> result = new ArrayList<>();
        ConfigurationSection section = CMD_INFO.getConfigurationSection(String.format("commands.%s", cmdKey));
        if(section == null)
            return result;

        for(String key : section.getKeys(false)) {
            String path = String.format("%s.%s", cmdKey, key);
            String permission = getCMDPerm(path);
            if(permission != null && sender.hasPermission(permission)) {
                String usage = getCMDUsage(path);
                String exec = getCMDExec(path);
                String desc = LangManager.getString(sender, String.format("cmd-desc.%s", path));
                String suggestion = getCMDSuggestion(path);
                result.add(new CmdInfo(usage, permission, exec, desc, suggestion));
            }
        }

        return result;
    }
}
