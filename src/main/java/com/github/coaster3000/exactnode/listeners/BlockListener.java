package com.github.coaster3000.exactnode.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.plugin.Plugin;

import com.github.coaster3000.exactnode.ExactNodeListener;
import com.github.coaster3000.exactnode.PlayerInformer;

public class BlockListener extends ExactNodeListener {

	private static final String NODE = "blocks";
	private boolean protectIgnite = true;
	private boolean blockplace = true;
	private boolean blockbreak = true;

	public BlockListener(Plugin plugin, ConfigurationSection config, PlayerInformer informer) {
		super(plugin, config, informer);
		protectIgnite = config.getBoolean("check.ignition", protectIgnite);
		blockbreak = config.getBoolean("check.block.break", blockbreak);
		blockplace = config.getBoolean("check.block.place", blockplace);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onIgnite(BlockIgniteEvent event) {
		if (!protectIgnite)
			return;

		if (!event.getBlock().equals(BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL))
			return;

		List<String> perms = new ArrayList<String>();
		perms.add(ROOTNODE + "." + NODE + "ignite." + getBlockPermission(event.getBlock()));
		if (useWildNode) {
			perms.add(ROOTNODE + "." + NODE + "ignite." + WILDCARD);
			perms.add(ROOTNODE + "." + NODE + WILDCARD + getBlockPermission(event.getBlock()));
			perms.add(ROOTNODE + "." + NODE + WILDCARD);
			perms.add(ROOTNODE + "." + WILDCARD);
		}
		if (permissionDenied(event.getPlayer(), perms.toArray(new String[0])))
			event.setCancelled(true);
	}

}
