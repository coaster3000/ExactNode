package com.github.coaster3000.exactnode;

import java.util.concurrent.ConcurrentHashMap;


public final class PermissionRegistry {
	private ConcurrentHashMap<String,PermissionWrapper> perms = new ConcurrentHashMap<String, PermissionWrapper>();
	
}
