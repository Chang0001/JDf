package com.fanwe.p2p.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

public class SDFileUtil
{
	/**
	 * sd卡是否存在
	 * 
	 * @return
	 */
	public static boolean isSdcardExist()
	{
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获得sd卡根目录file对象引用
	 * 
	 * @return
	 */
	public static File getSdCardRootFile()
	{
		if (isSdcardExist())
		{
			return Environment.getExternalStorageDirectory();
		} else
		{
			return null;
		}
	}

	public static boolean saveStringToFile(String content, String filePath)
	{
		if (content == null || TextUtils.isEmpty(filePath))
		{
			return false;
		} else
		{
			File file = new File(filePath);
			PrintWriter pw = null;
			try
			{
				if (!file.exists())
				{
					file.createNewFile();
				}
				if (file.exists())
				{
					pw = new PrintWriter(file);
					// pw = new PrintWriter(new BufferedWriter(
					// new FileWriter(file)));
					pw.write(content);
					return true;
				} else
				{
					return false;
				}
			} catch (Exception e)
			{
				return false;
			} finally
			{
				if (pw != null)
				{
					pw.close();
				}
			}

		}
	}

	@SuppressWarnings("resource")
	public static String readStringFromFile(String filePath)
	{
		if (TextUtils.isEmpty(filePath))
		{
			return null;
		}
		File file = new File(filePath);
		if (file.exists())
		{
			try
			{

				StringBuffer strBuffer = new StringBuffer();
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String strLine = "";
				while ((strLine = reader.readLine()) != null)
				{
					strBuffer.append(strLine);
				}
				return strBuffer.toString();
			} catch (Exception e)
			{
				return null;
			}
		} else
		{
			return null;
		}
	}

	public static File getDiskCacheDir(Context context, String uniqueName)
	{
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
		return new File(cachePath + File.separator + uniqueName);
	}
	
	public static File getExternalCacheDir(Context context)
	{
		final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}

}
