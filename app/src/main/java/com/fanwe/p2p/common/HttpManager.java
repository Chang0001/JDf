package com.fanwe.p2p.common;

import com.ta.SyncHttpClient;
import com.ta.util.http.AsyncHttpClient;

public class HttpManager
{
	private static final int TIME_OUT = 7 * 1000;
	
	private static AsyncHttpClient mAsyncHttpClient = null;
	private static SyncHttpClient mSyncHttpClient = null;
	
	static
	{
		mAsyncHttpClient = new AsyncHttpClient();
		mAsyncHttpClient.setTimeout(TIME_OUT);
		mSyncHttpClient = new SyncHttpClient();
		mSyncHttpClient.setTimeout(TIME_OUT);
	}
	
	

	private HttpManager()
	{
	}

	public static AsyncHttpClient getAsyncHttpClient()
	{
		return mAsyncHttpClient;
	}

	public static AsyncHttpClient getSyncHttpClient()
	{
		return mSyncHttpClient;
	}

	public static AsyncHttpClient newAsyncHttpClient()
	{
		return new AsyncHttpClient();
	}

	public static AsyncHttpClient newSyncHttpClient()
	{
		return new SyncHttpClient();
	}

}
