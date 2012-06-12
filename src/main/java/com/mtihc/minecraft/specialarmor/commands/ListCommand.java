package com.mtihc.minecraft.specialarmor.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.mtihc.minecraft.specialarmor.armor.ArmorSlot;

public class ListCommand extends SimpleCommand {

	public ListCommand(SimpleCommand parent, String label,
			List<String> aliases, String permission) {
		super(parent, label, aliases, permission, "You don't have permission to list all special armor types.",
				"", "Use the nested commands to see special armor types.");
	}

	@Override
	protected boolean onCommand(CommandSender sender, String label,
			String[] args) {
		try {
			int page = Integer.parseInt(args[0]);
			sendHelp(sender, page);
			return true;
		} catch(IndexOutOfBoundsException e) {
			sendHelp(sender, -1);
			return true;
		} catch(NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "Unknown command: /" + getUniqueName() + " " + args[0]);
			sender.sendMessage(ChatColor.RED + "For command help, type: " + ChatColor.WHITE + "/" + getUniqueName());
			return false;
		}
	}

	@Override
	public boolean hasNested() {
		return true;
	}

	@Override
	public SimpleCommand getNested(String labelOrAlias) {
		String lbl = labelOrAlias.toLowerCase();
		
		if(lbl.startsWith("shoe") || lbl.startsWith("boot")) {
			return new ListArmorSlot(this, "shoe", Arrays.asList(new String[]{"boot"}), ArmorSlot.SHOES);
		}
		else if(lbl.startsWith("pants") || lbl.startsWith("leg")) {
			return new ListArmorSlot(this, "pants", Arrays.asList(new String[]{"leg"}), ArmorSlot.PANTS);
		}
		else if(lbl.startsWith("shirt") || lbl.startsWith("chest")) {
			return new ListArmorSlot(this, "shirt", Arrays.asList(new String[]{"chest"}), ArmorSlot.SHIRT);
		}
		else if(lbl.startsWith("hat") || lbl.startsWith("helm")) {
			return new ListArmorSlot(this, "hat", Arrays.asList(new String[]{"helm"}), ArmorSlot.HAT);
		}
		else {
			return null;
		}
	}

	@Override
	public String[] getNestedCommandLabels() {
		return new String[]{"boot", "leg", "chest", "helmet"};
	}

}
