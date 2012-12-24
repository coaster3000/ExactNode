package com.github.coaster3000.exactnode;


import org.bukkit.plugin.java.JavaPlugin;


public class ExactNode extends JavaPlugin {
	private static boolean useCustomWildcards = true;
	private static boolean useDataNodes = true;
	
	public static boolean useCustomWildcards(){return useCustomWildcards;}
	public static boolean useDataNodes(){return useDataNodes;}
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
}
