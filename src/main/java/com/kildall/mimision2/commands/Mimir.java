package com.kildall.mimision2.commands;

import com.kildall.mimision2.Mimision2;
import com.kildall.mimision2.utils.ChatUtils;
import com.kildall.mimision2.utils.SleepUtils;
import com.kildall.mimision2.utils.VotingUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class Mimir implements CommandExecutor {

    private static final Logger logger = Mimision2.getInstance().getLogger();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            return false;
        }
        Player p = (Player) sender;

        if(command.getName().equals("mimir")){
            //Si no hay una votacion en progreso y puede dormir
            if(!VotingUtils.isVotingInProgress() && SleepUtils.canSleep(p)){
                //Comenzar una votacion, dando el primer voto al ejecutor del comando
                VotingUtils.startVoting(p);
                VotingUtils.vote(p);
            //Si hay una votacion en progreso y no ha votado todavia.
            } else if(VotingUtils.isVotingInProgress() && !VotingUtils.hasPlayerVoted(p)){
                //Se agrega un voto
                VotingUtils.vote(p);
                ChatUtils.sendMessage(p, "mensajes.votacion.Voto");
            //Si hay una votacion en progreso y el jugador ya voto
            } else if(VotingUtils.isVotingInProgress() && VotingUtils.hasPlayerVoted(p)){
                //Se avisa que ya voto
                ChatUtils.sendMessage(p, "mensajes.votacion.Votacion Repetida");
            //Si no puede dormir
            } else if(!SleepUtils.canSleep(p)){
                //Se avisa que no puede dormir
                ChatUtils.sendMessage(p, "mensajes.dormir.No Podes Dormir");
            }

        }

        return true;
    }
}
