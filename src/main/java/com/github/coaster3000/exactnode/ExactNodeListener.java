package com.github.coaster3000.exactnode;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class ExactNodeListener implements Listener {
	protected ExactNode plugin = null;
	public ExactNodeListener(ExactNode plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean hasPermission(Player player,String permission)
	{
		return player.hasPermission(permission);
	}
}
