package com.kildall.mimision2.commands;

import com.kildall.mimision2.utils.Voting;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

public class Mimir implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (!(sender instanceof Player)){
            return false;
        }
        Player p = (Player) sender;
        System.out.println("Comando recibido: " + command.getName());
        if(command.getName().equals("mimir")){
            System.out.println("Comando mimir ejecutado");
            Voting.startVoting(p.getServer());
        }

        return true;
    }
}
