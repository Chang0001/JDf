package com.fanwe.p2p.model.act;

import com.fanwe.p2p.model.BondBuyModel;

/**
 * Title:债券转让ActModel 接口uc_transfer
 * 
 * @author: yhz CreateTime：2014-6-17 上午10:44:34
 */
public class BondBuyActModel extends BaseActModel
{

	private BondBuyModel transfer = null;


	public BondBuyModel getTransfer()
	{
		return transfer;
	}

	public void setTransfer(BondBuyModel transfer)
	{
		this.transfer = transfer;
	}

}
