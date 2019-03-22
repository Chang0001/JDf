package com.fanwe.p2p.utils;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class SDIntentUtil
{
	/**
	 * 获得打开本地图库的intent
	 * 
	 * @return
	 */
	public static Intent getSelectLocalImageIntent()
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra("crop", true);
		intent.putExtra("return-data", true);
		return intent;
	}

	/**
	 * 获调用拍照的intent
	 * 
	 * @return
	 */
	public static Intent getTakePhotoIntent(File saveFile)
	{
		if (saveFile == null)
		{
			return null;
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));
		return intent;
	}

	public static Intent getCallNumberIntent(String number)
	{
		Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
		return intent;
	}
}
