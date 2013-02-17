package com.github.coaster3000.exactnode;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class ExactNodeListener implements Listener {
	protected ExactNode plugin = null;
	protected PermissionRegistry perms;
	public ExactNodeListener(ExactNode plugin,ConfigurationSection config)
	{
		this.plugin = plugin;
		perms = plugin.getPermissions();
		if (perms == null)
			throw new IllegalArgumentException("Plugin does not have permission registry!!!");
	}
	
	public boolean hasPermission(Player player,String permission)
	{
		return player.hasPermission(permission);
	}
	
	public void register(){
		ExactNode.getPluginManager().registerEvents(this, plugin);
	}
}
