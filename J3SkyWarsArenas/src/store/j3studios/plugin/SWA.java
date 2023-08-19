package store.j3studios.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import store.j3studios.plugin.arena.ArenaManager;
import store.j3studios.plugin.events.PlayerListener;
import store.j3studios.plugin.utils.Tools;
import store.j3studios.plugin.utils.socket.ArenaSocket;
import store.j3studios.plugin.utils.socket.SendTask;

public class SWA extends JavaPlugin {
    
    private static SWA ins;
        
    private ArenaManager am;
    
    public void onEnable() {
        ins = this;   
        
        // LOAD CONFIG
        this.getConfig();
        this.saveDefaultConfig();
        
        // LOAD BUNGEECORD SUPPORT
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        // LOAD EVENTS
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        
        // LOAD MANAGERS
        am = new ArenaManager();
        
        // LOAD SOCKET
        if (this.getConfig().getBoolean("server.socket-connector.enable")) {
        	ArenaSocket.lobbies.addAll(this.getConfig().getStringList("server.socket-connector.lobbies"));
        	new SendTask();
        }
                
        debug("&aPlugin loaded sucessfully!");
    }

    public void onDisable() {
    	if (this.getConfig().getBoolean("server.socket-connector.enable")) {
    		ArenaSocket.disable();
    	}
    }
    
    /*
      GETTING INSTANCE
     */
    public static SWA get() {
        return ins;
    }
    
    public static void debug(String str) {
        Bukkit.getConsoleSender().sendMessage(Tools.get().Text("&7[J3SWArenas]: " + str));
    }
    
    public ArenaManager getAM() {
    	return am;
    }
    
}
