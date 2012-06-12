package com.mtihc.minecraft.specialarmor.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mtihc.minecraft.specialarmor.armor.Armor;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlot;
import com.mtihc.minecraft.specialarmor.armor.ArmorSlotState;
import com.mtihc.minecraft.specialarmor.armor.DefaultSlotState;

public class ListArmorSlot extends SimpleCommand {

	private ArmorSlot slot;

	public ListArmorSlot(
			SimpleCommand parent, 
			String label, 
			List<String> aliases, ArmorSlot slot) {
		super(parent, label, aliases, parent.getPermission(), "You don't have permission to list shoe types.",
				"[page]", slot.name() + " List.");
		this.slot = slot;
	}

	@Override
	protected boolean onCommand(CommandSender sender, String label,
			String[] args) {
		int page;
		try {
			page = Integer.parseInt(args[0]);
		} catch(NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Expected a page number, instead of text.");
			sender.sendMessage(getUsage());
			return false;
		} catch(Exception e) {
			page = -1;
		}
		
		DefaultSlotState defaultStates = Armor.getDefaultSlotStates(slot);
		ArmorSlotState[] states = defaultStates.values();
		
		
		if(page < 1) {
			sender.sendMessage(ChatColor.GREEN + slot.name() + " list:");
			for (int i = 0; i < states.length; i++) {
				ArmorSlotState state = states[i];
				sendInfo(sender, i, state);
			}
			return true;
		}
		
		
		int total = states.length;
		int totalPerPage = 10;
		int startIndex = (page - 1) * totalPerPage;
		int endIndex = startIndex + totalPerPage;

		int totalPages = (int) Math.ceil((float) total / totalPerPage);
		if (page > totalPages || page < 1) {
			sender.sendMessage(ChatColor.RED + "Page " + page
					+ " does not exist.");
			return false;
		}
		if (totalPages > 1) {
			sender.sendMessage(ChatColor.GREEN + slot.name() + " List (page "
					+ page + "/" + totalPages + "):");
		} else {
			sender.sendMessage(ChatColor.GREEN + slot.name() + " List:");
		}

		for (int i = startIndex; i < endIndex && i < total; i++) {
			ArmorSlotState state = states[i];
			sendInfo(sender, i, state);
		}
		return true;
	}
	
	private void sendInfo(CommandSender sender, int i, ArmorSlotState state) {
		String[] desc = state.getDescription();
		desc = Arrays.copyOf(desc, desc.length);
		desc[0] = ChatColor.GRAY + String.valueOf(i + 1) + ": " + ChatColor.GOLD + state.getType().name().replace("_", " ") + ": " + ChatColor.WHITE + desc[0];
		for (int j = 0; j < desc.length; j++) {
			desc[j] = "  " + desc[j];
		}
		sender.sendMessage(desc);
	}

	@Override
	public boolean hasNested() {
		return false;
	}

	@Override
	public SimpleCommand getNested(String labelOrAlias) {
		return null;
	}

	@Override
	public String[] getNestedCommandLabels() {
		return null;
	}

}
