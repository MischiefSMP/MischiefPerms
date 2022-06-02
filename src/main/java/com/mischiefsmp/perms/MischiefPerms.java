package com.mischiefsmp.perms;

import com.mischiefsmp.perms.commands.perms.CommandPerms;
import com.mischiefsmp.perms.features.LangManager;
import com.mischiefsmp.perms.features.PermissionManager;
import com.mischiefsmp.perms.features.PluginConfig;
import com.mischiefsmp.perms.features.ReadOnly;
import com.mischiefsmp.perms.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MischiefPerms extends JavaPlugin {
    private static Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();
        FileUtils.init(this);
        ReadOnly.init(this);
        PluginConfig.init(this);
        LangManager.init(this);
        PermissionManager.init(this);
        registerCommand("perms", new CommandPerms());
    }

    private void registerCommand(String label, CommandExecutor executor) {
        PluginCommand cmd = Bukkit.getPluginCommand(label);
        if(cmd != null) {
            cmd.setExecutor(executor);
            return;
        }
        MischiefPerms.log("Error registering command <%s>!", Level.SEVERE, label);
    }

    public static void log(Object message, Level level, Object... args) {
        String msg = "null";
        if(message != null) {
            msg = message.toString();
            if(args.length > 0)
                msg = String.format(message.toString(), args);
        }
        logger.log(level, msg);
    }

}
