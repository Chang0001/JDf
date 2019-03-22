package com.fanwe.p2p.application;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.dao.LocalUserModelDao;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.jpush.observer.IJpushObserver;
import com.fanwe.p2p.jpush.observer.impl.P2pJpushObserver;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RuntimeConfigModel;
import com.fanwe.p2p.receiver.JPushReceiver;
import com.fanwe.p2p.runnable.ReConnectThread;
import com.fanwe.p2p.utils.SDFileUtil;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.busevent.SDEvent;
import com.ta.util.download.DownloadManager;
import com.ta.util.netstate.TANetChangeObserver;
import com.ta.util.netstate.TANetWorkUtil.netType;
import com.ta.util.netstate.TANetworkStateReceiver;

import de.greenrobot.event.EventBus;

public class App extends Application implements TANetChangeObserver, SDEvent
{

	private static App mApp = null;

	private LocalUserModel mLocalUser = null;
	private IJpushObserver mJpushObserver = null; // 极光

	private RuntimeConfigModel mRuntimeConfig = new RuntimeConfigModel();

	public RuntimeConfigModel getmRuntimeConfig()
	{
		return mRuntimeConfig;
	}

	public LocalUserModel getmLocalUser()
	{
		return mLocalUser;
	}

	public void setmLocalUser(LocalUserModel localUser)
	{
		if (localUser != null)
		{
			this.mLocalUser = localUser;
			LocalUserModelDao.getInstance().saveModel(localUser);
		}
	}

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		init();
	}

	private void init()
	{
		mApp = this;
		TANetworkStateReceiver.registerObserver(this);
		EventBus.getDefault().register(this);
		initDownloadManager();
		initLoginState();
		initJpush();
	}

	private void initJpush()
	{
		mJpushObserver = new P2pJpushObserver();
		JPushReceiver.registerObserver(mJpushObserver);
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}

	private void initLoginState()
	{
		this.mLocalUser = LocalUserModelDao.getInstance().getModel();
		CommonInterface.refreshLocalUser();
	}

	private void initDownloadManager()
	{
		if (SDFileUtil.isSdcardExist())
		{
			DownloadManager.getDownloadManager().setRootPath(SDFileUtil.getDiskCacheDir(getApplication(), "download").getAbsolutePath());
			DownloadManager.getDownloadManager().setProgressUpdateTime(1000);
		}
	}

	public static App getApplication()
	{
		return mApp;
	}

	public void exitApp(boolean isBackground)
	{
		releaseResource();
		EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_EXIT_APP));
		if (isBackground)
		{

		} else
		{
			System.exit(0);
		}
	}

	private void releaseResource()
	{
	}

	public void clearAppsLocalUserModel()
	{
		LocalUserModelDao.getInstance().deleteAllModel();
		this.mLocalUser = null;
	}

	@Override
	public void onConnect(netType type)
	{
		if (JPushInterface.isPushStopped(getApplication()))
		{
			new Thread(new ReConnectThread()).start();
		}
	}

	@Override
	public void onDisConnect()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(SDBaseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEventAsync(SDBaseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEventBackgroundThread(SDBaseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEventMainThread(SDBaseEvent e)
	{
		switch (e.getEventTagInt())
		{
		case EventTag.EVENT_LOGOUT_SUCCESS:
			clearAppsLocalUserModel();
			break;

		default:
			break;
		}

	}

	public static String getStringById(int resId)
	{
		return getApplication().getString(resId);
	}

}
