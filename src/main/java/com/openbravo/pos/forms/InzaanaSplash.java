/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.forms;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Session;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;

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
import java.io.File;

/**
 *
 * @author User
 */
public class InzaanaSplash extends javax.swing.JFrame {

    private final String INZAANA_ADMIN_ID = "Inzaana_admin@123";
    public final static String INZAANA_SECURITY_KEY = "INZAANA_SECURITY_KEY";
    public final static String INZAANA_USER_ID_KEY = "INZAANA_USER_ID";
    public final static String INZAANA_USER_NAME_KEY = "INZAANA_USER_NAME";
    public final static String INZAANA_URL_KEY = "INZAANA_URL";
    
    private String BASE_URL = "";
    
    private boolean isRegistered = false;
    private String userId = "";
    private String userPassword = "";
    private String userName = "";
    
    private AppView m_App;
    private Session s;
    private Connection con;
    private PreparedStatement pstmt;
    private String SQL;
    private ResultSet rs;
    
    private AppProperties config;
    

    /**
     * Creates new form InzaanaSplash
     */
    public InzaanaSplash() {
        initComponents();
    }

    public boolean initFrame(AppProperties props) {
        this.config = props;
        String id = getUserIdFromRegistry();
        userId = id;
        
        if (!id.equals("NOT_FOUND"))
        {
            String password = getPasswordFromRegistry();
            createPassword();
            
            if (!password.equals("NOT_FOUND") && password.equals(userPassword)) {
                isRegistered = true;
            }
        }
        
        if (isRegistered())
        {
            super.dispose();
            startInzaanaPos();
        }
        else
        {
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }
        
        return true;
    }

    public void testDb() {

        AppConfig m_config =  new AppConfig(new File((System.getProperty("user.home")), AppLocal.APP_ID + ".properties"));        
        m_config.load();
        
        try {
            s = AppViewConnection.createSession(m_config);
            con = s.getConnection();
        } catch (BasicException e) {
            System.out.println(e);
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_DANGER, e.getMessage(), e));
        } catch (SQLException ex) {
            Logger.getLogger(InzaanaSplash.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            SQL = "INSERT INTO test(ID, NAME) VALUES (?,?)";
            pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, 3);
            pstmt.setString(2, "name_2");
            pstmt.executeUpdate();
        } catch (Exception e) {
        }
    }
    
    private String getPasswordFromRegistry(){
        Preferences preference = Preferences.userRoot();
        return preference.get(INZAANA_SECURITY_KEY, "NOT_FOUND");
    }
    
    private String getUserIdFromRegistry(){
        Preferences preference = Preferences.userRoot();
        return preference.get(INZAANA_USER_ID_KEY, "NOT_FOUND");
    }
    
    private void setDataToRegistry()
    {
        Preferences preference = Preferences.userRoot();
        preference.put(INZAANA_SECURITY_KEY, userPassword);
        preference.put(INZAANA_USER_ID_KEY, userId);
        preference.put(INZAANA_URL_KEY, BASE_URL);
        preference.put(INZAANA_USER_NAME_KEY, userName);
    }
    
    private void createPassword()
    {
        String macAddress = null;
        InetAddress ip;
	try
	{
		ip = InetAddress.getLocalHost();
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		
		byte[] mac = network.getHardwareAddress();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
		}
                
                macAddress = sb.toString();
		System.out.println("Mac: " + mac);
		
	}
	catch (UnknownHostException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	catch (SocketException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        
        String password = "Inzaana_" + userId + "_" + macAddress;
        userPassword = Base64.encodeAsString(password);
    }
    
    private boolean regiterToInzaana(){
        
        createPassword();
        
        ClientConfig config = new ClientConfig();

        Client client = ClientBuilder.newClient(config);
        client.register(new LoggingFilter());
        client.register(new Authenticator(userId, userPassword));
        Response response = null;
        
        WebTarget target = client.target(BASE_URL).path("users").path("new").path("register");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        response = invocationBuilder.get();
        int responseStatus = response.getStatus();
        String responseEntity = response.readEntity(String.class);
        
        if (response == null) {
            return false;
        }
        if (responseStatus != 200) {
            return false;
        }
        if (responseEntity.contains("fail")) {
            return false;
        }
        
        userName = responseEntity;

        return true;
    }
    
    public boolean isRegistered()
    {
        return isRegistered;
    }
    
    private void startInzaanaPos()
    {
        String screenmode = config.getProperty("machine.screenmode");
        if ("fullscreen".equals(screenmode)) {
            JRootKiosk rootkiosk = new JRootKiosk();
            rootkiosk.initFrame(config);
        } else {
            JRootFrame rootframe = new JRootFrame();
            rootframe.initFrame(config);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();

        jLabel2.setText("jLabel2");

        jTextField1.setText("jTextField1");

        jTextField3.setText("jTextField3");

        jTextField4.setText("jTextField4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToggleButton1.setText("Register");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Inzaana Splash Screen");

        jLabel3.setText("Inzaana Server Url :");

        jLabel4.setText("User Id:");

        jTextField2.setText("http://localhost:8080/pos");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jToggleButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(jLabel1)
                        .addGap(0, 116, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(jTextField5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(67, 67, 67)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addComponent(jToggleButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        
        BASE_URL = jTextField2.getText();
        userId = jTextField5.getText();
        
        if (regiterToInzaana())
        {
            setDataToRegistry();
            isRegistered = true;
            JOptionPane.showMessageDialog(this, "Registration Successful", "", JOptionPane.PLAIN_MESSAGE);
            super.dispose();
            
            startInzaanaPos();
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Registration Failed..." + userId + " " + userPassword, "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InzaanaSplash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InzaanaSplash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InzaanaSplash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InzaanaSplash.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InzaanaSplash().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables

}
