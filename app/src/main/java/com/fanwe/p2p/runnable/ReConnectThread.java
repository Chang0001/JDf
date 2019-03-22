package com.fanwe.p2p.runnable;

import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.fanwe.p2p.application.App;

/**
 * 极光推送重连
 * 
 * @author js02
 * 
 */
public class ReConnectThread implements Runnable
{
	private int mRetryTime = 5;

	@Override
	public void run()
	{
		while (mRetryTime >= 0 && JPushInterface.isPushStopped(App.getApplication()))
		{
			JPushInterface.init(App.getApplication());
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
				break;
			}
			Log.i("ReConnectThread", "retry time:"+mRetryTime+",state:"+JPushInterface.isPushStopped(App.getApplication()));
			mRetryTime--;
		}
	}
}
