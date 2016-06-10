package com.inzaana.pos.models;

public abstract class DataModel
{

	public abstract boolean insertRecordIntoDB(String userID);
	public abstract boolean updateRecordInDB(String userID);
        public abstract String getId();
}
