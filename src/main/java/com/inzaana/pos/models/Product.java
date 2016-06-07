package com.inzaana.pos.models;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.inzaana.pos.db.DBManager;
import com.inzaana.pos.utils.DBTables;
import com.inzaana.pos.utils.Pair;

@XmlRootElement
public class Product
{
	final static public String			ID				= "ID";
	final static public String			USER_ID			= "USER_ID";
	final static public String			REFERENCE		= "REFERENCE";
	final static public String			CODE			= "CODE";
	final static public String			CODETYPE		= "CODETYPE";
	final static public String			NAME			= "NAME";
	final static public String			PRICEBUY		= "PRICEBUY";
	final static public String			PRICESELL		= "PRICESELL";
	final static public String			CATEGORY		= "CATEGORY";
	final static public String			TAXCAT			= "TAXCAT";
	final static public String			ATTRIBUTESET_ID	= "ATTRIBUTESET_ID";
	final static public String			STOCKCOST		= "STOCKCOST";
	final static public String			STOCKVOLUME		= "STOCKVOLUME";
	final static public String			IMAGE			= "IMAGE";
	final static public String			ISCOM			= "ISCOM";
	final static public String			ISSCALE			= "ISSCALE";
	final static public String			ISKITCHEN		= "ISKITCHEN";
	final static public String			PRINTKB			= "PRINTKB";
	final static public String			SENDSTATUS		= "SENDSTATUS";
	final static public String			ISSERVICE		= "ISSERVICE";
	final static public String			ATTRIBUTES		= "ATTRIBUTES";
	final static public String			DISPLAY			= "DISPLAY";
	final static public String			ISVPRICE		= "ISVPRICE";
	final static public String			ISVERPATRIB		= "ISVERPATRIB";
	final static public String			TEXTTIP			= "TEXTTIP";
	final static public String			WARRANTY		= "WARRANTY";
	final static public String			STOCKUNITS		= "STOCKUNITS";

	private String						id;
	private String						userId;
	private String						reference;
	private String						code;
	private String						codetype;
	private String						name;
	private double						priceBuy;
	private double						priceSell;
	private String						category;
	private String						taxcat;
	private String						attributeset_id;
	private double						stockCost;
	private double						stockVolume;
	private String						image;
	private boolean						iscom;
	private boolean						isScale;
	private boolean						isKitchen;
	private boolean						printkb;
	private boolean						sendStatus;
	private boolean						isService;
	private String						attributes;
	private String						display;
	private int							isVPrice;
	private int							isVerpatrib;
	private String						textTip;
	private int							warranty;
	private double						stockunits;

	ArrayList<Pair<String, String>>		productTableStringList;
	ArrayList<Pair<String, Integer>>	productTableIntegerList;
	ArrayList<Pair<String, Double>>		productTableDoubleList;
	ArrayList<Pair<String, Boolean>>	productTableBooleanList;

	public Product()
	{
		productTableStringList = new ArrayList<Pair<String, String>>();
		productTableIntegerList = new ArrayList<Pair<String, Integer>>();
		productTableDoubleList = new ArrayList<Pair<String, Double>>();
		productTableBooleanList = new ArrayList<Pair<String, Boolean>>();

	}

	/**
	 * @param id
	 * @param reference
	 * @param code
	 * @param codetype
	 * @param name
	 * @param priceBuy
	 * @param priceSell
	 * @param category
	 * @param taxcat
	 * @param attributeset_id
	 * @param stockCost
	 * @param stockVolume
	 * @param image
	 * @param iscom
	 * @param isScale
	 * @param isKitchen
	 * @param printkb
	 * @param sendStatus
	 * @param isService
	 * @param attributes
	 * @param display
	 * @param isPrice
	 * @param isVerpatrib
	 * @param textTip
	 * @param warranty
	 * @param stockunits
	 */
	public Product(String id, String reference, String code, String codetype, String name, double priceBuy,
			double priceSell, String category, String taxcat, String attributeset_id, double stockCost,
			double stockVolume, String image, boolean iscom, boolean isScale, boolean isKitchen, boolean printkb,
			boolean sendStatus, boolean isService, String attributes, String display, int isPrice, int isVerpatrib,
			String textTip, int warranty, double stockunits)
	{
		productTableStringList = new ArrayList<Pair<String, String>>();
		productTableIntegerList = new ArrayList<Pair<String, Integer>>();
		productTableDoubleList = new ArrayList<Pair<String, Double>>();
		productTableBooleanList = new ArrayList<Pair<String, Boolean>>();

		this.setId(id);
		this.setReference(reference);
		this.setCode(code);
		this.setCodetype(codetype);
		this.setName(name);
		this.setPriceBuy(priceBuy);
		this.setPriceSell(priceSell);
		this.setCategory(category);
		this.setTaxcat(taxcat);
		this.setAttributeset_id(attributeset_id);
		this.setStockCost(stockCost);
		this.setStockVolume(stockVolume);
		this.setImage(image);
		this.setIscom(iscom);
		this.setScale(isScale);
		this.setKitchen(isKitchen);
		this.setPrintkb(printkb);
		this.setSendStatus(sendStatus);
		this.setService(isService);
		this.setAttributes(attributes);
		this.setDisplay(display);
		this.setIsVPrice(isPrice);
		this.setIsVerpatrib(isVerpatrib);
		this.setTextTip(textTip);
		this.setWarranty(warranty);
		this.setStockunits(stockunits);
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
		productTableStringList.add(new Pair<String, String>(ID, id));
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
		productTableStringList.add(new Pair<String, String>(REFERENCE, reference));
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
		productTableStringList.add(new Pair<String, String>(CODE, code));
	}

