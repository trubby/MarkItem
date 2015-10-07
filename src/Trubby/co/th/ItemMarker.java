package Trubby.co.th;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemMarker implements Listener {
	private final ItemStack mark;
	private int count = 0;

	public ItemMarker() {
		String[] textures = Config.getConfig().getString("mark.texture").split(":");
		if (Material.getMaterial(textures[0]) == null) {
			MarkItem.getInstance().getLogger().log(Level.WARNING, "Material {0} not found", textures[0]);
			this.mark = new ItemStack(Material.AIR);
			return;
		}
		ItemStack item = new ItemStack(Material.getMaterial(textures[0]));
		if (textures.length == 2) {
			item.setDurability((short) Byte.parseByte(textures[1]));
		}
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("mark.name")));
		List<String> lore = new ArrayList<String>();
		Collections.addAll(lore,
				ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("mark.lore")).split("\n"));
		im.setLore(lore);
		item.setItemMeta(im);
		if (Config.getConfig().getBoolean("mark.glow", false)) {
			Glow.addGlow(item);
		}
		this.mark = item;
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		List<String> allowedList = Config.getConfig().getStringList("allowed");
		for (String allowed : allowedList) {
			if (allowed.contains("-")) {
				String[] splitted = allowed.split("-");
				int start = Integer.parseInt(splitted[0]);
				int end = Integer.parseInt(splitted[1]);
				for (int i = start; i <= end; i++) {
					ItemStack item = new ItemStack(i, 1, (short) -1);
					if (!isDenied(item)) {
						addRecipe(item);
					}
				}
			} else if (allowed.contains("#")) {
				String[] splitted = allowed.split("#");
				int id = Integer.parseInt(splitted[0]);
				short data = Short.parseShort(splitted[1]);

				ItemStack item = new ItemStack(id, 1, data);
				if (!isDenied(item)) {
					addRecipe(item);
				}
			} else {
				int id = Integer.parseInt(allowed);

				ItemStack item = new ItemStack(id, 1, (short) -1);
				if (!isDenied(item)) {
					addRecipe(item);
				}
			}
		}
		MarkItem.getInstance().getLogger().log(Level.INFO, "{0} item(s) have been initialized",
				Integer.valueOf(this.count));
	}

	@SuppressWarnings("deprecation")
	private boolean isDenied(ItemStack item) {
		List<String> deniedList = Config.getConfig().getStringList("denied");
		for (String denied : deniedList) {
			if (denied.contains("-")) {
				String[] splitted = denied.split("-");
				int start = Integer.parseInt(splitted[0]);
				int end = Integer.parseInt(splitted[1]);
				if ((item.getType().getId() >= start) && (item.getType().getId() <= end)) {
					return true;
				}
			} else if (denied.contains("#")) {
				String[] splitted = denied.split("#");
				int id = Integer.parseInt(splitted[0]);
				short data = Short.parseShort(splitted[1]);
				if ((item.getType().getId() == id) && (item.getDurability() == data)) {
					return true;
				}
			} else {
				int id = Integer.parseInt(denied);
				if (item.getType().getId() == id) {
					return true;
				}
			}
		}
		return false;
	}

	private void addRecipe(ItemStack item) {
		this.count += 1;
		ShapelessRecipe recipe = new ShapelessRecipe(item);
		recipe.addIngredient(item.getData());
		recipe.addIngredient(this.mark.getData());
		MarkItem.getInstance().getServer().addRecipe(recipe);
	}

	private ItemStack addMark(ItemStack item) {
		if (!hasMark(item)) {
			ItemMeta im = item.getItemMeta();
			List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<String>();
			lore.add(MarkItem.UNIQUE_MARK_TAG + getMarkText());
			im.setLore(lore);
			item.setItemMeta(im);
		}
		return item;
	}

	public boolean hasMark(ItemStack item) {
		if (item.getItemMeta().hasLore()) {
			for (String s : item.getItemMeta().getLore()) {
				if (s.startsWith(MarkItem.UNIQUE_MARK_TAG)) {
					return true;
				}
			}
		}
		return hasOldMark(item);
	}

	public boolean hasOldMark(ItemStack item) {
		return (item.getItemMeta().hasLore()) && (item.getItemMeta().getLore().contains(getMarkText()));
	}

	public ItemStack updateMark(ItemStack item) {
		if (hasOldMark(item)) {
			List<String> lore = item.getItemMeta().getLore();
			lore.remove(getMarkText());
			lore.add(MarkItem.UNIQUE_MARK_TAG + getMarkText());
			item.getItemMeta().setLore(lore);
		}
		return item;
	}

	public ItemStack getMark() {
		return this.mark;
	}

	private String getMarkText() {
		return ChatColor.translateAlternateColorCodes('&', Config.getConfig().getString("mark.text"));
	}

	@EventHandler
	public void onCraft(PrepareItemCraftEvent event) {
		List<ItemStack> matrix = new ArrayList<ItemStack>(Arrays.asList(event.getInventory().getMatrix()));
		if ((event.getRecipe() instanceof ShapelessRecipe)) {
			for (ItemStack item : event.getInventory().getMatrix()) {
				if ((item == null) || (item.getType() == Material.AIR)) {
					matrix.remove(item);
				}
			}
			if (matrix.size() != 2) {
				return;
			}
			if (matrix.contains(this.mark)) {
				matrix.remove(this.mark);
				ItemStack result = ((ItemStack) matrix.get(0)).clone();
				if ((isDenied(result)) || (hasMark(result))) {
					event.getInventory().setResult(null);
				} else {
					event.getInventory().setResult(addMark(result));
				}
			}
		}
	}
}
