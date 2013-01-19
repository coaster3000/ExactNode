package com.github.coaster3000.exactnode.permissions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.permissions.Permission;

public class PermissionMap implements PermissionSection {
	private PermissionSection parent = null;
	private Map<String, Object> data = new HashMap<String, Object>();
	private String name = null;

	public PermissionMap(PermissionSection parent, String path) {
		path = cleanPath(path);
		if (parent != null)
			this.parent = parent;
		if (path.indexOf('.') > 0) {
			name = path.substring(0, path.indexOf('.') - 1);
			createPermissionSection(path.substring(path.indexOf('.') + 1));
		} else
			name = path;

	}

	public java.util.List<String> getKeys(boolean deep) {
		ArrayList<String> keys = new ArrayList<String>();
		if (data.size() > 0)
			for (java.util.Map.Entry<String, Object> ob : data.entrySet()) {
				if (ob.getValue() instanceof PermissionSection)
					if (deep)
						keys.addAll(((PermissionSection) ob).getKeys(true));
					else
						continue;
				else
					keys.add(ob.getKey());
			}
		return keys;
	}

	public java.util.List<String> getKeys() {
		return getKeys(false);
	}

	public PermissionMap(String path) {
		this(null, path);
	}

	private static boolean isValidData(Object data) {
		return ((data instanceof PermissionSection) || (data instanceof Permission)) ? true : false;
	}

	private void setData(PermissionSection sect) {
		data.put(sect.getName(), sect);
	}

	public boolean isPermission(String path) {
		if (!isExistant(path))
			return false;
		Object d = data.get(path);
		return (d instanceof Permission);
	}

	public PermissionSection createPermissionSection(String path) {
		path = cleanPath(path);
		PermissionSection sect = getPermissionSection(path);

		boolean exist = (sect != null);
		if (exist)
			return sect;
		if (sect == null)
			sect = new PermissionMap(this, path);

		if (!exist)
			setData(sect);
		return sect;
	}

	public Permission getPermission(String path) {
		return (isPermission(path)) ? (Permission) data.get(path) : null;
	}

	public PermissionSection getPermissionSection(String path) {
		return (isPermissionSection(path)) ? (PermissionSection) data.get(path) : null;
	}

	public boolean isPermissionSection(String path) {
		if (!isExistant(path))
			return false;
		Object d = data.get(path);
		return (d instanceof PermissionSection);
	}

	public PermissionSection getRoot() {
		return (parent != null) ? parent.getRoot() : this;
	}

	public PermissionSection getParent() {
		return (parent != null) ? parent : this;
	}

	public void set(String path, Permission perm) throws PermissionMapException {
		if (isExistant(path))
			throw new PermissionAlreadyMappedException(path);
		if (perm == null)
			throw new PermissionMapException("Permission can't be null!");
		data.put(path, perm);
	}

	public boolean isExistant(String path) {
		return data.containsKey(path);
	}

	public void clean() {
		for (java.util.Map.Entry<String, Object> obj : data.entrySet())
			if (!isValidData(obj.getValue()))
				data.remove(obj.getKey());
	}

	private static String cleanPath(String original) {
		while (original.contains(".."))
			//Fail safe
			original = original.replaceAll("..", "."); //Cleanup code

		while (original.startsWith("."))
			//Fail Safe
			original = original.substring(1); //Cleanup code

		return original;
	}

	public String getName() {
		return name;
	}
}
