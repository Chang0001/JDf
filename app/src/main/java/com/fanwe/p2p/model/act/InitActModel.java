package com.fanwe.p2p.model.act;

import java.util.List;

import com.fanwe.p2p.model.InitActDeal_cate_listModel;
import com.fanwe.p2p.model.InitActIndex_List;

public class InitActModel extends BaseActModel
{
	private String kf_phone = null;
	private String kf_email = null;
	private String about_info = null;
	private String program_title = null;
	private String site_domain = null;
	private String version = null;
	private String page_size = null;
	private String virtual_money_1 = null;
	private String virtual_money_2 = null;
	private String virtual_money_3 = null;
	private InitActIndex_List index_list = null;
	private List<InitActDeal_cate_listModel> deal_cate_list = null;

	public List<InitActDeal_cate_listModel> getDeal_cate_list()
	{
		return deal_cate_list;
	}

	public void setDeal_cate_list(List<InitActDeal_cate_listModel> deal_cate_list)
	{
		this.deal_cate_list = deal_cate_list;
	}

	public String getKf_phone()
	{
		return kf_phone;
	}

	public void setKf_phone(String kf_phone)
	{
		this.kf_phone = kf_phone;
	}

	public String getKf_email()
	{
		return kf_email;
	}

	public void setKf_email(String kf_email)
	{
		this.kf_email = kf_email;
	}

	public String getAbout_info()
	{
		return about_info;
	}

	public void setAbout_info(String about_info)
	{
		this.about_info = about_info;
	}

	public String getProgram_title()
	{
		return program_title;
	}

	public void setProgram_title(String program_title)
	{
		this.program_title = program_title;
	}

	public String getSite_domain()
	{
		return site_domain;
	}

	public void setSite_domain(String site_domain)
	{
		this.site_domain = site_domain;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getPage_size()
	{
		return page_size;
	}

	public void setPage_size(String page_size)
	{
		this.page_size = page_size;
	}

	public String getVirtual_money_1()
	{
		return virtual_money_1;
	}

	public void setVirtual_money_1(String virtual_money_1)
	{
		this.virtual_money_1 = virtual_money_1;
	}

	public String getVirtual_money_2()
	{
		return virtual_money_2;
	}

	public void setVirtual_money_2(String virtual_money_2)
	{
		this.virtual_money_2 = virtual_money_2;
	}

	public String getVirtual_money_3()
	{
		return virtual_money_3;
	}

	public void setVirtual_money_3(String virtual_money_3)
	{
		this.virtual_money_3 = virtual_money_3;
	}

	public InitActIndex_List getIndex_list()
	{
		return index_list;
	}

	public void setIndex_list(InitActIndex_List index_list)
	{
		this.index_list = index_list;
	}

}
