package store.j3studios.plugin.commands;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import store.j3studios.plugin.SWA;
import store.j3studios.plugin.arena.ArenaManager;
import store.j3studios.plugin.utils.Cuboid;
import store.j3studios.plugin.utils.ItemBuilder;
import store.j3studios.plugin.utils.Tools;

public class ArenaCreatorCMD implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			SWA.debug("Solo puede ejecutar comando siendo jugador.");
			return true;
		}
		
		Player p = (Player)sender;
		
		if (cmd.getName().equalsIgnoreCase("j3skywars")) {

			if (!p.hasPermission("j3sw.admin")) {
				return true;
			}
			
			if(args.length == 0){
				return true;
			}
			
			World world = p.getWorld();
			ArenaManager am = new ArenaManager();
			
			File file = new File("plugins/Josn3rSkyWars/arenas/"+world.getName()+".yml");
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
						
			/**
			 * CREATE KIT
			 */
			if (args[0].equalsIgnoreCase("kit")) { 
				if (args.length == 1) {
					p.sendMessage(Tools.get().Text("&fColocar los items en tu inventario tal cual"));
					p.sendMessage(Tools.get().Text("&fsería el kit que estás creando, luego usa;"));
				    p.sendMessage(Tools.get().Text("&b/jsw kit create <kitNode>"));
				    p.sendMessage(Tools.get().Text("&eCon eso iniciarás la creación del kit."));
				    return true;
				}
				
				if (args[1].equalsIgnoreCase("editor")) {
					//new KitListEditor(p).open(p);
					return true;
				}
				
				if (args[1].equalsIgnoreCase("create")) {
					if (args.length == 2) {
						p.sendMessage(Tools.get().Text("&fColocar los items en tu inventario tal cual"));
						p.sendMessage(Tools.get().Text("&fsería el kit que estás creando, luego usa;"));
					    p.sendMessage(Tools.get().Text("&b/jsw kit create <kitNode>"));
					    p.sendMessage(Tools.get().Text("&eCon eso iniciarás la creación del kit."));
					    return true;
					}
					/*
					String kitNode = args[2];
					if (Skywars.kits.isSet("kits." + kitNode)) {
						p.sendMessage(Tools.get().Text(Skywars.PREFIX + "&cYa existe un kit con ese nombre."));
						return true;
					}
					
					String KIT_NODE = "kits." + kitNode;		
					String KIT_NAME = KIT_NODE + ".name";
					String KIT_SLOT = KIT_NODE + ".slot";
					String KIT_ICON = KIT_NODE + ".icon";
					String KIT_PERMISSION = KIT_NODE + ".permission";
					String KIT_RANK = KIT_NODE + ".rank";
					String KIT_PRICE = KIT_NODE + ".price";
					String KIT_ARMOR = KIT_NODE + ".items.armor";
					String KIT_CONTENT = KIT_NODE + ".items.contents";
					
					Skywars.kits.set(KIT_NAME, "Unknown");
					Skywars.kits.set(KIT_SLOT, 0);
					Skywars.kits.set(KIT_ICON, "STONE");
					Skywars.kits.set(KIT_PERMISSION, null);
					Skywars.kits.set(KIT_RANK, null);
					Skywars.kits.set(KIT_PRICE, -1.0);
					
					ArrayList<ItemStack> armor = new ArrayList<ItemStack>();
					ArrayList<ItemStack> inventory = new ArrayList<ItemStack>();
					for (ItemStack item : p.getInventory().getArmorContents()) {
						armor.add(item);
					}
					for (int i = 0; i<=35; ++i) {
						inventory.add(p.getInventory().getItem(i));
					}					
					String armadura = Convertor.itemToBase64(armor.toArray(new ItemStack[armor.size()]));
		    		String inventario = Convertor.itemToBase64(inventory.toArray(new ItemStack[inventory.size()]));    				
		    		Skywars.kits.set(KIT_ARMOR, armadura);
		    		Skywars.kits.set(KIT_CONTENT, inventario);
					new KitCreator(p, kitNode, "Unknown", 0, Material.STONE, null, null, -1.0).open(p);
					*/
				}
				return true;
			}
			
			/**
			 * CREATE COMMAND
			 */
			if (args[0].equalsIgnoreCase("create")) {  
				
				if(am.exist(world.getName())){
					p.sendMessage(Tools.get().Text("&bYa existe una arena con el nombre de &f" + world.getName()));
					return true;
				}
				
				//Arena a = new Arena (world.getName(), world.getName(), -1, -1);
			    //ArenaManager.save(config, file);		
			    //a.load();
			    
			    p.getInventory().clear();
			    p.getInventory().setArmorContents(null);
			    			    
			    p.getInventory().setItem(0, ItemBuilder.crearItem(138, 0, 64, "&bPlayer Spawn"));
			    
			    p.sendMessage(Tools.get().Text("&bSe ha creado la arena &f" + world.getName() + " &bcorrectamente..."));
			    p.sendMessage(Tools.get().Text(" "));
			    p.sendMessage(Tools.get().Text("&fEmpecemos la configuración:"));
			    p.sendMessage(Tools.get().Text("&71.- &b/jsw name <nombre> &7- Coloca un nombre al mapa!"));
							    
			} else if(args[0].equalsIgnoreCase("name")) {
				if(args.length  < 2) {
					p.sendMessage(Tools.get().Text("&71.- &b/jsw name <nombre> &7- Coloca un nombre al mapa!"));
					return true;
				}
				
				if(!am.exist(world.getName())) {
					p.sendMessage(Tools.get().Text("&c&lERROR:"));
					p.sendMessage(Tools.get().Text("&bTe encuentras en un mundo no registrado en las arenas..."));
					return true;
				}
				
				if (!config.getBoolean("config.editMode")) {
					p.sendMessage(Tools.get().Text("&c&lERROR: &7El modo editor del mapa está desactivado."));
					return true;
				}
				
				config.set("config.displayName", args[1]);
				Tools.get().save(config, file);
				p.sendMessage(Tools.get().Text("&bSe ha modificado el nombre del mapa a &f" + args[1]));
				p.sendMessage(Tools.get().Text("&b "));
				p.sendMessage(Tools.get().Text("&b&fConfiguración del mapa:"));
				p.sendMessage(Tools.get().Text("&71,25.- &b/jsw setMode <Normal/Insane> &7- Coloca el modo del mapa default!"));
				
			} else if(args[0].equalsIgnoreCase("setChest")) {				
				if(args.length  < 3) {
					p.sendMessage(Tools.get().Text("&71.- &b/jsw setChest <cofreType> <radio> &7- Coloca el tipo de cofres en un radio"));
					return true;
				}
				
				if(!am.exist(world.getName())) {
					p.sendMessage(Tools.get().Text("&c&lERROR:"));
					p.sendMessage(Tools.get().Text("&bTe encuentras en un mundo no registrado en las arenas..."));
					return true;
				}
				
				if (!config.getBoolean("config.editMode")) {
					p.sendMessage(Tools.get().Text("&c&lERROR: &7El modo editor del mapa está desactivado."));
					return true;
				}
				
				if (!args[1].equalsIgnoreCase("Normal") && !args[1].equalsIgnoreCase("Centro")) {
					p.sendMessage(Tools.get().Text("&c&lERROR: &7Solo existen dos tipos de cofres: &aNormal &7y &cCentro"));
					return true;
				}
				
				if (!Tools.get().isInt(args[2])) {
					p.sendMessage(Tools.get().Text("&c&lERROR: &7Especifica el radio para setear cofres."));
					return true;
				}
				
				int X = p.getLocation().getBlockX();
		    	int Y = p.getLocation().getBlockY();
		    	int Z = p.getLocation().getBlockZ();
		    	Integer ratio = Integer.valueOf(args[2]);
				
				Location max = new Location(p.getWorld(), X + ratio, Y + ratio, Z + ratio);
		    	Location min = new Location(p.getWorld(), X - ratio, Y - ratio, Z - ratio);
		    	Cuboid cuboid = new Cuboid(max, min);
		    	for (Block block : cuboid) {
		    		if (block.getType().equals(Material.CHEST)) {
			            Location loc = block.getLocation();			            
						if (args[1].equalsIgnoreCase("Normal")) {
							am.addChest(p, loc.getWorld(), loc, "Island");
						} else {
							am.addChest(p, loc.getWorld(), loc, "Center");
						}
		    		}
		    	}
				
			} else if(args[0].equalsIgnoreCase("setMin")) {				
				if (args.length < 2) {
					p.sendMessage(Tools.get().Text("&b/jsw setMin <MIN> &7- Coloca el minimo de jugadores!"));
					return true;
				}
				
				if(!am.exist(world.getName())){
					p.sendMessage(Tools.get().Text("&c&lERROR:"));
					p.sendMessage(Tools.get().Text("&bTe encuentras en un mundo no registrado en las arenas..."));
					return true;
				}
				
				if (!config.getBoolean("config.editMode")) {
					p.sendMessage(Tools.get().Text("&c&lERROR: &7El modo editor del mapa está desactivado."));
					return true;
				}
				
				try{
					int i = Integer.parseInt(args[1]);
					config.set("config.players.min", i);
					Tools.get().save(config, file);
				    
				    p.sendMessage(Tools.get().Text("&bSe ha colocado el mínimo de jugadores en: &f"+i));
				}catch(NumberFormatException e){
					p.sendMessage(Tools.get().Text("&c&lERROR: &7El numero es inválido."));
				}
				
			} else if(args[0].equalsIgnoreCase("setMax")) {
				
				if(args.length < 2){
					p.sendMessage(Tools.get().Text("&b/jsw setMax <MAX> &7- Coloca el máximo de jugadores!"));
					return true;
				}
				
				if(!am.exist(world.getName())){
					p.sendMessage(Tools.get().Text("&c&lERROR:"));
					p.sendMessage(Tools.get().Text("&bTe encuentras en un mundo no registrado en las arenas..."));
					return true;
				}
				
				if (!config.getBoolean("config.editMode")) {
					p.sendMessage(Tools.get().Text("&c&lERROR: &7El modo editor del mapa está desactivado."));
					return true;
				}
				
				try {
					int i = Integer.parseInt(args[1]);
					config.set("config.players.max", i);
					Tools.get().save(config, file);
				    
				    p.sendMessage(Tools.get().Text("&bSe ha colocado el máximo de jugadores en: &f" + i));
				} catch(NumberFormatException e) {
					p.sendMessage(Tools.get().Text("&c&lERROR: &7El numero es inválido."));
				}
			
			} else if (args[0].equalsIgnoreCase("save")) {				
				if (!am.exist(world.getName())) {
					p.sendMessage(Tools.get().Text("&c&lERROR:"));
					p.sendMessage(Tools.get().Text("&bTe encuentras en un mundo no registrado en las arenas..."));
					return true;
				}
				
				if (!config.getBoolean("config.editMode")) {
					p.sendMessage(Tools.get().Text("&c&lERROR: &7El modo editor del mapa está desactivado."));
					return true;
				}
				
				World wSaved = p.getWorld();				
				
				SWA.get().getAM().replaceBeaconsForAir(wSaved);
				SWA.get().getAM().saveWorld(wSaved);				
				config.set("config.editMode", false);
				
				Tools.get().save(config, file);				
				p.sendMessage(Tools.get().Text("&bSe ha guardado correctamente el mapa &f" + world.getName()));			    
			} else if (args[0].equalsIgnoreCase("editMode")) {
				if (!am.exist(world.getName())) {
					p.sendMessage(Tools.get().Text("&c&lERROR:"));
					p.sendMessage(Tools.get().Text("&bTe encuentras en un mundo no registrado en las arenas..."));
					return true;
				}
				
				if (config.getBoolean("config.editMode")) {
					p.sendMessage(Tools.get().Text("&c&lERROR: &7El modo editor del mapa ya está activado."));
					return true;
				}
				
				//am.getArena(p.getWorld().getName()).setArenaState(ArenaState.EDITMODE);
				config.set("config.editMode", true);
				Tools.get().save(config, file);
				p.sendMessage(Tools.get().Text("&bSe ha activado el modo editor en el mapa &f" + world.getName()));
				
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				
				p.getInventory().setItem(0, ItemBuilder.crearItem(138, 0, 64, "&bPlayer Spawn"));
			}
		}
		
		
		return true;
	}

}
