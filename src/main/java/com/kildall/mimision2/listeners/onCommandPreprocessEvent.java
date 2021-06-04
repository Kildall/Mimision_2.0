package com.kildall.mimision2.listeners;

import com.kildall.mimision2.utils.AFK;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class onCommandPreprocessEvent implements Listener {
    @EventHandler
    public void onSendMessage(PlayerCommandPreprocessEvent e){
        AFK.updateTimer(e.getPlayer());
        //Si el jugador se puso AFK con comandos, si se hace algo sacarlo.
        if(AFK.getAfkPlayers().containsKey(e.getPlayer().getUniqueId()) && !e.getMessage().equals("/afk")){
            if(AFK.getAfkPlayers().get(e.getPlayer().getUniqueId()).equals("command")){
                AFK.removeAFKByCommand(e.getPlayer());
            }
        }
    }
}
