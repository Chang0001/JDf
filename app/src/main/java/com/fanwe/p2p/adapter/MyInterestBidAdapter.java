package com.fanwe.p2p.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.R.color;
import com.fanwe.p2p.constant.Constant.DealStatus;
import com.fanwe.p2p.constant.Constant.DealStatusString;
import com.fanwe.p2p.customview.RoundProgressBar;
import com.fanwe.p2p.customview.SDImageCheckBox;
import com.fanwe.p2p.customview.SDImageCheckBox.SDCheckBoxListener;
import com.fanwe.p2p.model.DealsActItemModel;
import com.fanwe.p2p.utils.SDCollectionUtil;
import com.fanwe.p2p.utils.SDTypeParseUtil;
import com.fanwe.p2p.utils.SDViewBinder;
import com.fanwe.p2p.utils.ViewHolder;

public class MyInterestBidAdapter extends SDBaseAdapter<DealsActItemModel>
{
	private boolean mIsDeleteMode = false;

	public MyInterestBidAdapter(List<DealsActItemModel> listModel, Activity activity)
	{
		super(listModel, activity);
		// TODO Auto-generated constructor stub
	}

	public boolean ismIsDeleteMode()
	{
		return mIsDeleteMode;
	}

	public void setmIsDeleteMode(boolean mIsDeleteMode)
	{
		this.mIsDeleteMode = mIsDeleteMode;
		this.notifyDataSetChanged();
	}

	public void setItemsSelectState(boolean select)
	{
		if (mListModel != null)
		{
			for (DealsActItemModel model : mListModel)
			{
				model.setSelect(select);
			}
		}
	}


	class DeleteImageClickListener implements SDCheckBoxListener
	{

		private int nPos = 0;

		public DeleteImageClickListener(int pos)
		{
			super();
			this.nPos = pos;
		}

		@Override
		public void onChecked(boolean isChecked)
		{
			if (SDCollectionUtil.isListHasData(mListModel) && nPos < mListModel.size())
			{
				mListModel.get(nPos).setSelect(isChecked);
			}
		}
	}

	public List<DealsActItemModel> getSelectItems()
	{
		List<DealsActItemModel> listSelect = new ArrayList<DealsActItemModel>();
		if (mListModel != null)
		{
			for (DealsActItemModel model : mListModel)
			{
				if (model.isSelect())
				{
					listSelect.add(model);
				}
			}
		}
		return listSelect;
	}

