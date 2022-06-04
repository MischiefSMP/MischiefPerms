package com.mischiefsmp.perms.commands.perms;

import com.mischiefsmp.perms.features.GroupManager;
import com.mischiefsmp.perms.features.LangManager;
import com.mischiefsmp.perms.features.UserManager;
import com.mischiefsmp.perms.features.ReadOnly;
import com.mischiefsmp.perms.permission.MischiefGroup;
import com.mischiefsmp.perms.permission.MischiefPermission;
import com.mischiefsmp.perms.utils.SendUtils;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class CommandPermsGroup {
    public static void listGroups(CommandSender sender) {
        ArrayList<MischiefGroup> groups = GroupManager.getGroups();

        if(groups.size() == 0) {
            SendUtils.sendL(sender, "list-groups-none");
            return;
        }

        SendUtils.sendL(sender, "all-groups");
        for(MischiefGroup group : GroupManager.getGroups()) {
            //TODO: Account for index
            TextComponent groupText = new TextComponent(group.getId());
            TextComponent infoText = CommandPermsUtils.getGroupInfoTextComponent(sender, group.getId());
            sender.spigot().sendMessage(new ComponentBuilder(groupText).append(" ").append(infoText).create());
        }
    }

    public static void infoGroup(CommandSender sender, MischiefGroup group) {
        SendUtils.sendF(sender, "Group ID: %s", group.getId());
        SendUtils.sendF(sender, "Index: %d", group.getIndex());
        SendUtils.sendF(sender, "Prefix: %s", group.getPrefix() != null ? group.getPrefix() : "None");
        SendUtils.sendF(sender, "Suffix: %s", group.getSuffix() != null ? group.getSuffix() : "None");
        SendUtils.sendF(sender, "Users: %s", group.getMembers());

        TextComponent permissionsText = new TextComponent("Permissions: ");
        TextComponent addPermText = new TextComponent("[+]");
        addPermText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(LangManager.getString(sender, "click-to-add-perm"))));
        addPermText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format(ReadOnly.getCMDSuggestion("perms.group-add"), group.getId())));
        sender.spigot().sendMessage(new ComponentBuilder(permissionsText).append(addPermText).create());

        for(MischiefPermission p : group.getPermissions()) {
            String worldStr = p.getWorld() == null ? "" : String.format(" (%S)", p.getWorld());
            TextComponent permissionText = new TextComponent(String.format("> %s", p + worldStr));

            TextComponent invertText = new TextComponent(p.isAllowed() ? "[-]" : "[+]");
            String hoverText = p.isAllowed() ? "click-to-disallow" : "click-to-allow";
            invertText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(LangManager.getString(sender, hoverText))));
            MischiefPermission invertedPermission = new MischiefPermission(p);
            invertedPermission.setAllowed(!invertedPermission.isAllowed());
            String invertCMD = String.format(ReadOnly.getCMDExec("perms.group-add"), group.getId(), invertedPermission, invertedPermission.getWorld() != null ? invertedPermission.getWorld() : "");
            invertText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, invertCMD));

            TextComponent removeText = new TextComponent("[X]");
            removeText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(LangManager.getString(sender, "click-to-remove"))));
            String removeCMD = String.format(ReadOnly.getCMDExec("perms.group-remove"), group.getId(), p, p.getWorld() != null ? p.getWorld() : "");
            sender.sendMessage(removeCMD);
            removeText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, removeCMD));

            BaseComponent[] components = new ComponentBuilder(permissionText)
                    .append(" ").append(invertText)
                    .append(" ").append(removeText).create();
            sender.spigot().sendMessage(components);
        }
    }

    public static void createGroup(CommandSender sender, String groupID) {
        if(groupID == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        if(GroupManager.hasGroup(groupID)) {
            SendUtils.sendL(sender, "group-exists", groupID);
            return;
        }

        GroupManager.createGroup(groupID);
        SendUtils.sendL(sender, "group-created", groupID);
    }

    public static void deleteGroup(CommandSender sender, MischiefGroup group) {
        GroupManager.deleteGroup(group);
        SendUtils.sendL(sender, "group-removed", group.getId());
    }

    public static void clearGroup(CommandSender sender, MischiefGroup group) {
        group.clear();
        SendUtils.sendL(sender, "group-cleared", group.getId());
    }

    public static void addGroup(CommandSender sender, MischiefGroup group, String permission, String world) {
        if(permission == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        if(!CommandPermsUtils.ensureWorldExists(sender, world))
            return;

        MischiefPermission permToAdd = new MischiefPermission(permission, world);
        //Check if we already have this permission already (maybe not allowed?)
        MischiefPermission existingPerm = group.getPermission(permToAdd, true, false);
        if(existingPerm != null) {
            //Permission exists, check if completely equal
            if(existingPerm.equals(permToAdd)) {
                SendUtils.sendL(sender, "permission-exists");
                return;
            }
            //Permission exists, but possibly disabled/enabled? Switch!
            existingPerm.setAllowed(new MischiefPermission(permission).isAllowed());
        } else {
            //Permission doesn't exist, adding
            group.addPermission(permToAdd);
        }

        String toShow = world != null ?
                LangManager.getString(sender, "group-perm-added-world", permission, group.getId(), world) :
                LangManager.getString(sender, "group-perm-added", permission, group.getId());
        TextComponent successText = new TextComponent(toShow);
        sender.spigot().sendMessage(new ComponentBuilder(successText).append(" ").append(CommandPermsUtils.getGroupInfoTextComponent(sender, group.getId())).create());
    }

    public static void removeGroup(CommandSender sender, MischiefGroup group, String permission, String world) {
        if(permission == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        MischiefPermission permToRemove = new MischiefPermission(permission, world);

        if(group.hasPermission(permToRemove, false, false)) {
            //We have the permission to a t, remove it
            group.removePermission(permToRemove);
            SendUtils.sendL(sender, "group-perm-removed", permToRemove, group.getId());
            return;
        }

        MischiefPermission inverted = group.getPermission(permToRemove, true, false);
        if(inverted != null) {
            //We have the permission, but the allowed is wrong
            SendUtils.sendL(sender, "group-perm-remove-wrong-allowed", permission, inverted);
            return;
        }

        SendUtils.sendL(sender, "group-perm-remove-nf", permToRemove, group.getId());
    }

    public static void prefixGroup(CommandSender sender, MischiefGroup group, String prefix) {
        if(prefix == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        group.setPrefix(prefix);
        SendUtils.sendL(sender, "group-prefix-given", group.getId(), prefix);
    }

    public static void suffixGroup(CommandSender sender, MischiefGroup group, String suffix) {
        if(suffix == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        group.setSuffix(suffix);
        SendUtils.sendL(sender, "group-suffix-given", group.getId(), suffix);
    }

    public static void indexGroup(CommandSender sender, MischiefGroup group, String one) {
        if(one == null || !(one.equals("up") || one.equals("down"))) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        //TODO: Implement index
    }
}
