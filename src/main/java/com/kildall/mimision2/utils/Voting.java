package com.kildall.mimision2.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Server;

public class Voting {

    public static void startVoting(Server server){
        int numPlayers = server.getOnlinePlayers().size();
        //if ( numPlayers > 2){
        //    server.broadcastMessage(ChatColor.DARK_GREEN + Messages.getMessage("Votar Muchos"));
        //} else if (numPlayers > 1){
            TextComponent message = clickableMessage("Votar Pocos","/mimir","Hacer la mimision");
            message.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);
            server.getOnlinePlayers().forEach(player -> player.sendMessage(message.getText()));
        //}
    }

    //Return clickable message related to the command
    private static TextComponent clickableMessage(String key, String command, String hover){
        //Constructor
        TextComponent textComponent = new TextComponent();
        //Set Text
        textComponent.setText(Messages.getMessage(key));
        //Click event
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        //Hover Event
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));

        return textComponent;
    }


}
