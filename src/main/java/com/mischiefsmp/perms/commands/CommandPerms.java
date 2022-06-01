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

    private final String[] CMD_LIST = {"help", "group.create", "group.delete", "group.clear", "group.grant",
            "group.deny", "group.remove", "group.remove", "group.prefix", "group.suffix", "user.clear",
            "user.grant", "user.deny", "user.remove", "user.remove", "user.prefix", "user.suffix"};

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

        for(String item : CMD_LIST) {
            String itemKey = String.format("perms.%s", item);
            if(sender.hasPermission(ReadOnly.getCMDPerm(itemKey)))
                sender.sendMessage(ReadOnly.getCMDUsage(itemKey));
        }
    }
}
