package com.fanwe.p2p.model;

import java.io.Serializable;

public class Uc_BankActFee_configModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = null;
	private String name = null;
	private String min_price = null;
	private String max_price = null;
	private String fee = null;
	private String fee_format = null;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getMin_price()
	{
		return min_price;
	}

	public void setMin_price(String min_price)
	{
		this.min_price = min_price;
	}

	public String getMax_price()
	{
		return max_price;
	}

	public void setMax_price(String max_price)
	{
		this.max_price = max_price;
	}

	public String getFee()
	{
		return fee;
	}

	public void setFee(String fee)
	{
		this.fee = fee;
	}

	public String getFee_format()
	{
		return fee_format;
	}

	public void setFee_format(String fee_format)
	{
		this.fee_format = fee_format;
	}

}
