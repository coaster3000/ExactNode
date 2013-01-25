package com.github.coaster3000.exactnode;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

public class PermissionWrapper {
	private String name = null;

	public String getName() {
		return name;
	}

	public PermissionWrapper(String name) {
		this.name = name;
	}
	
	public void addChild(PermissionWrapper perm, boolean value) {
		getPermission().getChildren().put(perm.getName(), value);
		getPermission().recalculatePermissibles();
	}

	public Permission getPermission() {
		return Bukkit.getServer().getPluginManager().getPermission(name);
	}
}
