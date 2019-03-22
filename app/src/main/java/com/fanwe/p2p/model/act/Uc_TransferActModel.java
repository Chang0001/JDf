package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.PageModel;
import com.fanwe.p2p.model.TransferModel;

/**
 * Title:债券转让ActModel
 * 
 * @author: yhz CreateTime：2014-6-16 下午2:57:40
 */
public class Uc_TransferActModel extends BaseActModel
{

	private List<TransferModel> item = null;
	private PageModel page = null;

	public List<TransferModel> getItem()
	{
		return item;
	}

	public void setItem(List<TransferModel> item)
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
