package com.fanwe.p2p.model;

public class Uc_InchargeActBelow_paymentModel
{

	private String pay_id = null;
	private String bank_id = null;
	private String pay_name = null;
	private String pay_account_name = null;
	private String pay_account = null;
	private String pay_bank = null;

	private boolean isSelect = false;

	public boolean isSelect()
	{
		return isSelect;
	}

	public void setSelect(boolean isSelect)
	{
		this.isSelect = isSelect;
	}

	public String getPay_id()
	{
		return pay_id;
	}

	public void setPay_id(String pay_id)
	{
		this.pay_id = pay_id;
	}

	public String getBank_id()
	{
		return bank_id;
	}

	public void setBank_id(String bank_id)
	{
		this.bank_id = bank_id;
	}

	public String getPay_name()
	{
		return pay_name;
	}

	public void setPay_name(String pay_name)
	{
		this.pay_name = pay_name;
	}

	public String getPay_account_name()
	{
		return pay_account_name;
	}

	public void setPay_account_name(String pay_account_name)
	{
		this.pay_account_name = pay_account_name;
	}

	public String getPay_account()
	{
		return pay_account;
	}

	public void setPay_account(String pay_account)
	{
		this.pay_account = pay_account;
	}

	public String getPay_bank()
	{
		return pay_bank;
	}

	public void setPay_bank(String pay_bank)
	{
		this.pay_bank = pay_bank;
	}

}
