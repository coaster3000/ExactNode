package com.github.coaster3000.exactnode.listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;

import com.github.coaster3000.exactnode.ExactNode;
import com.github.coaster3000.exactnode.ExactNodeListener;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.permissions.Permission;

public class BlockListeners extends ExactNodeListener {

	protected final boolean blockPlaceCheck, blockBreakCheck, blockIgniteCheck;
	protected final boolean notifyPlace, notifyBreak, notifyIgnite;
	protected final String placeMsg, breakMsg, igniteMsg;

	public BlockListeners(ExactNode plugin, ConfigurationSection config) {
		super(plugin, config);
		blockPlaceCheck = config.getBoolean("checks.block.place", true);
		blockBreakCheck = config.getBoolean("checks.block.break", true);
		blockIgniteCheck = config.getBoolean("checks.block.ignite", true);
		placeMsg = config.getString("messages.block.place", "You are not allowed to place \"%block\"");
		breakMsg = config.getString("messages.block.break", "You are not allowed to break \"%block\"");
		igniteMsg = config.getString("messages.block.ignite", "You are not allowed to ignite \"%block\"");
		notifyBreak = config.getBoolean("notify.block.break", true);
		notifyIgnite = config.getBoolean("notify.block.ignite", true);
		notifyPlace = config.getBoolean("notify.block.place", true);
	}
	private void checkBreak(int id, byte data) {
		String check = "en.block.break.*";
		String check2 = "en.block.break."+id+":*";
		String perm = "en.block.break."+id+((data > 0)?":"+data:"");
		
		if (!perms.hasPermissions(check))
		{
			Permission permission = new Permission(check, "Gives access to break all blocks",ExactNode.getBlockBreak());
			ExactNode.getPluginManager().addPermission(permission);
			perms.addPermission(permission);
		}
		
		if (!perms.hasPermissions(check2))
		{
			Permission permission = new Permission(check2, "Gives all node access to all metadata of the specified id", ExactNode.getBlockBreak());
			permission.addParent(ExactNode.getPluginManager().getPermission(check), true);
			ExactNode.getPluginManager().addPermission(permission);
			perms.addPermission(perm);
		}
		if (!perms.hasPermissions(perm))
		{
			Permission permission = new Permission(perm,"Gives access to exact item with data value.",ExactNode.getBlockBreak());
			permission.addParent(ExactNode.getPluginManager().getPermission(check2),true);
			ExactNode.getPluginManager().addPermission(permission);
			perms.addPermission(permission);
		}		
	}
	
	private void checkIgnite(int id,byte data)
	{
		String perm = "en.block.ignite."+id + ((data > 0)?":"+data:"");
		String check2 = "en.block.ignite."+id+":*";
		String check = "en.block.ignite.*";
		
		if (!perms.hasPermissions(check))
		{
			Permission permission = new Permission(check, "Gives access to ignite all blocks",ExactNode.getBlockIgnite());
			ExactNode.getPluginManager().addPermission(permission);
			perms.addPermission(permission);
			
		}
		
		if (!perms.hasPermissions(check2))
		{
			Permission permission = new Permission(check2, "Gives all node access to all metadata of the specified id", ExactNode.getBlockIgnite());
			permission.addParent(ExactNode.getPluginManager().getPermission("en.block.ignite.*"), true);
			ExactNode.getPluginManager().addPermission(permission);
			perms.addPermission(perm);
		}
		if (!perms.hasPermissions(perm))
		{
			Permission permission = new Permission(perm,"Gives access to exact item with data value.",ExactNode.getBlockIgnite());
			permission.addParent(ExactNode.getPluginManager().getPermission(check),true);
			ExactNode.getPluginManager().addPermission(permission);
			perms.addPermission(permission);
		}
	}
	private void checkPlace(int id, byte data) {
		String check = "en.block.place.*";
		String check2 = "en.block.place."+id+":*";
		String perm = "en.block.place."+id+((data > 0)?":"+data:"");
		if (!perms.hasPermissions(check))
		{
			Permission permission = new Permission(check, "Gives access to place all blocks",ExactNode.getBlockPlace());
			ExactNode.getPluginManager().addPermission(permission);
			perms.addPermission(permission);
		}
		
		if (!perms.hasPermissions(check2))
		{
			Permission permission = new Permission(check2, "Gives all node access to all metadata of the specified id", ExactNode.getBlockPlace());
			permission.addParent(ExactNode.getPluginManager().getPermission(check), true);
			ExactNode.getPluginManager().addPermission(permission);
			perms.addPermission(perm);
		}
		if (!perms.hasPermissions(perm))
		{
			Permission permission = new Permission(perm,"Gives access to exact item with data value.",ExactNode.getBlockPlace());
			permission.addParent(ExactNode.getPluginManager().getPermission(check2),true);
			ExactNode.getPluginManager().addPermission(permission);
			perms.addPermission(permission);
		}
		
	}

	@EventHandler(ignoreCancelled = true,priority=EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent event) 
	{
		if (!blockBreakCheck)
			return;
		
		Player player = event.getPlayer();
		if (player == null)
			return;
		
		Block block = event.getBlock();
		
		int id = block.getTypeId();
		byte data = block.getData();
		String perm = "en.block.break."+id+((data > 0)?":"+data:"");
		
		checkBreak(id,data);
		if (!hasPermission(player,perm))
		{
			event.setCancelled(true);
			if (notifyBreak)
			{
				StringBuffer buff = new StringBuffer();
				String msg = buff.append(ChatColor.DARK_RED.toString()).append(breakMsg).toString();
				msg = msg.replaceAll("%block", "block : \""+id+((data > 0)?":"+data:""));
				player.sendMessage(msg);
			}
		}
		
	}
	@EventHandler(ignoreCancelled = true,priority=EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (!blockPlaceCheck)
			return;
		Player player = event.getPlayer();
		if (player == null)
			return;
		
		Block block = event.getBlockPlaced();
		int id = block.getTypeId();
		byte data = block.getData();
		
		String perm = "en.block.place."+id+((data > 0)?":"+data:"");
		checkPlace(id,data);
		if (!hasPermission(player,perm))
		{
			event.setCancelled(true);
			if (notifyPlace)
			{
				StringBuffer buff = new StringBuffer();
				String msg = buff.append(ChatColor.DARK_RED.toString()).append(placeMsg).toString();
				msg = msg.replaceAll("%block", "block : \""+id+((data > 0)?":"+data:""));
				player.sendMessage(msg);
			}
		}		
	}
	@EventHandler(ignoreCancelled = true,priority=EventPriority.LOW)
	public void onIgnite(BlockIgniteEvent event)
	{
		if (event.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL||!blockIgniteCheck)
			return;
		
		Player p = event.getPlayer();
		if (p == null)
			return;
		
		Block block = event.getBlock();
		
		int id = block.getTypeId();
		byte data = block.getData();
		
		checkIgnite(id, data);
		String perm = "en.block.ignite."+id + ((data > 0)?":"+data:"");
		if (!hasPermission(p,perm))
		{
			event.setCancelled(true);
			if (notifyIgnite)
			{
				StringBuffer buff = new StringBuffer();
				String msg = buff.append(ChatColor.DARK_RED.toString()).append(igniteMsg).toString();
				msg = msg.replaceAll("%block", "block : \""+id+((data > 0)?":"+data:""));
				p.sendMessage(msg);
			}
		}
	}
}