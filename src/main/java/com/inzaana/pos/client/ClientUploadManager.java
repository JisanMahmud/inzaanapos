package com.inzaana.pos.client;

import java.awt.image.BufferedImage;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.internal.util.Base64;

import com.inzaana.pos.models.Category;
import com.inzaana.pos.models.DataModel;
import com.inzaana.pos.utils.ImageUtil;
import com.inzaana.pos.utils.Authenticator;

public class ClientUploadManager extends Thread {

    private String BASE_URL = "http://localhost:8080/pos/";
    private String USER = "Jisan";
    private String DATA_MODEL_NAME = "NOT_FOUND";

    private DBTables dbTable = DBTables.NONE;
    private SQLMethod sqlMethod = SQLMethod.NONE;
    private String sqlForInzaana = "";
    private Object[] data = null;
    private boolean isBatchUpload = false;
    private DataModel dataModel = null;

    public ClientUploadManager(DBTables dbTable, SQLMethod sqlMethod, String sqlForInzaana, Object data) {
        this.dbTable = dbTable;
        this.sqlMethod = sqlMethod;
        this.sqlForInzaana = sqlForInzaana;
        this.data = (Object[]) data;
    }

    public ClientUploadManager() {
        isBatchUpload = true;
    }

    public void run() {
        System.out.println("thread is running...");
        startUploadProcess();
    }

    public void startUploadProcess() {
        // Write a Thread

        if (prepareTables()) {
            if (!uploadData()) {
                saveDataToDatabase();
            }
        }
    }

    private boolean prepareTables() {
        boolean success = false;

        switch (dbTable) {
            case CATEGORIES: {
                success = prepareCategoriesTable();
            }
            break;

            case PRODUCTS: {
                success = false;
            }
            break;

            case PAYMENTS: {
                success = false;
            }
            break;

            case STOCKDIARY: {
                success = false;
            }
            break;

            default: {
                success = false;
            }
            break;
        }

        return success;
    }

    private boolean prepareCategoriesTable() {
        boolean success = true;

        if (data.length < 6) {
            return false;
        }

        try {
            String id = (String) data[0];
            String name = (String) data[1];
            String parentId = (String) data[2];
            BufferedImage image = (BufferedImage) data[3];
            String textTip = (String) data[4];
            boolean catShowName = (Boolean) data[5];

            //String imageString = Base64.encodeAsString(ImageUtil.writeImage(image));
            String imageString = "Test_Image";
            dataModel = new Category(id, name, parentId, imageString, textTip, catShowName);
            DATA_MODEL_NAME = name;
        } catch (Exception e) {
            success = false;
        }

        return success;
    }

    private boolean uploadData() {

        if (dataModel == null) {
            return false;
        }

        ClientConfig config = new ClientConfig();

        Client client = ClientBuilder.newClient(config);
        client.register(new LoggingFilter());
        client.register(new Authenticator(USER, "1234"));
        Response response = null;

        switch (sqlMethod) {
            case INSERT: {
                WebTarget target = client.target(BASE_URL).path(dbTable.ToString().toLowerCase()).path(USER);
                Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.put(Entity.entity(dataModel, MediaType.APPLICATION_JSON));
            }
            break;

            case UPDATE: {
                WebTarget target = client.target(BASE_URL).path(dbTable.ToString().toLowerCase()).path(USER).path(DATA_MODEL_NAME);
                Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.post(Entity.entity(dataModel, MediaType.APPLICATION_JSON));
            }
            break;

            case DELETE: {
                WebTarget target = client.target(BASE_URL).path(dbTable.ToString().toLowerCase()).path(USER).path(DATA_MODEL_NAME);
                Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.delete();
            }
            break;

            default: {
            }
            break;
        }

        if (response == null) {
            return false;
        }
        if (response.getStatus() != 200) {
            return false;
        }
        if (response.readEntity(String.class).contains("fail")) {
            return false;
        }

        return true;
    }

    private void saveDataToDatabase() {
        if (dataModel == null) {
            return;
        }

        switch (sqlMethod) {
            case INSERT: {
                //dataModel.insertRecordIntoDB("Jisan");
            }
            break;

            case UPDATE: {
                //dataModel.updateRecordInDB("Jisan");
            }
            break;

            case DELETE: {

            }
            break;

            default: {

            }
            break;
        }

    }

    private void startBatchUpload() {

    }
}
