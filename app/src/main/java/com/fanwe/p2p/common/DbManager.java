package com.fanwe.p2p.common;

import net.tsz.afinal.database.FinalDb;
import net.tsz.afinal.database.FinalDb.DbUpdateListener;
import android.database.sqlite.SQLiteDatabase;

import com.fanwe.p2p.application.App;

public class DbManager
{
	private static final String targetDirectory = null;
	private static final String dbName = "Fanwe.db";
	private static final boolean isDebug = true;
	private static final int dbVersion = 1;

	private static FinalDb mFinalDb = null;

	private DbManager()
	{

	}

	public synchronized static FinalDb getFinalDb()
	{
		if (mFinalDb == null)
		{
			mFinalDb = FinalDb.create(App.getApplication(), targetDirectory, dbName, isDebug, dbVersion, new YPDbUpdateListener());
		}
		return mFinalDb;
	}

	private static class YPDbUpdateListener implements DbUpdateListener
	{

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{

		}

	}

}
