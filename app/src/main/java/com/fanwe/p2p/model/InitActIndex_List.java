package com.fanwe.p2p.model;

import java.util.List;

/**
 * Title:
 * 
 * @author: yhz CreateTime：2014-6-13 下午6:12:12
 */
public class InitActIndex_List
{
	private List<InitActAdv_ListModel> adv_list = null;
	private List<DealsActItemModel> deal_list = null;

	public List<InitActAdv_ListModel> getAdv_list()
	{
		return adv_list;
	}

	public void setAdv_list(List<InitActAdv_ListModel> adv_list)
	{
		this.adv_list = adv_list;
	}

	public List<DealsActItemModel> getDeal_list()
	{
		return deal_list;
	}

	public void setDeal_list(List<DealsActItemModel> deal_list)
	{
		this.deal_list = deal_list;
	}

}
