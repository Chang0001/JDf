package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.Article_listActListModel;
import com.fanwe.p2p.model.PageModel;

public class Article_listActModel extends BaseActModel
{
	
	private PageModel page;
	
	private List<Article_listActListModel> list;

	public PageModel getPage()
	{
		return page;
	}

	public void setPage(PageModel page)
	{
		this.page = page;
	}

	public List<Article_listActListModel> getList()
	{
		return list;
	}

	public void setList(List<Article_listActListModel> list)
	{
		this.list = list;
	}

	
	
}
