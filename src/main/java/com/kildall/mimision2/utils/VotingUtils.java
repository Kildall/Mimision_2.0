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


    //Booleanos de estado
    private static boolean votingInProgress = false;
    private static boolean nightPassing = false;

    //ArrayList de Votos
    private static ArrayList<UUID> votes = new ArrayList<>();

    //Numeros de configuracion
    private static int avaiablePlayers = 0;
    private static float porcetageToDay = (float) plugin.getConfig().getInt("opciones.mimision.porcentaje de votos") / 100;

    /**
     * @param p  = Bukkit Player
     *
     *           If number of players avaiable to sleep is greater than 2, then a major voting event starts.
     *           If number of players avaiable to sleep is 2, then a miner voting event starts.
     *           If there is only 1 player, simply pass the night.
     */
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

    //Getter y Setter de nightPassing
    /**
     * @return Has the voting event concluded but the night is still passing? True if yes, False if not
     */
    public static boolean isNightPassing() {
        return nightPassing;
    }

    /**
     * @param nightPassing Set nightPassing to True or False.
     */
    public static void setNightPassing(boolean nightPassing) {
        VotingUtils.nightPassing = nightPassing;
    }

    /**
     * Executes a clear of the votes list.
     */
    //Vaciar la lista de votos
    public static void clearVotes() {
        VotingUtils.votes.clear();
    }

    /**
     * @return Is there a voting event in progress? True if yes, False if not
     */
    //Getter y Setter para votingInProgress
    public static boolean isVotingInProgress() {
        return votingInProgress;
    }

    /**
     * @param votingInProgress Set the state of voting event.
     */
    public static void setVotingInProgress(boolean votingInProgress) {
        VotingUtils.votingInProgress = votingInProgress;
    }


    /**
     * @param p Adds a vote to the list if it's not already AND the night isn't passing AND there's a voting event in progress.
     */
    public static void vote(Player p){
        if(!votes.contains(p.getUniqueId()) && !nightPassing && votingInProgress){
            votes.add(p.getUniqueId());
        }
    }

    /**
     * @param p Removes vote from player of the list.
     */
    public static void removeVote(Player p){
        votes.remove(p.getUniqueId());
    }


    /**
     * @param p Player.
     * @return Has player p voted? True if yes, False if not.
     */
    public static boolean hasPlayerVoted(Player p){
        return votes.contains(p.getUniqueId());
    }


    private static void startSmallVote(Player p) {
        VotingUtils.setVotingInProgress(true);
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
                setVotingInProgress(true);
                if(votes.size() == 2){
                    SleepUtils.passNight(p.getServer());
                    successfulSleepMessage();
                    this.cancel();
                }
                if(!SleepUtils.isNight(p.getWorld())){
                    votes.clear();
                    unsuccessfulSleepMessage();
                    setVotingInProgress(false);
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(plugin,0,1);
    }

    private static void startLargeVote(Player p) {
        VotingUtils.setVotingInProgress(true);
        ChatUtils.broadcastMessage(p.getServer(),"mensajes.votacion.Iniciar Votacion");
        ChatUtils.broadcastCustomMessage(p.getServer(),"&l--------------------------------------------");
        broadcastWorldOfPlayers();
        ChatUtils.broadcastCustomMessage(p.getServer(),"&l--------------------------------------------");
        ChatUtils.broadcastClickableCommandMessage(plugin.getServer(), "mensajes.votacion.Votar Muchos", "/mimir", null);
        new BukkitRunnable(){
            @Override
            public void run() {
                if(votes.size() >= avaiablePlayers * porcetageToDay){
                    SleepUtils.passNight(p.getServer());
                    votes.clear();
                    successfulSleepMessage();
                    this.cancel();
                }
                if(!SleepUtils.isNight(p.getWorld())){
                    votes.clear();
                    unsuccessfulSleepMessage();
                    setVotingInProgress(false);
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
        if(AFK.getAfkPlayers().size() > 0){
            String afks = String.format(MessagesFile.getMessages().getString("mensajes.votacion.Jugadores AFK"),AFK.getAfkPlayers().size());
            plugin.getServer().broadcastMessage(ChatUtils.colorize(afks));
        }
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
