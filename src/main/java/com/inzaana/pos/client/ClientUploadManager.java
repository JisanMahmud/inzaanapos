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
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.forms.AppConfig;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppViewConnection;
import com.openbravo.pos.forms.InzaanaSplash;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class ClientUploadManager extends Thread {

    private String DATA_MODEL_ID = "NOT_FOUND";

    private DBTables dbTable = DBTables.NONE;
    private SQLMethod sqlMethod = SQLMethod.NONE;
    private String sqlForInzaana = "";
    private Object[] data = null;
    private boolean isBatchUpload = false;
    private DataModel dataModel = null;
    
    private Session s;
    private Connection con;
    private PreparedStatement pstmt;
    private String SQL;
    private ResultSet rs;

    public ClientUploadManager(DBTables dbTable, SQLMethod sqlMethod, String sqlForInzaana, Object data) {
        this.dbTable = dbTable;
        this.sqlMethod = sqlMethod;
        this.sqlForInzaana = sqlForInzaana;
        this.data = (Object[]) data;
        
        prepareSession();
    }

    public ClientUploadManager() {
        isBatchUpload = true;
    }

    public void run() {
        System.out.println("thread is running...");
        startUploadProcess();
    }

    public void startUploadProcess() {
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
            DATA_MODEL_ID = id;
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

        Preferences preference = Preferences.userRoot();
        String userId = preference.get(InzaanaSplash.INZAANA_USER_ID_KEY, "NOT_FOUND");
        String userName = preference.get(InzaanaSplash.INZAANA_USER_NAME_KEY, "NOT_FOUND");
        String userPassword = preference.get(InzaanaSplash.INZAANA_SECURITY_KEY, "NOT_FOUND");
        String baseUrl = preference.get(InzaanaSplash.INZAANA_URL_KEY, "NOT_FOUND");
        
        Client client = ClientBuilder.newClient(config);
        client.register(new LoggingFilter());
        client.register(new Authenticator(userId, userPassword));
        Response response = null;

        switch (sqlMethod) {
            case INSERT: {
                WebTarget target = client.target(baseUrl).path(dbTable.ToString().toLowerCase()).path(userName);
                Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.put(Entity.entity(dataModel, MediaType.APPLICATION_JSON));
            }
            break;

            case UPDATE: {
                WebTarget target = client.target(baseUrl).path(dbTable.ToString().toLowerCase()).path(userName);
                Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.post(Entity.entity(dataModel, MediaType.APPLICATION_JSON));
            }
            break;

            case DELETE: {
                WebTarget target = client.target(baseUrl).path(dbTable.ToString().toLowerCase()).path(userName).path(DATA_MODEL_ID);
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
    
    private void prepareSession()
    {
        AppConfig m_config =  new AppConfig(new File((System.getProperty("user.home")), AppLocal.APP_ID + ".properties"));        
        m_config.load();
        
        try {
            s = AppViewConnection.createSession(m_config);
            con = s.getConnection();
        } catch (BasicException e) {
            System.out.println(e);
            JMessageDialog.showMessage(null, new MessageInf(MessageInf.SGN_DANGER, e.getMessage(), e));
        } catch (SQLException ex) {
            Logger.getLogger(InzaanaSplash.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void saveDataToDatabase() {
        
        if (con == null) {
            return;
        }
        try {
            pstmt = con.prepareStatement(sqlForInzaana);
            for (int i=0; i < data.length; i++)
            {
                pstmt.setObject(i+1, data[i]);
            }
            pstmt.executeUpdate();
        } catch (Exception e) {
        }
    }

    private void startBatchUpload() {

    }
}
