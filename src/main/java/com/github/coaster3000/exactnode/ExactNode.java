package com.github.coaster3000.exactnode;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.github.coaster3000.exactnode.listeners.*;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ExactNode extends JavaPlugin {
	private PermissionDefault defaultBlockBreak = PermissionDefault.OP;
	private PermissionDefault defaultBlockPlace = PermissionDefault.OP;
	private PermissionDefault defaultBlockIgnite = PermissionDefault.OP;
	private PermissionDefault defaultItemUse = PermissionDefault.OP;
	private PermissionDefault defaultItemDrop = PermissionDefault.OP;
	private PermissionDefault defaultItemPickup = PermissionDefault.OP;
	private PermissionDefault defaultItemCraft = PermissionDefault.OP;
	private PermissionDefault defaultItemHave = PermissionDefault.OP;
	private PermissionDefault defaultChat = PermissionDefault.TRUE;
	private PermissionRegistry perms = null;
	private List<ExactNodeListener> listeners = new ArrayList<ExactNodeListener>();
	@SuppressWarnings("unchecked")
	protected static final Class<? extends ExactNodeListener>[] LISTENERS = new Class[] {};
	
	
	public PermissionRegistry getPermissions(){
		return perms;
	}
	
	public static PermissionRegistry getPermissionRegistry(){
		return instance.getPermissions();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		perms = new PermissionRegistry();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}

	protected void registerListeners() {
		for (Class<? extends ExactNodeListener> listenerClass : LISTENERS) {
			try {
				Constructor<? extends ExactNodeListener> constructor = listenerClass.getConstructor(Plugin.class, ConfigurationSection.class);
				ExactNodeListener listener = (ExactNodeListener) constructor.newInstance(this, this.getConfig());
				this.listeners.add(listener);
			} catch (Throwable e) {
				this.getLogger().warning("Failed to initialize \"" + listenerClass.getName() + "\" listener");
				e.printStackTrace();
			}
		}
	}
}
