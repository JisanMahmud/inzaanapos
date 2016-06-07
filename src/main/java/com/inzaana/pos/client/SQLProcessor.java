package com.inzaana.pos.client;

public class SQLProcessor
{
	private String		sqlQuery			= "";
	private String		sqlQueryForInzaana	= "";
	private SQLMethod	sqlMethod			= SQLMethod.NONE;
	private DBTables	dbTableName			= DBTables.NONE;

	public SQLProcessor(String sqlQuery)
	{
		this.sqlQuery = sqlQuery;
		System.out.println(this.sqlQuery);

		findSqlMethod();
		findAndReplaceDBTable();
	}

	private void findSqlMethod()
	{
		if (sqlQuery.startsWith(SQLMethod.INSERT.ToString().toLowerCase())
				|| sqlQuery.startsWith(SQLMethod.INSERT.ToString()))
		{
			sqlMethod = SQLMethod.INSERT;
		}
		else if (sqlQuery.startsWith(SQLMethod.UPDATE.ToString().toLowerCase())
				|| sqlQuery.startsWith(SQLMethod.UPDATE.ToString()))
		{
			sqlMethod = SQLMethod.UPDATE;
		}
		else if (sqlQuery.startsWith(SQLMethod.DELETE.ToString().toLowerCase())
				|| sqlQuery.startsWith(SQLMethod.DELETE.ToString()))
		{
			sqlMethod = SQLMethod.DELETE;
		}
		else
		{
			sqlMethod = SQLMethod.NONE;
		}

		System.out.println("SQL Method : " + sqlMethod.ToString());
	}

	private void findAndReplaceDBTable()
	{
		String[] sqlParts = sqlQuery.split(" ");
		String tableName = null;
		int tableIndex = 0;

		switch (sqlMethod)
		{
			case INSERT:
			case DELETE:
			{
				tableName = sqlParts[2];
				tableIndex = 2;
			}
				break;
			case UPDATE:
			{
				tableName = sqlParts[1];
				tableIndex = 1;
			}
				break;

			default:
			{
				dbTableName = DBTables.NONE;
                                tableName = dbTableName.ToString();
			}
				break;
		}

		dbTableName = getTable(tableName);

		if (dbTableName == DBTables.NONE)
		{
			return;
		}
		
		System.out.println("Table Name : " + dbTableName.ToString());

		for (int i = 0; i < sqlParts.length; i++)
		{
			if (i == tableIndex)
			{
				sqlParts[i] += "_INZAANA";
			}
			sqlQueryForInzaana += sqlParts[i];

			if (i < (sqlParts.length - 1))
			{
				sqlQueryForInzaana += " ";
			}
		}

		System.out.println(sqlQueryForInzaana);

	}

	public boolean shouldSendToServer()
	{
		if (sqlMethod == SQLMethod.NONE || dbTableName == DBTables.NONE)
		{
			return false;
		}

		return true;
	}

	public DBTables getTable()
	{
		return dbTableName;
	}
	
	public SQLMethod getSQLMethod()
	{
		return sqlMethod;
	}
	
	public String getSQLforInzaana()
	{
		return sqlQueryForInzaana;
	}

	private DBTables getTable(String tableName)
	{
		for (DBTables table : DBTables.values())
		{
			if (tableName.equalsIgnoreCase(table.ToString()))
			{
				return table;
			}
		}

		return DBTables.NONE;
	}
}
