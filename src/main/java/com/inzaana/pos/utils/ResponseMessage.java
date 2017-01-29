package com.inzaana.pos.utils;

import javax.ws.rs.core.Response.Status;

public class ResponseMessage
{
	private String	errorMessage	= "";
	private int statusCode	= 200;

	public ResponseMessage(String message)
	{
		this.errorMessage = message;
	}

	public ResponseMessage()
	{

	}

	public void setMessage(String message)
	{
		this.errorMessage = message;
	}

	public String getMessage()
	{
		return this.errorMessage;
	}
	
	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}
	
	public int getStatusCode()
	{
		return statusCode;
	}

	public void clear()
	{
		this.errorMessage = "";
		this.statusCode = 200;
	}

}
