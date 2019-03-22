package com.fanwe.p2p.common;

import com.fanwe.p2p.application.App;
import com.ta.util.config.TAPreferenceConfig;

/**
 * 
 * shareperference管理类
 * 
 */
public class ConfigManager
{
	private static final String PREFERENCE_NAME = "Fanwe";

	private static TAPreferenceConfig mConfig = null;

	public static TAPreferenceConfig getConfig()
	{
		if (mConfig == null)
		{
			mConfig = TAPreferenceConfig.getPreferenceConfig(App.getApplication());
			mConfig.setFilename(PREFERENCE_NAME);
			if (!mConfig.isLoadConfig())
			{
				mConfig.loadConfig();
			}
		}
		return mConfig;
	}
}
