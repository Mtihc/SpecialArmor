package com.mtihc.minecraft.specialarmor.states;

import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.mtihc.minecraft.specialarmor.armor.Armor;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlot;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlotState;
import com.mtihc.minecraft.specialarmor.armor.DefaultSlotState;


public class DefaultShirtState extends DefaultSlotState {

	public static final ArmorSlotState TNT = new ArmorSlotState(Material.TNT, 4) {
		@Override
		public String[] getDescription() {
			return new String[]{
					"Left-click with a button in your hand ", 
					"to activate the TNT. ", 
					"The more, the bigger the explosion."};
		}
		
		@Override
		protected void onInteract(PlayerInteractEvent event, Armor armor) {
			if(event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if(event.getItem().getType().equals(Material.STONE_BUTTON)) {
					float power = armor.getItemStack(ArmorSlot.SHIRT).getAmount();
					armor.setItemStack(ArmorSlot.SHIRT, new ItemStack(0));
					armor.damageArmor(ArmorSlot.SHIRT, armor.getArmorHealthMax(ArmorSlot.SHIRT));
					event.getPlayer().setHealth(1);
					event.getPlayer().getWorld().createExplosion(event.getPlayer().getLocation(), power, false);
				}
			}
		}
		
	};
	
	public DefaultShirtState() {
		super();
		values.put(TNT.getType(), TNT);
		
	}
}
