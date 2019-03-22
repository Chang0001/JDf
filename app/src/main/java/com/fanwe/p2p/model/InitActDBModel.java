package com.fanwe.p2p.model;

import net.tsz.afinal.database.annotation.sqlite.Id;

/**
 * Title:初始化数据存储类
 * 
 * @author: yhz CreateTime：2014-6-12 下午4:37:09
 */
public class InitActDBModel
{
	@Id
	private int id;
	private String initActContent;

	public String getInitActContent()
	{
		return initActContent;
	}

	public void setInitActContent(String initActContent)
	{
		this.initActContent = initActContent;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
