package com.mischiefsmp.perms.commands;

import com.mischiefsmp.perms.ICommandSettings;
import com.mischiefsmp.perms.MischiefPerms;
import com.mischiefsmp.perms.features.LangManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandPerms implements CommandExecutor {
    private final MischiefPerms plugin;

    public CommandPerms(MischiefPerms plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if(args.length <= 0 && isAllowed(sender, ICommandSettings.HELP_PERMS)) {
            sender.sendMessage(LangManager.getString(sender, "missing-arguments"));
            sender.sendMessage(LangManager.getString(sender, "try-cmd", ICommandSettings.HELP_USAGE));
            return true;
        }


        return true;
    }

    private boolean isAllowed(CommandSender sender, String permission) {
        boolean should = sender.isOp() || sender.hasPermission(permission) || sender.hasPermission("*");;
        if(!should)
            sender.sendMessage(LangManager.getString(sender, "noperm"));
        return should;
    }
}
