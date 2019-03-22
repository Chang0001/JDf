package com.fanwe.p2p.config;

import com.fanwe.p2p.common.ConfigManager;
import com.fanwe.p2p.utils.AESUtil;

public class P2pConfig
{

	public static boolean setRegistrationId(String registrationId)
	{
		if (registrationId != null)
		{
			String encryptId = AESUtil.encrypt(registrationId);
			return ConfigManager.getConfig().setString(ConfigKey.REGISTRATION_ID, encryptId);
		} else
		{
			return false;
		}
	}

	public static String getRegistrationId()
	{
		String encryptId = ConfigManager.getConfig().getString(ConfigKey.REGISTRATION_ID, null);
		if (encryptId != null)
		{
			return AESUtil.decrypt(encryptId);
		} else
		{
			return null;
		}
	}

	public static class ConfigKey
	{
		public static final String REGISTRATION_ID = "rid";
	}

}
