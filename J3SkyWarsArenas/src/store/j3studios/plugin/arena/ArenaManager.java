package store.j3studios.plugin.arena;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.Files;

import store.j3studios.plugin.SWA;
import store.j3studios.plugin.utils.Tools;
import store.j3studios.plugin.utils.WEdit;

public class ArenaManager {
    
	private ArrayList<String> arenaList = new ArrayList<String>();
	
	private ArrayList<String> arenas = new ArrayList<String>();
	private ArrayList<Arena> a = new ArrayList<Arena>();
	
	public void loadArenas() {
		for (String maps : SWA.get().getConfig().getConfigurationSection("config.bungee-shared.maps").getKeys(false)) {
			arenaList.add(maps);
			SWA.debug("&7Loading data map: &f" + maps);
		}
	}
	
	public ArrayList<String> getArenas() {
		return arenas;
	}
	
	/*
	 * 
	 */
		
    public World createWorld(String w, String schematic) {
        World world = Bukkit.getWorld(w);
        if (world == null) {
        	WorldCreator wc = new WorldCreator(w);
        	wc.environment(World.Environment.NORMAL);
        	wc.generateStructures(false);
        	wc.generatorSettings("2;0;1;");
        	wc.type(WorldType.FLAT);
        	
            world = wc.createWorld();
            world.setAutoSave(false);
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("mobGriefing", "false");
            world.setThundering(false);
            world.setWeatherDuration(0);
            world.setTime(6000L);
            world.setSpawnLocation(0, 100, 0);            
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv load " + world.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvm set monsters false " + world.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvm set animals false " + world.getName());
            Bukkit.getServer().getWorlds().add(world);
            try {
				WEdit.pasteSchematicFAWE(schematic, world.getSpawnLocation(), true);
			} catch (IOException e) {
				e.printStackTrace();
			} 
            
            return world;
        } else {
        	 SWA.debug("&7El mundo " + w + " ya está creado.");
        	 return null;
        }
    }

    /*
     * 
     */
    
    
    
    public void createArenas() throws IOException{
		a.clear();
		
		File files = new File("plugins/Josn3rSkyWars/arenas");
		File mapas = new File("plugins/Josn3rSkyWars/mapas");
		
		if(!mapas.exists()){
			mapas.mkdirs();
		}
        if(!files.exists()){
        	files.mkdir();
        }
        
		for(File file : files.listFiles()) {
			if(file.isFile() && !file.isDirectory()) {
								
				//File arenaFile = new File("plugins/Josn3rSkyWars/arenas/"+file.getName());
				//YamlConfiguration config = YamlConfiguration.loadConfiguration(arenaFile);
				
				String name = file.getName().replaceAll(".yml", "");
				
				File source = new File("plugins/Josn3rSkyWars/mapas/"+name);
				
				try {
					if(!source.exists()){
						//Skywars.log("No se encontro el mundo (" + name + ") de la arena: " + displayName);
					} else {
						resetWorld(Bukkit.getWorld(name));
						
						new BukkitRunnable() {
							@Override
							public void run() {
								//Arena a = new Arena(name, displayName, config.getInt("config.players.min"), config.getInt("config.players.max"));
								//ArenaManager.a.add(a);
								//ArenaManager.arenas.add(name);								
								//Skywars.log("Se registro correctamente la arena " + displayName + " (" + name + ").");          
							}
						}.runTaskLater(SWA.get(), 20L);
					}
				} catch (Exception io) {
					//Skywars.log("Error al cargar la arena " + name + "...");
					io.printStackTrace();
				}
			}
		}
	}
		
	public Arena getArena(String name){
		for(Arena as : a) {
			if(as.arena().equals(name)){
				return as;
			}
		}
		return null;
	}
	
	public boolean exist(String name){
		File file = new File("plugins/Josn3rSkyWars/arenas/"+name+".yml");	
		return file.exists();
	}

	public Arena getArena(Player p){
		for(Arena alls : a){
			if(alls.getAllPlayers().contains(p)){
				return alls;
			}
		}
		return null;
	}
	
