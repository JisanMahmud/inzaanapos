package com.inzaana.pos.utils;

public enum InzaanaDBTables
{
	CATEGORIES("CATEGORIES_INZAANA"), //
	PRODUCTS("PRODUCTS_INZAANA"), //
	PAYMENTS("Payment_INZAANA"), //
	STOCKDIARY("STOCKDIARY_INZAANA"), //
	USERS("USERS_INZAANA"); //

	String	tableName;

	InzaanaDBTables(String tableName)
	{
		this.tableName = tableName;
	}

        @Override
	public String toString()
	{
		return this.tableName;
	}

}
