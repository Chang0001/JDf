package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.Uc_BankActFee_configModel;
import com.fanwe.p2p.model.Uc_BankActItemModel;

public class Uc_BankActModel extends BaseActModel
{
	private List<Uc_BankActItemModel> item = null;
	private List<Uc_BankActFee_configModel> fee_config = null;

	public List<Uc_BankActFee_configModel> getFee_config()
	{
		return fee_config;
	}

	public void setFee_config(List<Uc_BankActFee_configModel> fee_config)
	{
		this.fee_config = fee_config;
	}

	public List<Uc_BankActItemModel> getItem()
	{
		return item;
	}

	public void setItem(List<Uc_BankActItemModel> item)
	{
		this.item = item;
	}

}
