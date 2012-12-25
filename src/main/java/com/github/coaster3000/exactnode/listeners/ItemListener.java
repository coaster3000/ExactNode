package com.github.coaster3000.exactnode.listeners;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;

import com.github.coaster3000.exactnode.ExactNodeListener;
import com.github.coaster3000.exactnode.PlayerInformer;

public class ItemListener extends ExactNodeListener {
	public static enum Method {
		CLICK, DROP, PICKUP;
		@Override
		public String toString() {
			switch (this) {
			case DROP:
				return "drop";
			case PICKUP:
				return "pickup";
			case CLICK:
			default:
				return "click";
			}
		}
		public static Method toEnum(String value)
		{
			if (value.equalsIgnoreCase("drop"))
				return Method.DROP;
			else if (value.equalsIgnoreCase("pickup"))
				return Method.PICKUP;
			else if (value.equalsIgnoreCase("click"))
				return Method.CLICK;
			else
				return null;
		}
	}

	public ItemListener(Plugin plugin, ConfigurationSection config, PlayerInformer informer) {
		super(plugin, config, informer);
	}
	@EventHandler(ignoreCancelled=true,priority=EventPriority.LOW)
	public void onItemPickup(PlayerPickupItemEvent event)
	{
		Player player = event.getPlayer();
		
	}
}
