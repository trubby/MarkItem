package Trubby.co.th;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class CommandExecutor {
	public static void giveMark(CommandSender sender) {
		if ((sender instanceof Player)) {
			giveMark((Player) sender);
		} else {
			sender.sendMessage("It is player side command");
		}
	}

	public static void giveMark(CommandSender sender, String name) {
		Player player = MarkItem.getInstance().getServer().getPlayer(name);
		if (player == null) {
			sender.sendMessage("Player " + name + " not found.");
		} else {
			giveMark(player);
		}
	}

	private static void giveMark(Player player) {
		if (player.getInventory().firstEmpty() != -1) {
			player.getInventory().addItem(new ItemStack[] { MarkItem.getItemMarker().getMark() });
		}
	}
}
