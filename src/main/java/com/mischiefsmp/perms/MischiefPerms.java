package com.mischiefsmp.perms;

import com.mischiefsmp.perms.commands.CommandPerms;
import com.mischiefsmp.perms.features.PluginConfig;
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
        PluginConfig.init(this);
        registerCommand("perms", new CommandPerms(this));
    }

    private void registerCommand(String label, CommandExecutor executor) {
        PluginCommand cmd = Bukkit.getPluginCommand(label);
        if(cmd != null) {
            cmd.setExecutor(executor);
            return;
        }
        MischiefPerms.log("Error registering command <%s>!", Level.SEVERE, label);
    }

    public static void log(String message, Level level, Object... args) {
        String msg = message;
        if(args.length > 0)
            msg = String.format(message, args);
        logger.log(level, msg);
    }

}
