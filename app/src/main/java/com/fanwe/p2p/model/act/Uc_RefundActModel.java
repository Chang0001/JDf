package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.PageModel;
import com.fanwe.p2p.model.Uc_RefundActItemModel;

public class Uc_RefundActModel extends BaseActModel
{

	private List<Uc_RefundActItemModel> item = null;
	private PageModel page = null;

	public List<Uc_RefundActItemModel> getItem()
	{
		return item;
	}

	public void setItem(List<Uc_RefundActItemModel> item)
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
