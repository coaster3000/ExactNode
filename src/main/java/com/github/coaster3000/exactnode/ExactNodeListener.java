package com.github.coaster3000.exactnode;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class ExactNodeListener implements Listener {
	protected ExactNode plugin = null;
	protected boolean informPlayers = false;
	protected boolean useMaterialNames = true;
	public ExactNodeListener(ExactNode plugin,ConfigurationSection config)
	{
		useMaterialNames = config.getBoolean("use-material-names",useMaterialNames);
		informPlayers = config.getBoolean("inform-players",informPlayers);
		this.plugin = plugin;
	}
	
	public boolean hasPermission(Player player,String permission)
	{
		return player.hasPermission(permission);
	}
}
