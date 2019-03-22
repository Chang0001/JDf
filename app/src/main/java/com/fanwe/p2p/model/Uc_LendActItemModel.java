package com.fanwe.p2p.model;

import android.text.TextUtils;

public class Uc_LendActItemModel
{
	private String id = null;
	private String deal_id = null;
	private String user_id = null;
	private String user_name = null;
	private String money = null;
	private String create_time = null;
	private String is_repay = null;
	private String is_rebate = null;
	private String is_auto = null;
	private String rate = null;
	private String repay_time = null;
	private String repay_time_type = null;
	private String deal_status = null;
	private String name = null;
	private String rate_format = null;
	private String money_format = null;
	private String repay_time_format = null;
	private String create_time_format = null;
	private String deal_status_format = null;
	private String app_url = null;

	// ==============add
	private String rate_format_percent = null;

	public String getRate_format_percent()
	{
		return rate_format_percent;
	}

	public void setRate_format_percent(String rate_format_percent)
	{
		this.rate_format_percent = rate_format_percent;
	}

	public String getApp_url()
	{
		return app_url;
	}

	public void setApp_url(String app_url)
	{
		this.app_url = app_url;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getDeal_id()
	{
		return deal_id;
	}

	public void setDeal_id(String deal_id)
	{
		this.deal_id = deal_id;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}

	public String getUser_name()
	{
		return user_name;
	}

	public void setUser_name(String user_name)
	{
		this.user_name = user_name;
	}

	public String getMoney()
	{
		return money;
	}

	public void setMoney(String money)
	{
		this.money = money;
	}

	public String getCreate_time()
	{
		return create_time;
	}

	public void setCreate_time(String create_time)
	{
		this.create_time = create_time;
	}

	public String getIs_repay()
	{
		return is_repay;
	}

	public void setIs_repay(String is_repay)
	{
		this.is_repay = is_repay;
	}

	public String getIs_rebate()
	{
		return is_rebate;
	}

	public void setIs_rebate(String is_rebate)
	{
		this.is_rebate = is_rebate;
	}

	public String getIs_auto()
	{
		return is_auto;
	}

	public void setIs_auto(String is_auto)
	{
		this.is_auto = is_auto;
	}

	public String getRate()
	{
		return rate;
	}

	public void setRate(String rate)
	{
		this.rate = rate;
		if (!TextUtils.isEmpty(rate))
		{
			this.rate_format_percent = rate + "%";
		}
	}

	public String getRepay_time()
	{
		return repay_time;
	}

	public void setRepay_time(String repay_time)
	{
		this.repay_time = repay_time;
	}

	public String getRepay_time_type()
	{
		return repay_time_type;
	}

	public void setRepay_time_type(String repay_time_type)
	{
		this.repay_time_type = repay_time_type;
	}

	public String getDeal_status()
	{
		return deal_status;
	}

	public void setDeal_status(String deal_status)
	{
		this.deal_status = deal_status;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRate_format()
	{
		return rate_format;
	}

	public void setRate_format(String rate_format)
	{
		this.rate_format = rate_format;
	}

	public String getMoney_format()
	{
		return money_format;
	}

	public void setMoney_format(String money_format)
	{
		this.money_format = money_format;
	}

	public String getRepay_time_format()
	{
		return repay_time_format;
	}

	public void setRepay_time_format(String repay_time_format)
	{
		this.repay_time_format = repay_time_format;
	}

	public String getCreate_time_format()
	{
		return create_time_format;
	}

	public void setCreate_time_format(String create_time_format)
	{
		this.create_time_format = create_time_format;
	}

	public String getDeal_status_format()
	{
		return deal_status_format;
	}

	public void setDeal_status_format(String deal_status_format)
	{
		this.deal_status_format = deal_status_format;
	}

}
