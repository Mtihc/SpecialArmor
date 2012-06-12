package com.mtihc.minecraft.specialarmor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mtihc.minecraft.specialarmor.armor.Armor;

public class PlayerRepository {

	private File directory;
	
	public PlayerRepository(String directory) {
		this(new File(directory));
	}
	
	public PlayerRepository(File directory) {
		this.directory = directory;
		this.directory.mkdirs();
		
	}
	
	public boolean hasPlayer(String name) {
		return getPlayerFile(name).exists();
	}
	
	public Armor loadPlayerExact(String name) throws IOException, InvalidConfigurationException {
		File file = getPlayerFile(name);
		if(!file.exists()) {
			return null;
		}
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.load(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		return (Armor) config.get("player");
	}
	
	public void savePlayer(Armor player) throws IOException {
		String name = player.getName();
		File file = getPlayerFile(name);
		YamlConfiguration config = new YamlConfiguration();
		config.set("player", player);
		config.save(file);
	}
	
	public boolean removePlayer(String name) {
		File file = getPlayerFile(name);
		return file.delete();
	}
	
	public Set<String> getPlayerNames() {
		final Set<String> result = new HashSet<String>();
		directory.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".yml")) {
					result.add(name.substring(0, name.length() - ".yml".length()));
				}
				return false;
			}
		});
		return result;
	}
	
	
	
	
	protected File getPlayerFile(String name) {
		return new File(directory + "/" + name + ".yml");
	}

}
