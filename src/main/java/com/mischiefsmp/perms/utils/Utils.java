package com.mischiefsmp.perms.utils;


import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Utils {
    public static TextComponent getHoverAndCMDText(String text, String hover, String command) {
        TextComponent txt = new TextComponent(text);
        if(hover != null)
            txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        if(command != null)
            txt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return txt;
    }
}
