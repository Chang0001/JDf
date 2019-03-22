package com.fanwe.p2p.model;

/**
 * Title:债券转让model
 * 
 * @author: yhz CreateTime：2014-6-17 上午10:48:15
 */
public class BondBuyModel
{
	private String dlid = null;
	private String name = null;
	private String next_repay_time_format = null;
	private String how_much_month = null;
	private String repay_time = null;
	private String dltid = null;
	private String left_benjin_format = null;
	private String left_lixi_format = null;
	private String transfer_amount_format = null;

	public String getDlid()
	{
		return dlid;
	}

	public void setDlid(String dlid)
	{
		this.dlid = dlid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getNext_repay_time_format()
	{
		return next_repay_time_format;
	}

	public void setNext_repay_time_format(String next_repay_time_format)
	{
		this.next_repay_time_format = next_repay_time_format;
	}

	public String getHow_much_month()
	{
		return how_much_month;
	}

	public void setHow_much_month(String how_much_month)
	{
		this.how_much_month = how_much_month;
	}

	public String getRepay_time()
	{
		return repay_time;
	}

	public void setRepay_time(String repay_time)
	{
		this.repay_time = repay_time;
	}

	public String getDltid()
	{
		return dltid;
	}

	public void setDltid(String dltid)
	{
		this.dltid = dltid;
	}

	public String getLeft_benjin_format()
	{
		return left_benjin_format;
	}

	public void setLeft_benjin_format(String left_benjin_format)
	{
		this.left_benjin_format = left_benjin_format;
	}

	public String getLeft_lixi_format()
	{
		return left_lixi_format;
	}

	public void setLeft_lixi_format(String left_lixi_format)
	{
		this.left_lixi_format = left_lixi_format;
	}

	public String getTransfer_amount_format()
	{
		return transfer_amount_format;
	}

	public void setTransfer_amount_format(String transfer_amount_format)
	{
		this.transfer_amount_format = transfer_amount_format;
	}

}
