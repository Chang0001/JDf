package com.fanwe.p2p.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.R.color;
import com.fanwe.p2p.constant.Constant.DealStatus;
import com.fanwe.p2p.constant.Constant.DealStatusString;
import com.fanwe.p2p.customview.RoundProgressBar;
import com.fanwe.p2p.model.DealsActItemModel;

public class PublishedBorrowAdapter extends BaseAdapter
{

	private List<DealsActItemModel> mListModel = null;
	private Activity mActivity = null;

	public PublishedBorrowAdapter(List<DealsActItemModel> listModel, Activity activity)
	{
		this.mListModel = listModel;
		this.mActivity = activity;
	}

	public void updateListViewData(List<DealsActItemModel> listModel)
	{
		if (listModel != null)
		{
			this.mListModel = listModel;
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount()
	{
		return mListModel.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mListModel.get(position);
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
			convertView = mActivity.getLayoutInflater().inflate(R.layout.item_lsv_published_borrow, null);
			holder.mPgbProgress = (RoundProgressBar) convertView.findViewById(R.id.item_lsv_published_borrow_pgb_borrow_progress);
			holder.mTxtAmountOfMoney = (TextView) convertView.findViewById(R.id.item_lsv_published_borrow_txt_amount_of_money);
			holder.mTxtAmountOfMoneyLabel = (TextView) convertView.findViewById(R.id.item_lsv_published_borrow_txt_amount_of_money_label);
			holder.mTxtProjectName = (TextView) convertView.findViewById(R.id.item_lsv_published_borrow_txt_borrow_name);
			holder.mTxtRate = (TextView) convertView.findViewById(R.id.item_lsv_published_borrow_txt_borrow_rate_year);
			holder.mTxtRateLabel = (TextView) convertView.findViewById(R.id.item_lsv_published_borrow_txt_borrow_rate_year_label);
			holder.mTxtStatus = (TextView) convertView.findViewById(R.id.item_lsv_published_borrow_txt_borrow_status);
			holder.mTxtTimeLimit = (TextView) convertView.findViewById(R.id.item_lsv_published_borrow_txt_time_limit);
			holder.mTxtTimeLimitLabel = (TextView) convertView.findViewById(R.id.item_lsv_published_borrow_txt_time_limit_label);
			holder.mLinBottom = (LinearLayout) convertView.findViewById(R.id.item_lsv_my_invest_lin_bottom);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		DealsActItemModel model = (DealsActItemModel) getItem(position);
		if (model != null)
		{
			// --------设置默认颜色---------
			holder.mTxtStatus.setTextColor(mActivity.getResources().getColor(color.text_gray));
			holder.mTxtProjectName.setTextColor(mActivity.getResources().getColor(color.text_gray));
			holder.mTxtAmountOfMoneyLabel.setTextColor(mActivity.getResources().getColor(color.text_gray));
			holder.mTxtRateLabel.setTextColor(mActivity.getResources().getColor(color.text_gray));
			holder.mTxtTimeLimitLabel.setTextColor(mActivity.getResources().getColor(color.text_gray));
			holder.mTxtAmountOfMoney.setTextColor(mActivity.getResources().getColor(color.text_gray));
			holder.mTxtRate.setTextColor(mActivity.getResources().getColor(color.text_orange));
			holder.mTxtTimeLimit.setTextColor(mActivity.getResources().getColor(color.text_black));
			// ------设置默认进度条-----
			holder.mPgbProgress.setCricleColor(mActivity.getResources().getColor(color.bg_pgb_round_normal));
			holder.mPgbProgress.setMax(100);
			holder.mPgbProgress.setRoundWidth(mActivity.getResources().getDimension(R.dimen.width_pgb_round));
			holder.mPgbProgress.setProgress(0);
			holder.mPgbProgress.setTextColor(mActivity.getResources().getColor(color.text_black));
			holder.mPgbProgress.setTextSize(mActivity.getResources().getDimension(R.dimen.text_pgb_round));

			// ------设置进度条-----
			if (!TextUtils.isEmpty(model.getProgress_point()))
			{
				if (model.getDeal_status() != null)
				{
					if (model.getDeal_status().equals(DealStatus.LOANING)) // 借款中
					{
						holder.mPgbProgress.setCricleProgressColor(mActivity.getResources().getColor(color.bg_pgb_round_progress_blue));
					} else
					{
						holder.mPgbProgress.setCricleProgressColor(mActivity.getResources().getColor(color.bg_pgb_round_progress_orange));
					}
				}
				try
				{
					double progress = Double.parseDouble(model.getProgress_point());
					holder.mPgbProgress.setProgress((float) progress);
				} catch (Exception e)
				{
					// TODO: handle exception
				}
			}
			// -------借款类型-------------
			if (model.getDeal_status() != null)
			{
				holder.mLinBottom.setBackgroundColor(mActivity.getResources().getColor(color.transparent));
				if (model.getDeal_status().equals(DealStatus.FAIL))
				{
					holder.mTxtStatus.setText(DealStatusString.FAIL);
				} else if (model.getDeal_status().equals(DealStatus.FULL))
				{
					holder.mTxtStatus.setText(DealStatusString.FULL);
				} else if (model.getDeal_status().equals(DealStatus.LOANING)) // 借款中
				{
					holder.mTxtStatus.setText(DealStatusString.LOANING);
					holder.mTxtStatus.setTextColor(mActivity.getResources().getColor(color.text_blue));
					// holder.mTxtAmountOfMoney.setTextColor(mActivity.getResources().getColor(color.color_loaning_white));
					// holder.mTxtAmountOfMoneyLabel.setTextColor(mActivity.getResources().getColor(color.color_loaning_white));
					// holder.mTxtRate.setTextColor(mActivity.getResources().getColor(color.color_loaning_white));
					// holder.mTxtRateLabel.setTextColor(mActivity.getResources().getColor(color.color_loaning_white));
					// holder.mTxtTimeLimit.setTextColor(mActivity.getResources().getColor(color.color_loaning_white));
					// holder.mTxtTimeLimitLabel.setTextColor(mActivity.getResources().getColor(color.color_loaning_white));
					// holder.mLinBottom.setBackgroundResource(R.drawable.bg_i_want_borrow_lsv_item_loaning_bottom);
				} else if (model.getDeal_status().equals(DealStatus.REPAYMENTED))
				{
					holder.mTxtStatus.setText(DealStatusString.REPAYMENTED);
				} else if (model.getDeal_status().equals(DealStatus.REPAYMENTING))
				{
					holder.mTxtStatus.setText(DealStatusString.REPAYMENTING);
				} else if (model.getDeal_status().equals(DealStatus.WAIT))
				{
					holder.mTxtStatus.setText(DealStatusString.WAIT);
				} else
				{
					holder.mTxtStatus.setText("未找到");
				}
			}

			// -------借款名字--------
			if (model.getName() != null)
			{
				holder.mTxtProjectName.setText(model.getName());
			} else
			{
				holder.mTxtProjectName.setText("未找到");
			}
			// -------金额--------
			if (model.getBorrow_amount_format() != null)
			{
				holder.mTxtAmountOfMoney.setText(model.getBorrow_amount_format());
			} else
			{
				holder.mTxtAmountOfMoney.setText("未找到");
			}
			// -------年利率--------
			if (model.getRate_foramt() != null)
			{
				holder.mTxtRate.setText(model.getRate_foramt() + "%");
			} else
			{
				holder.mTxtRate.setText("未找到");
			}
			// -------期限--------
			if (model.getRepay_time() != null && model.getRepay_time_type_format() != null)
			{
				holder.mTxtTimeLimit.setText(model.getRepay_time() + model.getRepay_time_type_format());
			} else
			{
				holder.mTxtTimeLimit.setText("未找到");
			}

		}

		return convertView;
	}

	static class ViewHolder
	{
		/** 圆形进度条 */
		public RoundProgressBar mPgbProgress = null;
		/** 金额 */
		public TextView mTxtAmountOfMoney = null;
		/** 金额标签 */
		public TextView mTxtAmountOfMoneyLabel = null;
		/** 借款名字 */
		public TextView mTxtProjectName = null;
		/** 利率 */
		public TextView mTxtRate = null;
		/** 利率标签 */
		public TextView mTxtRateLabel = null;
		/** 借款状态 */
		public TextView mTxtStatus = null;
		/** 期限 */
		public TextView mTxtTimeLimit = null;
		/** 期限标签 */
		public TextView mTxtTimeLimitLabel = null;
		/** 底部布局 */
		public LinearLayout mLinBottom = null;
	}

}
