package com.mtihc.minecraft.specialarmor.states;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.mtihc.minecraft.specialarmor.armor.Armor;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlot;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlotState;
import com.mtihc.minecraft.specialarmor.armor.DefaultSlotState;


public class DefaultShoesState extends DefaultSlotState {

	public static final ArmorSlotState FEATHER = new ArmorSlotState(Material.FEATHER, 1) {
		@Override
		public String[] getDescription() {
			return new String[]{"Fall down slow and safe."};
		}
		
		@Override
		protected void onMove(PlayerMoveEvent event, Armor armor) {
			if(event.getPlayer().getFallDistance() > 2) {
				event.getPlayer().setFallDistance(2);
				
				damageItem(armor, 1);
			}
		}
		
	};
	
	public static final ArmorSlotState TNT = new ArmorSlotState(Material.TNT, 4) {
		@Override
		public String[] getDescription() {
			return new String[]{"Jump from a height for explosive landings."};
		}
		
		@Override
		protected void onMove(PlayerMoveEvent event, Armor armor) {
			if(event.getPlayer().getFallDistance() > 3 && !getBlockBelow(event.getPlayer().getLocation()).isEmpty()) {
				CraftWorld world = (CraftWorld) event.getPlayer().getWorld();
				Location loc = event.getTo();
				world.getHandle().createExplosion(((CraftPlayer)event.getPlayer()).getHandle(), loc.getX(), loc.getY(), loc.getZ(), 2F, false);

				damageItem(armor, Material.LEATHER_BOOTS.getMaxDurability() / 4 + 1);
				subtractItem(armor);
			}
		}
		
	};
	
	public static final ArmorSlotState WATER_BUCKET = new ArmorSlotState(Material.WATER_BUCKET, 1) {
		@Override
		public String[] getDescription() {
			return new String[]{"Walk over lava."};
		}
		
		@Override
		protected void onMove(PlayerMoveEvent event, Armor armor) {
			Block block = getBlockBelow(event.getTo());
			if(block.getType().equals(Material.LAVA) || block.getType().equals(Material.STATIONARY_LAVA)) {
				block.setType(Material.OBSIDIAN);
				
				damageItem(armor, 2);
			}
		}
		
		@Override
		protected ItemStack[] getDrops() {
			return new ItemStack[]{new ItemStack(Material.BUCKET)};
		}
	};
	
	public static final ArmorSlotState ICE = new ArmorSlotState(Material.ICE, 1) {
		@Override
		public String[] getDescription() {
			return new String[]{"Walk over lava, more efficiently."};
		}
		
		@Override
		protected void onMove(PlayerMoveEvent event, Armor armor) {
			Block block = getBlockBelow(event.getTo());
			if(block.getType().equals(Material.LAVA) || block.getType().equals(Material.STATIONARY_LAVA)) {
				block.setType(Material.OBSIDIAN);
				
				damageItem(armor, 1);
			}
		}
		
	};
	

	
	
	
	public DefaultShoesState() {
		super();
		values.put(FEATHER.getType(), FEATHER);
		values.put(TNT.getType(), TNT);
		values.put(WATER_BUCKET.getType(), WATER_BUCKET);
		values.put(ICE.getType(), ICE);
		
	}
	
	public static Block getBlockBelow(Location location) {
		return location.getBlock().getRelative(0, -1, 0);
	}
	
	
	public static void damageItem(Armor armor, int damage) {
		armor.damageArmor(ArmorSlot.SHOES, damage);
	}
	
	public static void subtractItem(Armor armor) {
		ItemStack item = armor.getItemStack(ArmorSlot.SHOES);
		if(item.getAmount() == 1) {
			armor.setItemStack(ArmorSlot.SHOES, new ItemStack(0));
		}
		else {
			item.setAmount(item.getAmount() - 1);
		}
	}
}
