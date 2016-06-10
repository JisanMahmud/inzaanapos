/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inzaana.pos.client;

import com.inzaana.pos.models.DataModel;

/**
 *
 * @author User
 */
public class TableManager {
    private String dataModelId = "NOT_FOUND";
    private DBTables dbTable = DBTables.NONE;
    private Object[] data = null;
    private DataModel dataModel = null;
    
    
    public TableManager(DBTables dbTable, Object data, DataModel dataModel, String id)
    { 
    }
}