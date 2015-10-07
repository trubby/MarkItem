package Trubby.co.th;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Config {
	private static FileConfiguration config;

	public static void loadConfig(Plugin plugin) {
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if (!configFile.exists()) {
			plugin.saveDefaultConfig();
		}
		config = YamlConfiguration.loadConfiguration(configFile);
	}

	public static FileConfiguration getConfig() {
		return config;
	}
}
