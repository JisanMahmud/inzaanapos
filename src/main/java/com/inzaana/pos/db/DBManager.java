package com.inzaana.pos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

//import com.inzaana.pos.filters.AuthenticationRequestFilter;
import com.inzaana.pos.models.Category;
import com.inzaana.pos.models.Payment;
import com.inzaana.pos.models.Product;
import com.inzaana.pos.models.StockDiary;
import com.inzaana.pos.models.User;
import com.inzaana.pos.utils.DBTables;
import com.inzaana.pos.utils.UserRole;

public class DBManager
{

	// JDBC driver name and database URL
	private final String	JDBC_DRIVER			= "com.mysql.jdbc.Driver";
	private final String	DB_URL				= "jdbc:mysql://localhost:3306/inzaanaposservice";
	private final String	DB_USER_NAME		= "root";
	private final String	DB_PASS				= "";

	Connection				dbConnection		= null;
	Statement				dbStatement			= null;
	PreparedStatement		preparedStatement	= null;
	ResultSet				resultSet			= null;

	public DBManager()
	{
	}

	public boolean executeUpdate(String sqlQuery, ArrayList<String> paramList)
	{
		boolean success = true;

		try
		{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			dbConnection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASS);

			preparedStatement = dbConnection.prepareStatement(sqlQuery);
			for (int i = 0; i < paramList.size(); i++)
			{
				preparedStatement.setString(i + 1, paramList.get(i));
			}

			System.out.println(sqlQuery + paramList.toString());
			preparedStatement.executeUpdate();
		}
		catch (SQLException se)
		{
			se.printStackTrace();
			success = false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			success = false;
		}
		finally
		{
			closeConnection();
		}

