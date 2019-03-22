package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.DealsActItemModel;
import com.fanwe.p2p.model.PageModel;

public class Uc_BorrowedActModel extends BaseActModel
{
	private List<DealsActItemModel> item = null;

	private PageModel page = null;

	public List<DealsActItemModel> getItem()
	{
		return item;
	}

	public void setItem(List<DealsActItemModel> item)
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
