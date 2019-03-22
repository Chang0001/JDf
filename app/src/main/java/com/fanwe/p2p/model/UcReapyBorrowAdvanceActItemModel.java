package com.fanwe.p2p.model;

import com.fanwe.p2p.utils.SDFormatUtil;

/**
 * Title:
 * 
 * @author: yhz CreateTime：2014-6-24 下午2:34:24
 */
public class UcReapyBorrowAdvanceActItemModel
{
	private String name = null;// 大标题
	private String borrow_amount_format = null;// 借款金额
	private String rate = null;// 利率
	private String repay_money = null;// 已还总额
	private String repay_time = null;// 期限
	private String repay_time_type = null;// 0:天 其他:月
	private String true_month_repay_money = null;
	private String month_manage_money_format = null;// 管理费

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getBorrow_amount_format()
	{
		return borrow_amount_format;
	}

	public void setBorrow_amount_format(String borrow_amount_format)
	{
		this.borrow_amount_format = borrow_amount_format;
	}

	public String getRate()
	{
		return rate;
	}

	public void setRate(String rate)
	{
		this.rate = rate;
	}

	public String getRepay_money()
	{
		return repay_money;
	}

	public void setRepay_money(String repay_money)
	{
		this.repay_money = SDFormatUtil.formatNumberString(repay_money, 2);
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

	public String getTrue_month_repay_money()
	{
		return true_month_repay_money;
	}

	public void setTrue_month_repay_money(String true_month_repay_money)
	{
		this.true_month_repay_money = true_month_repay_money;
	}

	public String getMonth_manage_money_format()
	{
		return month_manage_money_format;
	}

	public void setMonth_manage_money_format(String month_manage_money_format)
	{
		this.month_manage_money_format = month_manage_money_format;
	}

}
