package com.mtihc.minecraft.specialarmor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.minecraft.specialarmor.armor.Armor;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlot;

public class ArmorClickListener implements Listener {

	private JavaPlugin plugin;
	private PlayerRepository repo;
	private Map<String, Armor> players;

	public ArmorClickListener(JavaPlugin plugin) {
		this.plugin = plugin;
		this.repo = new PlayerRepository(plugin.getDataFolder() + "/players");
		this.players = new HashMap<String, Armor>();
		
		// load online players' files
		Player[] online = plugin.getServer().getOnlinePlayers();
		for (Player player : online) {
			Armor p = getPlayer(player.getName());
			if(p != null) {
				players.put(player.getName(), p);
			}
		}
	}
	
	public Armor getPlayer(String name) {
		Armor p = players.get(name);
		if(p == null) {
			try {
				p = repo.loadPlayerExact(name);
			} catch (Exception e) {
				plugin.getLogger().log(Level.SEVERE, "Failed to load player " + name + "'s data.", e);
				return null;
			}
			if(p == null) {
				Player player = Bukkit.getPlayerExact(name);
				if(player != null) {
					p = new Armor(player);
				}
				else {
					return null;
				}
			}
		}
		return p;
	}
	
	public void savePlayer(Armor p) {
		try {
			repo.savePlayer(p);
		} catch (IOException e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to save player " + p.getName() + "'s data.", e);
			return;
		}
	}
	
	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if(event.getPlugin() == plugin) {
			// save players' files
			Iterator<Armor> values = players.values().iterator();
			while(values.hasNext()) {
				Armor p = values.next();
				savePlayer(p);
				values.remove();
			}
			
		}
	}
	
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		onPlayerLeave(event);
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		onPlayerLeave(event);
	}
	
	protected void onPlayerLeave(PlayerEvent event) {
		String name = event.getPlayer().getName();
		Armor p = players.remove(name);
		if(p != null) {
			savePlayer(p);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		String name = event.getPlayer().getName();
		Armor p = getPlayer(name);
		players.put(name, p);
		savePlayer(p);
		
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.isCancelled() || event.getResult().equals(Result.DENY)) {
			// event cancelled
			return;
		}
		
		InventoryHolder holder = event.getInventory().getHolder();
		if(!(holder instanceof Player)) {
			// not a player's inventory
			return;
		}
		
		if(!event.getSlotType().equals(SlotType.ARMOR)) {
			// not clicking armor slot
			return;
		}
		
		// get/load player
		Player player = (Player) holder;
		Armor p = getPlayer(player.getName());
		if(p == null) {
			return;
		}
		
		// the slot (hat, shirt, pants, shoes)
		int slot = event.getSlot();
		ArmorSlot s = ArmorSlot.fromSlot(slot);
		
		// the extra items inside the armor
		ItemStack item = p.getItemStack(s);
		
		if(event.getCurrentItem().getTypeId() == 0) {
			// no armor in armor slot
			
			// take this chance to update the player data just in case
			if(!item.getType().equals(Material.AIR)) {
				p.setItemStack(s, new ItemStack(0));
			}
			
			return;
		}
		
		ItemStack cursor = event.getCursor();
		
		// there is an item in the slot,
		// check item on cursor
		if(cursor.getTypeId() == 0) {
			// no item on cursor...
			// so, taking off armor
			
			if(item.getTypeId() != 0) {
				p.dropItemStack(s, player.getEyeLocation());
			}
			return;
		}
		else if(Armor.getDefaultSlotStates(s).fromType(cursor.getType()) == null) {
			return;
		}
		
		//
		// item on cursor, and
		// item on armor slot
		//
		// clicking armor with an item
		//
		
		
		// with item on cursor...
		// adding something to armor
		
		event.setCancelled(true);
		event.setResult(Result.DENY);
		
		// put on the special armor, 
		// or add item to existing special amor
		
		ItemStack newItem;
		
		if(item.getTypeId() == 0) {
			newItem = cursor.clone();
			newItem.setAmount(1);
		}
		else {
			
			// check if correct type
			if(!item.getType().equals(cursor.getType())) {
				player.sendMessage(ChatColor.RED + "You still have " + item.getAmount() + " " + item.getType().name().toLowerCase().replace("_", " ") + " in your " + s.name().toLowerCase().replace("_", " "));
				return;
			}
			else {
				if(!item.getEnchantments().equals(cursor.getEnchantments())) {
					player.sendMessage(ChatColor.RED + "There items' enchantments don't match.");
					return;
				}
			}
			
			// check max items for this type and slot
			if(item.getAmount() + 1 > p.getMaxItemStackSize(s)) {
				player.sendMessage(ChatColor.RED + "You already have " + item.getAmount() + " " + item.getType().name().toLowerCase().replace("_", " ") + " in your " + s.name().toLowerCase().replace("_", " "));
				return;
			}
			
			newItem = item;
			newItem.setAmount(newItem.getAmount() + 1);
			
		}
		
		p.setItemStack(s, newItem);
		player.sendMessage(ChatColor.GREEN + "You now have " + newItem.getAmount() + " " + newItem.getType().name().toLowerCase().replace("_", " ") + " in your " + s.name().toLowerCase().replace("_", " "));
		

		
		// take 1 from cursor stack
		int amount = cursor.getAmount();
		if(amount == 1) {
			// stack is empty
			event.setCursor(new ItemStack(0));
		}
		else {
			// stack is 1 smaller
			cursor.setAmount(amount - 1);
			event.setCursor(cursor);
		}
	}
}
