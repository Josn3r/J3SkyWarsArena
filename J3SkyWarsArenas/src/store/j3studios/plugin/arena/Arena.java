package store.j3studios.plugin.arena;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import store.j3studios.plugin.SWA;
import store.j3studios.plugin.enums.ArenaStatus;
import store.j3studios.plugin.utils.Tools;

public class Arena {
	
	private World world;
	private String name;
	private int minPlayers, maxPlayers;
	
	private int salir, salirTime, check, taskTiempo;
	private int refillCount;
	public boolean refilled;
	private boolean fall;
	
	private ArrayList<Player> alive = new ArrayList<Player>();
	private ArrayList<Player> spectator = new ArrayList<Player>();
	private ArrayList<Player> all = new ArrayList<Player>();
	
	private HashMap<String, Integer> cofre = new HashMap<String, Integer>();
	private HashMap<String, Integer> time = new HashMap<String, Integer>();
	private HashMap<String, Integer> vida = new HashMap<String, Integer>();
	private HashMap<String, Integer> proyectiles = new HashMap<String, Integer>();
	
	private ArrayList<String> acofre = new ArrayList<String>();
	private ArrayList<String> atime = new ArrayList<String>();
	private ArrayList<String> avida = new ArrayList<String>();
	private ArrayList<String> aproyectiles = new ArrayList<String>();
	
	private ArrayList<Location> islandChest = new ArrayList<Location>();
	private ArrayList<Location> centerChest = new ArrayList<Location>();
	
	private HashMap<Location, Boolean> Spawns = new HashMap<Location, Boolean>();
	private HashMap<Player, Location> PSpawns = new HashMap<Player, Location>();
	
	private List<Location> sp = new ArrayList<Location>();
	private List<Location> ic = new ArrayList<Location>();
	private List<Location> cc = new ArrayList<Location>();
	
	private ArenaStatus arenaStatus;
	
	public Arena (World world, String name, Integer minPlayers, Integer maxPlayers) {
		this.world = world;
		
		this.name = name;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		
		this.fall = true;
		salirTime = 30;
		refillCount = 300;
		refilled = false;
		
		setArenaStatus(ArenaStatus.WAITING);
		
		//back();
		//principal();
		//loadSpawns();
		//loadChests();
	}

	public ArenaStatus getArenaStatus() {
		return arenaStatus;
	}

	public void setArenaStatus(ArenaStatus arenaStatus) {
		this.arenaStatus = arenaStatus;
	}
	
	public boolean isArenaStatus(ArenaStatus status) {
		if (arenaStatus == status) {
			return true;
		}
		return false;
	}

	public ArrayList<Location> getIslandChest() {
		return islandChest;
	}

	public ArrayList<Location> getCenterChest() {
		return centerChest;
	}
	
	public String getChest() {
		String win = "Normal";
		if (cofre.get("Normal").intValue() >  cofre.get("Insane").intValue()) {
			win = "Normal";
		} else if (cofre.get("Normal").intValue() < cofre.get("Insane").intValue()) {
			win = "Insane";
		} else if (cofre.get("Normal").intValue() == cofre.get("Insane").intValue()) {
			win = "Normal";
		}
		return win;
	}
	
	public String getTiempo() {
		String win = "Dia";
		Integer m = 0;
		for (int i : time.values()) {
			if (i > m) {
				m = i;
			}
		}
		if (m==0) {
			return win;
		}
		for (String a : time.keySet()) {
			if (time.get(a) == m) {
				win = a;
			}
		}
		return win;
	}

	public String getCorazones() {
		String win = "C10";
		Integer m = 0;
		for (int i : vida.values()) {
			if (i > m) {
				m = i;
			}
		}
		if (m==0) {
			return win;
		}
		for (String a : vida.keySet()) {
			if (vida.get(a) == m) {
				win = a;
			}
		}
		return win;
	}	

