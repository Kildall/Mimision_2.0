package com.kildall.mimision2.commands;

import com.kildall.mimision2.utils.ChatUtils;
import com.kildall.mimision2.utils.SleepUtils;
import com.kildall.mimision2.utils.VotingUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NoMimir implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            return false;
        }
        Player p = (Player) sender;

        if(command.getName().equals("nomimir")){

            //Si el jugador ya voto
            if(VotingUtils.hasPlayerVoted(p)){
                //Remover el voto y mandar mensajes correspondientes
                VotingUtils.removeVote(p);
                ChatUtils.sendMessage(p,"mensajes.votacion.Voto Eliminado");
                ChatUtils.sendMessage(p,"mensajes.dormir.Dormir Cancelado");
            } else {
                //No se puede cancelar si no voto.
                ChatUtils.sendMessage(p, "mensajes.errores.Error al Cancelar");
            }

        }

        return true;
    }
}
