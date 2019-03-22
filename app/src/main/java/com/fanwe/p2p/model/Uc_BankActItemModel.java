package com.fanwe.p2p.model;

import java.io.Serializable;

public class Uc_BankActItemModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = null;
	private String bankcard = null;
	private String real_name = null;
	private String bank_name = null;
	private String bankcode = null;
	private String img = null;

	private boolean isSelect = false;

	public boolean isSelect()
	{
		return isSelect;
	}

	public void setSelect(boolean isSelect)
	{
		this.isSelect = isSelect;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getBankcard()
	{
		return bankcard;
	}

	public void setBankcard(String bankcard)
	{
		this.bankcard = bankcard;
	}

	public String getReal_name()
	{
		return real_name;
	}

	public void setReal_name(String real_name)
	{
		this.real_name = real_name;
	}

	public String getBank_name()
	{
		return bank_name;
	}

	public void setBank_name(String bank_name)
	{
		this.bank_name = bank_name;
	}

	public String getBankcode()
	{
		return bankcode;
	}

	public void setBankcode(String bankcode)
	{
		this.bankcode = bankcode;
	}

	public String getImg()
	{
		return img;
	}

	public void setImg(String img)
	{
		this.img = img;
	}

}
