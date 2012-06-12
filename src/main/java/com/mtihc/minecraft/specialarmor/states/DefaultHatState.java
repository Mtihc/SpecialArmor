package com.mtihc.minecraft.specialarmor.states;

import java.util.List;
import java.util.Random;

import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.SpawnEgg;

import com.mtihc.minecraft.specialarmor.armor.Armor;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlot;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlotState;
import com.mtihc.minecraft.specialarmor.armor.DefaultSlotState;


public class DefaultHatState extends DefaultSlotState {

	public static final ArmorSlotState ROTTEN_FLESH = new ArmorSlotState(Material.ROTTEN_FLESH, 1){

		@Override
		public String[] getDescription() {
			return new String[]{"Play the hurt-effect when you sneak"};
		}
		
		@Override
		protected void onToggleSneak(PlayerToggleSneakEvent event, Armor armor) {
			if(!event.isSneaking()) {
				return;
			}
			event.getPlayer().playEffect(EntityEffect.HURT);
			
			armor.damageArmor(ArmorSlot.HAT, 1);
		}
		
	};
	
	
	public static final ArmorSlotState EGG = new ArmorSlotState(Material.EGG, 3) {
		@Override
		public String[] getDescription() {
			return new String[]{
					"The more eggs in your hat, ",
					"the more change you have ",
					"of creatures dropping an egg."
			};
		}

		@Override
		protected void onKillEntity(EntityDeathEvent event, Armor armor) {
			if(event.getEntity() instanceof Player == false) {
				Random r = new Random();
				ItemStack eggs = armor.getItemStack(ArmorSlot.HAT);
				int random = r.nextInt(4 - eggs.getAmount());
				if(random == 0) {
					List<ItemStack> drops = event.getDrops();
					MaterialData data = new SpawnEgg(event.getEntityType());
					ItemStack egg = new ItemStack(Material.MONSTER_EGG, 1, (short)0, data.getData());
					drops.add(egg);
					
					armor.damageArmor(ArmorSlot.HAT, 3);
					eggs.setAmount(eggs.getAmount() - 1);
					if(eggs.getAmount() == 0) {
						eggs = new ItemStack(0);
					}
					armor.setItemStack(ArmorSlot.HAT, eggs);
				}
			}
			
			
		}

		
		
	};
	
	public DefaultHatState() {
		super();
		values.put(ROTTEN_FLESH.getType(), ROTTEN_FLESH);
		values.put(EGG.getType(), EGG);
	}
}