	public String getProyectiles() {
		String win = "Yes";
		Integer m = 0;
		for (int i : proyectiles.values()) {
			if (i > m) {
				m = i;
			}
		}
		if (m==0) {
			return win;
		}
		for (String a : proyectiles.keySet()) {
			if (proyectiles.get(a) == m) {
				win = a;
			}
		}
		return win;
	}
	
	public ArrayList<Player> getAllPlayers() {
		return all;
	}
	
	public ArrayList<Player> getAlive() {
		return alive;
	}
	
	public ArrayList<Player> getSpectators() {
		return spectator;
	}
	
	public HashMap<String, Integer> dataChest() {
		return cofre;
	}
	
	public HashMap<String, Integer> dataTime() {
		return time;
	}
	
	public HashMap<String, Integer> dataVida() {
		return vida;
	}
	
	public HashMap<String, Integer> dataProyectiles() {
		return proyectiles;
	}
	
	public int chestData (String value) {
		return this.cofre.get(value);
	}
	
	public int timeData (String value) {
		return this.time.get(value);
	}
	
	public int vidaData (String value) {
		return this.vida.get(value);
	}
	
	public int proyectilesData (String value) {
		return this.proyectiles.get(value);
	}
	
	//
	
	public void back() {
		acofre.clear();
		atime.clear();
		avida.clear();
		aproyectiles.clear();
		
		cofre.clear();
		time.clear();
		vida.clear();
		proyectiles.clear();
		
		acofre.add("Normal");
		acofre.add("Insane");
		
		atime.add("Dia");
		atime.add("Tarde");
		atime.add("Noche");
		
		avida.add("C10");
		avida.add("C20");
		avida.add("C30");
		avida.add("UHC");
		
		aproyectiles.add("No");
		aproyectiles.add("Yes");
		aproyectiles.add("Destructor");
		aproyectiles.add("Teleport");
		aproyectiles.add("Explosive");
		
		for (String b : acofre) {
			cofre.put(b, 0);
		}
		for (String b : atime) {
			time.put(b, 0);
		}
		for (String b : avida) {
			vida.put(b, 0);
		}
		for (String b : aproyectiles) {
			proyectiles.put(b, 0);
		}
		
		this.fall = true;
		salirTime = 30;
		refillCount = 300;
		refilled = false;
	}
	
	public void loadSpawns() {
		for (Location loc : sp) {
			Spawns.put(loc, false);
			Location l2 = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
			if (l2.getBlock().getType().equals(Material.BEACON)) {
				l2.getBlock().setType(Material.AIR);
			}
		}
	}
	
	public void loadChests() {
		for(Location loc : ic){
			islandChest.add(loc);
		}
		for(Location loc : cc){
			centerChest.add(loc);
		}
	}
	
	public Location getAnySpawn() {
		for(Entry<Location, Boolean> key : Spawns.entrySet()) {
			if(key.getValue().booleanValue() == false){
				return key.getKey();
			}
		}
		return null;
	}
	
	public boolean isArenaFull(){
		if(minPlayers >= maxPlayers){
			return true;
		}
		return false;
	}
	
