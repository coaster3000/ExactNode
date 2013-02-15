package com.github.coaster3000.exactnode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PermissionRegistry {
	protected TreeSet<String> perms = new TreeSet<String>();

	public PermissionRegistry()
	{
	}
	
	public synchronized void addPermission(String name) {
		perms.add(name);
	}

	public synchronized void removePermission(String name) {
		perms.remove(name);
	}

	public synchronized boolean hasPermissions(String name) {
		return perms.contains(name);
	}

	/**
	 * Updates permissions list to remove non existant data.
	 * 
	 * Also checks for new children attached to the nodes to update list.
	 * 
	 * <b>Warning This must be called on main server thread!</b>
	 */
	public synchronized void update() {
		if (!Bukkit.getServer().isPrimaryThread())
			throw new IllegalAccessError("dumpPermissionsDefaults() accessed off of main thread!");
		PluginManager pm = Bukkit.getServer().getPluginManager();
		List<String> removes = new ArrayList<String>();
		List<String> additions = new ArrayList<String>();
		
		for (String perm: perms)
		{
			Permission permission = pm.getPermission(perm);
			if (permission == null)
				removes.add(perm);
			else
			{
				for (String key: permission.getChildren().keySet())
				{
					Permission permission2 = pm.getPermission(key);
					if (permission2 == null)
						continue;
					else if (hasPermissions(key))
						continue;
					else
						additions.add(key);
				}
			}
		}
		for (String remove: removes)
		{
			removePermission(remove);
		}
		for (String addition: additions)
		{
			addPermission(addition);
		}
		
	}

	/**
	 * Warning <b>NOT THREAD SAFE</b>
	 * @return
	 */
	public synchronized List<String> dumpPermissionDefaults() {
		if (!Bukkit.getServer().isPrimaryThread())
			throw new IllegalAccessError("dumpPermissionsDefaults() accessed off of main thread!");
		PluginManager pm = Bukkit.getServer().getPluginManager();
		update();
		List<String> msgs = new ArrayList<String>();
		for (String name : perms) {
			Permission p = pm.getPermission(name);
			if (p == null) {
				continue;
			}
			String msg = new StringBuffer().append(p.getName()).append(" : ").append(p.getDefault().name()).toString();
			msgs.add(msg);
			Map<String,Boolean> d = p.getChildren();
			
			if (d.isEmpty())
				continue;
			for (Entry<String, Boolean> da:d.entrySet())
			{
				msg = new StringBuffer().append(">> ").append(da.getKey()).append(" : ").append(da.getValue()).toString();
				msgs.add(msg);
			}
		}
		return msgs;
	}

	public void addPermission(Permission permission) {
		addPermission(permission.getName());
	}

}
