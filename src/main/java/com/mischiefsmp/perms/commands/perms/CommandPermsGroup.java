package com.mischiefsmp.perms.commands.perms;

import com.mischiefsmp.perms.features.LangManager;
import com.mischiefsmp.perms.features.PermissionManager;
import com.mischiefsmp.perms.features.ReadOnly;
import com.mischiefsmp.perms.permission.MischiefGroup;
import com.mischiefsmp.perms.permission.MischiefPermission;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

public class CommandPermsGroup {
    public static void listGroups(CommandSender sender) {
        sender.sendMessage(LangManager.getString(sender, "all-groups"));
        for(MischiefGroup group : PermissionManager.getGroups()) {
            //TODO: Account for index
            TextComponent groupText = new TextComponent(group.getId());
            TextComponent infoText = CommandPermsUtils.getGroupInfoTextComponent(sender, group.getId());
            sender.spigot().sendMessage(new ComponentBuilder(groupText).append(" ").append(infoText).create());
        }
    }

    public static void infoGroup(CommandSender sender, MischiefGroup group) {
        //TODO: Refactor?

        sender.sendMessage(String.format("Group ID: %s", group.getId()));
        sender.sendMessage(String.format("Index: %d", group.getIndex()));
        sender.sendMessage(String.format("Prefix: %s", group.getPrefix()));
        sender.sendMessage(String.format("Suffix: %s", group.getSuffix()));
        sender.sendMessage(String.format("Users: %s", group.getMembers()));

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
            String removeCMD = String.format(ReadOnly.getCMDExec("perms.group-remove"), group.getId(), invertedPermission, "");
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

        if(PermissionManager.hasGroup(groupID)) {
            sender.sendMessage(LangManager.getString(sender, "group-exists", groupID));
            return;
        }

        PermissionManager.createGroup(groupID);
        sender.sendMessage(LangManager.getString(sender, "group-created", groupID));
    }

    public static void deleteGroup(CommandSender sender, MischiefGroup group) {
        PermissionManager.deleteGroup(group);
        sender.sendMessage(LangManager.getString(sender, "group-removed", group.getId()));
    }

    public static void clearGroup(CommandSender sender, MischiefGroup group) {
        group.clear();
        sender.sendMessage(LangManager.getString(sender, "group-cleared", group.getId()));
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
                sender.sendMessage(LangManager.getString(sender, "permission-exists"));
                return;
            }
            //Permission exists, but possibly disabled/enabled? Switch!
            existingPerm.setAllowed(new MischiefPermission(permission).isAllowed());
        } else {
            //Permission doesnt exist, adding
            group.addPermission(permToAdd);
        }

        TextComponent successText = new TextComponent(LangManager.getString(sender, "group-perm-added", permission, group.getId()));
        sender.spigot().sendMessage(new ComponentBuilder(successText).append(" ").append(CommandPermsUtils.getGroupInfoTextComponent(sender, group.getId())).create());
    }

    public static void removeGroup(CommandSender sender, MischiefGroup group, String permission, String world) {
        sender.sendMessage(String.format("REMOVE ID: %s PERM: %s, WORLD: %s", group.getId(), permission, world));
    }

    public static void prefixGroup(CommandSender sender, MischiefGroup group, String prefix) {
        sender.sendMessage(String.format("PREFIX ID: %s PREFIX: %s", group.getId(), prefix));
    }

    public static void suffixGroup(CommandSender sender, MischiefGroup group, String suffix) {
        sender.sendMessage(String.format("SUFFIX ID: %s PREFIX: %s", group.getId(), suffix));
    }
}
