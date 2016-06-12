package com.inzaana.pos.client;

public enum DBTables
{
	CATEGORIES("CATEGORIES"), //
	PRODUCTS("PRODUCTS"), //
//	PAYMENTS("PAYMENTS"), //
	STOCKDIARY("STOCKDIARY"), //
//	USERS("USERS"), //
	NONE("NONE"); //

	String	tableName;

	DBTables(String tableName)
	{
		this.tableName = tableName;
	}

	String ToString()
	{
		return this.tableName;
	}

}
