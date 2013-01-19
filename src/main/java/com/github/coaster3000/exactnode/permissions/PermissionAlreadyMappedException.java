package com.github.coaster3000.exactnode.permissions;

public class PermissionAlreadyMappedException extends PermissionMapException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 175947955337070791L;

	public PermissionAlreadyMappedException() {
		super("The permission already exists in map!");
	}

	/**
	 * @param arg0 - Path of permission
	 */
	public PermissionAlreadyMappedException(String arg0) {
		super("The permission '"+arg0+"' already exists in map!");
	}
	
}
