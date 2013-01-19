package com.github.coaster3000.exactnode.permissions;

import java.util.List;

import org.bukkit.permissions.Permission;

public interface PermissionSet{
	public boolean isPermission(String path);
	public PermissionSection getPermissionSection(String path);
	public void set(String path,Permission perm) throws PermissionMapException;
	public List<String> getKeys(boolean deep);
	public List<String> getKeys();
	public boolean isExistant(String path);
	public PermissionSection createPermissionSection(String path);
	public Permission getPermission(String path);
	
}
