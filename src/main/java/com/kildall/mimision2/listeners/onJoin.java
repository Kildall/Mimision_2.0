package com.kildall.mimision2.listeners;

import com.kildall.mimision2.utils.AFK;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        AFK.putPlayerIntoTimer(e.getPlayer());
        //Si el jugador se puso AFK con comandos, si se hace algo sacarlo.
        if(AFK.getAfkPlayers().containsKey(e.getPlayer().getUniqueId())){
            if(AFK.getAfkPlayers().get(e.getPlayer().getUniqueId()).equals("command")){
                AFK.removeAFKByCommand(e.getPlayer());
            }
        }
    }
}