	public String getCodetype()
	{
		return codetype;
	}

	public void setCodetype(String codetype)
	{
		this.codetype = codetype;
		productTableStringList.add(new Pair<String, String>(CODETYPE, codetype));
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		productTableStringList.add(new Pair<String, String>(NAME, name));
	}

	public double getPriceBuy()
	{
		return priceBuy;
	}

	public void setPriceBuy(double priceBuy)
	{
		this.priceBuy = priceBuy;
		productTableDoubleList.add(new Pair<String, Double>(PRICEBUY, priceBuy));
	}

	public double getPriceSell()
	{
		return priceSell;
	}

	public void setPriceSell(double priceSell)
	{
		this.priceSell = priceSell;
		productTableDoubleList.add(new Pair<String, Double>(PRICESELL, priceSell));
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
		productTableStringList.add(new Pair<String, String>(CATEGORY, category));
	}

	public String getTaxcat()
	{
		return taxcat;
	}

	public void setTaxcat(String taxcat)
	{
		this.taxcat = taxcat;
		productTableStringList.add(new Pair<String, String>(TAXCAT, taxcat));
	}

	public String getAttributeset_id()
	{
		return attributeset_id;
	}

	public void setAttributeset_id(String attributeset_id)
	{
		this.attributeset_id = attributeset_id;
		productTableStringList.add(new Pair<String, String>(ATTRIBUTESET_ID, attributeset_id));
	}

	public double getStockCost()
	{
		return stockCost;
	}

	public void setStockCost(double stockCost)
	{
		this.stockCost = stockCost;
		productTableDoubleList.add(new Pair<String, Double>(STOCKCOST, stockCost));
	}

	public double getStockVolume()
	{
		return stockVolume;
	}

	public void setStockVolume(double stockVolume)
	{
		this.stockVolume = stockVolume;
		productTableDoubleList.add(new Pair<String, Double>(STOCKVOLUME, stockVolume));
	}

	public String getImage()
	{
		return image;
	}

	public void setImage(String image)
	{
		this.image = image;
		productTableStringList.add(new Pair<String, String>(IMAGE, image));
	}

	public boolean getIscom()
	{
		return iscom;
	}

	public void setIscom(boolean iscom)
	{
		this.iscom = iscom;
		productTableBooleanList.add(new Pair<String, Boolean>(ISCOM, iscom));
	}

	public boolean isScale()
	{
		return isScale;
	}

	public void setScale(boolean isScale)
	{
		this.isScale = isScale;
		productTableBooleanList.add(new Pair<String, Boolean>(ISSCALE, isScale));
	}

	public boolean isKitchen()
	{
		return isKitchen;
	}

	public void setKitchen(boolean isKitchen)
	{
		this.isKitchen = isKitchen;
		productTableBooleanList.add(new Pair<String, Boolean>(ISKITCHEN, isKitchen));
	}

	public boolean isPrintkb()
	{
		return printkb;
	}

	public void setPrintkb(boolean printkb)
	{
		this.printkb = printkb;
		productTableBooleanList.add(new Pair<String, Boolean>(PRINTKB, printkb));
	}

	public boolean isSendStatus()
	{
		return sendStatus;
	}

	public void setSendStatus(boolean sendStatus)
	{
		this.sendStatus = sendStatus;
		productTableBooleanList.add(new Pair<String, Boolean>(SENDSTATUS, sendStatus));
	}

	public boolean isService()
	{
		return isService;
	}

	public void setService(boolean isService)
	{
		this.isService = isService;
		productTableBooleanList.add(new Pair<String, Boolean>(ISSERVICE, isService));
	}

	public String getAttributes()
	{
		return attributes;
	}

	public void setAttributes(String attributes)
	{
		this.attributes = attributes;
		productTableStringList.add(new Pair<String, String>(ATTRIBUTES, attributes));
	}

