package com.fanwe.p2p.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;

import android.text.TextUtils;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.config.P2pConfig;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.Refresh_UserActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDToast;
import com.sunday.busevent.SDBaseEvent;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

import de.greenrobot.event.EventBus;

public class CommonInterface
{

	public static void refreshLocalUser()
	{
		final LocalUserModel user = App.getApplication().getmLocalUser();
		if (user == null)
		{
			return;
		} else
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "refresh_user");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{

				@Override
				public void onFailureInMainThread(Throwable error, String content, Object result)
				{

				}

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					Refresh_UserActModel model = JSON.parseObject(content, Refresh_UserActModel.class);
					if (model != null)
					{
						if (model.getResponse_code() == 1)
						{
							user.setUserMoney(model.getUser_money());
							user.setUserMoneyFormat(model.getUser_money_format());
							App.getApplication().setmLocalUser(user);
							EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_REFRESH_USER_SUCCESS));
						}
					}
					return model;
				}

				@Override
				public void onStartInMainThread(Object result)
				{

				}

				@Override
				public void onFinishInMainThread(Object result)
				{

				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{

				}

			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}
	}
	
	public static void submitRegistrationID()
	{
		String regId = P2pConfig.getRegistrationId();
		if (!TextUtils.isEmpty(regId))
		{
			final LocalUserModel user = App.getApplication().getmLocalUser();
			if (user != null && user.getId() != null)
			{
				JPushInterface.setAlias(App.getApplication(), user.getId(), new TagAliasCallback()
				{
					@Override
					public void gotResult(int arg0, String arg1, Set<String> arg2)
					{
						switch (arg0)
						{
						case 0: //设置别名成功
							requestSubmitRegistrationID(user);
							break;
							
						default:
							SDToast.showToast("设置设备推送ID失败，您的设备暂无法收到推送消息!");
							break;
						}
					}
				});
			}
		}else
		{
			SDToast.showToast("获取设备推送ID失败，您的设备暂无法收到推送消息!");
		}
	}
	
	private static void requestSubmitRegistrationID(LocalUserModel user)
	{
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "apns");
		mapData.put("email", user.getUserName());
		mapData.put("pwd", user.getUserPassword());
		mapData.put("apns_code", user.getId());
		RequestModel model = new RequestModel(mapData);
		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{
			@Override
			public void onFailureInMainThread(Throwable error, String content, Object result)
			{
				
			}

			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				return null;
			}

			@Override
			public void onStartInMainThread(Object result)
			{

			}

			@Override
			public void onFinishInMainThread(Object result)
			{

			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{

			}

		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}
	
}
