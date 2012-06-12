package com.mtihc.minecraft.specialarmor.states;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import com.mtihc.minecraft.specialarmor.armor.Armor;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlot;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlotState;
import com.mtihc.minecraft.specialarmor.armor.DefaultSlotState;


public class DefaultPantsState extends DefaultSlotState {

	public static final ArmorSlotState PISTON = new ArmorSlotState(Material.PISTON_BASE, 8) {
		@Override
		public String[] getDescription() {
			return new String[]{"The more, the higher you jump."};
		}
		
		@Override
		protected void onMove(PlayerMoveEvent event, Armor armor) {
			Vector vel = event.getPlayer().getVelocity();
			if(vel.getY() < -0.15 && vel.getY() > -0.16 && event.getPlayer().getFallDistance() == 0) {
				double amount = armor.getItemStack(ArmorSlot.PANTS).getAmount();
				
				vel.setY(vel.getY() + (amount + 4) * 0.16D);
				event.getPlayer().setVelocity(vel);
				
				armor.damageArmor(ArmorSlot.PANTS, (int)amount);
			}
		}
		
	};
	
	public DefaultPantsState() {
		super();
		values.put(PISTON.getType(), PISTON);
	}
}
