package com.mtihc.minecraft.specialarmor;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import com.mtihc.minecraft.specialarmor.armor.Armor;

public class ArmorEventListener implements Listener {

	private ArmorClickListener listener;

	public ArmorEventListener(ArmorClickListener listener) {
		this.listener = listener;
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Armor p = listener.getPlayer(event.getPlayer().getName());
		if(p == null) {
			return;
		}
		p.onMove(event);
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Armor p = listener.getPlayer(event.getPlayer().getName());
		if(p == null) {
			return;
		}
		p.onInteract(event);
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Armor p = listener.getPlayer(event.getPlayer().getName());
		if(p == null) {
			return;
		}
		p.onInteractEntity(event);
	}
	
	@EventHandler
	public void onPlayerDamageEntity(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof Player) {
			Player damaged = (Player) event.getEntity();
			Armor p = listener.getPlayer(damaged.getName());
			if(p == null) {
				return;
			}
			p.onDamageByEntity(event);
		}
		if(event.isCancelled()) {
			return;
		}
		if(event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			Armor p = listener.getPlayer(damager.getName());
			if(p == null) {
				return;
			}
			p.onDamageEntity(event);
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		Player killer = event.getEntity().getKiller();
		if(killer != null) {
			Armor p = listener.getPlayer(killer.getName());
			if(p == null) {
				return;
			}
			p.onKillEntity(event);
		}
	}
	
	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Armor p = listener.getPlayer(event.getPlayer().getName());
		if(p == null) {
			return;
		}
		p.onToggleSneak(event);
	}
	
	@EventHandler
	public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
		Armor p = listener.getPlayer(event.getPlayer().getName());
		if(p == null) {
			return;
		}
		p.onToggleSprint(event);
	}
	
	@EventHandler
	public void onPlayerVelocity(PlayerVelocityEvent event) {
		Armor p = listener.getPlayer(event.getPlayer().getName());
		if(p == null) {
			return;
		}
		p.onVelocity(event);
	}
}
