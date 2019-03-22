package com.fanwe.p2p.utils;

import android.os.Handler;
import android.os.Looper;

public class SDHandlerUtil
{

	private static Handler handler = new Handler(Looper.getMainLooper());

	public synchronized static void runOnUiThread(Runnable r)
	{
		if (Looper.myLooper() != Looper.getMainLooper())
		{
			handler.post(r);
		} else
		{
			r.run();
		}
	}

	public synchronized static void runOnUiThreadFrontOfQueue(Runnable r)
	{
		handler.postAtFrontOfQueue(r);
	}

	public synchronized static void runOnUiThreadAtTime(Runnable r, long uptimeMillis)
	{
		handler.postAtTime(r, uptimeMillis);
	}

	public synchronized static void runOnUiThreadAtTime(Runnable r, Object msgObj, long uptimeMillis)
	{
		handler.postAtTime(r, msgObj, uptimeMillis);
	}

	public synchronized static void runOnUiThreadDelayed(Runnable r, long delayMillis)
	{
		handler.postDelayed(r, delayMillis);
	}

}
