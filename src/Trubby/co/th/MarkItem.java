package Trubby.co.th;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class MarkItem extends JavaPlugin {
	public static final String UNIQUE_MARK_TAG = ChatColor.translateAlternateColorCodes('&', "&m&a&r&k&r");
	private static MarkItem instance;
	private static ItemMarker itemMarker;

	public static MarkItem getInstance() {
		return instance;
	}

	public static ItemMarker getItemMarker() {
		return itemMarker;
	}

	public void onEnable() {
		instance = this;
		Config.loadConfig(this);
		if (!Config.getConfig().getBoolean("enabled")) {
			getLogger().warning("Plugin is not enabled!");
			setEnabled(false);
			return;
		}
		itemMarker = new ItemMarker();
		getServer().getPluginManager().registerEvents(itemMarker, this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			CommandExecutor.giveMark(sender);
		} else {
			CommandExecutor.giveMark(sender, args[0]);
		}
		return true;
	}
}
