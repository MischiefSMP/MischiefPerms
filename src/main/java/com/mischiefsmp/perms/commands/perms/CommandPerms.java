package com.mischiefsmp.perms.commands.perms;

import com.mischiefsmp.perms.features.ReadOnly;
import com.mischiefsmp.perms.features.LangManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

public class CommandPerms implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length <= 0 && CommandPermsUtils.isAllowed(sender, ReadOnly.getCMDPerm("perms.help"))) {
            sender.sendMessage(LangManager.getString(sender, "missing-arguments"));
            sender.sendMessage(LangManager.getString(sender, "try-cmd", ReadOnly.getCMDUsage("perms.help")));
            return true;
        }

        if(args[0].equals("help") && CommandPermsUtils.isAllowed(sender, ReadOnly.getCMDPerm("perms.help"))) {
            CommandPermsUtils.sendHelp(sender);
            return true;
        } else if(args[0].equals("list")) {
            if(args.length < 2) {
                CommandPermsUtils.sendWU(sender);
                return true;
            }
            switch (args[1]) {
                case "groups" -> CommandPermsGroup.listGroups(sender);
                case "users" -> CommandPermsUser.listUsers(sender);
                default -> CommandPermsUtils.sendWU(sender);
            }
            return true;
        } else if(args[0].equals("group")) {
            if(args.length < 2) {
                CommandPermsUtils.sendWU(sender);
                return true;
            }

            // -> /perms group  one  two   three  four
            // -> /perms group grant <id> <perm> [world]
            final String one = CommandPermsUtils.arg(args, 1);
            final String two = CommandPermsUtils.arg(args, 2);
            final String three = CommandPermsUtils.arg(args, 3);
            final String four = CommandPermsUtils.arg(args, 4);

            if(one == null) {
                CommandPermsUtils.sendWU(sender);
                return true;
            }

            //TODO: Split args properly to allow the usage of ""
            switch (one) {
                case "create" -> CommandPermsGroup.createGroup(sender, two);
                case "delete" -> CommandPermsGroup.deleteGroup(sender, two);
                case "clear" -> CommandPermsGroup.clearGroup(sender, two);
                case "add" -> CommandPermsGroup.addGroup(sender, two, three, four);
                case "remove" -> CommandPermsGroup.removeGroup(sender, two, three, four);
                case "prefix" -> CommandPermsGroup.prefixGroup(sender, two, three);
                case "suffix" -> CommandPermsGroup.suffixGroup(sender, two, three);
                // -> /perms group admin
                default -> CommandPermsGroup.infoGroup(sender, one);
            }

        } else if(args[0].equals("user")) {
            
        }

        return true;
    }
}
