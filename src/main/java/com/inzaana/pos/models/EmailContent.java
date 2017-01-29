package com.inzaana.pos.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EmailContent extends DataModel
{
	private String              companyName;
	private String				userName;
	private String				userAddress;
	private String				userEmail;
	private String				userPhoneNumber;
	private double					totalPrice;
	private double					totalPaid;
	
	public EmailContent()
	{

	}
	
	/**
	 * @param companyName
	 * @param userName
	 * @param userAddress
	 * @param userEmail
	 * @param userPhoneNumber
	 * @param totalPrice
	 * @param totalPaid
	 */
	public EmailContent(String companyName, String userName, String userAddress, String userEmail,
			String userPhoneNumber, double totalPrice, double totalPaid) {
		super();
		this.companyName = companyName;
		this.userName = userName;
		this.userAddress = userAddress;
		this.userEmail = userEmail;
		this.userPhoneNumber = userPhoneNumber;
		this.totalPrice = totalPrice;
		this.totalPaid = totalPaid;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
            if (companyName == null)
                return "";
            return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
            if (userName == null)
                return "";
            return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userAddress
	 */
	public String getUserAddress() {
            if (userAddress == null)
                return "";
            return userAddress;
	}

	/**
	 * @param userAddress the userAddress to set
	 */
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
            if (userEmail == null)
                return "";
            return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the userPhoneNumber
	 */
	public String getUserPhoneNumber() {
            if (userPhoneNumber == null)
                return "";
            return userPhoneNumber;
	}

	/**
	 * @param userPhoneNumber the userPhoneNumber to set
	 */
	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	/**
	 * @return the totalPrice
	 */
	public double getTotalPrice() {
            return totalPrice;
	}

	/**
	 * @param totalPrice the totalPrice to set
	 */
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * @return the totalPaid
	 */
	public double getTotalPaid() {
		return totalPaid;
	}

	/**
	 * @param totalPaid the totalPaid to set
	 */
	public void setTotalPaid(double totalPaid) {
		this.totalPaid = totalPaid;
	}

    @Override
    public boolean insertRecordIntoDB(String userID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateRecordInDB(String userID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
