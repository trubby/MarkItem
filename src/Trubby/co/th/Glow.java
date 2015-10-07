package Trubby.co.th;

import java.lang.reflect.Field;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;

class Glow extends EnchantmentWrapper {
	private static final Enchantment GLOW = new Glow();

	static {
		try {
			Field field = Enchantment.class.getDeclaredField("acceptingNew");
			field.setAccessible(true);
			field.set(null, Boolean.valueOf(true));

			Enchantment.registerEnchantment(GLOW);
		} catch (Exception ignored) {
		}
	}

	private Glow() {
		super(69);
	}

	public boolean canEnchantItem(ItemStack item) {
		return true;
	}

	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	public EnchantmentTarget getItemTarget() {
		return null;
	}

	public int getMaxLevel() {
		return 10;
	}

	public String getName() {
		return "Glow";
	}

	public int getStartLevel() {
		return 1;
	}

	public static void addGlow(ItemStack item) {
		item.addEnchantment(GLOW, 1);
	}
}
