package com.fanwe.p2p.jpush.observer.impl;

import android.content.Intent;

import com.fanwe.p2p.jpush.handler.impl.ActionConnectionChangeHandler;
import com.fanwe.p2p.jpush.handler.impl.ActionMessageReceivedHandler;
import com.fanwe.p2p.jpush.handler.impl.ActionNotificationOpenedHandler;
import com.fanwe.p2p.jpush.handler.impl.ActionNotificationReceivedHandler;
import com.fanwe.p2p.jpush.handler.impl.ActionRegistrationIdHandler;
import com.fanwe.p2p.jpush.handler.impl.ActionRichpushCallbackHandler;
import com.fanwe.p2p.jpush.handler.impl.ActionUnKnowHandler;
import com.fanwe.p2p.jpush.observer.IJpushObserver;

public class P2pJpushObserver implements IJpushObserver
{

	@Override
	public void onActionRegistrationId(Intent intent)
	{
		new ActionRegistrationIdHandler().handle(intent);
	}

	@Override
	public void onActionMessageReceived(Intent intent)
	{
		new ActionMessageReceivedHandler().handle(intent);
	}

	@Override
	public void onActionNotificationReceived(Intent intent)
	{
		new ActionNotificationReceivedHandler().handle(intent);
	}

	@Override
	public void onActionNotificationOpened(Intent intent)
	{
		new ActionNotificationOpenedHandler().handle(intent);
	}

	@Override
	public void onActionRichpushCallback(Intent intent)
	{
		new ActionRichpushCallbackHandler().handle(intent);
	}

	@Override
	public void onActionConnectionChange(Intent intent)
	{
		new ActionConnectionChangeHandler().handle(intent);
	}

	@Override
	public void onActionUnKnow(Intent intent)
	{
		new ActionUnKnowHandler().handle(intent);
	}

}
