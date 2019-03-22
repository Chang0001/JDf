package com.fanwe.p2p.model.act;

public class Refresh_UserActModel extends BaseActModel
{
	private String user_money = null;
	private String user_money_format = null;

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
