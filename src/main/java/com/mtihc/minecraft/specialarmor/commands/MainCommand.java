package com.mtihc.minecraft.specialarmor.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;


public class MainCommand extends SimpleCommand {

	public MainCommand(PluginCommand cmd) {
		super(null, cmd.getLabel(), cmd.getAliases(), null, "",
				"", "This is the main command.");
		// TODO Auto-generated constructor stub
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
		if(lbl.equals("list")) {
			return new ListCommand(this, "list", null, "specialarmor.list");
		}
		else {
			return null;
		}
	}

	@Override
	public String[] getNestedCommandLabels() {
		return new String[]{
				"list"
		};
	}

}
