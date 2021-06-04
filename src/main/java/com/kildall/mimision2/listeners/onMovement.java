package com.kildall.mimision2.listeners;

import com.kildall.mimision2.Mimision2;
import com.kildall.mimision2.utils.AFK;
import com.kildall.mimision2.utils.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class onMovement implements Listener {

    @EventHandler
    public void onMovement(PlayerMoveEvent e){
        //Si el jugador esta AFK
        if(AFK.getAfkPlayers().containsKey(e.getPlayer().getUniqueId())){
            /*
            Si la opcion del servidor es verdadera entonces:
                -No dejar mover
                -Si se mueve avisarle que tiene que poner un comando
                -Dormir 200ms
             */
            if(AFK.isImmovable()){
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        e.setCancelled(true);
                        ChatUtils.sendMessage(e.getPlayer(), "mensajes.afks.Jugador Moviendose AFK");
                        if(!AFK.getAfkPlayers().containsKey(e.getPlayer().getUniqueId())){
                            this.cancel();
                        }
                    }
                }.runTaskTimerAsynchronously(Mimision2.getInstance(),0,20);
            } else {
                //Si no, simplemente actualizar el tiempo del jugador
                AFK.updateTimer(e.getPlayer());
                //Si el jugador se puso AFK con comandos, si se hace algo sacarlo.
                if(AFK.getAfkPlayers().containsKey(e.getPlayer().getUniqueId())){
                    if(AFK.getAfkPlayers().get(e.getPlayer().getUniqueId()).equals("command")){
                        AFK.removeAFKByCommand(e.getPlayer());
                    }
                }
            }

        } else {
            //Si el jugador no esta AFK, entonces actualizar su tiempo.
            AFK.updateTimer(e.getPlayer());
        }
    }

}
