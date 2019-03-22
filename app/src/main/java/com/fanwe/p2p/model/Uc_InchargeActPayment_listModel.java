package com.fanwe.p2p.model;

public class Uc_InchargeActPayment_listModel
{

	private String id = null;
	private String class_name = null;
	private String description = null;
	private String logo = null;
	private String fee_type = null;
	private String fee_amount = null;

	private boolean isSelect = false;

	public boolean isSelect()
	{
		return isSelect;
	}

	public void setSelect(boolean isSelect)
	{
		this.isSelect = isSelect;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getClass_name()
	{
		return class_name;
	}

	public void setClass_name(String class_name)
	{
		this.class_name = class_name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getLogo()
	{
		return logo;
	}

	public void setLogo(String logo)
	{
		this.logo = logo;
	}

	public String getFee_type()
	{
		return fee_type;
	}

	public void setFee_type(String fee_type)
	{
		this.fee_type = fee_type;
	}

	public String getFee_amount()
	{
		return fee_amount;
	}

	public void setFee_amount(String fee_amount)
	{
		this.fee_amount = fee_amount;
	}

}
