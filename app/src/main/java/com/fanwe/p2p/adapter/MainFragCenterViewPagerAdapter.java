/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.p2p.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fanwe.p2p.ProjectDetailActivity;
import com.fanwe.p2p.R;
import com.fanwe.p2p.R.color;
import com.fanwe.p2p.constant.Constant.DealStatus;
import com.fanwe.p2p.constant.Constant.DealStatusString;
import com.fanwe.p2p.customview.RoundProgressBar;
import com.fanwe.p2p.model.DealsActItemModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * @author yhz
 * @version v1.0
 * @date：2013-11-28 上午10:58:26
 */
public class MainFragCenterViewPagerAdapter extends PagerAdapter
{

	private Activity mActivity;

	private List<DealsActItemModel> mListModel = null;

	private LayoutInflater inflater = null;

	public MainFragCenterViewPagerAdapter(Activity activity, List<DealsActItemModel> listModel)
	{
		this.mListModel = listModel;
		this.inflater = activity.getLayoutInflater();
		this.mActivity = activity;
	}

	@Override
	public int getCount()
	{
		return mListModel.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == (arg1);
	}

	@Override
	public Object instantiateItem(View container, final int position)
	{

		View v = inflater.inflate(R.layout.frag_main_center_item, null);
		RoundProgressBar rProgressBar = (RoundProgressBar) v.findViewById(R.id.frag_main_center_item_rp);
		setRoundProgressBarStyle(rProgressBar);
		if (mListModel.get(position).getDeal_status().equals(DealStatus.LOANING)) // 借款中
		{
			rProgressBar.setCricleProgressColor(mActivity.getResources().getColor(color.bg_pgb_round_progress_blue));
		} else
		{
			rProgressBar.setCricleProgressColor(mActivity.getResources().getColor(color.bg_pgb_round_progress_orange));
		}

		try
		{
			double progress = Double.parseDouble(mListModel.get(position).getProgress_point());
			rProgressBar.setProgress((float) progress);
		} catch (Exception e)
		{
			// TODO: handle exception
		}

		TextView tv_deal_status = (TextView) v.findViewById(R.id.frag_main_center_item_tv_deal_status);
		tv_deal_status.setBackgroundColor(mActivity.getResources().getColor(color.transparent));
		if (mListModel.get(position).getDeal_status().equals(DealStatus.WAIT))
		{
			tv_deal_status.setText(DealStatusString.WAIT);
		} else if (mListModel.get(position).getDeal_status().equals(DealStatus.LOANING))
		{
			tv_deal_status.setText(DealStatusString.LOANING);
			tv_deal_status.setTextColor(mActivity.getResources().getColor(color.text_blue));
		} else if (mListModel.get(position).getDeal_status().equals(DealStatus.FULL))
		{
			tv_deal_status.setText(DealStatusString.FULL);
		} else if (mListModel.get(position).getDeal_status().equals(DealStatus.FAIL))
		{
			tv_deal_status.setText(DealStatusString.FAIL);
		} else if (mListModel.get(position).getDeal_status().equals(DealStatus.REPAYMENTING))
		{
			tv_deal_status.setText(DealStatusString.REPAYMENTING);
		} else if (mListModel.get(position).getDeal_status().equals(DealStatus.REPAYMENTED))
		{
			tv_deal_status.setText(DealStatusString.REPAYMENTED);
		} else
		{
			tv_deal_status.setText("未知");
		}

		TextView tv_sub_name = (TextView) v.findViewById(R.id.frag_main_center_item_tv_sub_name);
		tv_sub_name.setText(mListModel.get(position).getSub_name());

		TextView tv_amount = (TextView) v.findViewById(R.id.frag_main_center_item_tv_amount);
		tv_amount.setText(mListModel.get(position).getBorrow_amount_format());

		TextView tv_rate = (TextView) v.findViewById(R.id.frag_main_center_item_tv_rate);
		tv_rate.setText(mListModel.get(position).getRate() + "%");

		TextView tv_deadline = (TextView) v.findViewById(R.id.frag_main_center_item_tv_deadline);
		tv_deadline.setText(mListModel.get(position).getRepay_time() + "个月");
		v.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(mActivity, ProjectDetailActivity.class);
				intent.putExtra(ProjectDetailActivity.EXTRA_DEALS_ITEM_MODEL, mListModel.get(position));
				mActivity.startActivity(intent);
			}
		});
		((ViewPager) container).addView(v);
		return v;
	}

	// ------设置进度条-----
	private void setRoundProgressBarStyle(RoundProgressBar rProgressBar)
	{
		rProgressBar.setCricleColor(color.bg_pgb_round_normal);
		rProgressBar.setMax(100);
		rProgressBar.setRoundWidth(mActivity.getResources().getDimension(R.dimen.width_pgb_round));
		rProgressBar.setProgress(0);
		rProgressBar.setTextColor(mActivity.getResources().getColor(color.text_black));
		rProgressBar.setTextSize(mActivity.getResources().getDimension(R.dimen.text_pgb_round));
	}

	/**
	 * 描述：移除View.
	 * 
	 * @param container
	 *            the container
	 * @param position
	 *            the position
	 * @param object
	 *            the object
	 * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.View,
	 *      int, java.lang.Object)
	 */
	@Override
	public void destroyItem(View container, int position, Object object)
	{
		((ViewPager) container).removeView((View) object);
	}

	/**
	 * 描述：很重要，否则不能notifyDataSetChanged.
	 * 
	 * @param object
	 *            the object
	 * @return the item position
	 * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
	 */
	@Override
	public int getItemPosition(Object object)
	{
		return POSITION_NONE;
	}

}