	public void addSpawn (Player p, World world, Location loc){
		File file = new File("plugins/Josn3rSkyWars/arenas/"+world.getName()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		List<String> l = config.getStringList("config.spawns");
		
		Location loc2 = loc.add(0.0D, 10.0D, 0.0D);
		l.add(Tools.get().setLocToString(loc2));
		
		config.set("config.spawns", l);
		Tools.get().save(config, file);
		
		SWA.debug("Created spawn location world " + world.getName() + ":  " + loc.add(0.0D, 10.0D, 0.0D).toString());
		p.sendMessage(Tools.get().Text("&bSe ha registrado un spawn correctamente. &7(#" + l.size() + ")"));
	}
	
	public void addChest (Player p, World world, Location loc, String type){
		File file = new File("plugins/Josn3rSkyWars/arenas/"+world.getName()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (type.equalsIgnoreCase("Island")) {
			List<String> l = config.getStringList("config.chests.island");
			if (l.contains(Tools.get().setLocToString(loc))) {
				return;
			}
			l.add(Tools.get().setLocToString(loc));
			config.set("config.chests.island", l);
			p.sendMessage(Tools.get().Text("&bSe ha registrado un &aCofre Isla &bcorrectamente. &7(#" + l.size() + ")"));
		} else {
			List<String> l = config.getStringList("config.chests.center");
			if (l.contains(Tools.get().setLocToString(loc))) {
				return;
			}
			l.add(Tools.get().setLocToString(loc));
			config.set("config.chests.center", l);
			p.sendMessage(Tools.get().Text("&bSe ha registrado un &aCofre Centro &bcorrectamente. &7(#" + l.size() + ")"));
		}
		Tools.get().save(config, file);
	}
	
	public void removeSpawn (Player p, World world, Location loc){
		File file = new File("plugins/Josn3rSkyWars/arenas/"+world.getName()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);		
		List<String> list = config.getStringList("config.spawns");
		if(list.isEmpty()){
			return;
		}
		for (String str : list) {
			String loc2 = Tools.get().setLocToString(p);
			if (str.contains(loc2)) {
				list.remove(str);
				config.set("config.spawns", list);
				Tools.get().save(config, file);
				p.sendMessage(Tools.get().Text("&bSe ha eliminado el spawn correctamente. &7(#" + str.split(",")[1] + ", " + str.split(",")[2] + ", " + str.split(",")[3] + ")"));
			}
		}
	}
	
	public void removeChest (Player p, World world, Location loc){
		File file = new File("plugins/Josn3rSkyWars/arenas/"+world.getName()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		List<String> isla = config.getStringList("config.chests.island");
		List<String> center = config.getStringList("config.chests.center");
		
		for (String load : isla) {
			Location locIsland = Tools.get().setStringToLoc(load, true);
			if (locIsland.equals(loc)) {
				isla.remove(load);
				config.set("config.chests.island", isla);
				p.sendMessage(Tools.get().Text("&bSe ha eliminado un &aCofre Isla &bcorrectamente. &7(#" + isla.size() + ")"));
			}
		}
		
		for (String load : center) {
			Location locIsland = Tools.get().setStringToLoc(load, true);
			if (locIsland.equals(loc)) {
				center.remove(load);
				config.set("config.chests.center", center);
				p.sendMessage(Tools.get().Text("&bSe ha eliminado un &aCofre Centro &bcorrectamente. &7(#" + center.size() + ")"));
			}
		}		
		Tools.get().save(config, file);
	}
	
	public void replaceBeaconsForAir (World world){
		File file = new File("plugins/Josn3rSkyWars/arenas/"+world.getName()+".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		List<String> list = config.getStringList("config.spawns");
				
		for (String str : list) {
			Location loc = Tools.get().setStringToLoc(str, false);
			Location loc2 = loc.subtract(0.0D, 10.0D, 0.0D);
			if (world.getBlockAt(loc2).getType().equals(Material.BEACON)) {
				world.getBlockAt(loc2).setType(Material.AIR);
			}
		}
	}		  
	
	public void saveWorld(World world) {
		if (world != null) {
			world.save();
			File worldFolder = new File("plugins/Josn3rSkyWars/mapas/" + world.getName());
			File srcWorldFolder = new File(world.getName());
			if (worldFolder.exists()) {
				deleteFolder(worldFolder);
			}
			copyWorldFolder(srcWorldFolder, worldFolder);
		}
	}

	public void resetWorld(final World world) {
		final File srcWorldFolder = new File("plugins/Josn3rSkyWars/mapas/" + world.getName());
		final File worldFolder = new File(world.getName());
	        
		if (srcWorldFolder.exists() && worldFolder.exists()) {
			//final Long seed = world.getSeed();
			final String generator = getChunkGeneratorAsName(world);
	            
			Bukkit.getServer().unloadWorld(world, false);
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload " + world.getName());
	            
			new BukkitRunnable(){
				public void run() {
					WorldCreator cw = new WorldCreator(world.getName());
					deleteFolder(worldFolder);
					new BukkitRunnable() {
						@Override
						public void run() {
							copyWorldFolder(srcWorldFolder, worldFolder);
	            				
							//cw.seed(seed.longValue());
							cw.environment(World.Environment.NORMAL);
							cw.generateStructures(false);
							cw.generator(generator);
							cw.type(WorldType.FLAT);
	    	                    
							World w = cw.createWorld();
							w.setAutoSave(false);
							w.setGameRuleValue("doMobSpawning", "false");
				            w.setGameRuleValue("doDaylightCycle", "false");
				            w.setGameRuleValue("mobGriefing", "false");
							w.setThundering(false);
							w.setWeatherDuration(0);
							w.setTime(6000L);
	    	    				
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv load " + world.getName());
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvm set monsters false " + world.getName());
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvm set animals false " + world.getName());
							Bukkit.getServer().getWorlds().add(w);
	    	                    
							for (Entity ent : w.getEntities()) {
								if (!(ent instanceof Player)) {
									ent.remove();
								}
							}
						}
					}.runTaskLater(SWA.get(), 10L);
				}
			}.runTaskLater(SWA.get(), 5L);    
		}
	} 
    /*
     * 
     */
    
    public void deleteWorld(World world) {
    	File worldFolder = new File(world.getName());
    	Bukkit.getServer().unloadWorld(world, false);
    	Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv unload " + world.getName());
        deleteFolder(worldFolder);
    }
    
    private static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                } else {
                    file.delete();
                } 
            }
        } 
        folder.delete();
    }
    
