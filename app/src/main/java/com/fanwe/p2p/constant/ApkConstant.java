package com.fanwe.p2p.constant;

public class ApkConstant
{

	public static final String SERVER_API_URL_MID = "10.63.8.122"; // 打包时将这个值修改为客户的网址

	public static final String SERVER_API_URL_PRE = "http://";
	public static final String SERVER_API_URL_END = "/mapi/index.php";

	public static final String SERVER_API_URL = SERVER_API_URL_PRE + SERVER_API_URL_MID + SERVER_API_URL_END;

	public static final String UPDATE_FILENAME_NAME = "fanwe" + "_" + SoftType.P2P;

	public static final class SoftType
	{
		public static final String O2O = "o2o";
		public static final String P2P = "p2p";
	}

	public static final class DeviceType
	{
		public static final String DEVICE_ANDROID = "android";
	}

}
