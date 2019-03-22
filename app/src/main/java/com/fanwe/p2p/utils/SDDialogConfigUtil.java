package com.fanwe.p2p.utils;

import android.app.AlertDialog;
import android.view.WindowManager;

import com.fanwe.p2p.application.App;

public class SDDialogConfigUtil
{
	public static AlertDialog setDialogWidth(AlertDialog dialog, int width)
	{
		if (dialog != null)
		{
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			if (width > 0 && width < SDUIUtil.getScreenWidth(App.getApplication()))
			{
				params.width = width;
			}
			dialog.getWindow().setAttributes(params);
		}
		return dialog;
	}

	public static AlertDialog setDialogHeight(AlertDialog dialog, int height)
	{
		if (dialog != null)
		{
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			if (height > 0 && height < SDUIUtil.getScreenWidth(App.getApplication()))
			{
				params.height = height;
			}
			dialog.getWindow().setAttributes(params);
		}
		return dialog;
	}

	public static AlertDialog setDialogWidthAndHeight(AlertDialog dialog, int width, int height)
	{
		return setDialogHeight(setDialogWidth(dialog, width), height);
	}

	public static AlertDialog setDialogPositionX(AlertDialog dialog, int x)
	{
		if (dialog != null)
		{
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			params.x = x;
			dialog.getWindow().setAttributes(params);
		}
		return dialog;
	}

	public static AlertDialog setDialogPositionY(AlertDialog dialog, int y)
	{
		if (dialog != null)
		{
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			params.y = y;
			dialog.getWindow().setAttributes(params);
		}
		return dialog;
	}

	public static AlertDialog setDialogPositionXAndY(AlertDialog dialog, int x, int y)
	{
		return setDialogPositionY(setDialogPositionX(dialog, x), y);
	}

	public static int getDialogHeight(AlertDialog dialog)
	{
		if (dialog != null)
		{
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			return params.height;
		} else
		{
			return -1;
		}
	}

	public static int getDialogWidth(AlertDialog dialog)
	{
		if (dialog != null)
		{
			WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
			return params.width;
		} else
		{
			return -1;
		}
	}

	public static AlertDialog setDialogPositionBottom(AlertDialog dialog)
	{
		return SDDialogConfigUtil.setDialogPositionY(dialog, (SDUIUtil.getScreenHeight(App.getApplication()) - getDialogHeight(dialog)) / 2);
	}

	public static AlertDialog setDialogPositionTop(AlertDialog dialog)
	{
		return SDDialogConfigUtil.setDialogPositionY(dialog, -(SDUIUtil.getScreenHeight(App.getApplication()) - getDialogHeight(dialog)) / 2);
	}

}
