package store.j3studios.plugin.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.EditSessionBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;

public class WEdit {
        
    public static void pasteSchematicFAWE (String schematic, Location loc, Boolean ignoreAirBlocks) throws IOException {
    	File file = new File("plugins/J3SkyWarsArena/schematics/" + schematic + "");
    	Vector to = new Vector(loc.getX(), loc.getY(), loc.getZ());
    	FaweAPI.load(file).paste(new BukkitWorld(loc.getWorld()), to, ignoreAirBlocks);
    }
    
    @SuppressWarnings("deprecation")
	public static void clearRegion(World world, Location min, Location max) {    	
    	CuboidRegion region = new CuboidRegion(world, new Vector(min.getX(), min.getY(), min.getZ()), new Vector(max.getX(), max.getY(), max.getZ()));
    	EditSessionBuilder esBuilder = FaweAPI.getEditSessionBuilder(world);
    	esBuilder.fastmode(true);
    	EditSession es = esBuilder.build();
    	BaseBlock block = new BaseBlock(0);
    	es.setBlocks((Region) region, block);
    	es.flushQueue();
    }
    
}
