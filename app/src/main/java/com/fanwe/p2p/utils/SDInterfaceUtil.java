package com.fanwe.p2p.utils;

import android.text.TextUtils;

import com.fanwe.p2p.model.act.BaseActModel;

public class SDInterfaceUtil
{

	public static boolean isActModelNull(BaseActModel actModel)
	{
		if (actModel != null)
		{
			if (!TextUtils.isEmpty(actModel.getShow_err()))
			{
				SDToast.showToast(actModel.getShow_err());
			}
			return false;
		} else
		{
			SDToast.showToast("接口访问失败或者json解析出错!");
			return true;
		}
	}

}
