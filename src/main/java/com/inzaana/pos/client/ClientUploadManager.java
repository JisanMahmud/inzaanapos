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

import com.inzaana.pos.models.Category;
import com.inzaana.pos.models.DataModel;
import com.inzaana.pos.models.Payment;
import com.inzaana.pos.models.Product;
import com.inzaana.pos.models.StockDiary;
import com.inzaana.pos.utils.Authenticator;
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
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.springframework.util.StringUtils;

public class ClientUploadManager extends Thread {

    private String DATA_MODEL_ID = "NOT_FOUND";

    private DBTables dbTable = DBTables.NONE;
    private SQLMethod sqlMethod = SQLMethod.NONE;
    private String sqlForInzaana = "";
    private Object[] data = null;
    private boolean isBatchUpload = false;
    private DataModel dataModel = null;

    AppConfig m_config;

    private int batchUploadSuccessCount;
    private int batchUploadFailureCount;

    public ClientUploadManager(DBTables dbTable, SQLMethod sqlMethod, String sqlForInzaana, Object data) {
        this.dbTable = dbTable;
        this.sqlMethod = sqlMethod;
        this.sqlForInzaana = sqlForInzaana;
        this.data = (Object[]) data;

        prepareAppConfig();
    }

    public ClientUploadManager() {
        prepareAppConfig();
    }

    public void run() {
        System.out.println("thread is running...");
        startUploadProcess();
    }

    public void startUploadProcess() {
        if (prepareTables()) {
            if (!uploadData()) {
                if (!isBatchUpload) {
                    saveDataToDatabase();
                } else {
                    increaseBatchUploadFailureCount();
                }
            } else if (isBatchUpload) {
                deleteDataFromDatabase();
            }
        }
    }

    private boolean prepareTables() {
        boolean success = false;
        if (isBatchUpload) {
            return true; // because we have already prepared the table.
        }
        switch (dbTable) {
            case CATEGORIES: {
                success = prepareCategoriesTable();
            }
            break;

            case PRODUCTS: {
                success = prepareProductsTable();
            }
            break;

            case PAYMENTS: {
                success = preparePaymentsTable();
            }
            break;

            case STOCKDIARY: {
                success = prepareStockDiaryTable();
            }
            break;

            default: {
                success = false;
            }
            break;
        }

        return success;
    }

