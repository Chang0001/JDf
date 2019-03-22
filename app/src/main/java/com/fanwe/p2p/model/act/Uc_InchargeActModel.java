package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.Uc_InchargeActBelow_paymentModel;
import com.fanwe.p2p.model.Uc_InchargeActPayment_listModel;

public class Uc_InchargeActModel extends BaseActModel
{

	private List<Uc_InchargeActPayment_listModel> payment_list;
	private List<Uc_InchargeActBelow_paymentModel> below_payment;

	public List<Uc_InchargeActPayment_listModel> getPayment_list()
	{
		return payment_list;
	}

	public void setPayment_list(List<Uc_InchargeActPayment_listModel> payment_list)
	{
		this.payment_list = payment_list;
	}

	public List<Uc_InchargeActBelow_paymentModel> getBelow_payment()
	{
		return below_payment;
	}

	public void setBelow_payment(List<Uc_InchargeActBelow_paymentModel> below_payment)
	{
		this.below_payment = below_payment;
	}

}
