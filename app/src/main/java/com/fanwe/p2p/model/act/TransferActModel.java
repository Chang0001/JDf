package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.PageModel;
import com.fanwe.p2p.model.TransferActItemModel;

public class TransferActModel extends BaseActModel
{

	private List<TransferActItemModel> item = null;
	private PageModel page = null;

	public List<TransferActItemModel> getItem()
	{
		return item;
	}

	public void setItem(List<TransferActItemModel> item)
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
