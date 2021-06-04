package com.kildall.mimision2.utils;

import com.kildall.mimision2.Mimision2;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class AFK {

    private static final Mimision2 plugin = Mimision2.getInstance();
    private static final Logger logger = plugin.getLogger();


    private static HashMap<UUID, Long> playerTimers = new HashMap<>();
    private static HashMap<UUID, String> afkPlayers = new HashMap<>();



    private static boolean immovable = plugin.getConfig().getBoolean("opciones.afk.inmovible");
    private static boolean invincible = plugin.getConfig().getBoolean("opciones.afk.invencible");
    private static long timeToAFK = plugin.getConfig().getInt("opciones.afk.tiempo hasta afk") * 60L;

    /*
    Bukkit Task que se ocupa de checkear si algun jugador ya ha pasado los 5 minutos
    desde el ultimo movimiento, si es asi, agregarlo a la lista de AFKs y mandar el
    mensaje correspondiente.
     */

    public static void checkIfPlayerAFK(){
        logger.info("Se ha comenzado a checkear los jugadores AKF");
        plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {

                //Todos los jugadores conectados

                for (Player p : plugin.getServer().getOnlinePlayers()){
                    /*

                    -----------------------------Debug---------------------------------
                    logger.info("--------------------------------------------------");

                    logger.info("Jugadores conectados: ");
                    plugin.getServer().getOnlinePlayers().forEach(temp -> logger.info("    -"+ p.getDisplayName()));

                    */

                    //Jugador se encuentra en la lista del contador AFK?

                    if(playerTimers.containsKey(p.getUniqueId())){

                        /*
                        -----------------------------Debug---------------------------------
                        logger.info("--------------------------------------------------");
                        logger.info(p.getDisplayName() + " esta dentro de la lista de jugadores.");
                        logger.info(playerTimers.toString());
                        logger.info("--------------------------------------------------");
                        logger.info("El tiempo actual del sistema es:" + Long.toString(System.currentTimeMillis() / 1000L));
                        logger.info("--------------------------------------------------");
                        */


                        //Si el jugador ya esta dentro de la lista del contador AFK
                        /*
                        Si el ultimo movimiento del jugador fue hace mas de 5 minutos y no esta afk
                        agregarlo a la lista de afks y mandar el mensaje correspondiente.

                        Si no, continuar con el loop.
                         */

                        if((System.currentTimeMillis() / 1000L) - playerTimers.get(p.getUniqueId()) > timeToAFK && !afkPlayers.containsKey(p.getUniqueId())){
                            ChatUtils.broadcastAFKMessage(p);
                            afkPlayers.put(p.getUniqueId(), "time");
                            p.setInvulnerable(invincible);
                        } else if ((System.currentTimeMillis() / 1000L) - playerTimers.get(p.getUniqueId()) < timeToAFK && afkPlayers.containsKey(p.getUniqueId())) {
                            if(afkPlayers.get(p.getUniqueId()).equals("time")){
                                ChatUtils.broadcastNoLongerAFKMessage(p);
                                afkPlayers.remove(p.getUniqueId());
                            }
                        }

                    } else {
                        //Si no se encuentra, agregarlo
                        putPlayerIntoTimer(p);
                    }
                }
            }
        },0,5);
    }
    public static void addAFKByCommand(Player p){
        afkPlayers.put(p.getUniqueId(), "command");
        ChatUtils.broadcastAFKMessage(p);
    }

    public static void removeAFKByCommand(Player p){
        afkPlayers.remove(p.getUniqueId());
        ChatUtils.broadcastNoLongerAFKMessage(p);
    }

    public static void updateTimer(Player p){
        playerTimers.put(p.getUniqueId(), System.currentTimeMillis()/1000L);
    }

    public static void putPlayerIntoTimer(Player p){
        playerTimers.put(p.getUniqueId(), System.currentTimeMillis() / 1000L);
    }

    public static void removePlayerFromTimer(Player p){
        playerTimers.remove(p.getUniqueId());
    }

    public static boolean isImmovable() {
        return immovable;
    }

    public static boolean isInvincible() {
        return invincible;
    }

    public static HashMap<UUID, String> getAfkPlayers() {
        return afkPlayers;
    }

    private static Player getPlayerFromUUID(UUID uuid){
        return plugin.getServer().getPlayer(uuid);
    }



}
