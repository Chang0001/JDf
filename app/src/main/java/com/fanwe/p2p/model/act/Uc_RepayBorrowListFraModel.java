package com.fanwe.p2p.model.act;

import java.util.List;
import com.fanwe.p2p.model.PageModel;
import com.fanwe.p2p.model.Uc_RefundActItemModel;

/**
 * Title:会员中心还款列表
 * 
 * @author: yhz CreateTime：2014-6-20 上午9:19:59
 */
public class Uc_RepayBorrowListFraModel extends BaseActModel
{

	private PageModel page = null;
	private List<Uc_RefundActItemModel> item = null;

	public PageModel getPage()
	{
		return page;
	}

	public void setPage(PageModel page)
	{
		this.page = page;
	}

	public List<Uc_RefundActItemModel> getItem()
	{
		return item;
	}

	public void setItem(List<Uc_RefundActItemModel> item)
	{
		this.item = item;
	}

}
