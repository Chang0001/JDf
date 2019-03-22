package com.fanwe.p2p.model.act;

import com.fanwe.p2p.model.UcReapyBorrowAdvanceActItemModel;

/**
 * Title:提前还款Model
 * 
 * @author: yhz CreateTime：2014-6-24 上午11:25:45
 */
public class UcReapyBorrowAdvanceActModel extends BaseActModel
{
	private UcReapyBorrowAdvanceActItemModel deal = null;

	private String impose_money_format = null;// 罚息

	private String total_repay_money_format = null;// 应付本息

	private String true_total_repay_money_format = null;// 合计还款

	public String getImpose_money_format()
	{
		return impose_money_format;
	}

	public void setImpose_money_format(String impose_money_format)
	{
		this.impose_money_format = impose_money_format;
	}

	public String getTotal_repay_money_format()
	{
		return total_repay_money_format;
	}

	public void setTotal_repay_money_format(String total_repay_money_format)
	{
		this.total_repay_money_format = total_repay_money_format;
	}

	public String getTrue_total_repay_money_format()
	{
		return true_total_repay_money_format;
	}

	public void setTrue_total_repay_money_format(String true_total_repay_money_format)
	{
		this.true_total_repay_money_format = true_total_repay_money_format;
	}

	public UcReapyBorrowAdvanceActItemModel getDeal()
	{
		return deal;
	}

	public void setDeal(UcReapyBorrowAdvanceActItemModel deal)
	{
		this.deal = deal;
	}

}
