package com.fanwe.p2p.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.model.Uc_LendActItemModel;
import com.fanwe.p2p.utils.SDViewBinder;
import com.fanwe.p2p.utils.ViewHolder;

public class BidRecorderAdapter extends SDBaseAdapter<Uc_LendActItemModel>
{

	public BidRecorderAdapter(List<Uc_LendActItemModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent, Uc_LendActItemModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_lsv_bid_recorder, null);
		}
		TextView tvStatus = ViewHolder.get(convertView, R.id.item_lsv_bid_recorder_txt_borrow_status); // 状态
		TextView tvProjectName = ViewHolder.get(convertView, R.id.item_lsv_bid_recorder_txt_borrow_name); // 项目名字
		TextView tvAmountOfMoney = ViewHolder.get(convertView, R.id.item_lsv_bid_recorder_txt_amount_of_money_bid); // 投标金额
		TextView tvRate = ViewHolder.get(convertView, R.id.item_lsv_bid_recorder_txt_borrow_rate_year); // 年利率
		TextView tvTimeLimit = ViewHolder.get(convertView, R.id.item_lsv_bid_recorder_txt_time_limit); // 期限
		TextView tvBidTime = ViewHolder.get(convertView, R.id.item_lsv_bid_recorder_txt_bid_time); // 投标时间

		if (model != null)
		{
			SDViewBinder.setTextView(tvStatus, model.getDeal_status_format(), App.getStringById(R.string.not_found));
			SDViewBinder.setTextView(tvProjectName, model.getName(), App.getStringById(R.string.not_found));
			SDViewBinder.setTextView(tvAmountOfMoney, model.getMoney_format(), App.getStringById(R.string.not_found));
			SDViewBinder.setTextView(tvRate, model.getRate_format_percent(), App.getStringById(R.string.not_found));
			SDViewBinder.setTextView(tvTimeLimit, model.getRepay_time_format(), App.getStringById(R.string.not_found));
			SDViewBinder.setTextView(tvBidTime, model.getCreate_time_format(), App.getStringById(R.string.not_found));
		}
		return convertView;
	}

}
