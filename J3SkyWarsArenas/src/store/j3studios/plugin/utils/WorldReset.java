package store.j3studios.plugin.utils;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldReset {
    
    private static final String PATH_SAVE = "plugins/J3SkyWars/WorldSaves/";
    
    public static void kickPlayers(World world, String kickMessage) {
        if (Bukkit.getOnlinePlayers() != null && !Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.kickPlayer(kickMessage);                
            }
        }
    }
  
    public static void kickPlayers(String worldName, String kickMessage) {
        World world = Bukkit.getWorld(worldName);
        kickPlayers(world, kickMessage);
    }
  
    public static Boolean worldSaved(String worldName) {
        File worldFolder = new File(PATH_SAVE + worldName);
        return worldFolder.exists();
    }
  
    public static void deleteWorldSave(String worldName) {
        File worldFolder = new File(PATH_SAVE + worldName);
        if (worldFolder.exists())
            deleteFolder(worldFolder); 
    }
  
    public static void saveWorld(World world) {
        if (world != null) {
            world.save();
            File worldFolder = new File(PATH_SAVE + world.getName());
            File srcWorldFolder = new File(world.getName());
            if (worldFolder.exists())
                deleteFolder(worldFolder); 
            copyWorldFolder(srcWorldFolder, worldFolder);
            
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(new File(worldFolder, "WorldSettings.yml"));
            yamlConfiguration.set("World.Seed", world.getSeed());
            yamlConfiguration.set("World.Environment", world.getEnvironment().toString());
            yamlConfiguration.set("World.Structures", world.canGenerateStructures());
            yamlConfiguration.set("World.Generator", getChunkGeneratorAsName(world));
            yamlConfiguration.set("World.Type", world.getWorldType().toString());
            try {
                yamlConfiguration.save(new File(worldFolder, "WorldSettings.yml"));
            } catch (IOException e) {
                System.out.println("[WorldReset] Couldn't create a WorldSettings file!");
            } 
        } 
    }
  
    public static void saveWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        saveWorld(world);
    }
  
    public static void resetWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        resetWorld(world);
    }
  
    public static void resetWorld(World world) {
        File srcWorldFolder = new File(PATH_SAVE + world.getName());
        File worldFolder = new File(world.getName());
        if (srcWorldFolder.exists() && 
        worldFolder.exists())
        if (world.getName().equals("world")) {
            System.out.println("[WorldReset] The world 'world' is the main world and can't be resetted!");
        } else {
            Boolean saveSett = false;
            kickPlayers(world, "Reseting world...");
            Long seed = null;
            World.Environment environment = null;
            Boolean structures = null;
            String generator = null;
            WorldType worldType = null;
            File settingsFile = new File(srcWorldFolder, "WorldSettings.yml");
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(settingsFile);
            if (settingsFile.exists() && yamlConfiguration.get("World.Seed") != null) {
                seed = yamlConfiguration.getLong("World.Seed");
                environment = World.Environment.valueOf(yamlConfiguration.getString("World.Environment"));
                structures = yamlConfiguration.getBoolean("World.Structures");
                generator = yamlConfiguration.getString("World.Generator");
                worldType = WorldType.valueOf(yamlConfiguration.getString("World.Type"));
            } else {
                seed = world.getSeed();
                environment = world.getEnvironment();
                structures = world.canGenerateStructures();
                generator = getChunkGeneratorAsName(world);
                worldType = world.getWorldType();
                yamlConfiguration.set("World.Seed", world.getSeed());
                yamlConfiguration.set("World.Environment", world.getEnvironment().toString());
                yamlConfiguration.set("World.Structures", world.canGenerateStructures());
                yamlConfiguration.set("World.Generator", getChunkGeneratorAsName(world));
                yamlConfiguration.set("World.Type", world.getWorldType().toString());
                saveSett = true;
            } 
            Bukkit.getServer().unloadWorld(world, true);
            WorldCreator w = new WorldCreator(world.getName());
            deleteFolder(worldFolder);
            copyWorldFolder(srcWorldFolder, worldFolder);
            if (saveSett)
                try {
                    yamlConfiguration.save(settingsFile);
                } catch (IOException e) {
                    System.out.println("[WorldReset] Couldn't save the WorldSettings file!");
                }  
            w.seed(seed);
            w.environment(environment);
            w.generateStructures(structures);
            w.generator(generator);
            w.type(worldType);
            /*if (main.useMultiverse().booleanValue()) {
                ((MultiverseCore)Bukkit.getPluginManager().getPlugin("Multiverse-Core")).getMVWorldManager().addWorld(worldName, environment, seed.toString(), worldType, structures, generator);
            } else {
                Bukkit.getServer().createWorld(w);
            }*/ 
            Bukkit.getServer().createWorld(w);
        }  
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
  
    private static String getChunkGeneratorAsName(World world) {
        String generator = null;
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            WorldCreator wc = new WorldCreator("ThisMapWillNeverBeCreated");
            wc.generator(plugin.getName());
            if (wc.generator() != null && world.getGenerator() != null && wc.generator().getClass().getName().equals(world.getGenerator().getClass().getName()))
                generator = plugin.getName(); 
        }
        return generator;
    }
  
    private static void copyWorldFolder(File from, File to) {
        try {
            ArrayList<String> ignore = new ArrayList<>();
            ignore.add("session.dat");
            ignore.add("session.lock");
            ignore.add("WorldSettings.yml");
            if (!ignore.contains(from.getName()))
                if (from.isDirectory()) {
                    if (!to.exists())
                        to.mkdirs(); 
                    
                    for (String file : from.list()) {
                        File srcFile = new File(from, file);
                        File destFile = new File(to, file);
                        copyWorldFolder(srcFile, destFile);
                    }
                } else {
                    Files.copy(from, to);
                }  
        } catch (IOException e) {
        } 
    }
    
}
