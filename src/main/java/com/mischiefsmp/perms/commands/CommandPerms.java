package com.mischiefsmp.perms.commands;

import com.mischiefsmp.perms.features.ReadOnly;
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

        if(args.length <= 0 && isAllowed(sender, ReadOnly.getCMDPerm("perms.help"))) {
            sender.sendMessage(LangManager.getString(sender, "missing-arguments"));
            sender.sendMessage(LangManager.getString(sender, "try-cmd", ReadOnly.getCMDUsage("perms.help")));
            return true;
        }

        if(args[0].equals("help") && isAllowed(sender, ReadOnly.getCMDPerm("perms.help"))) {
            sendHelp(sender);
            return true;
        } else if(args[0].equals("group")) {

        } else if(args[0].equals("user")) {

        }


        return true;
    }

    private boolean isAllowed(CommandSender sender, String permission) {
        boolean should = sender.isOp() || sender.hasPermission(permission) || sender.hasPermission("*");
        if(!should)
            sender.sendMessage(LangManager.getString(sender, "noperm"));
        return should;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(LangManager.getString(sender, "available-cmds"));
        for(String cmd : ReadOnly.getCMDHelp(sender, "perms")) {
            sender.sendMessage(cmd);
        }
    }
}
