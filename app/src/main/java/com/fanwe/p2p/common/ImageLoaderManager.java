package com.fanwe.p2p.common;

import android.graphics.Bitmap;

import com.fanwe.p2p.R;
import com.fanwe.p2p.application.App;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageLoaderManager
{

	private static ImageLoader mImageLoader = ImageLoader.getInstance();
	private static DisplayImageOptions options = null;

	private ImageLoaderManager()
	{
	}

	public static ImageLoader getImageLoader()
	{
		initImageLoader();
		return mImageLoader;
	}

	public static DisplayImageOptions getOptions()
	{
		if (options == null)
		{
			options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_frag_main_head_empty).showImageOnFail(R.drawable.ic_frag_main_head_error).resetViewBeforeLoading(true)
					.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
		}
		return options;
	}

	public static void setOptions(DisplayImageOptions options)
	{
		ImageLoaderManager.options = options;
	}

	private static void initImageLoader()
	{
		if (!mImageLoader.isInited())
		{
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(App.getApplication()).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
					.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).defaultDisplayImageOptions(getOptions()).build();
			mImageLoader.init(config);
		}
	}

}
