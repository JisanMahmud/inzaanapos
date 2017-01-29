/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inzaana.pos.client;

import com.inzaana.pos.models.EmailContent;
import com.inzaana.pos.utils.ResponseMessage;
import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.InzaanaSplash;
import com.openbravo.pos.payment.PaymentInfo;
import java.util.prefs.Preferences;

/**
 *
 * @author SixtyNine
 */
public class CustomerNotifier {
    private CustomerInfoExt customerInfo = null;
    private PaymentInfo paymentInfo = null;
    private EmailContent content = null;
    private String errorMessage = "";
    private String baseUrl = "";
    
    public CustomerNotifier(CustomerInfoExt customerInfo, PaymentInfo paymentInfo)
    {
        this.customerInfo = customerInfo;
        this.paymentInfo = paymentInfo;
        prepareContent();
    }
    
    private void prepareContent()
    {
        if (customerInfo == null || paymentInfo == null)
        {
            return;
        }
        Preferences preference = Preferences.userRoot();
        baseUrl = preference.get(InzaanaSplash.INZAANA_URL_KEY, "NOT_FOUND");
        
        content = new EmailContent();
        content.setCompanyName("");
        content.setTotalPaid(paymentInfo.getTotal());
        content.setTotalPrice(paymentInfo.getPaid());
        
        String userName = customerInfo.getFirstname() + " " + customerInfo.getLastname();
        if (userName.isEmpty())
        {
            userName = customerInfo.getName();
        }
        content.setUserName(userName);
        
        String userAddress = customerInfo.getAddress();
        if (userAddress.isEmpty())
        {
            userAddress = customerInfo.getAddress2();
        }
        userAddress += ", " + customerInfo.getCity() + ", " + customerInfo.getCountry();
        content.setUserAddress(userAddress);
        content.setUserEmail(customerInfo.getEmail());
        content.setUserPhoneNumber(customerInfo.getPhone());
        if (content.getUserPhoneNumber().isEmpty())
        {
            content.setUserPhoneNumber(customerInfo.getPhone2());
        }
    }
    
    public boolean notifyPayment(boolean viaEmail, boolean viaSMS)
    {
        boolean result = false;
        if (viaEmail && hasEmailUtilityPermission())
        {
            result = notifyViaEmail();
        }
        
        if (viaSMS && hasSMSUtilityPermission())
        {
             result = notifyViaSMS();
        }
        
        return result;
    }
    
    private boolean notifyViaEmail()
    {
        if (content == null)
        {
            errorMessage = "Email Content is Empty";
            return false;
        }
        
        String emailUrl = baseUrl + "/util/email";
        
        RESTManager restManager = new RESTManager();
        restManager.setUrl("http://localhost:8080/pos/util/email");
        ResponseMessage response = restManager.put(content);
        if (response.getStatusCode() != 200)
        {
            errorMessage =  response.getMessage();
            return false;
        }
        
        return true;
    }
    
    private boolean notifyViaSMS()
    {
        if (content == null)
        {
            errorMessage = "SMS content is empty";
            return false;
        }
        String smsUrl = baseUrl + "/util/sms";
        
        RESTManager restManager = new RESTManager();
        restManager.setUrl("http://localhost:8080/pos/util/sms");
        ResponseMessage response = restManager.put(content);
        if (response.getStatusCode() != 200)
        {
            errorMessage = response.getMessage();
            return false;
        }
        
        return true;
    }
    
    public String getErrorMessage()
    {
        return errorMessage;
    }
    
    private boolean hasEmailUtilityPermission()
    {
        return true;
    }
    
    private boolean hasSMSUtilityPermission()
    {
        return true;
    }
}
