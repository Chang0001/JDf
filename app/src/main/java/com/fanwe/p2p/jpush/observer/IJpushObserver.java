package com.fanwe.p2p.jpush.observer;

import android.content.Intent;

/**
 * 极光推送观察者接口
 * @author js02
 *
 */
public interface IJpushObserver
{

	public void onActionRegistrationId(Intent intent);

	public void onActionMessageReceived(Intent intent);

	public void onActionNotificationReceived(Intent intent);

	public void onActionNotificationOpened(Intent intent);

	public void onActionRichpushCallback(Intent intent);

	public void onActionConnectionChange(Intent intent);

	public void onActionUnKnow(Intent intent);

}
