package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.PageModel;
import com.fanwe.p2p.model.Uc_LendActItemModel;

public class Uc_LendActModel extends BaseActModel
{
	
	private List<Uc_LendActItemModel> item = null;
	private PageModel page = null;
	public List<Uc_LendActItemModel> getItem()
	{
		return item;
	}
	public void setItem(List<Uc_LendActItemModel> item)
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
