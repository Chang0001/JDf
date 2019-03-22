package com.fanwe.p2p.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.model.Uc_Money_LogActItemModel;

public class RechargeLogAdapter extends BaseAdapter
{

	private List<Uc_Money_LogActItemModel> mListModel = null;
	private Activity mActivity = null;

	public RechargeLogAdapter(List<Uc_Money_LogActItemModel> listModel, Activity activity)
	{
		this.mListModel = listModel;
		this.mActivity = activity;
	}

	public void updateListViewData(List<Uc_Money_LogActItemModel> listModel)
	{
		if (listModel != null && listModel.size() > 0)
		{
			this.mListModel = listModel;
			this.notifyDataSetChanged();
		}

	}

	@Override
	public int getCount()
	{
		if (mListModel != null)
		{
			return mListModel.size();
		} else
		{
			return 0;
		}

	}

	@Override
	public Object getItem(int position)
	{
		if (mListModel != null && position < mListModel.size())
		{
			return mListModel.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mActivity.getLayoutInflater().inflate(R.layout.item_lsv_recharge_log, null);
			holder.mTxtRechargeLogType = (TextView) convertView.findViewById(R.id.item_lsv_recharge_log_txt_log_type);
			holder.mTxtRechargeLogTime = (TextView) convertView.findViewById(R.id.item_lsv_recharge_log_txt_log_time);
			holder.mTxtRechargeMoney = (TextView) convertView.findViewById(R.id.item_lsv_recharge_log_txt_log_money);
			holder.mTxtRechargeLockMoney = (TextView) convertView.findViewById(R.id.item_lsv_recharge_log_txt_lock_or_withdraw_money);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		Uc_Money_LogActItemModel model = (Uc_Money_LogActItemModel) getItem(position);
		if (model != null)
		{
			//名字
			if (model.getLog_info() != null)
			{
				holder.mTxtRechargeLogType.setText(model.getLog_info());
			} else
			{
				holder.mTxtRechargeLogType.setText("加载中");
			}

			//时间
			if (model.getLog_time_format() != null)
			{
				holder.mTxtRechargeLogTime.setText(model.getLog_time_format());
			} else
			{
				holder.mTxtRechargeLogTime.setText("加载中");
			}

			//金额
			if (model.getMoney_format() != null)
			{
				holder.mTxtRechargeMoney.setText(model.getMoney_format());
			} else
			{
				holder.mTxtRechargeMoney.setText("加载中");
			}

			//冻结/提现
			if (model.getLock_money_format() != null)
			{
				holder.mTxtRechargeLockMoney.setText(model.getLock_money_format());
			} else
			{
				holder.mTxtRechargeLockMoney.setText("加载中");
			}

		}

		return convertView;
	}

	static class ViewHolder
	{
		public TextView mTxtRechargeLogType = null;
		public TextView mTxtRechargeLogTime = null;
		public TextView mTxtRechargeMoney = null;
		public TextView mTxtRechargeLockMoney = null;
	}

}
