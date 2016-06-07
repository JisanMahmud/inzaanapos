package com.inzaana.pos.client;

public enum SQLMethod
{
	INSERT("INSERT INTO"), //
	UPDATE("UPDATE"), //
	DELETE("DELETE"), //
	SELECT("SELECT"), //
	NONE("NONE");

	String	sqlMethod;

	SQLMethod(String sqlMethod)
	{
		this.sqlMethod = sqlMethod;
	}

	String ToString()
	{
		return this.sqlMethod;
	}
}
