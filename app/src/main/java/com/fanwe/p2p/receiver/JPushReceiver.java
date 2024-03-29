package com.fanwe.p2p.receiver;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.fanwe.p2p.jpush.observer.IJpushObserver;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver
{
	private static final String TAG = "JPush";

	private static List<IJpushObserver> mListObserver = new ArrayList<IJpushObserver>();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
		notifyObservers(intent);

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
		{
			String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...
			
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			JPushInterface.reportNotificationOpened(context, bundle.getString(JPushInterface.EXTRA_MSG_ID));

			// 打开自定义的Activity
			// Intent i = new Intent(context, TestActivity.class);
			// i.putExtras(bundle);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// context.startActivity(i);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
		{
			Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
		{
			boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.e(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
		} else
		{
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle)
	{
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet())
		{
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID))
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE))
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else
			{
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// // send msg to MainActivity
	// private void processCustomMessage(Context context, Bundle bundle)
	// {
	// if (MainActivity.isForeground)
	// {
	// String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	// String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	// Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
	// msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
	// if (!ExampleUtil.isEmpty(extras))
	// {
	// try
	// {
	// JSONObject extraJson = new JSONObject(extras);
	// if (null != extraJson && extraJson.length() > 0)
	// {
	// msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
	// }
	// } catch (JSONException e)
	// {
	//
	// }
	//
	// }
	// context.sendBroadcast(msgIntent);
	// }
	// }

	/**
	 * 注册观察者
	 * @param observer
	 */
	public static void registerObserver(IJpushObserver observer)
	{
		if (!mListObserver.contains(observer))
		{
			mListObserver.add(observer);
		}
	}

	/**
	 * 取消观察者
	 * @param observer
	 */
	public static void unRegisterObserver(IJpushObserver observer)
	{
		if (mListObserver != null)
		{
			mListObserver.remove(observer);
		}
	}
	
	/**
	 * 通知观察者
	 * @param intent
	 */
	private void notifyObservers(Intent intent)
	{
		IJpushObserver observer = null;
		for (int i = 0; i < mListObserver.size(); i++)
		{
			observer = mListObserver.get(i);
			if (observer != null)
			{
				if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction()))
				{
					observer.onActionRegistrationId(intent);
				} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
				{
					observer.onActionMessageReceived(intent);
				} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))
				{
					observer.onActionNotificationReceived(intent);
				} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
				{
					observer.onActionNotificationOpened(intent);
				} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction()))
				{
					observer.onActionRichpushCallback(intent);
				} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction()))
				{
					observer.onActionConnectionChange(intent);
				} else
				{
					observer.onActionUnKnow(intent);
				}
			}
		}
	}

}
