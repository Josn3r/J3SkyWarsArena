package store.j3studios.plugin.utils.socket;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import store.j3studios.plugin.SWA;

public class SendTask {

	/*
	 * The is used to send data to new lobby servers to improve data sync
	 */
	
	public SendTask() {
		Bukkit.getScheduler().runTaskTimer(SWA.get(), () -> {
			// Create List for Arenas
			new BukkitRunnable() {
				@Override
				public void run() {
					// for (Arena a : arenas)
					//ArenaSocket.sendMessage("");
					ArenaSocket.sendMessage(MessageType.formatUpdateMessage());
				}
			}.runTaskAsynchronously(SWA.get());
		}, 100L, 30L);
	}
	
}
