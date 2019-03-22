package com.fanwe.p2p.jpush.handler.impl;

import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fanwe.p2p.BaseActivity;
import com.fanwe.p2p.ProjectDetailWebviewActivity;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.constant.Constant.PushType;
import com.fanwe.p2p.jpush.handler.IJpushActionHandler;
import com.fanwe.p2p.utils.SDPackageUtil;

public class ActionNotificationOpenedHandler implements IJpushActionHandler
{

	@Override
	public void handle(Intent intent)
	{
		Bundle bundle = intent.getExtras();
		if (bundle != null)
		{
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			if (extras != null)
			{
				JSONObject obj = JSON.parseObject(extras);
				if (obj != null)
				{
					int type = obj.getIntValue("type");
					String data = obj.getString("data");
					String title = obj.getString("title");
					switch (type)
					{
					case PushType.NORMAL:
						SDPackageUtil.startCurrentApp();
						break;
					case PushType.PROJECT_ID:
						SDPackageUtil.startCurrentApp();
						break;
					case PushType.ARTICLE_ID:
						 Intent iArticle = new Intent(App.getApplication(), ProjectDetailWebviewActivity.class);
						 iArticle.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, title);
						 iArticle.putExtra(ProjectDetailWebviewActivity.EXTRA_ARTICLE_ID, data);
						 iArticle.putExtra(BaseActivity.EXTRA_IS_START_BY_NOTIFICATION, true);
						 iArticle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						 startProjectDetailWebviewActivity(iArticle);
						break;
					case PushType.URL:
						 Intent iUrl = new Intent(App.getApplication(), ProjectDetailWebviewActivity.class);
						 iUrl.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, title);
						 iUrl.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, data);
						 iUrl.putExtra(BaseActivity.EXTRA_IS_START_BY_NOTIFICATION, true);
						 iUrl.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						 startProjectDetailWebviewActivity(iUrl);
						break;

					default:
						break;
					}
				}else
				{
					SDPackageUtil.startCurrentApp();
				}
			} else
			{
				SDPackageUtil.startCurrentApp();
			}

		}
	}
	
	private void startProjectDetailWebviewActivity(Intent intent)
	{
		if (App.getApplication().getmRuntimeConfig().getProjectDetailWebviewActivity() != null)
		{
			App.getApplication().getmRuntimeConfig().getProjectDetailWebviewActivity().finish();
			App.getApplication().getmRuntimeConfig().setProjectDetailWebviewActivity(null);
		}
		App.getApplication().startActivity(intent);
	}
	
	
	

}
