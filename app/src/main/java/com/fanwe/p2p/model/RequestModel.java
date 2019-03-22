package com.fanwe.p2p.model;

import com.fanwe.p2p.constant.Constant.RequestDataType;
import com.fanwe.p2p.constant.Constant.ResponseDataType;

public class RequestModel
{
	private int mRequestDataType = RequestDataType.REQUEST;
	private int mResponseDataType = ResponseDataType.JSON;
	private Object mData = null;

	private boolean isNeedCache = true;

	public RequestModel()
	{
	}

	public RequestModel(Object data)
	{
		this.mData = data;
	}

	public Object getmData()
	{
		return mData;
	}

	public void setmData(Object mData)
	{
		this.mData = mData;
	}

	public int getmRequestDataType()
	{
		return mRequestDataType;
	}

	public void setmRequestDataType(int mRequestDataType)
	{
		this.mRequestDataType = mRequestDataType;
	}

	public int getmResponseDataType()
	{
		return mResponseDataType;
	}

	public void setmResponseDataType(int mResponseDataType)
	{
		this.mResponseDataType = mResponseDataType;
	}

	public boolean isNeedCache()
	{
		return isNeedCache;
	}

	public void setNeedCache(boolean isNeedCache)
	{
		this.isNeedCache = isNeedCache;
	}

}
