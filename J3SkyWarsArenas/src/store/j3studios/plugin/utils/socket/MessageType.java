package store.j3studios.plugin.utils.socket;

import org.bukkit.Bukkit;

import com.google.gson.JsonObject;

public class MessageType {

    public static String formatUpdateMessage() {
        
        JsonObject js = new JsonObject();
        js.addProperty("type", "UPDATE");
        js.addProperty("server_name", "arenas");
        js.addProperty("max_players", ""+Bukkit.getOnlinePlayers().size());
        js.addProperty("current_players", ""+Bukkit.getMaxPlayers());
        return js.toString();
    }
	
}
