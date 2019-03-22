package com.fanwe.p2p.model.act;

public class Uc_Save_InchargeActModel extends BaseActModel
{

	private String pay_status = null; // 0:未付款 1:已付款
	private String order_id = null;
	private String payment_notice_id = null;
	private String pay_code = null;
	private String pay_type = null; // 1线上支付 2:线下支付
	private String pay_wap = null; // 线上支付时候才有的支付链接

	public String getPay_status()
	{
		return pay_status;
	}

	public void setPay_status(String pay_status)
	{
		this.pay_status = pay_status;
	}

	public String getOrder_id()
	{
		return order_id;
	}

	public void setOrder_id(String order_id)
	{
		this.order_id = order_id;
	}

	public String getPayment_notice_id()
	{
		return payment_notice_id;
	}

	public void setPayment_notice_id(String payment_notice_id)
	{
		this.payment_notice_id = payment_notice_id;
	}

	public String getPay_code()
	{
		return pay_code;
	}

	public void setPay_code(String pay_code)
	{
		this.pay_code = pay_code;
	}

	public String getPay_type()
	{
		return pay_type;
	}

	public void setPay_type(String pay_type)
	{
		this.pay_type = pay_type;
	}

	public String getPay_wap()
	{
		return pay_wap;
	}

	public void setPay_wap(String pay_wap)
	{
		this.pay_wap = pay_wap;
	}

}
