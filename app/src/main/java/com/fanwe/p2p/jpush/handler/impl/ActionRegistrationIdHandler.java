package com.fanwe.p2p.jpush.handler.impl;

import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.config.P2pConfig;
import com.fanwe.p2p.jpush.handler.IJpushActionHandler;
import com.fanwe.p2p.utils.SDToast;

public class ActionRegistrationIdHandler implements IJpushActionHandler
{

	@Override
	public void handle(Intent intent)
	{
		if (intent != null)
		{
			Bundle bundle = intent.getExtras();
			if (bundle != null)
			{
				String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
				if (P2pConfig.setRegistrationId(regId))
				{
					CommonInterface.submitRegistrationID();
				}else
				{
					SDToast.showToast("保存设备推送ID失败，您的设备暂无法收到推送消息!");
				}
			}
		}
	}
	
	
	
	
}
