package com.mischiefsmp.perms.commands.perms;

import com.mischiefsmp.perms.features.LangManager;
import com.mischiefsmp.perms.features.ReadOnly;
import com.mischiefsmp.perms.utils.CmdInfo;
import com.mischiefsmp.perms.utils.SendUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class CommandPermsUtils {

    public static String arg(String[] args, int index) {
        if(args.length > index)
            return args[index];
        return null;
    }

    public static boolean ensureWorldExists(CommandSender sender, String world) {
        //If world is null we are not trying to set a world
        if(world == null)
            return true;

        if(Bukkit.getWorld(world) == null) {
            SendUtils.sendL(sender, "world-nf", world);
            return false;
        }
        return true;
    }

    public static boolean isAllowed(CommandSender sender, String permission) {
        boolean should = sender.isOp() || sender.hasPermission(permission) || sender.hasPermission("*");
        if(!should)
            SendUtils.sendL(sender, "noperm");
        return should;
    }

    public static void sendWU(CommandSender sender) {
        SendUtils.sendL(sender, "wrong-usage");
        SendUtils.sendL(sender, "try-cmd", ReadOnly.getCMDUsage("perms.help"));
    }

    public static void sendHelp(CommandSender sender) {
        SendUtils.sendL(sender, "available-cmds");
        for(CmdInfo cmd : ReadOnly.getCMDHelp(sender, "perms")) {
            SendUtils.sendF(sender, "%s: %s", cmd.usage(), cmd.desc());
        }
    }

    public static TextComponent getGroupInfoTextComponent(CommandSender sender, String groupID) {
        TextComponent infoText = new TextComponent("[I]");
        infoText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(LangManager.getString(sender, "click-group-info"))));
        String infoCMD = String.format(ReadOnly.getCMDExec("perms.group"), groupID);
        infoText.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, infoCMD));
        return infoText;
    }
}
