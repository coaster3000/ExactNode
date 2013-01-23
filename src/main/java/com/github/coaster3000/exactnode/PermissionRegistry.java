package com.github.coaster3000.exactnode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public class PermissionRegistry {
	private ConcurrentHashMap<String,Permission>perms = new ConcurrentHashMap<String, Permission>();
	private PluginManager pm = null;
	public PermissionRegistry() {
		pm = Bukkit.getServer().getPluginManager();
	}
	public void addPerm(Permission perm)
	{
		if (perm == null)
			return;
		if (perms.containsKey(perm.getName()))
			perms.remove(perm);
		perms.put(perm.getName(),perm);
	}
	public void updatePerm(String name)
	{
		Permission permission = pm.getPermission(name);
		if (permission==null)
			return;
		if (perms.containsKey(name))
			perms.remove(name);
		perms.put(name,permission);
	}
	private List<String> detailPermission(Permission perm)
	{
		ArrayList<String> ret = new ArrayList<String>();
		ret.add(perm.getName()+ " : "+ perm.getDefault().name().toLowerCase());
		if (perm.getChildren().isEmpty())
			return ret;
		for (Entry<String, Boolean> dat : perm.getChildren().entrySet())
			ret.add("    "+dat.getKey()+ " : "+dat.getValue().toString().toLowerCase());
		return ret;
	}
	
	public List<String> dumpPermissions()
	{
		ArrayList<String> ret = new ArrayList<String>();
		for (Permission perm:perms.values())
			ret.addAll(detailPermission(perm));
		return ret;
	}
}
