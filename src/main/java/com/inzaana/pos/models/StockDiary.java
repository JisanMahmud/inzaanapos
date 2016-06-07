package com.inzaana.pos.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StockDiary
{

	private String	id;
	private String	dateNew;
	private int		reason;
	private String	location;
	private String	product;
	private String	attributeSetInstance_id;
	private double	units;
	private double	price;
	private String	appUser;

	public StockDiary()
	{

	}

	/**
	 * @param id
	 * @param dateNew
	 * @param reason
	 * @param location
	 * @param product
	 * @param attributeSetInstance_id
	 * @param units
	 * @param price
	 * @param appUser
	 */
	public StockDiary(String id, String dateNew, int reason, String location, String product,
			String attributeSetInstance_id, double units, double price, String appUser)
	{
		this.id = id;
		this.dateNew = dateNew;
		this.reason = reason;
		this.location = location;
		this.product = product;
		this.attributeSetInstance_id = attributeSetInstance_id;
		this.units = units;
		this.price = price;
		this.appUser = appUser;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDateNew()
	{
		return dateNew;
	}

	public void setDateNew(String dateNew)
	{
		this.dateNew = dateNew;
	}

	public int getReason()
	{
		return reason;
	}

	public void setReason(int reason)
	{
		this.reason = reason;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getProduct()
	{
		return product;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}

	public String getAttributeSetInstance_id()
	{
		return attributeSetInstance_id;
	}

	public void setAttributeSetInstance_id(String attributeSetInstance_id)
	{
		this.attributeSetInstance_id = attributeSetInstance_id;
	}

	public double getUnits()
	{
		return units;
	}

	public void setUnits(double units)
	{
		this.units = units;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}

	public String getAppUser()
	{
		return appUser;
	}

	public void setAppUser(String appUser)
	{
		this.appUser = appUser;
	}

}
