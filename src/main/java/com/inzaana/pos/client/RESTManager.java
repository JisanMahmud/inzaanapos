/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inzaana.pos.client;

import com.inzaana.pos.models.DataModel;
import com.inzaana.pos.utils.Authenticator;
import com.inzaana.pos.utils.ResponseMessage;
import com.openbravo.pos.forms.InzaanaSplash;
import java.util.prefs.Preferences;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;

/**
 *
 * @author SixtyNine
 */
public class RESTManager {
    
    private Response response;
    private Client client;
    private WebTarget target;
    private Invocation.Builder invocationBuilder;
    private ResponseMessage responseMessage;
    
    
    public RESTManager()
    {
        this.response = null;
        this.client = null;
        this.target = null;
        this.invocationBuilder = null;
        this.responseMessage = null;
        
        initialize();
    }
    
    private void initialize()
    {
        responseMessage  = new ResponseMessage();
        ClientConfig config = new ClientConfig();

        Preferences preference = Preferences.userRoot();
        String userId = preference.get(InzaanaSplash.INZAANA_USER_ID_KEY, "NOT_FOUND");
        String userName = preference.get(InzaanaSplash.INZAANA_USER_NAME_KEY, "NOT_FOUND");
        String userPassword = preference.get(InzaanaSplash.INZAANA_SECURITY_KEY, "NOT_FOUND");

        client = ClientBuilder.newClient(config);
        client.register(new LoggingFilter());
        //client.register(new Authenticator(userId, userPassword));
        client.register(new Authenticator("Mahmud", "123"));
    }
    
    public void setUrl(String url)
    {
        target = client.target(url);
        invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        responseMessage.clear();
    }
    
    public ResponseMessage get()
    {   
        try {
            response = invocationBuilder.get();
            responseMessage.setMessage(response.readEntity(String.class));
            responseMessage.setStatusCode(response.getStatus());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return responseMessage;
    }
    
    public ResponseMessage put(DataModel dataModel)
    {
        if (dataModel == null)
            return null;
        
        try {
            response = invocationBuilder.put(Entity.entity(dataModel, MediaType.APPLICATION_JSON));
            responseMessage.setMessage(response.readEntity(String.class));
            responseMessage.setStatusCode(response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return responseMessage;
    }
    
    public ResponseMessage post(DataModel dataModel)
    {
        if (dataModel == null)
            return null;
        
        try {
            response = invocationBuilder.post(Entity.entity(dataModel, MediaType.APPLICATION_JSON));
            responseMessage.setMessage(response.readEntity(String.class));
            responseMessage.setStatusCode(response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return responseMessage;
    }
    
    public ResponseMessage delete()
    {   
        try {
            response = invocationBuilder.delete();
            responseMessage.setMessage(response.readEntity(String.class));
            responseMessage.setStatusCode(response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return responseMessage;
    }
}
