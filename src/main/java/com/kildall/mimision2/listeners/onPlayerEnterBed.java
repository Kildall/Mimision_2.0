package com.kildall.mimision2.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class onPlayerEnterBed implements Listener {

    @EventHandler
    public void onPlayerEnterBed(PlayerBedEnterEvent e){
        if(e.getBedEnterResult().equals(PlayerBedEnterEvent.BedEnterResult.OK)){
            e.getPlayer().performCommand("mimir");
        }
    }
}
