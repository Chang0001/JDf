package com.fanwe.p2p.dao;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.common.DbManager;
import com.fanwe.p2p.model.InitActDBModel;
import com.fanwe.p2p.model.act.InitActModel;

/**
 * Title:
 * 
 * @author: yhz CreateTime：2014-6-23 下午3:47:35
 */
public class InitActDBModelDao
{
	public static void saveInitActModel(String content)
	{
		InitActDBModel dbinit = new InitActDBModel();
		// String encryptStr = AESUtil.encrypt(content,
		// ApkConstant.DEFAULT_AES_KEY);
		// dbinit.setInitActContent(encryptStr);
		dbinit.setInitActContent(content);
		DbManager.getFinalDb().deleteAll(InitActDBModel.class);
		DbManager.getFinalDb().save(dbinit);
	}

	public static synchronized InitActModel readInitDB()
	{
		List<InitActDBModel> list = DbManager.getFinalDb().findAll(InitActDBModel.class);
		if (list.size() > 0)
		{
			String dBcontent = list.get(0).getInitActContent();

			return JSON.parseObject(dBcontent, InitActModel.class);
		}
		return null;
	}

	public static synchronized String getSite_domain()
	{
		List<InitActDBModel> list = DbManager.getFinalDb().findAll(InitActDBModel.class);
		if (list.size() > 0)
		{
			String dBcontent = list.get(0).getInitActContent();
			return JSON.parseObject(dBcontent, InitActModel.class).getSite_domain();
		}
		return null;
	}
}
