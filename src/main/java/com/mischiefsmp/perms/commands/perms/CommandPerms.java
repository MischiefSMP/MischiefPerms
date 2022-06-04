package com.mischiefsmp.perms.commands.perms;

import com.mischiefsmp.perms.features.GroupManager;
import com.mischiefsmp.perms.features.ReadOnly;
import com.mischiefsmp.perms.features.LangManager;
import com.mischiefsmp.perms.permission.MischiefGroup;
import com.mischiefsmp.perms.utils.SendUtils;
import com.mischiefsmp.perms.utils.Utils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.jetbrains.annotations.NotNull;

public class CommandPerms implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length <= 0 && CommandPermsUtils.isAllowed(sender, ReadOnly.getCMDPerm("perms.help"))) {
            SendUtils.sendL(sender, "missing-arguments");
            SendUtils.sendL(sender, "try-cmd", ReadOnly.getCMDUsage("perms.help"));
            return true;
        }

        if(args[0].equals("help") && CommandPermsUtils.isAllowed(sender, ReadOnly.getCMDPerm("perms.help"))) {
            CommandPermsUtils.sendHelp(sender);
            return true;
        } else if(args[0].equals("list")) {
            if(args.length < 2) {
                String clickExec = LangManager.getString(sender, "click-to-exec");
                String execGroups = ReadOnly.getCMDExec("perms.list-groups");
                String execUsers = ReadOnly.getCMDExec("perms.list-users");

                TextComponent groupsText = new TextComponent(execGroups);
                TextComponent groupsRunText = Utils.getHoverAndCMDText("[R]", clickExec, execGroups);

                TextComponent usersText = new TextComponent(execUsers);
                TextComponent usersRunText = Utils.getHoverAndCMDText("[R]", clickExec, execUsers);

                SendUtils.sendL(sender, "wrong-usage");
                sender.spigot().sendMessage(new ComponentBuilder(groupsText).append(" ").append(groupsRunText).create());
                sender.spigot().sendMessage(new ComponentBuilder(usersText).append(" ").append(usersRunText).create());
                return true;
            }
            switch (args[1]) {
                case "groups"   -> CommandPermsGroup.listGroups(sender);
                case "users"    -> CommandPermsUser.listUsers(sender);
                default         -> CommandPermsUtils.sendWU(sender);
            }
            return true;
        } else if(args[0].equals("group")) {
            if(args.length < 2) {
                CommandPermsUtils.sendWU(sender);
                return true;
            }

            // -> /perms group <id>  type   one  two
            // -> /perms group <id> grant <perm> [world]
            final String groupID    = CommandPermsUtils.arg(args, 1);
            final String type    = CommandPermsUtils.arg(args, 2);
            final String one  = CommandPermsUtils.arg(args, 3);
            final String two   = CommandPermsUtils.arg(args, 4);

            if(!GroupManager.hasGroup(groupID)) {
                //Group does not exist, but we want to create it
                if(type != null && type.equals("create")) {
                    CommandPermsGroup.createGroup(sender, groupID);
                    return true;
                }
                //Group does not exist
                SendUtils.sendL(sender, "group-nf", groupID);
                return true;
            }

            MischiefGroup group = GroupManager.getGroup(groupID);

            if(type == null) {
                // -> /perms group admin
                CommandPermsGroup.infoGroup(sender, group);
                return true;
            }

            //TODO: Split args properly to allow the usage of ""
            switch (type) {
                case "delete"   -> CommandPermsGroup.deleteGroup(sender, group);
                case "clear"    -> CommandPermsGroup.clearGroup(sender, group);
                case "add"      -> CommandPermsGroup.addGroup(sender, group, one, two);
                case "remove"   -> CommandPermsGroup.removeGroup(sender, group, one, two);
                case "prefix"   -> CommandPermsGroup.prefixGroup(sender, group, one);
                case "suffix"   -> CommandPermsGroup.suffixGroup(sender, group, one);
                case "index"    -> CommandPermsGroup.indexGroup(sender, group, one);
            }

        } else if(args[0].equals("user")) {

        }

        return true;
    }
}
