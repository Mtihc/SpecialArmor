package com.mtihc.minecraft.specialarmor.armor;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;

public abstract class DefaultSlotState {

	protected final Map<Material, ArmorSlotState> values = new LinkedHashMap<Material, ArmorSlotState>();
	
	public DefaultSlotState() {
		
	}
	
	public ArmorSlotState fromType(Material type) {
		return values.get(type);
	}
	
	public ArmorSlotState[] values() {
		return values.values().toArray(new ArmorSlotState[values.size()]);
	}
}
