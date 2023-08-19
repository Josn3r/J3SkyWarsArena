package store.j3studios.plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import store.j3studios.plugin.SWA;

public class ArenaCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			SWA.debug("Solo puede ejecutar comando siendo jugador.");
			return true;
		}
		
		//Player p = (Player)sender;
		
		return true;
	}

}
