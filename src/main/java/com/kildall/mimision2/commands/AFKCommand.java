package com.kildall.mimision2.commands;

import com.kildall.mimision2.utils.AFK;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFKCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage("No puedes ejecutar este comando desde la consola.");
            return false;
        }

        Player player = (Player) sender;
        if(command.getName().equals("afk") && AFK.getAfkPlayers().containsKey(player.getUniqueId())){
            AFK.removeAFKByCommand(player);
        } else {
            AFK.addAFKByCommand(player);
        }


        return true;
    }
}