    private boolean prepareDataModel(ResultSet resultSet) {
        boolean success = false;

        switch (dbTable) {
            case CATEGORIES: {
                success = prepareCategoriesDataModel(resultSet);
            }
            break;

            case PRODUCTS: {
                success = prepareProductsDataModel(resultSet);
            }
            break;

            case PAYMENTS: {
                success = preparePaymentsDataModel(resultSet);
            }
            break;

            case STOCKDIARY: {
                success = prepareStockDiaryDataModel(resultSet);
            }
            break;

            default: {
                success = false;
            }
            break;
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
                try {
                    response = invocationBuilder.put(Entity.entity(dataModel, MediaType.APPLICATION_JSON));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;

            case UPDATE: {
                WebTarget target = client.target(baseUrl).path(dbTable.ToString().toLowerCase()).path(userName);
                Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
                try {
                    response = invocationBuilder.post(Entity.entity(dataModel, MediaType.APPLICATION_JSON));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;

            case DELETE: {
                WebTarget target = client.target(baseUrl).path(dbTable.ToString().toLowerCase()).path(userName).path(DATA_MODEL_ID);
                Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
                try {
                    response = invocationBuilder.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void prepareAppConfig() {
        m_config = new AppConfig(new File((System.getProperty("user.home")), AppLocal.APP_ID + ".properties"));
        m_config.load();
    }

    private void saveDataToDatabase() {
        int occurance = StringUtils.countOccurrencesOf(sqlForInzaana, "?");
        Session session_1 = null;
        Connection dbConnection_1 = null;
        PreparedStatement pStatement_1 = null;

        try {
            session_1 = AppViewConnection.createSession(m_config);
            dbConnection_1 = session_1.getConnection();
            pStatement_1 = dbConnection_1.prepareStatement(sqlForInzaana);

            for (int i = 0; i < occurance; i++) {

                if ((sqlMethod == sqlMethod.UPDATE || sqlMethod == sqlMethod.DELETE) && i == (occurance - 1)) {
                    pStatement_1.setObject(i + 1, DATA_MODEL_ID);
                } else {
                    pStatement_1.setObject(i + 1, data[i]);
                }
            }
            pStatement_1.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStatement_1 != null) {
                    pStatement_1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (dbConnection_1 != null) {
                    dbConnection_1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (session_1 != null) {
                session_1.close();
            }
        }

        String sqlUpdate = "UPDATE " + dbTable.ToString() + "_INZAANA SET sqlmethod=? WHERE ID=?";
        Session session_2 = null;
        Connection dbConnection_2 = null;
        PreparedStatement pStatement_2 = null;

        try {
            session_2 = AppViewConnection.createSession(m_config);
            dbConnection_2 = session_2.getConnection();
            pStatement_2 = dbConnection_2.prepareStatement(sqlUpdate);
            pStatement_2.setObject(1, sqlMethod.ToString());
            pStatement_2.setObject(2, DATA_MODEL_ID);
            pStatement_2.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pStatement_2 != null) {
                    pStatement_2.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                if (dbConnection_2 != null) {
                    dbConnection_2.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (session_2 != null) {
                session_2.close();
            }
        }
    }

    private boolean deleteDataFromDatabase() {
        String sql = "DELETE FROM " + dbTable.ToString() + "_INZAANA" + " WHERE ID=?;";
        Session session = null;
        Connection dbConnection = null;
        PreparedStatement pStatement = null;
        try {
            session = AppViewConnection.createSession(m_config);
            dbConnection = session.getConnection();
            pStatement = dbConnection.prepareStatement(sql);
            pStatement.setObject(1, DATA_MODEL_ID);
            pStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            increaseBatchUploadFailureCount();
            return false;
        } finally {
            try {
                if (pStatement != null) {
                    pStatement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (session != null) {
                session.close();
            }
        }

        increaseBatchUploadSuccessCount();

        return false;
    }

    private void increaseBatchUploadSuccessCount() {
        batchUploadSuccessCount++;
    }

    private void increaseBatchUploadFailureCount() {
        batchUploadFailureCount++;
    }

    public void startBatchUpload() {
        isBatchUpload = true;
        batchUploadSuccessCount = 0;
        batchUploadFailureCount = 0;

        for (DBTables table : DBTables.values()) {
            this.dbTable = table;

            String sql = "SELECT * FROM " + dbTable.ToString() + "_INZAANA;";
            Session session = null;
            Connection dbConnection = null;
            PreparedStatement pStatement = null;
            ResultSet resultSet = null;

            try {
                session = AppViewConnection.createSession(m_config);
                dbConnection = session.getConnection();
                pStatement = dbConnection.prepareStatement(sql);

                pStatement = dbConnection.prepareStatement(sql);
                resultSet = pStatement.executeQuery();

                while (resultSet.next()) {
                    if (prepareDataModel(resultSet)) {
                        startUploadProcess();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    pStatement.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
                }

                session.close();
            }
        }

    }

    private SQLMethod getSqlMethod(String method) {
        for (SQLMethod sqlMethod : SQLMethod.values()) {
            if (method.equalsIgnoreCase(sqlMethod.ToString())) {
                return sqlMethod;
            }
        }

        return sqlMethod.NONE;
    }

////////////////////////////////////////////////////////////////////////////////////////////
    private boolean prepareCategoriesDataModel(ResultSet resultSet) {
        try {
            String id = resultSet.getString(Category.ID);
            String name = resultSet.getString(Category.NAME);
            String parentId = resultSet.getString(Category.PARENTID);
            String imageString = resultSet.getString(Category.IMAGE);
            String textTip = resultSet.getString(Category.TEXTTIP);
            boolean catShowName = resultSet.getBoolean(Category.CATSHOWNAME);
            sqlMethod = getSqlMethod(resultSet.getString("SQLMETHOD"));

            dataModel = new Category(id, name, parentId, imageString, textTip, catShowName);
            DATA_MODEL_ID = id;

        } catch (SQLException ex) {
            Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    private boolean prepareProductsDataModel(ResultSet resultSet) {
        try {
            String id = resultSet.getString(Product.ID);
            String reference = resultSet.getString(Product.REFERENCE);
            String code = resultSet.getString(Product.CODE);
            String codetype = resultSet.getString(Product.CODETYPE);
            String name = resultSet.getString(Product.NAME);
            double priceBuy = resultSet.getDouble(Product.PRICEBUY);
            double priceSell = resultSet.getDouble(Product.PRICESELL);
            String category = resultSet.getString(Product.CATEGORY);
            String taxcat = resultSet.getString(Product.TAXCAT);
            String attributeset_id = resultSet.getString(Product.ATTRIBUTESET_ID);
            double stockCost = resultSet.getDouble(Product.STOCKCOST);
            double stockVolume = resultSet.getDouble(Product.STOCKVOLUME);
            BufferedImage image = (BufferedImage) resultSet.getObject(Product.IMAGE);
            boolean iscom = resultSet.getBoolean(Product.ISCOM);
            boolean isScale = resultSet.getBoolean(Product.ISSCALE);
            boolean isKitchen = resultSet.getBoolean(Product.ISKITCHEN);
            boolean printkb = resultSet.getBoolean(Product.PRINTKB);
            boolean sendStatus = resultSet.getBoolean(Product.SENDSTATUS);
            boolean isService = resultSet.getBoolean(Product.ISSERVICE);
            String attributes = resultSet.getString(Product.ATTRIBUTES);
            String display = resultSet.getString(Product.DISPLAY);
            boolean isVPrice = resultSet.getBoolean(Product.ISVPRICE);
            boolean isVerpatrib = resultSet.getBoolean(Product.ISVERPATRIB);
            String textTip = resultSet.getString(Product.TEXTTIP);
            boolean warranty = resultSet.getBoolean(Product.WARRANTY);
            double stockunits = resultSet.getDouble(Product.STOCKUNITS);
            sqlMethod = getSqlMethod(resultSet.getString("SQLMETHOD"));

            //String imageString = Base64.encodeAsString(ImageUtil.writeImage(image));
            String imageString = "Test_Image";

            dataModel = new Product(id, reference, code, codetype, name, priceBuy, priceSell, category, taxcat, attributeset_id, stockCost, stockVolume, imageString, iscom, isScale, isKitchen, printkb, sendStatus, isService, attributes, display, isVPrice, isVerpatrib, textTip, warranty, stockunits);
            DATA_MODEL_ID = id;

        } catch (SQLException ex) {
            Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    private boolean preparePaymentsDataModel(ResultSet resultSet) {
        try {
            String id = resultSet.getString(Payment.ID);
            String receipt = resultSet.getString(Payment.RECEIPT);
            String payment = resultSet.getString(Payment.PAYMENT);
            double total = resultSet.getDouble(Payment.TOTAL);
            String transId = resultSet.getString(Payment.TRANSID);
            String returnMsg = resultSet.getString(Payment.RETURNMSG);
            String notes = resultSet.getString(Payment.NOTES);
            double tendered = resultSet.getDouble(Payment.TENDERED);
            String cardName = resultSet.getString(Payment.CARDNAME);

            sqlMethod = getSqlMethod(resultSet.getString("SQLMETHOD"));
            dataModel = new Payment(id, receipt, payment, total, transId, returnMsg, notes, tendered, cardName);
            DATA_MODEL_ID = id;

        } catch (SQLException ex) {
            Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    private boolean prepareStockDiaryDataModel(ResultSet resultSet) {
        try {
            String id = resultSet.getString(StockDiary.ID);
            Date time = resultSet.getDate(StockDiary.DATANEW);
            int reason = resultSet.getInt(StockDiary.REASON);
            String location = resultSet.getString(StockDiary.LOCATION);
            String product = resultSet.getString(StockDiary.PRODUCT);
            String attributeSetInstance_id = resultSet.getString(StockDiary.ATTRIBUTESETINSTANCE_ID);
            double units = resultSet.getDouble(StockDiary.UNITS);
            double price = resultSet.getDouble(StockDiary.PRICE);
            String appUser = resultSet.getString(StockDiary.APPUSER);

            String dateNew = time.toString();
            sqlMethod = getSqlMethod(resultSet.getString("SQLMETHOD"));
            dataModel = new StockDiary(id, dateNew, reason, location, product, attributeSetInstance_id, units, price, appUser);
            DATA_MODEL_ID = id;

        } catch (SQLException ex) {
            Logger.getLogger(ClientUploadManager.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////////
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

    private boolean prepareProductsTable() {
        boolean success = true;

        if (data.length < 26) {
            return false;
        }

        try {
            String id = (String) data[0];
            String reference = (String) data[1];
            String code = (String) data[2];
            String codetype = (String) data[3];
            String name = (String) data[4];
            double priceBuy = (Double) data[5];
            double priceSell = (Double) data[6];
            String category = (String) data[7];
            String taxcat = (String) data[8];
            String attributeset_id = (String) data[9];
            double stockCost = (Double) data[10];
            double stockVolume = (Double) data[11];
            BufferedImage image = (BufferedImage) data[12];
            boolean iscom = (Boolean) data[13];
            boolean isScale = (Boolean) data[14];
            boolean isKitchen = (Boolean) data[15];
            boolean printkb = (Boolean) data[16];
            boolean sendStatus = (Boolean) data[17];
            boolean isService = (Boolean) data[18];
            String attributes = (String) data[19];
            String display = (String) data[20];
            boolean isVPrice = (Boolean) data[21];
            boolean isVerpatrib = (Boolean) data[22];
            String textTip = (String) data[23];
            boolean warranty = (Boolean) data[24];
            double stockunits = (Double) data[25];

            //String imageString = Base64.encodeAsString(ImageUtil.writeImage(image));
            String imageString = "Test_Image";
            dataModel = new Product(id, reference, code, codetype, name, priceBuy, priceSell, category, taxcat, attributeset_id, stockCost, stockVolume, imageString, iscom, isScale, isKitchen, printkb, sendStatus, isService, attributes, display, isVPrice, isVerpatrib, textTip, warranty, stockunits);
            DATA_MODEL_ID = id;
        } catch (Exception e) {
            success = false;
        }

        return success;
    }

    private boolean preparePaymentsTable() {
        boolean success = true;

        if (data.length < 9) {
            return false;
        }

        try {
            String id = (String) data[0];
            String receipt = (String) data[1];
            String payment = (String) data[2];
            double total = (Double) data[3];
            String transId = (String) data[4];
            String returnMsg = (String) data[5];
            String notes = (String) data[6];
            double tendered = (Double) data[7];
            String cardName = (String) data[8];

            dataModel = new Payment(id, receipt, payment, total, transId, returnMsg, notes, tendered, cardName);
            DATA_MODEL_ID = id;

        } catch (Exception e) {
            success = false;
        }

        return success;
    }

    private boolean prepareStockDiaryTable() {
        boolean success = true;

        if (data.length < 9) {
            return false;
        }

        try {
            String id = (String) data[0];
            Date time = (Date) data[1];
            int reason = (Integer) data[2];
            String location = (String) data[3];
            String product = (String) data[4];
            String attributeSetInstance_id = (String) data[5];
            double units = (Double) data[6];
            double price = (Double) data[7];
            String appUser = (String) data[8];

            String dateNew = time.toString();
            
            dataModel = new StockDiary(id, dateNew, reason, location, product, attributeSetInstance_id, units, price, appUser);
            DATA_MODEL_ID = id;

        } catch (Exception e) {
            success = false;
        }

        return success;
    }
}
