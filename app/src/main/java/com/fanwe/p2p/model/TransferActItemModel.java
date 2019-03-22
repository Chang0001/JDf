package com.fanwe.p2p.model;

import java.io.Serializable;

import com.fanwe.p2p.utils.SDTypeParseUtil;

public class TransferActItemModel implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = null;
	private String deal_id = null;
	private String load_id = null;
	private String user_id = null;
	private String transfer_amount = null;
	private String transfer_amount_format = null; // 转让金额
	private String last_repay_time = null;
	private String near_repay_time = null;
	private String near_repay_time_format = null;
	private String transfer_number = null;
	private String t_user_id = null;
	private String transfer_time = null;
	private String transfer_time_format = null;
	private String create_time = null;
	private String status = null; // 1:可转让; 0:撤销; t_user_id > 0 已转让
	private String callback_count = null;
	private String name = null; // 名字
	private String icon = null;
	private String duser_id = null;
	private String rate = null;
	private String rate_format = null;
	private String repay_time = null; // 总还款期限
	private String repay_time_type = null;
	private String repay_time_type_format = null;

	private TransferActDuserModel duser = null; // 原贷款人
	private TransferActUserModel user = null; // 转让者
	private TransferActTuserModel tuser = null; // 承接者

	private String url = null;
	private String how_much_month = null; // 剩余期限
	private String month_repay_money = null;
	private String all_must_repay_money = null;
	private String left_benjin = null; // 剩余本金
	private String left_benjin_format = null;
	private String left_lixi = null; // 剩余利息
	private String left_lixi_format = null;
	private String remain_time = null;
	private String remain_time_format = null;
	private String app_url = null; // 查看详情

	private String transfer_income_format = null; // 受让收益
	private String transfer_income = null;

	// =========================add
	private int status_format_int = 0;
	private int t_user_id_format_int = 0;

	public int getT_user_id_format_int()
	{
		return t_user_id_format_int;
	}

	public void setT_user_id_format_int(int t_user_id_format_int)
	{
		this.t_user_id_format_int = t_user_id_format_int;
	}

	public int getStatus_format_int()
	{
		return status_format_int;
	}

	public void setStatus_format_int(int status_format_int)
	{
		this.status_format_int = status_format_int;
	}

	public String getTransfer_time_format()
	{
		return transfer_time_format;
	}

	public void setTransfer_time_format(String transfer_time_format)
	{
		this.transfer_time_format = transfer_time_format;
	}

	public String getRepay_time_type_format()
	{
		return repay_time_type_format;
	}

	public void setRepay_time_type_format(String repay_time_type_format)
	{
		this.repay_time_type_format = repay_time_type_format;
	}

	public String getNear_repay_time_format()
	{
		return near_repay_time_format;
	}

	public void setNear_repay_time_format(String near_repay_time_format)
	{
		this.near_repay_time_format = near_repay_time_format;
	}

	public String getRate_format()
	{
		return rate_format;
	}

	public void setRate_format(String rate_format)
	{
		this.rate_format = rate_format;
	}

	public String getTransfer_income()
	{
		return transfer_income;
	}

	public void setTransfer_income(String transfer_income)
	{
		this.transfer_income = transfer_income;
	}

	public String getTransfer_amount_format()
	{
		return transfer_amount_format;
	}

	public void setTransfer_amount_format(String transfer_amount_format)
	{
		this.transfer_amount_format = transfer_amount_format;
	}

	public String getTransfer_income_format()
	{
		return transfer_income_format;
	}

	public void setTransfer_income_format(String transfer_income_format)
	{
		this.transfer_income_format = transfer_income_format;
	}

	private PageModel page = null;

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

	public String getLoad_id()
	{
		return load_id;
	}

	public void setLoad_id(String load_id)
	{
		this.load_id = load_id;
	}

	public String getUser_id()
	{
		return user_id;
	}

	public void setUser_id(String user_id)
	{
		this.user_id = user_id;
	}

	public String getTransfer_amount()
	{
		return transfer_amount;
	}

	public void setTransfer_amount(String transfer_amount)
	{
		this.transfer_amount = transfer_amount;
	}

	public String getLast_repay_time()
	{
		return last_repay_time;
	}

	public void setLast_repay_time(String last_repay_time)
	{
		this.last_repay_time = last_repay_time;
	}

	public String getNear_repay_time()
	{
		return near_repay_time;
	}

	public void setNear_repay_time(String near_repay_time)
	{
		this.near_repay_time = near_repay_time;
	}

	public String getTransfer_number()
	{
		return transfer_number;
	}

	public void setTransfer_number(String transfer_number)
	{
		this.transfer_number = transfer_number;
	}

	public String getT_user_id()
	{
		return t_user_id;
	}

	public void setT_user_id(String t_user_id)
	{
		this.t_user_id = t_user_id;
		this.t_user_id_format_int = SDTypeParseUtil.getIntFromString(t_user_id, 0);
	}

	public String getTransfer_time()
	{
		return transfer_time;
	}

	public void setTransfer_time(String transfer_time)
	{
		this.transfer_time = transfer_time;
	}

	public String getCreate_time()
	{
		return create_time;
	}

	public void setCreate_time(String create_time)
	{
		this.create_time = create_time;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
		this.status_format_int = SDTypeParseUtil.getIntFromString(status, 0);
	}

	public String getCallback_count()
	{
		return callback_count;
	}

	public void setCallback_count(String callback_count)
	{
		this.callback_count = callback_count;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getIcon()
	{
		return icon;
	}

	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	public String getDuser_id()
	{
		return duser_id;
	}

	public void setDuser_id(String duser_id)
	{
		this.duser_id = duser_id;
	}

	public String getRate()
	{
		return rate;
	}

	public void setRate(String rate)
	{
		this.rate = rate;
		if (rate != null)
		{
			this.rate_format = rate + "%";
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
		if (this.repay_time_type != null)
		{
			if (this.repay_time_type.equals("0"))
			{
				this.repay_time_type_format = "天";
			} else
			{
				this.repay_time_type_format = "个月";
			}
		}
	}

	public TransferActDuserModel getDuser()
	{
		return duser;
	}

	public void setDuser(TransferActDuserModel duser)
	{
		this.duser = duser;
	}

	public TransferActUserModel getUser()
	{
		return user;
	}

	public void setUser(TransferActUserModel user)
	{
		this.user = user;
	}

	public TransferActTuserModel getTuser()
	{
		return tuser;
	}

	public void setTuser(TransferActTuserModel tuser)
	{
		this.tuser = tuser;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getHow_much_month()
	{
		return how_much_month;
	}

	public void setHow_much_month(String how_much_month)
	{
		this.how_much_month = how_much_month;
	}

	public String getMonth_repay_money()
	{
		return month_repay_money;
	}

	public void setMonth_repay_money(String month_repay_money)
	{
		this.month_repay_money = month_repay_money;
	}

	public String getAll_must_repay_money()
	{
		return all_must_repay_money;
	}

	public void setAll_must_repay_money(String all_must_repay_money)
	{
		this.all_must_repay_money = all_must_repay_money;
	}

	public String getLeft_benjin()
	{
		return left_benjin;
	}

	public void setLeft_benjin(String left_benjin)
	{
		this.left_benjin = left_benjin;
	}

	public String getLeft_benjin_format()
	{
		return left_benjin_format;
	}

	public void setLeft_benjin_format(String left_benjin_format)
	{
		this.left_benjin_format = left_benjin_format;
	}

	public String getLeft_lixi()
	{
		return left_lixi;
	}

	public void setLeft_lixi(String left_lixi)
	{
		this.left_lixi = left_lixi;
	}

	public String getLeft_lixi_format()
	{
		return left_lixi_format;
	}

	public void setLeft_lixi_format(String left_lixi_format)
	{
		this.left_lixi_format = left_lixi_format;
	}

	public String getRemain_time()
	{
		return remain_time;
	}

	public void setRemain_time(String remain_time)
	{
		this.remain_time = remain_time;
	}

	public String getRemain_time_format()
	{
		return remain_time_format;
	}

	public void setRemain_time_format(String remain_time_format)
	{
		this.remain_time_format = remain_time_format;
	}

	public String getApp_url()
	{
		return app_url;
	}

	public void setApp_url(String app_url)
	{
		this.app_url = app_url;
	}

	public PageModel getPage()
	{
		return page;
	}

	public void setPage(PageModel page)
	{
		this.page = page;
	}

}
