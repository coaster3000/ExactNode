package com.github.coaster3000.exactnode.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import com.github.coaster3000.exactnode.ExactNodeListener;
import com.github.coaster3000.exactnode.PlayerInformer;

public class ItemListener extends ExactNodeListener {
	private static final String NODE = "items";
	private boolean itemPickupCheck = true;
	private boolean itemDropCheck = true;
	private boolean itemHaveCheck = true;
	private boolean itemUseCheck = true;
	private boolean fullscan = false;
	private Method method = Method.CLICK;

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

		public static Method toEnum(String value) {
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
		method = Method.toEnum(config.getString("methods.item-permissions", "click"));
		fullscan = config.getBoolean("methods.item-fullscan", fullscan);
		itemPickupCheck = config.getBoolean("checks.items.pickup",itemPickupCheck);
		itemDropCheck = config.getBoolean("checks.items.drop",itemDropCheck);
		itemHaveCheck = config.getBoolean("checks.items.have",itemHaveCheck);
		itemUseCheck = config.getBoolean("checks.items.use",itemUseCheck);
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onItemUse(PlayerInteractEvent event)
	{
		ItemStack item = event.getItem();
		Player player = event.getPlayer();
		if (item == null)
			return;
		if (itemHaveCheck&&!fullscan)
		{
			
			if (!canHaveItem(player, item))
			{
				PlayerInventory inv = player.getInventory();
				if (canDropItem(player, item))
					dropItem(player.getLocation(), inv.getItemInHand());
				inv.setItemInHand(null);
			}
		}
			
		
		
		if (!itemUseCheck||event.useItemInHand().equals(Event.Result.DENY))
			return;
		
		if (!canUseItem(player, item))
		{
			event.setUseItemInHand(Result.DENY);
			event.setCancelled(true);
		}
	}
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onItemDrop(PlayerDropItemEvent event)
	{
		if (!itemDropCheck)
			return;
		
		Player player = event.getPlayer();
		
		if (!canDropItem(player, event.getItemDrop().getItemStack()))
			event.setCancelled(true);
		
		if (event.isCancelled()&&informPlayers)
			return; //FIXME: Add informer shit.
		
		if (method.equals(Method.DROP) && fullscan&&itemHaveCheck)
			checkInventory(player);
		
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (!itemPickupCheck)
			return;
		Player player = event.getPlayer();//TODO: Finish onItemPickup event

		if (!canHaveItem(player,event.getItem().getItemStack()))
			event.setCancelled(true);
		if (!canPickUpItem(player, event.getItem().getItemStack()))
			event.setCancelled(true);
		
		if (method.equals(Method.PICKUP) && fullscan&&itemHaveCheck)
			checkInventory(player);
		
		if (event.isCancelled())
			return; //FIXME: Add informer shit
	}
	

	public void checkInventory(Player player) {
		WeakHashMap<String, Boolean> cache = new WeakHashMap<String, Boolean>();
		ItemStack[] contents = player.getInventory().getContents();
		for (int i = 0; i < contents.length; i++) {
			if (contents[i] == null)
				continue;
			ItemStack item = contents[i];
			String t = item.getTypeId() + ":" + item.getData().getData();
			if (cache.containsKey(t)) {
				if (!cache.get(t)) {
					if (canDropItem(player, item))
						dropItem(player.getLocation(), item);
					contents[i] = null;
				}
			} else {
				boolean a = canHaveItem(player, item);
				cache.put(t, a);
				if (!a) {
					if (canDropItem(player, item))
						dropItem(player.getLocation(), item);
					contents[i] = null;
				}
			}

		}
	}

	private void dropItem(Location location, ItemStack stack) {
		location.getWorld().dropItemNaturally(location, stack);
	}

	private boolean canDropItem(Player player, ItemStack item) {
		List<String> perms = new ArrayList<String>();
		perms.add(ROOTNODE + "." + NODE + ".drop." + getItemPermission(item));
		if (useWildNode) {
			perms.add(ROOTNODE + "." + NODE + ".drop." + WILDCARD);
			perms.add(ROOTNODE + "." + NODE + "." + WILDCARD + "." + getItemPermission(item));
			perms.add(ROOTNODE + "." + NODE + "." + WILDCARD);
			perms.add(ROOTNODE + "." + WILDCARD);
			perms.add(WILDCARD);
		}
		return !(permissionDenied(player, perms.toArray(new String[0])));
	}

	private boolean canHaveItem(Player player, ItemStack item) {
		List<String> perms = new ArrayList<String>();
		perms.add(ROOTNODE + "." + NODE + ".have." + getItemPermission(item));
		if (useWildNode) {
			perms.add(ROOTNODE + "." + NODE + ".have." + WILDCARD);
			perms.add(ROOTNODE + "." + NODE + "." + WILDCARD + "." + getItemPermission(item));
			perms.add(ROOTNODE + "." + NODE + "." + WILDCARD);
			perms.add(ROOTNODE + "." + WILDCARD);
			perms.add(WILDCARD);
		}
		return !(permissionDenied(player, perms.toArray(new String[0])));
	}
	private boolean canPickUpItem(Player player, ItemStack item) {
		if (!canHaveItem(player, item))
			return false;
		List<String> perms = new ArrayList<String>();
		perms.add(ROOTNODE + "." + NODE + ".pickup." + getItemPermission(item));
		if (useWildNode) {
			perms.add(ROOTNODE + "." + NODE + ".pickup." + WILDCARD);
			perms.add(ROOTNODE + "." + NODE + "." + WILDCARD + "." + getItemPermission(item));
			perms.add(ROOTNODE + "." + NODE + "." + WILDCARD);
			perms.add(ROOTNODE + "." + WILDCARD);
			perms.add(WILDCARD);
		}
		return !(permissionDenied(player, perms.toArray(new String[0])));
	}
	private boolean canUseItem(Player player, ItemStack item) {
		List<String> perms = new ArrayList<String>();
		perms.add(ROOTNODE + "." + NODE + ".use." + getItemPermission(item));
		if (useWildNode) {
			perms.add(ROOTNODE + "." + NODE + ".use." + WILDCARD);
			perms.add(ROOTNODE + "." + NODE + "." + WILDCARD + "." + getItemPermission(item));
			perms.add(ROOTNODE + "." + NODE + "." + WILDCARD);
			perms.add(ROOTNODE + "." + WILDCARD);
			perms.add(WILDCARD);
		}
		return !(permissionDenied(player, perms.toArray(new String[0])));
	}

}
