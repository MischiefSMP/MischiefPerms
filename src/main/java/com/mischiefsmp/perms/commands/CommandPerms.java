package com.mischiefsmp.perms.commands;

import com.mischiefsmp.perms.features.PermissionManager;
import com.mischiefsmp.perms.features.ReadOnly;
import com.mischiefsmp.perms.MischiefPerms;
import com.mischiefsmp.perms.features.LangManager;
import com.mischiefsmp.perms.permission.MischiefGroup;
import com.mischiefsmp.perms.permission.MischiefPermission;
import com.mischiefsmp.perms.utils.CmdInfo;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
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
        } else if(args[0].equals("list")) {
            if(args.length < 2) {
                sendWU(sender);
                return true;
            }
            switch (args[1]) {
                case "groups" -> listGroups(sender);
                case "users" -> listUsers(sender);
                default -> sendWU(sender);
            }
            return true;
        } else if(args[0].equals("group")) {
            if(args.length < 2) {
                sendWU(sender);
                return true;
            }

            // -> /perms group  one  two   three  four
            // -> /perms group grant <id> <perm> [world]
            final String one = arg(args, 1);
            final String two = arg(args, 2);
            final String three = arg(args, 3);
            final String four = arg(args, 4);

            if(one == null) {
                sendWU(sender);
                return true;
            }

            //TODO: Split args properly to allow the usage of ""
            switch (one) {
                case "create" -> createGroup(sender, two);
                case "delete" -> deleteGroup(sender, two);
                case "clear" -> clearGroup(sender, two);
                case "add" -> addGroup(sender, two, three, four);
                case "remove" -> removeGroup(sender, two, three, four);
                case "prefix" -> prefixGroup(sender, two, three);
                case "suffix" -> suffixGroup(sender, two, three);
                // -> /perms group admin
                default -> infoGroup(sender, one);
            }

        } else if(args[0].equals("user")) {

        }


        return true;
    }

    private String arg(String[] args, int index) {
        if(args.length > index)
            return args[index];
        return null;
    }

    private boolean isAllowed(CommandSender sender, String permission) {
        boolean should = sender.isOp() || sender.hasPermission(permission) || sender.hasPermission("*");
        if(!should)
            sender.sendMessage(LangManager.getString(sender, "noperm"));
        return should;
    }

    private void listUsers(CommandSender sender) {
        sender.sendMessage("All users:");
    }

    private void listGroups(CommandSender sender) {
        sender.sendMessage("All groups:");
    }

    private void infoGroup(CommandSender sender, String id) {
        if(id == null) {
            sendWU(sender);
            return;
        }

        if(!PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-nf", id));
            return;
        }
        //TODO: add per world stuff
        //TODO: Refactor?

        MischiefGroup g = PermissionManager.getGroup(id);
        sender.sendMessage(String.format("Group ID: %s", id));
        sender.sendMessage(String.format("Index: %d", g.getIndex()));
        sender.sendMessage(String.format("Prefix: %s", g.getPrefix()));
        sender.sendMessage(String.format("Suffix: %s", g.getSuffix()));
        sender.sendMessage(String.format("Users: %s", g.getMembers()));
        sender.sendMessage("Permissions:");
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

    private void createGroup(CommandSender sender, String id) {
        if(id == null) {
            sendWU(sender);
            return;
        }

        if(PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-exists", id));
            return;
        }

        PermissionManager.createGroup(id);
        sender.sendMessage(LangManager.getString(sender, "group-created", id));
    }

    private void deleteGroup(CommandSender sender, String id) {
        if(id == null) {
            sendWU(sender);
            return;
        }

        if(!PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-nf", id));
            return;
        }

        PermissionManager.deleteGroup(id);
        sender.sendMessage(LangManager.getString(sender, "group-removed", id));
    }

    private void clearGroup(CommandSender sender, String id) {
        if(id == null) {
            sendWU(sender);
            return;
        }

        if(!PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-nf", id));
            return;
        }

        PermissionManager.getGroup(id).clear();
        sender.sendMessage(LangManager.getString(sender, "group-cleared", id));
    }

    private void addGroup(CommandSender sender, String id, String permission, String world) {
        if(id == null || permission == null) {
            sendWU(sender);
            return;
        }

        if(!PermissionManager.hasGroup(id)) {
            sender.sendMessage(LangManager.getString(sender, "group-nf", id));
            return;
        }

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

        sender.spigot().sendMessage(new ComponentBuilder(successText).append(" ").append(getInfoTextComponent(sender, id)).create());
    }

    private TextComponent getInfoTextComponent(CommandSender sender, String groupID) {
        TextComponent infoText = new TextComponent("[I]");
        infoText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(LangManager.getString(sender, "click-group-info"))));
        String infoCMD = String.format(ReadOnly.getCMDExec("perms.group"), groupID);
        infoText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, infoCMD));
        return infoText;
    }

    private void removeGroup(CommandSender sender, String id, String permission, String world) {
        sender.sendMessage(String.format("REMOVE ID: %s PERM: %s, WORLD: %s", id, permission, world));
    }

    private void prefixGroup(CommandSender sender, String id, String prefix) {
        sender.sendMessage(String.format("PREFIX ID: %s PREFIX: %s", id, prefix));
    }

    private void suffixGroup(CommandSender sender, String id, String suffix) {
        sender.sendMessage(String.format("SUFFIX ID: %s PREFIX: %s", id, suffix));
    }

    private void sendWU(CommandSender sender) {
        sender.sendMessage(LangManager.getString(sender, "wrong-usage"));
        sender.sendMessage(LangManager.getString(sender, "try-cmd", ReadOnly.getCMDUsage("perms.help")));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(LangManager.getString(sender, "available-cmds"));
        for(CmdInfo cmd : ReadOnly.getCMDHelp(sender, "perms")) {
            sender.sendMessage(String.format("%s: %s", cmd.usage(), cmd.desc()));
        }
    }
}
