package com.github.coaster3000.exactnode;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerInformer {

	public static String NO_PERM = "&4You do not have permission";
	public static String EMPTY = "for that!";
	public static String FULL = "&e%1$s &4%2$s.";
	public static String ACTIONONLY = "to &e%1$s.";

	public static enum Action {
		USE, PICKUP, DROP, HAVE, BREAK, PLACE, CHAT;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public static void inform(Player player) {
		inform(player, (Material) null, (Action) null);
	}

	public static void inform(Player player, Action action) {
		informPlayer(player, NO_PERM + " " + ((action != null) ? String.format(ACTIONONLY, action) : EMPTY));
	}

	public static void inform(Player player, Material mat, Action action) {
		informPlayer(player, NO_PERM + " " + ((mat != null && action != null) ? String.format(FULL, action, mat.name().replaceAll("_", "")) : EMPTY));
	}

	protected static final void informPlayer(Player player, String msg) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	}
}