    public void copyWorldFolder(File from, File to) {
        try {
        	
            ArrayList<String> ignore = new ArrayList<String>();
            
            ignore.add("session.dat");
            ignore.add("session.lock");
            ignore.add("WorldSettings.yml");
            
            if (!ignore.contains(from.getName())) {
            	
                if (from.isDirectory()) {
                	
                    if (!to.exists()) {
                        to.mkdirs();
                    }
                    
                    String[] files = from.list();
                    int j = files.length;
                    
                    for (int i = 0; i < j; i++) {
                        String file = files[i];
                        File srcFile = new File(from, file);
                        File destFile = new File(to, file);
                        copyWorldFolder(srcFile, destFile);
                    }
                    
                } else {
                	
                    Files.copy((File)from, (File)to);
                }
                
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getChunkGeneratorAsName(World world) {
        String generator = null;
        Plugin[] arrayOfPlugin = Bukkit.getPluginManager().getPlugins();
        int j = arrayOfPlugin.length;
        int i = 0;
        while (i < j) {
            Plugin plugin = arrayOfPlugin[i];
            WorldCreator wc = new WorldCreator("ThisMapWillNeverBeCreated");
            wc.generator(plugin.getName());
            if (wc.generator() != null && world.getGenerator() != null && wc.generator().getClass().getName().equals(world.getGenerator().getClass().getName())) {
                generator = plugin.getName();
            }
            ++i;
        }
        return generator;
    }
	
}