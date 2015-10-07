package Trubby.co.th;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

class PlayerListener implements Listener {
	private static final Map<UUID, ItemStack[]> INVENTORIES = new HashMap<UUID, ItemStack[]>();
	private static final Map<UUID, ItemStack[]> ARMORS = new HashMap<UUID, ItemStack[]>();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();

		List<ItemStack> armorList = new ArrayList<ItemStack>();
		for (ItemStack armor : player.getInventory().getArmorContents()) {
			if ((armor.getType() == Material.AIR) || (!MarkItem.getItemMarker().hasMark(armor))) {
				armorList.add(new ItemStack(Material.AIR, 0));
			} else {
				armorList.add(armor);
				event.getDrops().remove(armor);
			}
		}
		ARMORS.put(player.getUniqueId(), armorList.toArray(new ItemStack[armorList.size()]));

		List<ItemStack> contents = new ArrayList<ItemStack>();
		ArrayList<ItemStack> drops = new ArrayList<>();
		for (ItemStack drop : event.getDrops() {
			player.sendMessage("s");
			if ((drop.getType() != Material.AIR) && (MarkItem.getItemMarker().hasMark(drop))) {
				contents.add(drop);
				event.getDrops().remove(drop);
				Bukkit.broadcastMessage("test");
			}
		}
		INVENTORIES.put(player.getUniqueId(), contents.toArray(new ItemStack[contents.size()]));
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		player.getInventory().setArmorContents((ItemStack[]) ARMORS.get(player.getUniqueId()));
		player.getInventory().addItem((ItemStack[]) INVENTORIES.get(player.getUniqueId()));

		ARMORS.remove(player.getUniqueId());
		INVENTORIES.remove(player.getUniqueId());
	}
}
