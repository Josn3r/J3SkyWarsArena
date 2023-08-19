package store.j3studios.plugin.utils.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import store.j3studios.plugin.SWA;
import store.j3studios.plugin.utils.Tools;

public class ArenaSocket {
	
	public static List<String> lobbies = new ArrayList<String>();
	private static final ConcurrentHashMap<String, RemoteLobby> sockets = new ConcurrentHashMap<>();
	
	/*
	 * SEND ARENA DATA TO THE LOBBIES
	 */
	
	public static void sendMessage (String message) {
		if (message == null) return;
		if (message.isEmpty()) return;
		
		for (String lobby : lobbies) {
			String[] l = lobby.split(":");
			
			if (l.length != 2) continue;
			if (!Tools.get().isInt(l[1])) continue;
			
			if (sockets.containsKey(lobby)) {
				sockets.get(lobby).sendMessage(message);
			} else {
				try {
					Socket socket = new Socket(l[0], Integer.parseInt(l[1]));
					RemoteLobby rl = new RemoteLobby(socket, lobby);
					if (rl.out != null) {
						sockets.put(lobby, rl);
						rl.sendMessage(message);
					}
				} catch (IOException ignore) {
					
				}
			}			
		}
	}
	
	/*
	 * 
	 */
	
	private static class RemoteLobby {
		private Socket socket;
		private PrintWriter out;
		private Scanner in;
		private String lobby;
		private boolean compute = true;
		
		private RemoteLobby (Socket socket, String lobby) {
			this.socket = socket;
			this.lobby = lobby;
			try {
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				out = null;
				return;
			}
			
			try {
				in = new Scanner(socket.getInputStream());
			} catch (IOException e) {
				return;
			}
			
			SWA.debug("&aSocket connection success: &f" + lobby + " &7- &f" + socket.toString());
			Bukkit.getScheduler().runTaskAsynchronously(SWA.get(), () -> {
				while(compute) {
					if (in.hasNext()) {
						String msg = in.next();
						SWA.debug(msg);
						if (msg.isEmpty()) continue;
						
						final JsonObject json;
						try {
							json = new JsonParser().parse(msg).getAsJsonObject();
						} catch (JsonSyntaxException e) {
							SWA.debug("Received bad data from: " + socket.getInetAddress().toString());
							continue;
						}
						if (json == null) continue;
						if (!json.has("type")) continue;
						
						switch (json.get("type").getAsString().toUpperCase()) {
							case "PLD":
								 if (!json.has("uuid")) break;
								 if (!json.has("arena_identifier")) break;
			                        
								SWA.debug("&eSocket server sending PLD data:");
								SWA.debug("&aUUID: &7" + json.get("uuid").getAsString());
		    					SWA.debug("&aArena ID: &f" + json.get("arena_identifier").getAsString());
		    					SWA.debug(" ");
								break;
						}
					} else {
						disable();
					}
				}
			});
		}
		
		/*
		 * Send a message to the given host with target port
		 */
		
		private boolean sendMessage(String message) {
			if (socket == null) {
				disable();
				return false;
			}
			if (!socket.isConnected()) {
				disable();
				return false;
			}
			if (out == null) {
				disable();
				return false;
			}
			if (in == null) {
				disable();
				return false;
			}
			if (out.checkError()) {
				disable();
				return false;
			}
			out.println(message);
			return true;
		}
		
		private void disable() {
			compute = false;
			SWA.debug("Disabling socket: " + socket.toString());
			sockets.remove(lobby);
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			in = null;
			out = null;
		}
	}

	public static void disable() {
		for (RemoteLobby rl : new ArrayList<>(sockets.values())) {
			rl.disable();
		}
	}
	
}
