package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.PageModel;
import com.fanwe.p2p.model.Uc_Money_LogActItemModel;

public class Uc_Money_LogActModel extends BaseActModel
{
	private List<Uc_Money_LogActItemModel> item = null;
	private PageModel page = null;

	public List<Uc_Money_LogActItemModel> getItem()
	{
		return item;
	}

	public void setItem(List<Uc_Money_LogActItemModel> item)
	{
		this.item = item;
	}

	public PageModel getPage()
	{
		return page;
	}

	public void setPage(PageModel page)
	{
		this.page = page;
	}

}
