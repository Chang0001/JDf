package com.fanwe.p2p.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SDValidateUtil
{

	public static boolean isCellPhoneNumber(String strMobiles)
	{
		return true;
	}

	/** url地址验证 */
	public static boolean isUrl(String url)
	{
		String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
		Pattern patt = Pattern.compile(regex);
		Matcher matcher = patt.matcher(url);
		boolean isMatch = matcher.matches();
		if (!isMatch)
		{
			return false;
		} else
		{
			return true;
		}
	}
}
