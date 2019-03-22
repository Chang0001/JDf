package com.fanwe.p2p.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.customview.SDImageCheckBox;
import com.fanwe.p2p.model.Uc_InchargeActBelow_paymentModel;

public class PayOfflineAdapter extends BaseAdapter
{

	private List<Uc_InchargeActBelow_paymentModel> mListModel = null;
	private Activity mActivity = null;
	private PayOfflineAdapterListener mListener = null;

	private int mCurrentSelect = 0;

	public Uc_InchargeActBelow_paymentModel getSelectItem()
	{
		if (mListModel != null && mCurrentSelect >= 0 && mListModel.size() > 0 && mCurrentSelect < mListModel.size())
		{
			return mListModel.get(mCurrentSelect);
		} else
		{
			return null;
		}
	}

	public PayOfflineAdapter(List<Uc_InchargeActBelow_paymentModel> listModel, Activity activity, PayOfflineAdapterListener listener)
	{
		this.mListModel = listModel;
		this.mActivity = activity;
		this.mListener = listener;
	}

	public void updateListViewData(List<Uc_InchargeActBelow_paymentModel> listModel)
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
			convertView = mActivity.getLayoutInflater().inflate(R.layout.item_lsv_pay_offline_method, null);
			holder.mTxtBankName = (TextView) convertView.findViewById(R.id.item_lsv_pay_offline_method_txt_bank_name);
			holder.mTxtBankAccountName = (TextView) convertView.findViewById(R.id.item_lsv_pay_offline_method_txt_bank_account_name);
			holder.mTxtBankAccountLocation = (TextView) convertView.findViewById(R.id.item_lsv_pay_offline_method_txt_bank_account_location);
			holder.mTxtBankAccountNumber = (TextView) convertView.findViewById(R.id.item_lsv_pay_offline_method_txt_bank_account_number);
			holder.mCheckSelect = (SDImageCheckBox) convertView.findViewById(R.id.item_lsv_pay_offline_method_check_select);
			holder.mViewSeperate = convertView.findViewById(R.id.item_lsv_pay_offline_method_view_seperate);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		Uc_InchargeActBelow_paymentModel model = (Uc_InchargeActBelow_paymentModel) getItem(position);
		if (model != null)
		{
			if (model.getPay_name() != null)
			{
				holder.mTxtBankName.setText(model.getPay_name());
			}
			if (model.getPay_account_name() != null)
			{
				holder.mTxtBankAccountName.setText(model.getPay_account_name());
			}
			if (model.getPay_bank() != null)
			{
				holder.mTxtBankAccountLocation.setText(model.getPay_bank());
			}
			if (model.getPay_account() != null)
			{
				holder.mTxtBankAccountNumber.setText(model.getPay_account());
			}
			holder.mCheckSelect.setCheckState(model.isSelect());
			holder.mCheckSelect.setOnClickListener(new PayOnlineAdapter_OnClickListener(position, holder));
		}
		if (mListModel != null && mListModel.size() - 1 == position)
		{
			holder.mViewSeperate.setVisibility(View.GONE);
		} else
		{
			holder.mViewSeperate.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	class PayOnlineAdapter_OnClickListener implements OnClickListener
	{

		private int nPostion = 0;
		private ViewHolder nHolder = null;

		public PayOnlineAdapter_OnClickListener(int nPostion, ViewHolder holder)
		{
			super();
			this.nPostion = nPostion;
			this.nHolder = holder;
		}

		@Override
		public void onClick(View v)
		{
			if (mListModel != null && mListModel.size() > 0 && nPostion < mListModel.size())
			{
				if (mCurrentSelect == nPostion)
				{
					return;
				}
				mListModel.get(nPostion).setSelect(true);

				if (mCurrentSelect != -1)
				{
					mListModel.get(mCurrentSelect).setSelect(false);
				}
				mCurrentSelect = nPostion;
				notifyDataSetChanged();
				if (mListener != null)
				{
					mListener.onItemSelect(mListModel.get(nPostion));
				}
			}
		}

	}

	static class ViewHolder
	{
		public TextView mTxtBankName = null;
		public TextView mTxtBankAccountName = null;
		public TextView mTxtBankAccountLocation = null;
		public TextView mTxtBankAccountNumber = null;
		public View mViewSeperate = null;
		public SDImageCheckBox mCheckSelect = null;
	}

	public interface PayOfflineAdapterListener
	{
		public void onItemSelect(Uc_InchargeActBelow_paymentModel selectOfflineItem);
	}

}
