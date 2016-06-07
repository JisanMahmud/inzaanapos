package com.inzaana.pos.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Payment
{

	private String	id;
	private String	receipt;
	private String	payment;
	private double	total;
	private String	transId;
	private String	returnMsg;
	private String	notes;
	private double	tendered;
	private String	cardName;

	public Payment()
	{

	}

	/**
	 * @param id
	 * @param receipt
	 * @param payment
	 * @param total
	 * @param transId
	 * @param returnMsg
	 * @param notes
	 * @param tendered
	 * @param cardName
	 */
	public Payment(String id, String receipt, String payment, double total, String transId, String returnMsg,
			String notes, double tendered, String cardName)
	{
		this.id = id;
		this.receipt = receipt;
		this.payment = payment;
		this.total = total;
		this.transId = transId;
		this.returnMsg = returnMsg;
		this.notes = notes;
		this.tendered = tendered;
		this.cardName = cardName;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getReceipt()
	{
		return receipt;
	}

	public void setReceipt(String receipt)
	{
		this.receipt = receipt;
	}

	public String getPayment()
	{
		return payment;
	}

	public void setPayment(String payment)
	{
		this.payment = payment;
	}

	public double getTotal()
	{
		return total;
	}

	public void setTotal(double total)
	{
		this.total = total;
	}

	public String getTransId()
	{
		return transId;
	}

	public void setTransId(String transId)
	{
		this.transId = transId;
	}

	public String getReturnMsg()
	{
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg)
	{
		this.returnMsg = returnMsg;
	}

	public String getNotes()
	{
		return notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	public double getTendered()
	{
		return tendered;
	}

	public void setTendered(double tendered)
	{
		this.tendered = tendered;
	}

	public String getCardName()
	{
		return cardName;
	}

	public void setCardName(String cardName)
	{
		this.cardName = cardName;
	}

}
