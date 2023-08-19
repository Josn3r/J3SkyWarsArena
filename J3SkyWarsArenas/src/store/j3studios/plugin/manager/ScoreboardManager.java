package store.j3studios.plugin.manager;
  
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import store.j3studios.plugin.SWA;
import store.j3studios.plugin.utils.ScoreboardUtil;
import store.j3studios.plugin.utils.Tools;

public class ScoreboardManager {
    
    private static ScoreboardManager sm;
    private int lobby;
    
    public static ScoreboardManager get() {
        if (sm == null) {
            sm = new ScoreboardManager();
        }
        return sm;
    }
	        
    public void stop(int i) {
    	Bukkit.getScheduler().cancelTask(i);    	
    }
    
    @SuppressWarnings("deprecation")
    public void createScoreboard (Player p) {
        final ScoreboardUtil s = new ScoreboardUtil("&6&lSCOREBOARD", "board");
    	
    	this.lobby = Bukkit.getScheduler().scheduleAsyncRepeatingTask(SWA.get(), () -> {
            if (!p.isOnline()) {
                return;
            }
            
            //Integer i = 15;
            s.setName("&6&lSCOREBOARD"); 
            s.lines(4, Tools.get().Text(" "));
            s.lines(3, Tools.get().Text("&7SkyWars Arena"));
            s.lines(2, Tools.get().Text("&7Developing..."));
            s.lines(1, Tools.get().Text(""));
            s.lines(0, Tools.get().Text("&eminespazio.com"));
        }, 0L, 3L);    	
    	s.build(p);
    }
         
    public void stopLobby () {
    	this.stop(lobby);
    }
    	
}
