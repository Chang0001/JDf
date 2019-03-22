package com.fanwe.p2p.adapter;

import java.util.List;
import com.fanwe.p2p.R;
import com.fanwe.p2p.RepayBorrowRepayActivity;
import com.fanwe.p2p.UcRepayBorrowAdvanceActivity;
import com.fanwe.p2p.fragment.RepayBorrowListFragment;
import com.fanwe.p2p.model.Uc_RefundActItemModel;
import com.fanwe.p2p.utils.SDFormatUtil;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Title:个人中心偿还贷款还款列表
 * 
 * @author: yhz CreateTime：2014-6-20 下午5:38:14
 */
public class RepayBorrowFragAdapter extends BaseAdapter
{
	private Fragment mFragment = null;
	private List<Uc_RefundActItemModel> mListModel = null;

	public RepayBorrowFragAdapter(Fragment fragment, List<Uc_RefundActItemModel> list)
	{
		this.mFragment = fragment;
		this.mListModel = list;
	}

	public void updateListViewData(List<Uc_RefundActItemModel> listModel)
	{
		if (listModel != null && listModel.size() > 0)
		{
			this.mListModel = listModel;
		}else 
		{
			this.mListModel.clear();
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		if (mListModel != null)
		{
			return mListModel.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
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
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mFragment.getActivity().getLayoutInflater().inflate(R.layout.uc_frag_repay_borrow_listitem, null);
			holder.ucFraRepBorLisItem_tv_subname = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_subname);
			holder.ucFraRepBorLisItem_tv_borrow_amount_format = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_borrow_amount_format);
			holder.ucFraRepBorLisItem_tv_rate = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_rate);
			holder.ucFraRepBorLisItem_tv_repay_time = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_repay_time);
			holder.ucFraRepBorLisItem_tv_true_month_repay_money = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_true_month_repay_money);
			holder.ucFraRepBorLisItem_tv_need_money = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_need_money);
			holder.ucFraRepBorLisItem_tv_next_repay_time = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_next_repay_time);
			holder.ucFraRepBorLisItem_tv_repayment = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_repayment);
			holder.ucFraRepBorLisItem_tv_prepayment = (TextView) convertView.findViewById(R.id.ucFraRepBorLisItem_tv_prepayment);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Uc_RefundActItemModel model = (Uc_RefundActItemModel) getItem(position);
		if (model != null)
		{
			if (model.getSub_name() != null)
			{
				holder.ucFraRepBorLisItem_tv_subname.setText(model.getSub_name());
			}
			if (model.getBorrow_amount_format() != null)
			{
				holder.ucFraRepBorLisItem_tv_borrow_amount_format.setText(model.getBorrow_amount_format());
			}
			if (model.getRate() != null)
			{
				holder.ucFraRepBorLisItem_tv_rate.setText(model.getRate() + "%");
			}
			if (model.getRepay_time_type() != null && model.getRepay_time() != null)
			{

				switch (Integer.valueOf(model.getRepay_time_type()))
				{
				case 0:
					holder.ucFraRepBorLisItem_tv_repay_time.setText("期限" + model.getRepay_time() + "天");
					break;
				default:
					holder.ucFraRepBorLisItem_tv_repay_time.setText("期限" + model.getRepay_time() + "个月");
					break;
				}
			}
			if (model.getTrue_month_repay_money() != null)
			{
				holder.ucFraRepBorLisItem_tv_true_month_repay_money.setText("¥" + model.getTrue_month_repay_money());
			}
			if (model.getNeed_money() != null)
			{
				holder.ucFraRepBorLisItem_tv_need_money.setText(model.getNeed_money());
			}
			if (model.getNext_repay_time_format() != null)
			{
				holder.ucFraRepBorLisItem_tv_next_repay_time.setText(model.getNext_repay_time_format());
			}
		}

		holder.ucFraRepBorLisItem_tv_repayment.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (mListModel.get(position).getId() != null)
				{
					Intent intent = new Intent(mFragment.getActivity(), RepayBorrowRepayActivity.class);
					intent.putExtra(RepayBorrowRepayActivity.EXTRA_DEAL_ID, mListModel.get(position).getId());
					mFragment.startActivityForResult(intent, RepayBorrowListFragment.REQUEST_CODE_REPAY);
				}
			}
		});

		holder.ucFraRepBorLisItem_tv_prepayment.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if (mListModel.get(position).getId() != null)
				{
					Intent intent = new Intent(mFragment.getActivity(), UcRepayBorrowAdvanceActivity.class);
					intent.putExtra(UcRepayBorrowAdvanceActivity.EXTRA_DEAL_ID, mListModel.get(position).getId());
					mFragment.startActivityForResult(intent, RepayBorrowListFragment.REQUEST_CODE_REPAY_ADVANCE);
				}
			}
		});

		return convertView;
	}

	static class ViewHolder
	{
		TextView ucFraRepBorLisItem_tv_subname = null;
		TextView ucFraRepBorLisItem_tv_repayment = null;
		TextView ucFraRepBorLisItem_tv_prepayment = null;
		TextView ucFraRepBorLisItem_tv_borrow_amount_format = null;
		TextView ucFraRepBorLisItem_tv_rate = null;
		TextView ucFraRepBorLisItem_tv_repay_time = null;
		TextView ucFraRepBorLisItem_tv_true_month_repay_money = null;
		TextView ucFraRepBorLisItem_tv_need_money = null;
		TextView ucFraRepBorLisItem_tv_next_repay_time = null;
	}

}
