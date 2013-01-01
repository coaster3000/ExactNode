package com.github.coaster3000.exactnode.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import com.github.coaster3000.exactnode.ExactNodeListener;
import com.github.coaster3000.exactnode.PlayerInformer;
import com.github.coaster3000.exactnode.PlayerInformer.Action;

public class PlayerListener extends ExactNodeListener {

	private boolean checkChat = true;

	public PlayerListener(Plugin plugin, ConfigurationSection config) {
		super(plugin, config);
		checkChat = config.getBoolean("checks.chat", checkChat);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onAsyncChat(AsyncPlayerChatEvent event) {
		if (!checkChat)
			return;

		Player player = event.getPlayer();
		List<String> perms = new ArrayList<String>();
		perms.add(ROOTNODE + ".chat");
		if (useWildNode) {
			perms.add(ROOTNODE + "." + WILDCARD);
			perms.add(WILDCARD);
		}
		if (permissionDenied(player, perms.toArray(new String[0]))) {
			event.setCancelled(true);
			if (informPlayers)
				PlayerInformer.inform(event.getPlayer(), Action.CHAT);
		}
	}

}
