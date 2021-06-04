package com.kildall.mimision2.utils;

import com.kildall.mimision2.files.MessagesFile;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ChatUtils {

    /*
    Toma parametros de:
        -Jugador al cual se dirige el mensaje
        -Path al mensaje guardado en Mensajes.yml
     */
    public static void sendMessage(Player p, String path){
        p.sendMessage(colorize(MessagesFile.getMessages().getString("mensajes.prefijo") + MessagesFile.getMessages().getString(path)));
    }

    /*
    Toma parametros de:
        -Jugador al cual se dirige el mensaje
        -Path al mensaje guardado en Mensajes.yml
        -Comando a ejecutar si se clickea el mensaje
        -Parametro opcional Hover que permite poner un mensaje al pasar el click por encima
     */
    public static void sendClickableCommandMessage(Player p, String path, String command, String hover){
        //Constructor
        TextComponent textComponent = new TextComponent();
        //Set Text
        textComponent.setText(colorize(MessagesFile.getMessages().getString("mensajes.prefijo") + MessagesFile.getMessages().getString(path)));
        //Click event
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        //Hover Event
        if(hover != null){
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        } else {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BOLD + "Haz click aqui")));
        }
        //Mandar mensaje al jugador
        p.spigot().sendMessage(textComponent);
    }
    /*
    Toma parametros de:
        -Servidor al cual se dirige el mensaje
        -Path al mensaje guardado en Mensajes.yml
     */
    public static void broadcastMessage(Server s, String path){
        String prefix = MessagesFile.getMessages().getString("mensajes.prefijo");
        String message = MessagesFile.getMessages().getString(path);
        s.broadcastMessage(colorize(prefix + message));
    }

    public static void broadcastCustomMessage(Server s, String message){
        s.broadcastMessage(colorize(message));
    }

    /*
    Toma parametros de:
        -Servidor al cual se dirige el mensaje
        -Path al mensaje guardado en Mensajes.yml
        -Comando a ejecutar si se clickea el mensaje
        -Parametro opcional Hover que permite poner un mensaje al pasar el click por encima
     */
    public static void broadcastClickableCommandMessage(Server s, String path, String command, String hover){
        //Constructor
        TextComponent textComponent = new TextComponent();

        //Set Text
        textComponent.setText(colorize(MessagesFile.getMessages().getString("mensajes.prefijo") + MessagesFile.getMessages().getString(path)));

        //Click event
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        //Hover Event
        if(hover != null){
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        } else {
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.BOLD + "Haz click aqui")));
        }

        for(Player online : s.getOnlinePlayers()){
            online.spigot().sendMessage(textComponent);
        }
    }

    /*
    A partir del jugador se envia el mensaje con la informacion correspondiente
     */
    public static void broadcastAFKMessage(Player p){
        String prefix = MessagesFile.getMessages().getString("mensajes.prefijo");
        String player = ChatColor.DARK_PURPLE + p.getDisplayName();
        String message = MessagesFile.getMessages().getString("mensajes.afks.Jugador AFK");
        p.getServer().broadcastMessage(colorize(prefix + player + message));
    }

    public static void broadcastNoLongerAFKMessage(Player p){
        String prefix = MessagesFile.getMessages().getString("mensajes.prefijo");
        String player = ChatColor.DARK_PURPLE + p.getDisplayName();
        String message = MessagesFile.getMessages().getString("mensajes.afks.Jugador no AFK");
        p.getServer().broadcastMessage(colorize(prefix + player + message));
    }



    public static String colorize(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
