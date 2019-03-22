package com.fanwe.p2p.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.HttpManager;
import com.fanwe.p2p.constant.ApkConstant;
import com.fanwe.p2p.constant.Constant.RequestDataType;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.proxy.impl.P2p_ISDAsyncHttpResponseHandlerProxyImpl;
import com.fanwe.p2p.utils.MD5Util;
import com.fanwe.p2p.utils.MiGBase64;
import com.fanwe.p2p.utils.SDToast;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;
import com.ta.sunday.proxy.impl.SDAsyncHttpResponseHandlerProxy;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.RequestParams;
import com.ta.util.netstate.TANetWorkUtil;

/**
 * 接口请求类
 */
public class InterfaceServer
{

	private static final String TAG = "InterfaceServer";

	private volatile static InterfaceServer mInterfaceServer = null;

	private List<String> mListNoMd5Act = new ArrayList<String>();

	private InterfaceServer()
	{
		mListNoMd5Act.add("register"); // 注册接口
		mListNoMd5Act.add("save_reset_pwd"); // 忘记密码接口
		mListNoMd5Act.add("uc_save_pwd"); // 修改密码接口

	}

	public static InterfaceServer getInstance()
	{
		if (mInterfaceServer == null)
		{
			synchronized (InterfaceServer.class)
			{
				if (mInterfaceServer == null)
				{
					mInterfaceServer = new InterfaceServer();
				}
			}
		}
		return mInterfaceServer;
	}

	public void requestInterface(RequestModel model, SDAsyncHttpResponseHandler responseListener, boolean isNeedProxy)
	{
		requestInterface(model, true, responseListener, null, isNeedProxy);
	}

	public void requestInterface(RequestModel model, boolean isAsyncRequest, SDAsyncHttpResponseHandler responseListener, boolean isNeedProxy)
	{
		requestInterface(model, isAsyncRequest, responseListener, null, isNeedProxy);
	}

	/**
	 * 请求接口方法
	 * 
	 * @param model
	 *            requestModel
	 * @param isAsyncRequest
	 *            是否异步请求
	 * @param responseListener
	 *            请求结果监听
	 * @param httpClient
	 *            AsyncHttpClient实例,如果该参数不为null则用该对象发起接口请求，否则用全局公用的一个对象发起请求
	 *            要产生AsyncHttpClient对象，调用HttpManager的静态方法产生。
	 */
	public void requestInterface(RequestModel model, boolean isAsyncRequest, SDAsyncHttpResponseHandler responseListener, AsyncHttpClient httpClient, boolean isNeedProxy)
	{

		if (TANetWorkUtil.isNetworkAvailable(App.getApplication()))
		{
			if (model != null)
			{
				RequestParams requestParams = getRequestParamsNoJson(model);
				SDAsyncHttpResponseHandler listener = null;
				if (isNeedProxy)
				{
					listener = getDefaultProxy(responseListener, model);
					// listener = responseListener;
				} else
				{
					listener = responseListener;
				}
				if (httpClient != null)
				{
					httpClient.post(ApkConstant.SERVER_API_URL, requestParams, listener);
				} else
				{
					if (isAsyncRequest)
					{
						HttpManager.getAsyncHttpClient().post(ApkConstant.SERVER_API_URL, requestParams, listener);
					} else
					{
						HttpManager.getSyncHttpClient().post(ApkConstant.SERVER_API_URL, requestParams, listener);
					}
				}
			}
		} else
		{
			SDToast.showToast("当前网络不可用!");
			responseListener.onFinishInMainThread(null);
		}
	}

	private SDAsyncHttpResponseHandlerProxy getDefaultProxy(SDAsyncHttpResponseHandler handler, RequestModel model)
	{
		return new SDAsyncHttpResponseHandlerProxy(new P2p_ISDAsyncHttpResponseHandlerProxyImpl(model), handler);
	}

	private RequestParams getRequestParamsNoJson(RequestModel model)
	{
		RequestParams requestParams = new RequestParams();
		Object data = model.getmData();
		if (data != null)
		{
			Map<String, Object> mapData = (Map<String, Object>) data;
			// printRequestUrl(model);
			String act = null;
			if (mapData.containsKey("act"))
			{
				act = String.valueOf(mapData.get("act"));
			}
			for (Entry<String, Object> entry : mapData.entrySet())
			{
				String key = (entry.getKey()) == null ? "" : (entry.getKey());
				String value = (entry.getValue()) == null ? "" : (String.valueOf(entry.getValue()));
				if (mListNoMd5Act.contains(act))
				{
				} else
				{
					if (key.equals("pwd"))
					{
						value = MD5Util.getMD5(value);
//						Log.i(TAG, "act:" + act + "pwd:" + value);
					}
				}
				requestParams.put(key, value);
			}
			requestParams.put("i_type", String.valueOf(model.getmRequestDataType()));
			requestParams.put("r_type", String.valueOf(model.getmResponseDataType()));
			// requestParams.put("soft_type", ApkConstant.SoftType.P2P);
			// requestParams.put("dev_type",
			// ApkConstant.DeviceType.DEVICE_ANDROID);
			printRequestUrl(model);
		}
		return requestParams;
	}

	private void printRequestUrl(RequestModel model)
	{
		String url = ApkConstant.SERVER_API_URL + "?";
		if (model != null && model.getmData() != null)
		{
			Map<String, Object> mapData = (Map<String, Object>) model.getmData();

			for (Entry<String, Object> entry : mapData.entrySet())
			{
				url = url + entry.getKey() + "=" + entry.getValue() + "&";
			}
			url = url + "i_type=" + String.valueOf(model.getmRequestDataType()) + "&" + "r_type=2";
		}
		Log.i(TAG, url);
	}

	private RequestParams getRequestParams(RequestModel model)
	{
		RequestParams requestParams = new RequestParams();
		Object data = model.getmData();
		if (data != null)
		{
			String requestData = "";
			if (model.getmRequestDataType() == RequestDataType.BASE64)
			{
				requestData = MiGBase64.encode(JSON.toJSONString(data));
			} else if (model.getmRequestDataType() == RequestDataType.REQUEST)
			{
				requestData = JSON.toJSONString(data);
			}
			requestParams.put("requestData", requestData);
			requestParams.put("i_type", String.valueOf(model.getmRequestDataType()));
			requestParams.put("r_type", String.valueOf(model.getmResponseDataType()));
			requestParams.put("soft_type", ApkConstant.SoftType.O2O);
			requestParams.put("dev_type", ApkConstant.DeviceType.DEVICE_ANDROID);
		}
		return requestParams;
	}

}
