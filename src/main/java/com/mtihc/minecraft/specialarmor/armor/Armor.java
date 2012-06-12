package com.mtihc.minecraft.specialarmor.armor;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;

public class Armor implements ConfigurationSerializable {

	private static DefaultSlotState[] armorDefaultStates = new DefaultSlotState[4];
	
	public static DefaultSlotState getDefaultSlotStates(ArmorSlot slot) {
		return armorDefaultStates[slot.ordinal()];
	}
	
	public static void setDefaultSlotStates(
			DefaultSlotState hat, 
			DefaultSlotState shirt, 
			DefaultSlotState pants, 
			DefaultSlotState shoes) 
	{
		armorDefaultStates[3] = hat;
		armorDefaultStates[2] = shirt;
		armorDefaultStates[1] = pants;
		armorDefaultStates[0] = shoes;
	}
	
	
	
	
	
	
	
	
	
	
	

	private OfflinePlayer player;
	private ItemStack[] armor;
	
	
	
	
	
	public Armor(Player player) {
		this.player = player;
		
		this.armor = new ItemStack[4];
		this.armor[3] = new ItemStack(0);
		this.armor[2] = new ItemStack(0);
		this.armor[1] = new ItemStack(0);
		this.armor[0] = new ItemStack(0);
		
	}

	public Armor(Player player, ItemStack[] armor) {
		this.player = player;
		this.armor = armor;
	}
	
	
	
	
	
	
	
	
	
	public String getName() {
		return player.getName();
	}
	
	public OfflinePlayer getOfflinePlayer() {
		return player;
	}
	
	public Player getPlayer() {
		OfflinePlayer p = Bukkit.getOfflinePlayer(player.getName());
		if(p.isOnline()) {
			player = p.getPlayer();
		}
		return player.getPlayer();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public ItemStack getArmor(ArmorSlot slot) {
		return getPlayer().getInventory().getArmorContents()[slot.ordinal()];
	}
	
	public int getArmorHealthMax(ArmorSlot slot) {
		ItemStack a = getArmor(slot);
		return a.getType().getMaxDurability();
	}
	
	public int getArmorHealth(ArmorSlot slot) {
		ItemStack a = getArmor(slot);
		return a.getType().getMaxDurability() - a.getDurability();
	}
	
	public void setArmorHealth(ArmorSlot slot, int health) {
		ItemStack a = getArmor(slot);
		if(health < 0) {
			health = 0;
		}
		else if(health > a.getType().getMaxDurability()) {
			health = a.getType().getMaxDurability();
		}
		
		a.setDurability((short) (a.getType().getMaxDurability() - health));
		
		if(a.getDurability() >= a.getType().getMaxDurability()) {
			Player p = getPlayer();
			p.getInventory().setItem(slot.getSlot(), new ItemStack(0));
		}
	}
	
	public void damageArmor(ArmorSlot slot, int damage) {
		ItemStack a = getArmor(slot);
		if(damage < 0) {
			damage = 0;
		}
		else if(damage > a.getType().getMaxDurability()) {
			damage = a.getType().getMaxDurability();
		}
		
		a.setDurability((short) (a.getDurability() + damage));
		
		if(a.getDurability() >= a.getType().getMaxDurability()) {
			Player p = getPlayer();
			dropItemStack(slot, getPlayer().getLocation());
			p.getInventory().setItem(slot.getSlot(), new ItemStack(0));
		}
	}
	
	public void healArmor(ArmorSlot slot, int health) {
		ItemStack a = getArmor(slot);
		if(health < 0) {
			health = 0;
		}
		else if(health > a.getType().getMaxDurability()) {
			health = a.getType().getMaxDurability();
		}
		a.setDurability((short) (a.getDurability() - health));
	}
	
	
	
	
	
	public ItemStack getItemStack(ArmorSlot slot) {
		return armor[slot.ordinal()];
	}
	
	public void setItemStack(ArmorSlot slot, ItemStack itemStack) {
		if(itemStack == null) {
			itemStack = new ItemStack(0);
		}
		armor[slot.ordinal()] = itemStack;
	}
	
	public void dropItemStack(ArmorSlot slot, Location location) {
		ArmorSlotState state = getSlotState(slot);
		if(state == null) {
			return;
		}
		ItemStack[] drops = state.getDrops();
		if(drops == null) {
			dropItemStack(getItemStack(slot), location);
		}
		else {
			for (ItemStack drop : drops) {
				dropItemStack(drop, location);
			}
		}
		
	}
	
	public static void dropItemStack(ItemStack itemStack, Location location) {
		location.getWorld().dropItem(location, itemStack).setVelocity(location.getDirection().multiply(0.25));
	}
	
	
	public ArmorSlotState getSlotState(ArmorSlot slot) {
		Material type = armor[slot.ordinal()].getType();
		return getDefaultSlotStates(slot).fromType(type);
	}
	
	
	
	
	
	public int getMaxItemStackSize(ArmorSlot slot) {
		return getSlotState(slot).getMaxItemStackSize();
	}
	
	
	
	
	public void onMove(PlayerMoveEvent event) {
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			ArmorSlotState state = getSlotState(slot);
			if(state == null) {
				continue;
			}
			if(event.getPlayer().getInventory().getArmorContents()[slot.ordinal()].getTypeId() == 0) {
				continue;
			}
			if(armor[slot.ordinal()].getTypeId() == 0) {
				return;
			}
			state.onMove(event, this);
		}
	}
	
