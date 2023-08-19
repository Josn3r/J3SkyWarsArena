package store.j3studios.plugin.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import store.j3studios.plugin.manager.ScoreboardManager;

public class PlayerListener implements Listener {
    
    @EventHandler
    public void onPlayerLogin (PlayerLoginEvent e) {
        
    }
    
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        
        p.teleport(new Location(Bukkit.getWorld("arenas"), 0.0, 100.0, 0.0));
        p.setAllowFlight(true);
        p.setFlying(true);
        
        ScoreboardManager.get().createScoreboard (p);
    }
    
}
