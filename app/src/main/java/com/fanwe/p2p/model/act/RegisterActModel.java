package com.fanwe.p2p.model.act;

public class RegisterActModel extends BaseActModel
{
	private String id = null;
	private String user_name = null;
	private String user_money = null;
	private String user_money_format = null;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUser_name()
	{
		return user_name;
	}

	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
	}

	public String getUser_money()
	{
		return user_money;
	}

	public void setUser_money(String user_money)
	{
		this.user_money = user_money;
	}

	public String getUser_money_format()
	{
		return user_money_format;
	}

	public void setUser_money_format(String user_money_format)
	{
		this.user_money_format = user_money_format;
	}

}
