package com.github.coaster3000.exactnode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ExactNode extends JavaPlugin {
	
	protected PlayerInformer informer;
	protected List<ExactNodeListener> listeners = new ArrayList<ExactNodeListener>();
	protected final static Class<? extends ExactNodeListener>[] LISTENERS = new Class[] {};
	protected FileConfiguration config;
	protected File configFile;

	@Override
	public void onLoad() {
		configFile = new File(this.getDataFolder(), "config.yml");
	}

	@Override
	public void onEnable() {
		this.config = this.getConfig();

		if (!config.isConfigurationSection("messages")) {
			this.getLogger().severe("Deploying default config");
			this.initializeConfiguration(config);
		}

		this.saveConfig();
	}

	@Override
	public void onDisable() {
		this.listeners.clear();
		this.config = null;

	}

	@Override
	public void saveConfig() {
		try {
			this.config.save(configFile);
		} catch (IOException e) {
			this.getLogger().severe("Failed to save configuration file: " + e.getMessage());
		}
	}

	protected void initializeConfiguration(FileConfiguration config) {
		// Flags

		// Messages

		// Predefined messages
	}

	@Override
	public FileConfiguration getConfig() {
		if (this.config == null) {
			this.reloadConfig();
		}

		return this.config;
	}

	protected void registerListeners() {
		for (Class listenerClass : LISTENERS) {
			try {
				Constructor constructor = listenerClass.getConstructor(Plugin.class, ConfigurationSection.class, PlayerInformer.class);
				ExactNodeListener listener = (ExactNodeListener) constructor.newInstance(this, this.getConfig(), this.informer);
				this.listeners.add(listener);
			} catch (Throwable e) {
				this.getLogger().warning("Failed to initialize \"" + listenerClass.getName() + "\" listener");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void reloadConfig() {
		this.config = new YamlConfiguration();
		config.options().pathSeparator('/');

		try {
			config.load(configFile);
		} catch (FileNotFoundException e) {
			this.getLogger().severe("Configuration file not found - deploying default one");
			InputStream defConfigStream = getResource("config.yml");
			if (defConfigStream != null) {
				try {
					this.config.load(defConfigStream);
				} catch (Exception de) {
					this.getLogger().severe("Default config file is broken. Please tell this to Modifyworld author.");
				}
			}
		} catch (Exception e) {
			this.getLogger().severe("Failed to load configuration file: " + e.getMessage());
		}

		InputStream defConfigStream = getResource("config.yml");
		if (defConfigStream != null) {
			this.config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
		}
	}
}