	public void load() {
		File file = new File("plugins/Josn3rSkyWars/arenas/"+this.name+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("config.editMode", true);
		config.set("config.displayName", this.name);
		config.set("config.players.min", this.minPlayers);
		config.set("config.players.max", this.maxPlayers);
		config.set("config.spawns", null);
		config.set("config.chests.island", null);
		config.set("config.chests.center", null);
		Tools.get().save(config, file);
	}
	
	public void sender(String sender){
		for(Player p : all){
			p.sendMessage(Tools.get().Text(sender));
		}
	}

	public void senderCenter (String sender){
		for(Player p : all){
			Tools.get().sendCenteredMessage(p, Tools.get().Text(sender));
		}
	}
	
	/*
	@SuppressWarnings("deprecation")
	public void join(Player p) {
		Party party = PartyManager.get().getTeam(p);
		
		if (party != null) {
			if (p != party.getOwner()) {
				if (ArenaManager.get().getArena(p) != null) {
					ArenaManager.get().getArena(p).leaveReset(p);
				}
				p.sendMessage(Tools.get().Text(PartyManager.get().getPrefix() + "&cSolo el lider de la party puede entrar o cambiar una partida!"));
				return;
			}
			
			join2(p);
			
		 	for (Player player : party.getPlayersList()) {
		 		if (player != p) {
		 			if (ArenaManager.get().getArena(player) != null) {
			 			ArenaManager.get().getArena(player).leaveReset(p);
			 			p.setMaxHealth(20);
						p.setHealth(20);
						p.setGameMode(GameMode.SURVIVAL);
						p.teleport(Tools.get().setStringToLoc(SWA.get().getConfig().getString("locations.main-lobby"), false));
			 		}
			 		join2(player);
		 		}
		 	}
		 	
		} else {
			join2(p);
		}
 	}*/
	
	public void join2(Player p) {
		/*
		if(isArenaFull()) {
			p.sendMessage(Tools.get().Text(Skywars.PREFIX + Skywars.lang.getString("messages.arena.tryJoinArena.arenaFull")));
			return;
		}
		
		if(isArenaState(ArenaState.EDITMODE)) {
			p.sendMessage(Tools.get().Text(Skywars.PREFIX + Skywars.lang.getString("messages.arena.tryJoinArena.arenaEditor")));
			return;
		}
		
		if(isArenaState(ArenaState.FINISH)) {
			p.sendMessage(Tools.get().Text(Skywars.PREFIX + Skywars.lang.getString("messages.arena.tryJoinArena.arenaRestarting")));
			return;
		}
		
		if(isArenaState(ArenaState.GAME)) {
			if (!p.hasPermission("jsw.game.spectate")) {
				p.sendMessage(Tools.get().Text(Skywars.PREFIX + Skywars.lang.getString("messages.arena.tryJoinArena.arenaInGame")));
				return;
			}
			spectJoin(p);
			return;
		}
		
		Location loc = getAnySpawn();
		if(loc == null){
			p.sendMessage(Tools.get().Text(Skywars.PREFIX + Skywars.lang.getString("messages.arena.tryJoinArena.errorTryJoin.spawnNotFound")));
			return ;
		}
		
		SPlayer player = PlayerManager.get().getPlayer(p.getUniqueId());
		all.add(p);
		ingame.add(p);
		playerKills.put(p, 0);
				
		Spawns.put(loc, true);
		PSpawns.put(p, loc);
		
		p.teleport(loc.clone().add(0.5, 0.0, 0.5));
		player.setSpawnLoc(loc);
		player.setArenaGlass(loc);
		
		p.setGameMode(GameMode.ADVENTURE);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setFireTicks(0);
		p.setLevel(0);
		p.setExp(0);
		p.getInventory().setArmorContents(null);
		p.getInventory().clear();
		for(PotionEffect e : p.getActivePotionEffects()){
			p.removePotionEffect(e.getType());
		}
		
		sender(Tools.get().Text(Skywars.PREFIX + Skywars.lang.getString("messages.arena.onJoin")).replaceAll("<PLAYER>", p.getName()).replaceAll("<PLAYERS>",""+ ingame.size()).replaceAll("<MIN>",""+ min).replaceAll("<MAX>",""+ max));
		
		String title = Skywars.lang.getString("messages.arena.joinArenaTitle").replace("<MODE>", this.getChest().equals("Normal") ? "&aNormal" : "&cInsane");
		Tools.get().sendTitle(p, title.split(" : ")[0], title.split(" : ")[1], 20, 100, 20);
		
		Tools.get().playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 10));		
		
		p.getInventory().setArmorContents(null);
		p.getInventory().clear();
		
		Skywars.giveArenaItem(p);
		p.updateInventory();
		
		if (ingame.size() >= this.min) {
			if(isArenaState(ArenaState.STARTING)) {
				return;
			}
			second();
			setArenaState(ArenaState.STARTING);
		}	
		*/
	}

	public void spect(final Player p){
		
		if (spectator.contains(p)) {
			return;
		}
				
		new BukkitRunnable() {
			@Override
			public void run() {	
				//SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
				p.spigot().respawn();
				
				alive.remove(p); 
				spectator.add(p);
				/*
				sp.setDeaths(sp.getDeaths()+1);
				
				for (String str : Skywars.lang.getStringList("messages.arena.onDeath.player")) {
					str = str.replace("<COINS>", ""+Vault.getCoinsKill(p));
					p.sendMessage(Tools.get().Text(str));
				}
				VoteChest.maxVote.remove(p);
				VoteTime.maxVote.remove(p);*/
				
				//sp.setLastHit(null);
				
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				
				new BukkitRunnable() {
					@Override
					public void run() {
						p.sendMessage(Tools.get().Text(" "));
						p.sendMessage(Tools.get().Text("&f&m----------------------------------------------------"));
						p.sendMessage(Tools.get().Text(" "));				
						Tools.get().sendClickeableMessage(p, Tools.get().Text("       &a&lJugar otra vez &7- Juega otra partida más."), "/wsw playagaincmd");
						Tools.get().sendClickeableMessage(p, Tools.get().Text("       &c&lSalir &7- Abandona la arena y vuelve al lobby."), "/leave");
						p.sendMessage(Tools.get().Text(" "));
						p.sendMessage(Tools.get().Text("&f&m---------------------------------------------------"));
						p.sendMessage(Tools.get().Text(" "));
					}
				}.runTaskLater(SWA.get(), 10L);
							
				if (alive.size() > 1) {
					Player random = alive.get(new Random().nextInt(alive.size()-1));
					if(random!=null){
						p.teleport(random.getLocation());
					}
				} else {
					Player random = alive.get(0);
					if(random!=null){
						p.teleport(random.getLocation());
					}
				}
				
				
				new BukkitRunnable() {
					@Override
					public void run() {
						p.setGameMode(GameMode.SPECTATOR);
					}
				}.runTaskLater(SWA.get(), 5L);
			}
		}.runTaskLater(SWA.get(), 1L);
	}
	
	public void spectJoin(final Player p){
		
		if (spectator.contains(p)) {
			return;
		}
				
		new BukkitRunnable() {
			@Override
			public void run() {	
				//SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
				
				all.add(p);
				spectator.add(p);
				
				p.setGameMode(GameMode.SPECTATOR);
				p.setHealth(20);
				p.setFoodLevel(20);
				p.setFireTicks(0);
				p.setLevel(0);
				p.setExp(0);
				p.getInventory().setArmorContents(null);
				p.getInventory().clear();
				for(PotionEffect e : p.getActivePotionEffects()){
					p.removePotionEffect(e.getType());
				}
								
				p.sendMessage(Tools.get().Text(" "));
				p.sendMessage(Tools.get().Text("&f&m----------------------------------------------------"));
				p.sendMessage(Tools.get().Text("&fAhora estás especteando la partida"));
				p.sendMessage(Tools.get().Text("&f&m---------------------------------------------------"));
				p.sendMessage(Tools.get().Text(" "));
								
				Player random = alive.get(new Random().nextInt(alive.size()-1));
				if(random!=null){
					p.teleport(random.getLocation());
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						p.setGameMode(GameMode.SPECTATOR);
					}
				}.runTaskLater(SWA.get(), 5L);
			}
		}.runTaskLater(SWA.get(), 1L);
	}

