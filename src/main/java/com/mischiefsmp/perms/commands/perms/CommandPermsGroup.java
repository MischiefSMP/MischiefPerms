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

    public static void infoGroup(CommandSender sender, String id) {
        if(id == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        if(!PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-nf", id));
            return;
        }
        //TODO: Refactor?

        MischiefGroup g = PermissionManager.getGroup(id);
        sender.sendMessage(String.format("Group ID: %s", id));
        sender.sendMessage(String.format("Index: %d", g.getIndex()));
        sender.sendMessage(String.format("Prefix: %s", g.getPrefix()));
        sender.sendMessage(String.format("Suffix: %s", g.getSuffix()));
        sender.sendMessage(String.format("Users: %s", g.getMembers()));

        TextComponent permissionsText = new TextComponent("Permissions: ");
        TextComponent addPermText = new TextComponent("[+]");
        addPermText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(LangManager.getString(sender, "click-to-add-perm"))));
        addPermText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format(ReadOnly.getCMDSuggestion("perms.group-add"), g.getId())));

        sender.spigot().sendMessage(new ComponentBuilder(permissionsText).append(addPermText).create());

        for(MischiefPermission p : g.getPermissions()) {
            String worldStr = p.getWorld() == null ? "" : String.format(" (%S)", p.getWorld());
            TextComponent permissionText = new TextComponent(String.format("> %s", p + worldStr));

            TextComponent invertText = new TextComponent(p.isAllowed() ? "[-]" : "[+]");
            String hoverText = p.isAllowed() ? "click-to-disallow" : "click-to-allow";
            invertText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(LangManager.getString(sender, hoverText))));
            MischiefPermission invertedPermission = new MischiefPermission(p);
            invertedPermission.setAllowed(!invertedPermission.isAllowed());
            String invertCMD = String.format(ReadOnly.getCMDExec("perms.group-add"), g.getId(), invertedPermission, "");
            invertText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, invertCMD));

            TextComponent removeText = new TextComponent("[X]");
            removeText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(LangManager.getString(sender, "click-to-remove"))));
            String removeCMD = String.format(ReadOnly.getCMDExec("perms.group-remove"), g.getId(), invertedPermission, "");
            removeText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, removeCMD));

            BaseComponent[] components = new ComponentBuilder(permissionText)
                    .append(" ").append(invertText)
                    .append(" ").append(removeText).create();
            sender.spigot().sendMessage(components);
        }
    }

    public static void createGroup(CommandSender sender, String id) {
        if(id == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        if(PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-exists", id));
            return;
        }

        PermissionManager.createGroup(id);
        sender.sendMessage(LangManager.getString(sender, "group-created", id));
    }

    public static void deleteGroup(CommandSender sender, String id) {
        if(id == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        if(!PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-nf", id));
            return;
        }

        PermissionManager.deleteGroup(id);
        sender.sendMessage(LangManager.getString(sender, "group-removed", id));
    }

    public static void clearGroup(CommandSender sender, String id) {
        if(id == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        if(!PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-nf", id));
            return;
        }

        PermissionManager.getGroup(id).clear();
        sender.sendMessage(LangManager.getString(sender, "group-cleared", id));
    }

    public static void addGroup(CommandSender sender, String id, String permission, String world) {
        if(id == null || permission == null) {
            CommandPermsUtils.sendWU(sender);
            return;
        }

        if(!PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-nf", id));
            return;
        }

        if(!CommandPermsUtils.ensureWorldExists(sender, world))
            return;

        //Check if we already have this permission already (maybe not allowed?)
        MischiefGroup group = PermissionManager.getGroup(id);
        MischiefPermission perm = group.getPermission(permission, true);
        if(perm != null) {
            perm.setAllowed(new MischiefPermission(permission).isAllowed());
            if(world != null)
                perm.setWorld(world);
        } else
            group.addPermission(permission, world);

        TextComponent successText = new TextComponent(LangManager.getString(sender, "group-perm-added", permission, id));

        sender.spigot().sendMessage(new ComponentBuilder(successText).append(" ").append(CommandPermsUtils.getGroupInfoTextComponent(sender, id)).create());
    }

    public static void removeGroup(CommandSender sender, String id, String permission, String world) {
        sender.sendMessage(String.format("REMOVE ID: %s PERM: %s, WORLD: %s", id, permission, world));
    }

    public static void prefixGroup(CommandSender sender, String id, String prefix) {
        sender.sendMessage(String.format("PREFIX ID: %s PREFIX: %s", id, prefix));
    }

    public static void suffixGroup(CommandSender sender, String id, String suffix) {
        sender.sendMessage(String.format("SUFFIX ID: %s PREFIX: %s", id, suffix));
    }
}
