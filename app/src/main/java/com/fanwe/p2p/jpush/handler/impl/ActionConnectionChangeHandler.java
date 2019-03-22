package com.fanwe.p2p.jpush.handler.impl;

import android.content.Intent;
import cn.jpush.android.api.JPushInterface;

import com.fanwe.p2p.application.App;
import com.fanwe.p2p.jpush.handler.IJpushActionHandler;
import com.fanwe.p2p.runnable.ReConnectThread;

public class ActionConnectionChangeHandler implements IJpushActionHandler
{
	@Override
	public void handle(Intent intent)
	{
		if (JPushInterface.isPushStopped(App.getApplication()))
		{
			new Thread(new ReConnectThread()).start();
		}
	}
}