	public void setTime(){
		taskTiempo = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SWA.get(), new Runnable() {
			@Override
			public void run() {
				if (getTiempo().equalsIgnoreCase("Day")) {
					world.setTime(0L);
				}else if (getTiempo().equalsIgnoreCase("Sunset")) {
					world.setTime(12000L);
				}else if (getTiempo().equalsIgnoreCase("Night")) {
					world.setTime(18000L);
				}
			}
		}, 0L, 20L);
	}
	
	public void setCorazones () {
		for (Player player : all) {
			if (getCorazones().equalsIgnoreCase("C10")) {
				player.setMaxHealth(20);
				player.setHealth(player.getMaxHealth());
			} else if (getCorazones().equalsIgnoreCase("C20")) {
				player.setMaxHealth(40);
				player.setHealth(player.getMaxHealth());
			} else if (getCorazones().equalsIgnoreCase("C30")) {
				player.setMaxHealth(60);
				player.setHealth(player.getMaxHealth());
			} else if (getCorazones().equalsIgnoreCase("UHC")) {
				player.setMaxHealth(20);
				player.setHealth(player.getMaxHealth());
			}
		}		
	}
	
/*
	public void leave(Player p) {
		Party party = PartyManager.get().getTeam(p);
		
		if (party != null) {
			if (p != party.getOwner()) {
				p.sendMessage(Tools.get().Text(PartyManager.get().getPrefix() + "&cSolo el lider de la party puede abandonar o cambiar una partida!"));
				return;
			}
			leave2(p);
		 	for (Player player : party.getPlayersList()) {
		 		if (player != p) {
		 			leave2(player);
		 		}
		 	}
		} else {
			leave2(p);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void leave2(Player p){	
		
		if (isArenaState(ArenaState.WAITING) || isArenaState(ArenaState.STARTING)) {
			if (!p.getWorld().getName().equals(Tools.get().getLobbyWorld())) {
				Tools.get().deleteCage(p.getLocation());
			}
		}
		
		all.remove(p);
		SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
		sp.setKillsGame(0);
		sp.setSpawnLoc(null);
		sp.stopWinEffect();
		
		if (VoteChest.maxVote.containsKey(p)) {
			dataChest().put(VoteChest.maxVote.get(p).toString(), chestData(VoteChest.maxVote.get(p).toString())-1);
		}
		if (VoteTime.maxVote.containsKey(p)) {
			dataTime().put(VoteTime.maxVote.get(p).toString(), timeData(VoteTime.maxVote.get(p).toString())-1);
		}
		
		VoteTime.maxVote.remove(p);
		VoteChest.maxVote.remove(p);	
		
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setGameMode(GameMode.SURVIVAL);
		p.setMaxHealth(20);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setFireTicks(0);
		p.setLevel(0);
		p.setExp(0);
		ingame.remove(p);
		out.remove(p);
		
		p.teleport(Tools.get().setStringToLoc(SWA.get().getConfig().getString("locations.main-lobby"), false));
		
		
		if (isArenaState(ArenaState.WAITING) || isArenaState(ArenaState.STARTING)) {
			Spawns.put(PSpawns.get(p), false);
			playerKills.remove(p);
			
			sender(Tools.get().Text(Skywars.PREFIX + Skywars.lang.getString("messages.arena.onLeave")).replaceAll("<PLAYER>", p.getName()).replaceAll("<PLAYERS>",""+ ingame.size()).replaceAll("<MIN>",""+ min).replaceAll("<MAX>",""+ max));

			if(ingame.size() < this.min){
				setArenaState(ArenaState.WAITING);
				stop(salir);
				salirtime = SWA.get().getConfig().getInt("config.game.countdowns.startingCount");
			}
		}
		
		Skywars.giveLobbyItem(p);
		
		Tools.get().playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 10), false);
		Tools.get().sendTitle(p, "", "", 1, 1, 1);
		
		p.updateInventory();
		
	}
	
	@SuppressWarnings("deprecation")
	public void leaveReset(Player p){
		
		if (!isArenaState(ArenaStatus.GAME)) {
			if (!p.getWorld().getName().equals(Tools.get().getLobbyWorld())) {
				Tools.get().deleteCage(p.getLocation());
			}
		}
		
		/*
		if (VoteChest.maxVote.containsKey(p)) {
			dataChest().put(VoteChest.maxVote.get(p).toString(), chestData(VoteChest.maxVote.get(p).toString())-1);
		}
		if (VoteTime.maxVote.containsKey(p)) {
			dataTime().put(VoteTime.maxVote.get(p).toString(), timeData(VoteTime.maxVote.get(p).toString())-1);
		}
		
		
		VoteTime.maxVote.remove(p);
		VoteChest.maxVote.remove(p);
		VoteHearts.maxVote.remove(p);
		ProjectileVote.maxVote.remove(p);
		
		SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
		sp.setKillsGame(0);
		sp.setSpawnLoc(null);
		
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setGameMode(GameMode.SURVIVAL);
		p.setMaxHealth(20);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setFireTicks(0);
		p.setLevel(0);
		p.setExp(0);
		
		all.remove(p);
		ingame.remove(p);
		out.remove(p);	
		playerKills.remove(p);
		
		for (PotionEffect potionEffect : p.getActivePotionEffects()) {
            p.removePotionEffect(potionEffect.getType());
        }
		
		Skywars.giveLobbyItem(p);
	}
*/
	public String arena(){
		return this.name;
	}
	
	public World b(){
		return this.world;
	}
		
	public void gteleport(Player p){
		for(Location loc : sp){
			p.teleport(loc);
		}
	}
	
	public static void dar(Player p, ItemStack stack){
		p.getInventory().addItem(stack);
	}
	
	public static void dar(Player p, ItemStack stack, int i){
		p.getInventory().setItem(i, stack);
	}

	public void principal(){
		File file = new File("plugins/Josn3rSkyWars/arenas/"+this.name+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		for(String g : config.getStringList("config.spawns")) {
			sp.add(Tools.get().setStringToLoc(g, false));
		}
		
		for(String islche : config.getStringList("config.chests.island")) {
			ic.add(Tools.get().setStringToLoc(islche, true));
		}
		
		for(String cenche : config.getStringList("config.chests.center")) {
			cc.add(Tools.get().setStringToLoc(cenche, true));
		}		
	}
	
	public boolean fallMode(){
		return fall;
	}

	public void second(){
		salir = Bukkit.getScheduler().scheduleSyncRepeatingTask(SWA.get(), new Runnable() {
			@Override
			public void run() {
				
				for(Player p : getAllPlayers()){
					String formatColor = "";
					if (salirTime <= 5) {
						formatColor = "&c";
					} else if (salirTime <= 10) {
						formatColor = "&e";
					} else if (salirTime <= 300) {
						formatColor = "&a";
					}
					
					p.setLevel(salirTime);
					Tools.get().sendActionBar(p, "&eIniciando partida en " + formatColor + "" + salirTime + "s");
				}
				
				if(salirTime > 0 && salirTime < 5 || salirTime == 10 || salirTime == 20 || salirTime == 30 || salirTime == 60){
					/*String formatColor = "";
					
					if (salirTime <= 5) {
						formatColor = "&c";
						for(Player p : getAllPlayers()){
							Tools.get().playSound(p, Sound.ORB_PICKUP, 1.0f, 1.0f);
						}
					} else if (salirTime <= 10) {
						formatColor = "&e";
					} else if (salirTime <= 300) {
						formatColor = "&a";
					}
					*/
					
					/*for (Player p : getAllPlayers()){
						String title = Skywars.lang.getString("messages.arena.countdowns.titleStarting");
						Tools.get().sendTitle(p, title.split(" : ")[0], title.split(" : ")[1], 0, 30, 20);
						Tools.get().playSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
					}
					*/
					
					//sender(Skywars.lang.getString("messages.arena.countdowns.messageStarting").replaceAll("<COUNT>", ""+salirtime).replaceAll("<FORMAT_COLOR>", ""+formatColor));
				
				} else if (salirTime <= 0) {
					
					stop(salir);
					
					for (Entity ent : world.getEntities()) {
                    	if (!(ent instanceof Player)) {
                    		ent.remove();
                    	}
                    }
					
					for(Player p : getAllPlayers()){
						//SPlayer sp = PlayerManager.get().getPlayer(p.getUniqueId());
						p.closeInventory();
						play(p);
						
						/*if (sp.getKitSelect() != null) {
							sp.getKitSelect().setKit(p);
						}
						if(KitController.getData().get(p)!=null){
							KitController.getData().get(p).giveKit(p);
						}
						String title = Skywars.lang.getString("messages.arena.onGameStart.titleStarted");
						Tools.get().sendTitle(p, title.split(" : ")[0], title.split(" : ")[1], 0, 40, 20);
						Tools.get().playSound(p, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);*/
					}
					
					setTime();
					setCorazones();
					/*
					for (String str : Skywars.lang.getStringList("messages.arena.onGameStart.message")) {
						str = Tools.get().Text(str)
								.replaceAll("<EMPTY>", " ")
								.replaceAll("<CHEST_VOTE>", getChest()).replaceAll("<TIME_VOTE>", getTime())
								.replaceAll("<HEARTS_VOTE>", getCorazones()).replaceAll("<PROJECTILE_VOTE>", getProjectile())
								.replaceAll("<MAP_NAME>", ""+getCustomName()).replaceAll("<MAP_NAME_UPPERCASE>", ""+getCustomName().toUpperCase())
								.replaceAll("<TK_1_N>", tk_1_n).replaceAll("<TK_2_N>", tk_2_n).replaceAll("<TK_3_N>", tk_3_n)
								.replaceAll("<TK_1_K>", ""+tk_1_k).replaceAll("<TK_2_K>", ""+tk_2_k).replaceAll("<TK_3_K>", ""+tk_3_k);
						senderCenter(str);
					}
					
					setArenaState(ArenaState.GAME);*/
					arenaCheck(); // CHECK ARENA STATUS FOR FINISH
	
					new BukkitRunnable() {
						Integer timing = 10;						
						@Override
						public void run() {
							if (timing > 0) {
								for(Player p : getAllPlayers()){
									Tools.get().sendActionBar(p, "&eEl borde desaparecerá en &c" + timing + "s&e.");
								}
								--timing;
							} else {
								fall = false;
								for(Player p : getAllPlayers()){
									Tools.get().sendActionBar(p, "&eEl borde ha desaparecido, ya no estás protegido!");
								}
								cancel();
							}
						}
					}.runTaskTimer(SWA.get(), 20L, 20L);
				}
				
				salirTime-=1;
			}
		}, 0L, 20L);
	}
	
	public Integer getSalirTime () {
		return salirTime;
	}
	
	public void stop(int i){
		Bukkit.getScheduler().cancelTask(i);
	}

	public void play(Player p){
		/*SPlayer player = PlayerManager.get().getPlayer(p.getUniqueId());
		player.setPlayed(player.getPlayed()+1);*/
		
		p.setGameMode(GameMode.SURVIVAL);
		//Tools.get().deleteCage(PSpawns.get(p));
		p.getInventory().clear();
		p.setHealth(20);
		p.setFireTicks(0);
		p.setFoodLevel(20);
		p.setLevel(0);
		p.setExp(0);
	}

	public void importNewWorld() {
	    for(Player p : Bukkit.getWorld(name).getPlayers()){
	    	p.kickPlayer("Error, reporta esto a un administrador (#339)");
	    }
	    
		new BukkitRunnable() {
			@Override
			public void run() {
				//ArenaManager.get().resetWorld(world);
			}
		}.runTaskLater(SWA.get(), 20L);
		
		new BukkitRunnable() {
			@Override
			public void run() {
				principal();
			    loadSpawns();
			    loadChests();
			    
				setArenaStatus(ArenaStatus.WAITING);
			}
		}.runTaskLater(SWA.get(), 120L);
	}
	
	public void finishArena(){
		back();
		new BukkitRunnable() {
			@Override
			public void run() {
				importNewWorld();
			}
		}.runTaskLater(SWA.get(), 20L);
	}
	
	public void checkPlayer(){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getWorld().getName().equalsIgnoreCase(world.getName())){
				
				/*SPlayer player = PlayerManager.get().getPlayer(p.getUniqueId());
				player.setKillsGame(0);
				player.setSpawnLoc(null);*/
				
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				p.setGameMode(GameMode.SURVIVAL);
				
				p.setHealth(20);
				p.setFoodLevel(20);
				p.setFireTicks(0);
				p.setLevel(0);
				p.setExp(0);
	
				//p.teleport(Tools.get().setStringToLoc(""), false));
				
				p.setGameMode(GameMode.SURVIVAL);
				
				//leaveReset(p);
			}
		}
	}
		
	@SuppressWarnings("deprecation")
	public void arenaCheck(){
		check = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SWA.get(), new BukkitRunnable() {
			@Override
			public void run() {
				
				if (!refilled) {
					if (refillCount > 0) {
						--refillCount;
					} else {
						getIslandChest().clear();
						getCenterChest().clear();
						for (Player p : getAllPlayers()) {
							Tools.get().playSound(p, Sound.CHEST_OPEN, 1.0f, 1.0f);
							Tools.get().sendTitle(p, "&b&lRELLENADO!", "&eTodos los cofres han sido rellenados!", 0, 40, 0);
						}
						sender(Tools.get().Text("¡Todos los cofres se han rellenado!"));
						refilled = true;
					}
				}
				
				
				if(alive.size() <= 1 || alive.isEmpty()){
					
					fall = true;
					stop(taskTiempo);
					stop(check);
					
					setArenaStatus(ArenaStatus.FINISH);
					
					new BukkitRunnable() {
						@Override
						public void run() {
							finishArena();
						}
					}.runTaskLater(SWA.get(), 20L*15);
					
					/*SPlayer player = PlayerManager.get().getPlayer(getPlayerWin().getUniqueId());
					player.setWins(player.getWins()+1);
					player.startWinEffect();
					
					new BukkitRunnable() {
						@Override
						public void run() {
							player.stopWinEffect();
							checkPlayer();								
							resetData();
						}
					}.runTaskLater(SWA.get(), 20L*8);
										
					LeaderboardUtil.get().getLeaderboard(arena);					
					for (Player p : getAllPlayers()) {
						Tools.get().playSound(p, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 1.0f, 1.0f);
					}
					
					for (String str : Skywars.lang.getStringList("messages.arena.onGameFinish.message")) {
						str = Tools.get().Text(str)
								.replaceAll("<EMPTY>", " ")
								.replaceAll("<WINNER>", ""+getPlayerWin().getName())
								.replaceAll("<CHEST_TYPE>", getChest()).replaceAll("<TIME>", getTime())
								.replaceAll("<MAP_NAME>", ""+getCustomName()).replaceAll("<MAP_NAME_UPPERCASE>", ""+getCustomName().toUpperCase())
								.replaceAll("<TK_1_N>", tk_1_n).replaceAll("<TK_2_N>", tk_2_n).replaceAll("<TK_3_N>", tk_3_n)
								.replaceAll("<TK_1_K>", ""+tk_1_k).replaceAll("<TK_2_K>", ""+tk_2_k).replaceAll("<TK_3_K>", ""+tk_3_k);
						senderCenter(str);
					}
					
					LevelUtil.get().addEloWin(getPlayerWin());
					Vault.setMoney(getPlayerWin(), Vault.getCoinsWin(getPlayerWin()));
					
					Tools.get().sendTitle(getPlayerWin(), "&a&lVICTORIA!", "&fGanaste la partida!", 20, 100, 20);
					
					for (String str : Skywars.lang.getStringList("messages.arena.onGameFinish.player")) {
						str = str.replace("<COINS>", ""+Vault.getCoinsWin(getPlayerWin()));
						getPlayerWin().sendMessage(Tools.get().Text(str));
					}
					*/
				}
			}
		}, 0L, 20L);
	}
		
	public Player getPlayerWin(){
		if (!alive.isEmpty()) {
			return alive.get(0);
		}
		return null;
	}


	public void resetData(){
		sp.clear();
		Spawns.clear();
		PSpawns.clear();
		islandChest.clear();
	    centerChest.clear();
	    //playerKills.clear();
		all.clear();
		alive.clear();
		spectator.clear();
	}
	
}
