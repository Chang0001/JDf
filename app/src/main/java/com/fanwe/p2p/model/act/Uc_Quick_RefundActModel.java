package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.Uc_Quick_RefundActDealModel;
import com.fanwe.p2p.model.Uc_Quick_RefundActLoan_ListModel;

public class Uc_Quick_RefundActModel extends BaseActModel
{
	private Uc_Quick_RefundActDealModel deal = null;
	
	private List<Uc_Quick_RefundActLoan_ListModel> loan_list = null;

	

	public Uc_Quick_RefundActDealModel getDeal()
	{
		return deal;
	}

	public void setDeal(Uc_Quick_RefundActDealModel deal)
	{
		this.deal = deal;
	}

	public List<Uc_Quick_RefundActLoan_ListModel> getLoan_list()
	{
		return loan_list;
	}

	public void setLoan_list(List<Uc_Quick_RefundActLoan_ListModel> loan_list)
	{
		this.loan_list = loan_list;
	}
	

}
