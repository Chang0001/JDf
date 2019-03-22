package com.fanwe.p2p.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.common.ImageLoaderManager;
import com.fanwe.p2p.customview.SDImageCheckBox;
import com.fanwe.p2p.customview.SDImageCheckBox.SDCheckBoxListener;
import com.fanwe.p2p.model.Uc_BankActItemModel;
import com.fanwe.p2p.model.Uc_CollectActItemModel;
import com.fanwe.p2p.utils.SDCollectionUtil;

public class WithdrawBankAdapter extends BaseAdapter
{

	private List<Uc_BankActItemModel> mListModel = null;
	private Activity mActivity = null;
	private boolean isEditMode = false;

	public List<Uc_BankActItemModel> getSelectItems()
	{
		List<Uc_BankActItemModel> listSelect = new ArrayList<Uc_BankActItemModel>();
		if (mListModel != null)
		{
			for (Uc_BankActItemModel model : mListModel)
			{
				if (model.isSelect())
				{
					listSelect.add(model);
				}
			}
		}
		return listSelect;
	}

	public boolean isEditMode()
	{
		return isEditMode;
	}

	public void setEditMode(boolean isEditMode)
	{
		this.isEditMode = isEditMode;
		this.notifyDataSetChanged();
	}

	public WithdrawBankAdapter(List<Uc_BankActItemModel> listModel, Activity activity)
	{
		this.mListModel = listModel;
		this.mActivity = activity;
	}

	public void updateListViewData(List<Uc_BankActItemModel> listModel)
	{
		if (listModel != null && listModel.size() > 0)
		{
			this.mListModel = listModel;
			this.notifyDataSetChanged();
		}
	}

	public void setItemsSelectState(boolean select)
	{
		if (mListModel != null)
		{
			for (Uc_BankActItemModel model : mListModel)
			{
				model.setSelect(select);
			}
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
			convertView = mActivity.getLayoutInflater().inflate(R.layout.item_lsv_withdraw_bank, null);
			holder.mImgBank = (ImageView) convertView.findViewById(R.id.item_lsv_withdraw_bank_img_bank);
			holder.mTxtBankName = (TextView) convertView.findViewById(R.id.item_lsv_withdraw_bank_txt_bank_name);
			holder.mTxtBankCardNumber = (TextView) convertView.findViewById(R.id.item_lsv_withdraw_bank_txt_bank_card_number);
			holder.mTxtBankCardHolderRealName = (TextView) convertView.findViewById(R.id.item_lsv_withdraw_bank_txt_bank_card_holder_real_name);
			holder.mViewSeperate = convertView.findViewById(R.id.item_lsv_withdraw_bank_view_seperate);
			holder.mLinLeft = (LinearLayout) convertView.findViewById(R.id.item_lsv_withdraw_bank_lin_left);
			holder.mCheck = (SDImageCheckBox) convertView.findViewById(R.id.item_lsv_withdraw_bank_check_state);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		Uc_BankActItemModel model = (Uc_BankActItemModel) getItem(position);
		if (model != null)
		{
			if (isEditMode)
			{
				holder.mLinLeft.setVisibility(View.VISIBLE);
				holder.mCheck.setmListener(new WithdrawBankAdapter_SDCheckBoxListener(position));
			} else
			{
				holder.mLinLeft.setVisibility(View.GONE);
			}
			holder.mCheck.setCheckState(model.isSelect());

			if (model.getImg() != null)
			{
				ImageLoaderManager.getImageLoader().displayImage(model.getImg(), holder.mImgBank, ImageLoaderManager.getOptions());
			}
			if (model.getBank_name() != null)
			{
				holder.mTxtBankName.setText(model.getBank_name());
			}
			if (model.getBankcard() != null)
			{
				holder.mTxtBankCardNumber.setText(model.getBankcard());
			}
			if (model.getReal_name() != null)
			{
				holder.mTxtBankCardHolderRealName.setText(model.getReal_name());
			}

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

	class WithdrawBankAdapter_SDCheckBoxListener implements SDCheckBoxListener
	{

		private int nPostion = 0;

		public WithdrawBankAdapter_SDCheckBoxListener(int nPostion)
		{
			super();
			this.nPostion = nPostion;
		}

		@Override
		public void onChecked(boolean isChecked)
		{
			if (SDCollectionUtil.isListHasData(mListModel) && nPostion < mListModel.size())
			{
				mListModel.get(nPostion).setSelect(isChecked);
			}
		}

	}

	static class ViewHolder
	{
		public LinearLayout mLinLeft = null;
		public ImageView mImgBank = null;
		public TextView mTxtBankName = null;
		public TextView mTxtBankCardNumber = null;
		public TextView mTxtBankCardHolderRealName = null;
		public View mViewSeperate = null;
		public SDImageCheckBox mCheck = null;
	}

}
