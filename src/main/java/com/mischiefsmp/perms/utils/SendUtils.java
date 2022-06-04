package com.mischiefsmp.perms.utils;

import com.mischiefsmp.perms.features.LangManager;
import org.bukkit.command.CommandSender;

public class SendUtils {
    public static void sendF(CommandSender sender, String message, Object... args) {
        sender.sendMessage(String.format(message, args));
    }

    public static void sendL(CommandSender sender, String key, Object... args) {
        String message = LangManager.getString(sender, key, args);
        sender.sendMessage(message);
    }
}
