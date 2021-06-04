package com.kildall.mimision2.utils;

import com.kildall.mimision2.Mimision2;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.ArrayList;

public class SleepUtils {

    private static final Mimision2 plugin = Mimision2.getInstance();
    private static ArrayList<World> normalWorlds = new ArrayList<>();
    private static int nightSpeed = loadPassingSpeed();

    public static ArrayList<World> getNormalWorlds() {
        return normalWorlds;
    }

    //Suma tiempo hasta que se vuelva de dia de nuevo
    public static void passNight(Server s){
        VotingUtils.setNightPassing(true);
        VotingUtils.setVotingInProgress(false);
        for (World world : s.getWorlds()){
            if(world.getEnvironment().equals(World.Environment.NORMAL)){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        world.setTime(world.getTime() + nightSpeed);
                        if(world.getTime() < 1000){
                            VotingUtils.setNightPassing(false);
                            VotingUtils.clearVotes();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 1);
            }
        }
    }


    //Devuelve true si hay una cama cerca, false si no la hay
    public static boolean isBedNear(Player p) {
        for (double x = -10; x <= 10; x++) {
            for (double z = -10; z <= 10; z++) {
                for (double y = -10; y <= 5; y++) {
                    //Si alguna de las coordenadas contiene un bloque con el "bed" en el nombre, devolver true
                    if (new Location(p.getWorld(),p.getLocation().getX() + x,p.getLocation().getY() + y,p.getLocation().getZ() + z).getBlock().getType().toString().toLowerCase().contains("bed")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Puede dormir el jugador en el mundo y en el tiempo que se encuentra?
    public static boolean canSleep(Player p){
        return isNight(p.getWorld()) && sleepableWorld(p.getWorld()) && isBedNear(p);
    }

    public static boolean isNight(World w){
        return w.getTime() >= 13000;
    }

    public static boolean sleepableWorld(World w){
        return w.getEnvironment().equals(World.Environment.NORMAL);
    }

    private static int loadPassingSpeed(){
        int temp = plugin.getConfig().getInt("opciones.mimision.velocidad del amanecer");
        if(temp > 1000 || temp < 1){
            return temp;
        } else {
            return 125;
        }
    }



}