	public String getSelectIds()
	{
		List<DealsActItemModel> listSelect = getSelectItems();
		if (SDCollectionUtil.isListHasData(listSelect))
		{
			StringBuilder builder = new StringBuilder();
			for (DealsActItemModel model : listSelect)
			{
				builder.append(model.getId());
				builder.append(",");
			}
			String result = builder.toString();
			return result.substring(0, result.length() - 1);
		} else
		{
			return null;
		}
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent, DealsActItemModel model)
	{
		if (convertView == null)
		{
			convertView = mActivity.getLayoutInflater().inflate(R.layout.item_lsv_my_interest_bid, null);
		}
		TextView tvStatus = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_txt_borrow_status); // 状态
		TextView tvProjectName = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_txt_borrow_name); // 项目名称
		TextView tvAmountOfMoney = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_txt_amount_of_money); // 金额
		TextView tvAmountOfMoneyLabel = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_txt_amount_of_money_label);
		TextView tvRate = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_txt_borrow_rate_year); // 年利率
		TextView tvRateLabel = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_txt_borrow_rate_year_label);
		TextView tvTimeLimit = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_txt_time_limit); // 期限
		TextView tvTimeLimitLabel = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_txt_time_limit_label);
		LinearLayout llBottom = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_lin_bottom); // 底部布局
		LinearLayout llTop = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_ll_top); // 头部布局
		LinearLayout llLeft = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_lin_left); // 头部布局
		RoundProgressBar rpbProgress = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_pgb_borrow_progress); // 圆形进度条
		SDImageCheckBox cbSelected = ViewHolder.get(convertView, R.id.item_lsv_my_interest_bid_check_bid_state);

		if (model != null)
		{
			// --------设置默认颜色---------
			SDViewBinder.setTextViewColorByColorId(tvStatus, color.white);
			SDViewBinder.setTextViewColorByColorId(tvProjectName, color.white);
			SDViewBinder.setTextViewColorByColorId(tvAmountOfMoney, color.text_gray);
			SDViewBinder.setTextViewColorByColorId(tvAmountOfMoneyLabel, color.text_gray);
			SDViewBinder.setTextViewColorByColorId(tvRate, color.text_orange);
			SDViewBinder.setTextViewColorByColorId(tvRateLabel, color.text_gray);
			SDViewBinder.setTextViewColorByColorId(tvTimeLimit, color.text_gray);
			SDViewBinder.setTextViewColorByColorId(tvTimeLimitLabel, color.text_gray);
			llBottom.setBackgroundResource(R.drawable.layer_lsv_item_bottom);
			llTop.setBackgroundResource(R.drawable.layer_lsv_item_top);

			// ------设置默认进度条-----
			rpbProgress.setDefaultConfig();
			// ------------------设置选中项------------------
			if (model.isSelect())
			{
				SDViewBinder.setTextViewColorByColorId(tvAmountOfMoney, color.white);
				SDViewBinder.setTextViewColorByColorId(tvAmountOfMoneyLabel, color.white);
				SDViewBinder.setTextViewColorByColorId(tvRate, color.white);
				SDViewBinder.setTextViewColorByColorId(tvRateLabel, color.white);
				SDViewBinder.setTextViewColorByColorId(tvTimeLimit, color.white);
				SDViewBinder.setTextViewColorByColorId(tvTimeLimitLabel, color.white);
				rpbProgress.setTextColor(mActivity.getResources().getColor(color.white));
				llBottom.setBackgroundResource(R.drawable.layer_lsv_item_bottom_select);
			}

			// ------设置进度条-----
			if (!TextUtils.isEmpty(model.getProgress_point()))
			{
				if (model.getDeal_status() != null)
				{
					if (model.getDeal_status().equals(DealStatus.LOANING)) // 借款中
					{
						rpbProgress.setCricleProgressColor(mActivity.getResources().getColor(color.bg_pgb_round_progress_blue));
					} else
					{
						rpbProgress.setCricleProgressColor(mActivity.getResources().getColor(color.bg_pgb_round_progress_orange));
					}
				}
				rpbProgress.setProgress(SDTypeParseUtil.getFloatFromString(model.getProgress_point(), 0));
			}

			// -------借款类型-------------
			if (model.getDeal_status() != null)
			{
				if (model.getDeal_status().equals(DealStatus.FAIL))
				{
					tvStatus.setText(DealStatusString.FAIL);
				} else if (model.getDeal_status().equals(DealStatus.FULL))
				{
					tvStatus.setText(DealStatusString.FULL);
				} else if (model.getDeal_status().equals(DealStatus.LOANING)) // 借款中
				{
					tvStatus.setText(DealStatusString.LOANING);
					llTop.setBackgroundResource(R.drawable.layer_lsv_item_top_borrowing);
				} else if (model.getDeal_status().equals(DealStatus.REPAYMENTED))
				{
					tvStatus.setText(DealStatusString.REPAYMENTED);
				} else if (model.getDeal_status().equals(DealStatus.REPAYMENTING))
				{
					tvStatus.setText(DealStatusString.REPAYMENTING);
				} else if (model.getDeal_status().equals(DealStatus.WAIT))
				{
					tvStatus.setText(DealStatusString.WAIT);
				} else
				{
					tvStatus.setText("未找到");
				}
			}

			// -------借款名字--------
			SDViewBinder.setTextView(tvProjectName, model.getName(), "未找到");
			// -------金额--------
			SDViewBinder.setTextView(tvAmountOfMoney, model.getBorrow_amount_format(), "未找到");
			// -------年利率--------
			SDViewBinder.setTextView(tvRate, model.getRate_foramt(), "未找到");
			// -------期限--------
			if (model.getRepay_time() != null && model.getRepay_time_type_format() != null)
			{
				tvTimeLimit.setText(model.getRepay_time() + model.getRepay_time_type_format());
			} else
			{
				tvTimeLimit.setText("未找到");
			}

			// =========================================
			cbSelected.setmListener(new DeleteImageClickListener(position));
			if (mIsDeleteMode)
			{
				llLeft.setVisibility(View.VISIBLE);
			} else
			{
				llLeft.setVisibility(View.GONE);
			}
			cbSelected.setCheckState(model.isSelect());
			// ============================================
		}

		return convertView;
	}

}
