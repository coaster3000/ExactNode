package com.github.coaster3000.exactnode;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerInformer {

	public static String NO_PERM = "&4You do not have permission";
	public static String EMPTY = "for that!";
	public static String FULL = "&e%1$s %2$s";

	public static enum Action {
		USE, PICKUP, DROP, HAVE, BREAK, PLACE;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public void inform(Player player)
	{
		inform(player,(Material)null,(Action)null);
	}
	
	public void inform(Player player, Material mat, Action action) {
		informPlayer(player, NO_PERM + ((mat != null && action != null) ? String.format(FULL, action, mat.name().replaceAll("_", "")) : EMPTY));
	}

	protected final void informPlayer(Player player, String msg) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}

}
