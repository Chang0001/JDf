package com.fanwe.p2p.adapter;

import java.util.List;

import com.fanwe.p2p.ProjectDetailActivity;
import com.fanwe.p2p.ProjectDetailWebviewActivity;
import com.fanwe.p2p.R;
import com.fanwe.p2p.common.ImageLoaderManager;
import com.fanwe.p2p.constant.Constant;
import com.fanwe.p2p.model.InitActAdv_ListModel;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.UIHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Title:
 * 
 * @author: yhz CreateTime：2014-6-13 上午10:56:26
 */
public class MainFragImageHeadAdapter extends PagerAdapter
{

	private List<InitActAdv_ListModel> mList;
	private LayoutInflater inflater;
	private DisplayImageOptions mOptions;
	private Activity mActivity;

	public MainFragImageHeadAdapter(List<InitActAdv_ListModel> list, Activity activity, DisplayImageOptions op)
	{
		this.mActivity = activity;
		this.mList = list;
		inflater = activity.getLayoutInflater();
		this.mOptions = op;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeView((View) object);
	}

	@Override
	public int getCount()
	{
		return mList.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position)
	{
		View imageLayout = inflater.inflate(R.layout.frag_main_head_item_pager_image, view, false);
		imageLayout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub

				String data = mList.get(position).getData();
				String open_rul_type = mList.get(position).getOpen_url_type();
				clickListener(data, open_rul_type, position);
			}
		});
		assert imageLayout != null;
		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
		String img = mList.get(position).getImg();
		if (img != null)
			ImageLoaderManager.getImageLoader().displayImage(img, imageView, mOptions, new SimpleImageLoadingListener()
			{
				@Override
				public void onLoadingStarted(String imageUri, View view)
				{
					spinner.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason)
				{
					String message = null;
					switch (failReason.getType())
					{
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case DECODING_ERROR:
						message = "Image can't be decoded";
						break;
					case NETWORK_DENIED:
						message = "Downloads are denied";
						break;
					case OUT_OF_MEMORY:
						message = "Out Of Memory error";
						break;
					case UNKNOWN:
						message = "Unknown error";
						break;
					}

					SDToast.showToast(message, Toast.LENGTH_SHORT);
					spinner.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
				{
					spinner.setVisibility(View.GONE);
				}
			});

		view.addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader)
	{
	}

	@Override
	public Parcelable saveState()
	{
		return null;
	}

	private void clickListener(String data, String open_rul_type, int position)
	{
		if (Integer.valueOf(open_rul_type) == Constant.Init_Adv_List.Init_Adv_List_Open_Url_Type_0)
		{
			UIHelper.showHTML(mActivity, data);
		}
		if (Integer.valueOf(open_rul_type) == Constant.Init_Adv_List.Init_Adv_List_Open_Url_Type_1)
		{
			Intent intent = new Intent(mActivity, ProjectDetailWebviewActivity.class);
			intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "");
			intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, data);
			mActivity.startActivity(intent);
		}
	}
}
