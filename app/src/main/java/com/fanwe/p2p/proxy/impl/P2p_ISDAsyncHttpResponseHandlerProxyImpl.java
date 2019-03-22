package com.fanwe.p2p.proxy.impl;

import org.apache.http.Header;

import android.text.TextUtils;

import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.utils.SDToast;
import com.sunday.busevent.SDBaseEvent;
import com.ta.sunday.proxy.ISDAsyncHttpResponseHandlerProxy;

import de.greenrobot.event.EventBus;

public class P2p_ISDAsyncHttpResponseHandlerProxyImpl implements ISDAsyncHttpResponseHandlerProxy
{

	private RequestModel mModel = null;

	public P2p_ISDAsyncHttpResponseHandlerProxyImpl(RequestModel model)
	{
		this.mModel = model;
	}

	@Override
	public Object beforeOnStartInRequestThread()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterOnStartInRequestThread()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeOnStartInMainThread(Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterOnStartInMainThread(Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeOnProgressInRequestThread(long totalSize, long currentSize, long speed)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterOnProgressInRequestThread(long totalSize, long currentSize, long speed)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeOnProgressInMainThread(long totalSize, long currentSize, long speed, Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterOnProgressInMainThread(long totalSize, long currentSize, long speed, Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeOnFailureInRequestThread(Throwable error, String content)
	{
		if (error == null)
		{
			if (!TextUtils.isEmpty(content))
			{
				SDToast.showToast("错误:" + content);
			} else
			{
				SDToast.showToast("网络请求失败!");
			}
		} else
		{
			SDToast.showToast("错误:" + "网络请求异常!");
		}
		return null;
	}

	@Override
	public Object afterOnFailureInRequestThread(Throwable error, String content)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeOnFailureInMainThread(Throwable error, String content, Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterOnFailureInMainThread(Throwable error, String content, Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeOnSuccessInRequestThread(int statusCode, Header[] headers, String content)
	{
		if (content.contains("\"user_login_status\":0"))
		{
			SDToast.showToast("您已经处于离线状态!");
			EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_LOGOUT_SUCCESS));
		}
		return null;
	}

	@Override
	public Object afterOnSuccessInRequestThread(int statusCode, Header[] headers, String content)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeOnSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterOnSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeOnFinishInRequestThread()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterOnFinishInRequestThread()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object beforeOnFinishInMainThread(Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object afterOnFinishInMainThread(Object result)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
