package com.github.coaster3000.exactnode.permissions;

public interface PermissionSection extends PermissionSet {
	public boolean isPermissionSection(String path);
	public PermissionSection getRoot();
	public PermissionSection getParent();
	public String getName();
}
