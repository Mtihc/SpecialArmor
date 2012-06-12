package com.mtihc.minecraft.specialarmor.armor;

import java.util.LinkedHashMap;

public enum ArmorSlot {
	 
	SHOES(36),
	PANTS(37), 
	SHIRT(38), 
	HAT(39);
	
	private static ArmorSlot[] values = ArmorSlot.values();
	private static LinkedHashMap<Integer, ArmorSlot> slots;
	
	static {
		slots = new LinkedHashMap<Integer, ArmorSlot>();
		ArmorSlot[] values = values();
		for (ArmorSlot slot : values) {
			slots.put(slot.getSlot(), slot);
		}
	}
	
	public static ArmorSlot fromSlot(int slot) {
		return slots.get(slot);
	}
	
	public static ArmorSlot fromIndex(int index) {
		if(index < 0 || index >= values.length) {
			return null;
		}
		else {
			return values[index];
		}
	}
	
	private int slot;

	private ArmorSlot(int slot) {
		this.slot = slot;
	}
	
	public int getSlot() {
		return slot;
	}
}