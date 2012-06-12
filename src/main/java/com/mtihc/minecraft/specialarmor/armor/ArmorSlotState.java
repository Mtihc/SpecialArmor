package com.mtihc.minecraft.specialarmor.armor;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class ArmorSlotState {

	private Material type;
	private int maxStackSize;
	
	public ArmorSlotState(Material type, int maxStackSize) {
		this.type = type;
		this.maxStackSize = maxStackSize;
	}
	
	public abstract String[] getDescription();
	
	public Material getType() {
		return type;
	}
	
	public String getUserfriendlyTypeName() {
		return type.name().toLowerCase();
	}
	
	public int getMaxItemStackSize() {
		return maxStackSize;
	}
	
	protected ItemStack[] getDrops() {
		return null;
	}
	
	protected void onMove(PlayerMoveEvent event, Armor armor) {
		
	}
	
	protected void onInteract(PlayerInteractEvent event, Armor armor) {
		
	}
	
	protected void onInteractEntity(PlayerInteractEntityEvent event, Armor armor) {
		
	}

	protected void onToggleSneak(PlayerToggleSneakEvent event, Armor armor) {
		
	}

	protected void onToggleSprint(PlayerToggleSprintEvent event, Armor armor) {
		
	}

	protected void onVelocity(PlayerVelocityEvent event, Armor armor) {
		
	}

	protected void onDamageByEntity(EntityDamageByEntityEvent event, Armor armor) {
		
	}

	protected void onDamageEntity(EntityDamageByEntityEvent event, Armor armor) {
		
	}

	protected void onKillEntity(EntityDeathEvent event, Armor armor) {
		
	}


}
