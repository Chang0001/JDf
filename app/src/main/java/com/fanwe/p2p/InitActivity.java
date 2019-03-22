package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.dao.InitActDBModelDao;
import com.fanwe.p2p.fragment.MyMainActivity;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.InitActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.service.AppUpgradeService;
import com.fanwe.p2p.utils.SDHandlerUtil;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

public class InitActivity extends BaseActivity
{

	@ViewInject(id = R.id.act_init_txt_msg)
	private TextView mTxtMsg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_init);
		SDIoc.injectView(this);
		init();

	}

	private void init()
	{
		requestInitInterface();
		startUpgradeService();
	}

	private void startUpgradeService()
	{
		Intent updateIntent = new Intent(InitActivity.this, AppUpgradeService.class);
		startService(updateIntent);
	}

	/**
	 * 请求初始化接口
	 */
	private void requestInitInterface()
	{
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "init");
		RequestModel model = new RequestModel(mapData);

		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{

			@Override
			public void onStartInMainThread(Object result)
			{
				// TODO Auto-generated method stub
				super.onStartInMainThread(result);
			}

			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				try
				{

					InitActModel model = JSON.parseObject(content, InitActModel.class);
					return model;
				} catch (Exception e)
				{
					return null;
				}

			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				InitActModel model = (InitActModel) result;
				if (!SDInterfaceUtil.isActModelNull(model))
				{
					// TODO:对初始化返回结果进行处理
					switch (model.getResponse_code())
					{
					case 1:
						InitActDBModelDao.saveInitActModel(content);
						break;

					default:
						break;
					}
				}
			}

			@Override
			public void onFailureInMainThread(Throwable error, String content, Object result)
			{
				startMainActivity();
			}

			@Override
			public void onFinishInMainThread(Object result)
			{
				startMainActivity();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	private void startMainActivity()
	{
		SDHandlerUtil.runOnUiThreadDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				startActivity(new Intent(InitActivity.this, MainActivity.class));
				InitActivity.this.finish();
			}
		}, 1000 * 2);

	}

}
