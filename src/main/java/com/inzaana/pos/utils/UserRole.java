package com.inzaana.pos.utils;

/*
 * Users will have different Roles and according 
 * to the Role the access level will be defined.
 */
public enum UserRole {
	GUEST("GUEST"), // GUEST will have no access to any resource.
	ADMIN("ADMIN"), // ADMIN will have all access to all resources and all methods.
	POS("POS"), // POS will have access to Inventory and Sales DB with GET, PUT and POST method.
	USER("USER"); // USER will have access to Inventory and Sales DB with only GET method.  

	String sUserRole;

	UserRole(String sRole) {
		sUserRole = sRole;
	}

	String ToString() {
		return sUserRole;
	}
}
