package com.mtihc.minecraft.specialarmor;

import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import com.mtihc.minecraft.specialarmor.armor.Armor;
import com.mtihc.minecraft.specialarmor.commands.MainCommand;
import com.mtihc.minecraft.specialarmor.states.DefaultHatState;
import com.mtihc.minecraft.specialarmor.states.DefaultPantsState;
import com.mtihc.minecraft.specialarmor.states.DefaultShirtState;
import com.mtihc.minecraft.specialarmor.states.DefaultShoesState;

public class SpecialArmorPlugin extends JavaPlugin {

	static {
		ConfigurationSerialization.registerClass(Armor.class);
	}
	
	private static SpecialArmorPlugin plugin;
	
	public static SpecialArmorPlugin getPlugin() {
		return plugin;
	}
	
	private ArmorClickListener listener;
	
	private MainCommand cmd;
	
	protected Map<String, Armor> players;
	protected PlayerRepository repo;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(label.equalsIgnoreCase(cmd.getLabel()) || cmd.getAliases().contains(label.toLowerCase())) {
			cmd.execute(sender, label, args);
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public void onDisable() {
		
		
		plugin = null;
	}

	@Override
	public void onEnable() {
		plugin = this;

		Armor.setDefaultSlotStates(
				new DefaultHatState(), 
				new DefaultShirtState(), 
				new DefaultPantsState(), 
				new DefaultShoesState());

		// listener
		listener = new ArmorClickListener(this);
		ArmorEventListener behavior = new ArmorEventListener(listener);
		getServer().getPluginManager().registerEvents(listener, this);
		getServer().getPluginManager().registerEvents(behavior, this);
		
		cmd = new MainCommand(getCommand("armor"));
	}


	

}
