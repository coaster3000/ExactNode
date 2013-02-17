package com.github.coaster3000.exactnode;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.github.coaster3000.exactnode.listeners.*;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ExactNode extends JavaPlugin {
	private PermissionRegistry perms = null;
	private List<ExactNodeListener> listeners = new ArrayList<ExactNodeListener>();
	@SuppressWarnings("unchecked")
	protected static final Class<? extends ExactNodeListener>[] LISTENERS = new Class[] {BlockListeners.class};
	private static PermissionDefault blockBreak = PermissionDefault.OP;
	private static PermissionDefault blockPlace = PermissionDefault.OP;
	private static PermissionDefault itemUse = PermissionDefault.OP;
	private static PermissionDefault itemDrop = PermissionDefault.OP;
	private static PermissionDefault itemCraft = PermissionDefault.OP;
	private static PermissionDefault itemHave = PermissionDefault.OP;
	private static PermissionDefault itemPickup = PermissionDefault.OP;
	private static PermissionDefault blockIgnite = PermissionDefault.OP;
	private static PermissionDefault itemHold = PermissionDefault.OP;

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		FileConfiguration conf = getConfig();
		blockBreak = getDefault(conf.getString("permission-defaults.block.break", blockBreak.name()));
		blockPlace = getDefault(conf.getString("permission-defaults.block.place", blockPlace.name()));
		blockIgnite = getDefault(conf.getString("permission-defaults.block.ignite", blockIgnite.name()));
		itemUse = getDefault(conf.getString("permission-defaults.item.use", itemUse.name()));
		itemDrop = getDefault(conf.getString("permission-defaults.item.drop", itemDrop.name()));
		itemCraft = getDefault(conf.getString("permission-defaults.item.craft", itemCraft.name()));
		itemHave = getDefault(conf.getString("permission-defaults.item.have", itemHave.name()));
		itemPickup = getDefault(conf.getString("permission-defaults.item.pickup", itemPickup.name()));
		itemHold = getDefault(conf.getString("permission-defaults.item.hold", itemHold.name()));
	}

	@Override
	public void saveConfig() {
		FileConfiguration conf = getConfig();
		conf.set("permission-defaults.block.break", blockBreak.name());
		conf.set("permission-defaults.block.place", blockPlace.name());
		conf.set("permission-defaults.block.ignite", blockIgnite.name());
		conf.set("permission-defaults.item.use", itemUse.name());
		conf.set("permission-defaults.item.drop", itemDrop.name());
		conf.set("permission-defaults.item.craft", itemCraft.name());
		conf.set("permission-defaults.item.have", itemHave.name());
		conf.set("permission-defaults.item.pickup", itemPickup.name());
		conf.set("permission-defaults.item.hold", itemHold.name());
		super.saveConfig();
	}

	private PermissionDefault getDefault(String def) {
		return PermissionDefault.getByName(def);
	}

	public PermissionRegistry getPermissions() {
		return perms;
	}

	@Override
	public void onEnable() {
		super.onEnable();
		perms = new PermissionRegistry();
		Permission perm = new Permission("en.*",PermissionDefault.OP);
		getServer().getPluginManager().addPermission(perm);
	}
	
	/**
	 * Convenience method
	 * @return Bukkit.getServer().getPluginManager()
	 */
	public static PluginManager getPluginManager()
	{
		return Bukkit.getServer().getPluginManager();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	/*
	 * Used in modify world. Helps with listener to be loaded dynamically. without extra code.
	 */
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

	/**
	 * @return the blockBreak
	 */
	public static final PermissionDefault getBlockBreak() {
		return blockBreak;
	}

	/**
	 * @return the blockPlace
	 */
	public static final PermissionDefault getBlockPlace() {
		return blockPlace;
	}

	/**
	 * @return the itemUse
	 */
	public static final PermissionDefault getItemUse() {
		return itemUse;
	}

	/**
	 * @return the itemDrop
	 */
	public static final PermissionDefault getItemDrop() {
		return itemDrop;
	}

	/**
	 * @return the itemCraft
	 */
	public static final PermissionDefault getItemCraft() {
		return itemCraft;
	}

	/**
	 * @return the itemHave
	 */
	public static final PermissionDefault getItemHave() {
		return itemHave;
	}

	/**
	 * @return the itemPickup
	 */
	public static final PermissionDefault getItemPickup() {
		return itemPickup;
	}

	/**
	 * @return the blockIgnite
	 */
	public static final PermissionDefault getBlockIgnite() {
		return blockIgnite;
	}

	/**
	 * @return the itemHold
	 */
	public static final PermissionDefault getItemHold() {
		return itemHold;
	}
}