	public void onInteract(PlayerInteractEvent event) {
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			ArmorSlotState state = getSlotState(slot);
			if(state == null) {
				continue;
			}
			if(event.getPlayer().getInventory().getArmorContents()[slot.ordinal()].getTypeId() == 0) {
				continue;
			}
			if(armor[slot.ordinal()].getTypeId() == 0) {
				return;
			}
			state.onInteract(event, this);
		}
	}
	
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			ArmorSlotState state = getSlotState(slot);
			if(state == null) {
				continue;
			}
			if(event.getPlayer().getInventory().getArmorContents()[slot.ordinal()].getTypeId() == 0) {
				continue;
			}
			if(armor[slot.ordinal()].getTypeId() == 0) {
				return;
			}
			state.onInteractEntity(event, this);
		}
	}


	public void onToggleSneak(PlayerToggleSneakEvent event) {
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			ArmorSlotState state = getSlotState(slot);
			if(state == null) {
				continue;
			}
			if(event.getPlayer().getInventory().getArmorContents()[slot.ordinal()].getTypeId() == 0) {
				continue;
			}
			if(armor[slot.ordinal()].getTypeId() == 0) {
				return;
			}
			state.onToggleSneak(event, this);
		}
	}

	public void onToggleSprint(PlayerToggleSprintEvent event) {
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			ArmorSlotState state = getSlotState(slot);
			if(state == null) {
				continue;
			}
			if(event.getPlayer().getInventory().getArmorContents()[slot.ordinal()].getTypeId() == 0) {
				continue;
			}
			if(armor[slot.ordinal()].getTypeId() == 0) {
				return;
			}
			state.onToggleSprint(event, this);
		}
	}

	public void onVelocity(PlayerVelocityEvent event) {
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			ArmorSlotState state = getSlotState(slot);
			if(state == null) {
				continue;
			}
			if(event.getPlayer().getInventory().getArmorContents()[slot.ordinal()].getTypeId() == 0) {
				continue;
			}
			if(armor[slot.ordinal()].getTypeId() == 0) {
				return;
			}
			state.onVelocity(event, this);
		}
	}

	public void onDamageByEntity(EntityDamageByEntityEvent event) {
		Player player = (Player) event.getEntity();
		
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			ArmorSlotState state = getSlotState(slot);
			if(state == null) {
				continue;
			}
			if(player.getInventory().getArmorContents()[slot.ordinal()].getTypeId() == 0) {
				continue;
			}
			if(armor[slot.ordinal()].getTypeId() == 0) {
				return;
			}
			state.onDamageByEntity(event, this);
		}
	}
	
	public void onDamageEntity(EntityDamageByEntityEvent event) {
		Player player = (Player) event.getDamager();
		
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			ArmorSlotState state = getSlotState(slot);
			if(state == null) {
				continue;
			}
			if(player.getInventory().getArmorContents()[slot.ordinal()].getTypeId() == 0) {
				continue;
			}
			if(armor[slot.ordinal()].getTypeId() == 0) {
				return;
			}
			state.onDamageEntity(event, this);
		}
	}
	
	public void onKillEntity(EntityDeathEvent event) {
		Player player = event.getEntity().getKiller();
		
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			ArmorSlotState state = getSlotState(slot);
			if(state == null) {
				continue;
			}
			if(player.getInventory().getArmorContents()[slot.ordinal()].getTypeId() == 0) {
				continue;
			}
			if(armor[slot.ordinal()].getTypeId() == 0) {
				return;
			}
			state.onKillEntity(event, this);
		}
	}
	
	
	
	
	
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> values = new LinkedHashMap<String, Object>();
		
		values.put("name", getName());
		
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			values.put(slot.name(), getItemStack(slot));
		}
		
		return values;
	}
	
	public static Armor deserialize(Map<String, Object> values) {
		Player p = Bukkit.getPlayerExact(values.get("name").toString());
		if(p == null) {
			return null;
		}
		
		ItemStack[] a = new ItemStack[4];
		ArmorSlot[] slots = ArmorSlot.values();
		for (ArmorSlot slot : slots) {
			a[slot.ordinal()] = (ItemStack) values.get(slot.name());
		}
		
		return new Armor(p, a);
 	}

	
	
}
