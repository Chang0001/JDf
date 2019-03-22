package com.fanwe.p2p.dao;

import java.util.List;

import com.fanwe.p2p.common.DbManager;
import com.fanwe.p2p.model.LocalUserModel;

public class LocalUserModelDao
{

	private static LocalUserModelDao mDao = null;

	private LocalUserModelDao()
	{
	}

	public static synchronized LocalUserModelDao getInstance()
	{
		if (mDao == null)
		{
			mDao = new LocalUserModelDao();
		}
		return mDao;
	}

	public boolean saveModel(LocalUserModel model)
	{
		if (model != null)
		{
			LocalUserModel cloneModel = model.deepClone();
			if (cloneModel != null)
			{
				DbManager.getFinalDb().deleteAll(LocalUserModel.class);
				cloneModel.encryptModel();
				DbManager.getFinalDb().save(cloneModel);
				return true;
			} else
			{
				return false;
			}
		} else
		{
			return false;
		}
	}

	public LocalUserModel getModel()
	{
		List<LocalUserModel> listModel = DbManager.getFinalDb().findAll(LocalUserModel.class);
		if (listModel != null && listModel.size() == 1)
		{
			LocalUserModel model = listModel.get(0);
			model.decryptModel();
			return model;
		} else
		{
			return null;
		}
	}

	public boolean deleteAllModel()
	{
		try
		{
			DbManager.getFinalDb().deleteAll(LocalUserModel.class);
			return true;
		} catch (Exception e)
		{
			return false;
		}
	}

}
