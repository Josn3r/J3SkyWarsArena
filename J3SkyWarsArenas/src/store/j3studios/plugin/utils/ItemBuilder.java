package store.j3studios.plugin.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
	
    public static ItemStack crearCabeza(String owner, String name, String ... loreOptions) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        ArrayList<String> color = new ArrayList<>();
        for (String lore : loreOptions) {
            color.add(Tools.get().Text(lore));
            meta.setLore(color);
        }
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
    
    public static ItemStack crearCabeza(String owner, int amount, String name, String ... loreOptions) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short)3);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        ArrayList<String> color = new ArrayList<>();
        for (String lore : loreOptions) {
            color.add(Tools.get().Text(lore));
            meta.setLore(color);
        }
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
	
    @SuppressWarnings("deprecation")
	public static ItemStack crearItem(int id, int data, int amount) {
    	ItemStack item = new ItemStack(id, amount, (short)data);
        return item;
    }
    
    @SuppressWarnings("deprecation")
	public static ItemStack crearItem(int id, int data, int amount, String name, String ... loreOptions) {
        ItemStack item = new ItemStack(id, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Tools.get().Text(name));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);        
        ArrayList<String> color = new ArrayList<>();
        for (String lore : loreOptions) {
            color.add(Tools.get().Text(lore));
            meta.setLore(color);
        }
        item.setItemMeta(meta);
        return item;
    }
    
    @SuppressWarnings("deprecation")
	public static ItemStack crearItem(int id, int data, int amount, String name, List<String> lore) {
        ItemStack item = new ItemStack(id, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Tools.get().Text(name));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);        
        ArrayList<String> color = new ArrayList<>();
        for (String str : lore) {
        	color.add(Tools.get().Text(str));
        	meta.setLore(color);
        }                  
        item.setItemMeta(meta);
        return item;
    }

}