		return success;
	}

	public List<Category> getCategoryItems(String userName)
	{
		List<Category> categoryList = new ArrayList<Category>();

		if (!validateUserName(userName))
		{
			return categoryList;
		}

		String sqlQuery = "SELECT * FROM " + DBTables.CATEGORIES.toString();

		if (!userName.isEmpty())
		{
			sqlQuery += " WHERE USER_ID = ?";
		}

		try
		{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			dbConnection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASS);
			preparedStatement = dbConnection.prepareStatement(sqlQuery);

			if (!userName.isEmpty())
			{
				preparedStatement.setString(1, userName);
			}

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				Category category = new Category();
				category.setId(resultSet.getString(Category.ID));
				category.setUserId(resultSet.getString(Category.USER_ID));
				category.setName(resultSet.getString(Category.NAME));
				category.setParentId(resultSet.getString(Category.PARENTID));
				category.setImage(resultSet.getString(Category.IMAGE));
				category.setTextTip(resultSet.getString(Category.TEXTTIP));
				category.setCatShowName(resultSet.getBoolean(Category.CATSHOWNAME));
				categoryList.add(category);
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		return categoryList;
	}

	public List<Product> getProducts(String userName)
	{
		List<Product> productList = new ArrayList<Product>();

		if (!validateUserName(userName))
		{
			return productList;
		}

		String sqlQuery = "SELECT * FROM " + DBTables.PRODUCTS.toString();

		if (!userName.isEmpty())
		{
			sqlQuery += " WHERE USER_ID = ?";
		}

		try
		{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			dbConnection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASS);
			preparedStatement = dbConnection.prepareStatement(sqlQuery);

			if (!userName.isEmpty())
			{
				preparedStatement.setString(1, userName);
			}

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				Product product = new Product();
				product.setId(resultSet.getString(Product.ID));
				product.setUserId(resultSet.getString(Product.USER_ID));
				product.setReference(resultSet.getString(Product.REFERENCE));
				product.setCode(resultSet.getString(Product.CODE));
				product.setCodetype(resultSet.getString(Product.CODETYPE));
				product.setName(resultSet.getString(Product.NAME));
				product.setPriceBuy(resultSet.getDouble(Product.PRICEBUY));
				product.setPriceSell(resultSet.getDouble(Product.PRICESELL));
				product.setCategory(resultSet.getString(Product.CATEGORY));
				product.setTaxcat(resultSet.getString(Product.TAXCAT));
				product.setAttributeset_id(resultSet.getString(Product.ATTRIBUTESET_ID));
				product.setStockCost(resultSet.getDouble(Product.STOCKCOST));
				product.setStockVolume(resultSet.getDouble(Product.STOCKVOLUME));
				product.setImage(resultSet.getString(Product.IMAGE));
				product.setIscom(resultSet.getBoolean(Product.ISCOM));
				product.setScale(resultSet.getBoolean(Product.ISSCALE));
				product.setKitchen(resultSet.getBoolean(Product.ISKITCHEN));
				product.setPrintkb(resultSet.getBoolean(Product.PRINTKB));
				product.setSendStatus(resultSet.getBoolean(Product.SENDSTATUS));
				product.setService(resultSet.getBoolean(Product.ISSERVICE));
				product.setAttributes(resultSet.getString(Product.ATTRIBUTES));
				product.setDisplay(resultSet.getString(Product.DISPLAY));
				product.setIsVPrice(resultSet.getInt(Product.ISVPRICE));
				product.setIsVerpatrib(resultSet.getInt(Product.ISVERPATRIB));
				product.setTextTip(resultSet.getString(Product.TEXTTIP));
				product.setWarranty(resultSet.getInt(Product.WARRANTY));
				product.setStockunits(resultSet.getDouble(Product.STOCKUNITS));
				productList.add(product);
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		return productList;
	}

	public List<Payment> getPaymentItems(String userName)
	{
		List<Payment> paymentList = new ArrayList<Payment>();

		if (!validateUserName(userName))
		{
			return paymentList;
		}

		String sqlQuery = "SELECT * FROM " + DBTables.PAYMENTS.toString();

		if (!userName.isEmpty())
		{
			sqlQuery += " WHERE USER_ID = ?";
		}

		try
		{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			dbConnection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASS);
			preparedStatement = dbConnection.prepareStatement(sqlQuery);

			if (!userName.isEmpty())
			{
				preparedStatement.setString(1, userName);
			}

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				Payment payment = new Payment();
				paymentList.add(payment);
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		return paymentList;
	}

	public List<StockDiary> getStockDiaryItems(String userName)
	{
		List<StockDiary> stockList = new ArrayList<StockDiary>();

		if (!validateUserName(userName))
		{
			return stockList;
		}

		String sqlQuery = "SELECT * FROM " + DBTables.PRODUCTS.toString();

		if (!userName.isEmpty())
		{
			sqlQuery += " WHERE USER_ID = ?";
		}

		try
		{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			dbConnection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASS);
			preparedStatement = dbConnection.prepareStatement(sqlQuery);

			if (!userName.isEmpty())
			{
				preparedStatement.setString(1, userName);
			}

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				StockDiary stock = new StockDiary();
				stockList.add(stock);
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		return stockList;
	}

	public List<User> getUsers(String userName)
	{
		List<User> userList = new ArrayList<User>();

		if (!validateUserName(userName))
		{
			return userList;
		}

		String sqlQuery = "SELECT * FROM " + DBTables.PRODUCTS.toString();

		if (!userName.isEmpty())
		{
			sqlQuery += " WHERE NAME = ?";
		}

		try
		{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			dbConnection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASS);
			preparedStatement = dbConnection.prepareStatement(sqlQuery);

			if (!userName.isEmpty())
			{
				preparedStatement.setString(1, userName);
			}

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				User user = new User();
				user.SetUserName(resultSet.getString(User.NAME));
				user.SetUserPassword(resultSet.getString(User.PASSWORD));
				user.SetUserRole(resultSet.getString(User.ROLE));
				userList.add(user);
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		return userList;
	}

	public String getUserPassword(String userName)
	{
		String password = "*@*@*@**USER_PASSWORD_NOT_FOUND**@*@*@*";

		if (!validateUserName(userName))
		{
			return password;
		}

		String sqlQuery = "SELECT password FROM " + DBTables.USERS.toString() + " WHERE name = ?";

		try
		{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			dbConnection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASS);
			preparedStatement = dbConnection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, userName);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				password = resultSet.getString(User.PASSWORD);
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		return password;
	}

	public String getUserRole(String userName)
	{
		String userRole = UserRole.GUEST.toString();

		if (!validateUserName(userName))
		{
			return userRole;
		}

		String sqlQuery = "SELECT role FROM " + DBTables.USERS.toString() + " WHERE name = ?";

		try
		{
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
			dbConnection = DriverManager.getConnection(DB_URL, DB_USER_NAME, DB_PASS);
			preparedStatement = dbConnection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, userName);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				userRole = resultSet.getString(User.ROLE);
			}
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			closeConnection();
		}

		return userRole;
	}

	public boolean validateUserID(String id)
	{
		// Important
		return true;
	}

	public boolean validateUserName(String name)
	{
		// Important

//		if (AuthenticationRequestFilter.USER_ROLE.equals(UserRole.ADMIN.toString())
//				|| name.equalsIgnoreCase(AuthenticationRequestFilter.USER_NAME))
//		{
//			return true;
//		}
//
//		return false;
		return true;
	}

	private void closeConnection()
	{
		try
		{
			if (resultSet != null)
				resultSet.close();
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}

		try
		{
			if (preparedStatement != null)
				preparedStatement.close();
		}
		catch (SQLException se2)
		{
		}
		try
		{
			if (dbConnection != null)
				dbConnection.close();
		}
		catch (SQLException se)
		{
			se.printStackTrace();
		}
	}
}