	public String getDisplay()
	{
		return display;
	}

	public void setDisplay(String display)
	{
		this.display = display;
		productTableStringList.add(new Pair<String, String>(DISPLAY, display));
	}

	public int getIsVPrice()
	{
		return isVPrice;
	}

	public void setIsVPrice(int isPrice)
	{
		this.isVPrice = isPrice;
		productTableIntegerList.add(new Pair<String, Integer>(ISVPRICE, isPrice));
	}

	public int getIsVerpatrib()
	{
		return isVerpatrib;
	}

	public void setIsVerpatrib(int isVerpatrib)
	{
		this.isVerpatrib = isVerpatrib;
		productTableIntegerList.add(new Pair<String, Integer>(ISVERPATRIB, isVerpatrib));
	}

	public String getTextTip()
	{
		return textTip;
	}

	public void setTextTip(String textTip)
	{
		this.textTip = textTip;
		productTableStringList.add(new Pair<String, String>(TEXTTIP, textTip));
	}

	public int getWarranty()
	{
		return warranty;
	}

	public void setWarranty(int warranty)
	{
		this.warranty = warranty;
		productTableIntegerList.add(new Pair<String, Integer>(WARRANTY, warranty));
	}

	public double getStockunits()
	{
		return stockunits;
	}

	public void setStockunits(double stockunits)
	{
		this.stockunits = stockunits;
		productTableDoubleList.add(new Pair<String, Double>(STOCKUNITS, stockunits));
	}

	// ----------------------------------------------

	public boolean insertRecordIntoDB(String userID)
	{

		String sqlQuery = "INSERT INTO " + DBTables.PRODUCTS.toString() + " (";
		String colNameList = USER_ID;
		String colValueList = "?";

		for (int i = 0; i < productTableStringList.size(); i++)
		{
			colNameList += ", " + productTableStringList.get(i).getFirst();
			colValueList += ", '" + productTableStringList.get(i).getSecond() + "'";
		}

		for (int i = 0; i < productTableIntegerList.size(); i++)
		{
			colNameList += ", " + productTableIntegerList.get(i).getFirst();
			colValueList += ", " + productTableIntegerList.get(i).getSecond().toString();
		}

		for (int i = 0; i < productTableDoubleList.size(); i++)
		{
			colNameList += ", " + productTableDoubleList.get(i).getFirst();
			colValueList += ", " + productTableDoubleList.get(i).getSecond().toString();
		}

		for (int i = 0; i < productTableBooleanList.size(); i++)
		{
			colNameList += ", " + productTableBooleanList.get(i).getFirst();
			Integer intValueOfBoolean = productTableBooleanList.get(i).getSecond() ? 1 : 0;
			colValueList += ", " + intValueOfBoolean.toString();
		}

		sqlQuery += colNameList + ") " + "VALUES (" + colValueList + ");";

		DBManager dbManager = new DBManager();

		if (!dbManager.validateUserID(userID))
		{
			return false;
		}

		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add(userID);

		return dbManager.executeUpdate(sqlQuery, paramList);
	}

	public boolean updateRecordInDB(String userID)
	{

		String sqlQuery = "UPDATE " + DBTables.PRODUCTS.toString() + " SET ";

		for (int i = 0; i < productTableStringList.size(); i++)
		{
			sqlQuery += productTableStringList.get(i).getFirst() + "=";
			sqlQuery += "'" + productTableStringList.get(i).getSecond() + "'";

			if (i < (productTableStringList.size() - 1))
			{
				sqlQuery += ", ";
			}
		}

		for (int i = 0; i < productTableIntegerList.size(); i++)
		{
			sqlQuery += ", " + productTableIntegerList.get(i).getFirst() + "=";
			sqlQuery += productTableIntegerList.get(i).getSecond().toString();
		}

		for (int i = 0; i < productTableDoubleList.size(); i++)
		{
			sqlQuery += ", " + productTableDoubleList.get(i).getFirst() + "=";
			sqlQuery += productTableDoubleList.get(i).getSecond().toString();
		}

		for (int i = 0; i < productTableBooleanList.size(); i++)
		{
			sqlQuery += ", " + productTableBooleanList.get(i).getFirst() + "=";
			Integer intValueOfBoolean = productTableBooleanList.get(i).getSecond() ? 1 : 0;
			sqlQuery += intValueOfBoolean.toString();
		}

		sqlQuery += " WHERE " + USER_ID + "=? AND " + NAME + "=?;";

		DBManager dbManager = new DBManager();

		if (!dbManager.validateUserID(userID))
		{
			return false;
		}

		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add(userID);
		paramList.add(name);

		return dbManager.executeUpdate(sqlQuery, paramList);
	}

}
