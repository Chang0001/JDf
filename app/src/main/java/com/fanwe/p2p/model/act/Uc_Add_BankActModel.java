package com.fanwe.p2p.model.act;

import java.util.List;

public class Uc_Add_BankActModel extends BaseActModel
{
	private String real_name = null;

	private List<Uc_Add_BankActItemModel> item = null;

	public String getReal_name()
	{
		return real_name;
	}

	public void setReal_name(String real_name)
	{
		this.real_name = real_name;
	}

	public List<Uc_Add_BankActItemModel> getItem()
	{
		return item;
	}

	public void setItem(List<Uc_Add_BankActItemModel> item)
	{
		this.item = item;
	}

}
