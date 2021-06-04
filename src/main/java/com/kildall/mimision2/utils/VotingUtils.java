package com.kildall.mimision2.utils;

import com.kildall.mimision2.Mimision2;
import com.kildall.mimision2.files.MessagesFile;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VotingUtils {

    private static final Mimision2 plugin = Mimision2.getInstance();
    private static boolean votingInProgress = false;
    private static ArrayList<UUID> votes = new ArrayList<>();
    private static int avaiablePlayers = 0;
    private static float porcetageToDay = (float) plugin.getConfig().getInt("opciones.mimision.porcentaje de votos") / 100;

    //Comenzar votacion
    public static void startVoting(Player p){
        if(avaiablePlayers > 2){
            startLargeVote(p);
        } else if (avaiablePlayers > 1){
            startSmallVote(p);
        } else {
            SleepUtils.passNight(p.getServer());
        }

    }

    public static boolean isVotingInProgress() {
        return votingInProgress;
    }

    public static void vote(Player p){
        if(!votes.contains(p.getUniqueId())){
            votes.add(p.getUniqueId());
        }
    }

    public static void removeVote(Player p){
        votes.remove(p.getUniqueId());
    }

    public static boolean hasPlayerVoted(Player p){
        return votes.contains(p.getUniqueId());
    }

    private static void startSmallVote(Player p) {
        ChatUtils.broadcastMessage(p.getServer(),"mensajes.votacion.Iniciar Votacion");
        for(Player subject : p.getServer().getOnlinePlayers()){
            if(!subject.equals(p)){
                ChatUtils.sendClickableCommandMessage(subject, "mensajes.votacion.Votar Pocos","/mimir",null);
            }
        }
        plugin.getLogger().info("Se ha comenzado la votacion");
        new BukkitRunnable(){
            @Override
            public void run() {
                votingInProgress = true;
                if(votes.size() == 2){
                    SleepUtils.passNight(p.getServer());
                    votingInProgress = false;
                    votes.clear();
                    successfulSleepMessage();
                    this.cancel();
                }
                if(!SleepUtils.isNight(p.getWorld())){
                    votingInProgress = false;
                    votes.clear();
                    unsuccessfulSleepMessage();
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin,0,1);
    }

    private static void startLargeVote(Player p) {
        ChatUtils.broadcastMessage(p.getServer(),"mensajes.votacion.Iniciar Votacion");
        ChatUtils.broadcastCustomMessage(p.getServer(),"&l--------------------------------------------");
        broadcastWorldOfPlayers();
        ChatUtils.broadcastCustomMessage(p.getServer(),"&l--------------------------------------------");
        ChatUtils.broadcastClickableCommandMessage(plugin.getServer(), "mensajes.votacion.Votar Muchos", "/mimir", null);
        new BukkitRunnable(){
            @Override
            public void run() {
                votingInProgress = true;
                if(votes.size() > avaiablePlayers * porcetageToDay){
                    SleepUtils.passNight(p.getServer());
                    votingInProgress = false;
                    votes.clear();
                    successfulSleepMessage();
                    this.cancel();
                }
                if(!SleepUtils.isNight(p.getWorld())){
                    votingInProgress = false;
                    votes.clear();
                    unsuccessfulSleepMessage();
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin,0,1);
    }

    private static void broadcastWorldOfPlayers() {
        for (Map.Entry<World.Environment, Integer> world : playersPerWorld().entrySet()){
            if(world.getValue() > 0){

                String worldType;

                switch (world.getKey()){
                    case NORMAL:
                        worldType = "&2&lNormal";
                        break;
                    case NETHER:
                        worldType = "&4&lNether";
                        break;
                    case THE_END:
                        worldType = "&8&lEnd";
                        break;
                    default:
                        worldType = "&c&lUnknown";
                        break;
                }

                String message = String.format(MessagesFile.getMessages().getString("mensajes.votacion.Jugadores Mundo"), world.getValue(), worldType);
                plugin.getServer().broadcastMessage(ChatUtils.colorize(message));
            }
        }
        String afks = String.format(MessagesFile.getMessages().getString("mensajes.votacion.Jugadores AFK"),AFK.getAfkPlayers().size());
        plugin.getServer().broadcastMessage(ChatUtils.colorize(afks));
    }

    private static void successfulSleepMessage() {
        for(Player subject : plugin.getServer().getOnlinePlayers()){
            if(subject.getWorld().getEnvironment() == World.Environment.NORMAL){
                ChatUtils.sendMessage(subject,"mensajes.dormir.Durmio Bien");
            }
        }
    }

    private static void unsuccessfulSleepMessage() {
        for(Player subject : plugin.getServer().getOnlinePlayers()){
            if(subject.getWorld().getEnvironment() == World.Environment.NORMAL){
                ChatUtils.sendMessage(subject,"mensajes.dormir.Durmio Mal");
            }
        }
    }

    private static HashMap<World.Environment, Integer> playersPerWorld() {
        HashMap<World.Environment,Integer> totalPlayers = new HashMap<>();

        for(World w : plugin.getServer().getWorlds()){
            if(w.getEnvironment() == World.Environment.THE_END){
                totalPlayers.put(World.Environment.THE_END, w.getPlayers().size());
            }
            if(w.getEnvironment() == World.Environment.NORMAL){
                totalPlayers.put(World.Environment.NORMAL, w.getPlayers().size());
            }
            if(w.getEnvironment() == World.Environment.NETHER){
                totalPlayers.put(World.Environment.NETHER, w.getPlayers().size());
            }
        }
        return totalPlayers;
    }

    public static void checkAvaiablePlayers(){
        new BukkitRunnable(){
            @Override
            public void run() {
                int totalPlayers = plugin.getServer().getOnlinePlayers().size();
                int afkPlayers = AFK.getAfkPlayers().size();
                int otherWorlds = playersPerWorld().get(World.Environment.NETHER) + playersPerWorld().get(World.Environment.THE_END);
                avaiablePlayers = totalPlayers - afkPlayers - otherWorlds;
            }
        }.runTaskTimerAsynchronously(plugin,0,2);
    }
}
