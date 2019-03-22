package com.fanwe.p2p.utils;

import com.fanwe.p2p.customview.SDProgressDialog;
import com.fanwe.p2p.model.act.BaseActModel;

import android.app.Dialog;
import android.text.TextUtils;

public class DialogHelper
{
	public static void setDialogMsg(Dialog sdProgressDialog, BaseActModel model)
	{
		if (sdProgressDialog != null && model != null && sdProgressDialog instanceof SDProgressDialog && !TextUtils.isEmpty(model.getShow_err()))
		{
			SDProgressDialog sdDialog = (SDProgressDialog) sdProgressDialog;
			sdDialog.getmTxtMsg().setText(model.getShow_err());
		}
	}

}
