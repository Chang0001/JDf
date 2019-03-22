package com.fanwe.p2p.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.common.ImageLoaderManager;
import com.fanwe.p2p.customview.SDImageCheckBox;
import com.fanwe.p2p.model.Uc_InchargeActPayment_listModel;

public class PayOnlineAdapter extends BaseAdapter
{

	private List<Uc_InchargeActPayment_listModel> mListModel = null;
	private Activity mActivity = null;

	private int mCurrentSelect = 0;

	public Uc_InchargeActPayment_listModel getSelectItem()
	{
		if (mListModel != null && mCurrentSelect >= 0 && mListModel.size() > 0 && mCurrentSelect < mListModel.size())
		{
			return mListModel.get(mCurrentSelect);
		} else
		{
			return null;
		}
	}

	public PayOnlineAdapter(List<Uc_InchargeActPayment_listModel> listModel, Activity activity)
	{
		this.mListModel = listModel;
		this.mActivity = activity;
	}

	public void updateListViewData(List<Uc_InchargeActPayment_listModel> listModel)
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
			convertView = mActivity.getLayoutInflater().inflate(R.layout.item_lsv_pay_online_method, null);
			holder.mImgMethod = (ImageView) convertView.findViewById(R.id.item_lsv_pay_online_method_img_method);
			holder.mTxtMethodName = (TextView) convertView.findViewById(R.id.item_lsv_pay_online_method_txt_method_name);
			holder.mTxtMethodDescription = (TextView) convertView.findViewById(R.id.item_lsv_pay_online_method_txt_method_description);
			holder.mCheckSelect = (SDImageCheckBox) convertView.findViewById(R.id.item_lsv_pay_online_method_check_select);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		Uc_InchargeActPayment_listModel model = (Uc_InchargeActPayment_listModel) getItem(position);
		if (model != null)
		{
			if (model.getLogo() != null)
			{
				ImageLoaderManager.getImageLoader().displayImage(model.getLogo(), holder.mImgMethod, ImageLoaderManager.getOptions());
			}
			if (model.getClass_name() != null)
			{
				holder.mTxtMethodName.setText(model.getClass_name());
			}
			if (model.getDescription() != null && model.getDescription().length() > 0)
			{
				holder.mTxtMethodDescription.setText(model.getDescription());
			} else
			{
				holder.mTxtMethodDescription.setVisibility(View.GONE);
			}
			if (model.isSelect())
			{
				holder.mCheckSelect.setCheckState(true);
			} else
			{
				holder.mCheckSelect.setCheckState(false);
			}
			if (model.isSelect())
			{
				holder.mCheckSelect.setCheckState(true);
			} else
			{
				holder.mCheckSelect.setCheckState(false);
			}
			holder.mCheckSelect.setOnClickListener(new PayOnlineAdapter_SDCheckBoxListener(position, holder));
		}
		return convertView;
	}

	class PayOnlineAdapter_SDCheckBoxListener implements OnClickListener
	{

		private int nPostion = 0;
		private ViewHolder nHolder = null;

		public PayOnlineAdapter_SDCheckBoxListener(int nPostion, ViewHolder holder)
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
			}
		}

	}

	static class ViewHolder
	{
		public ImageView mImgMethod = null;
		public TextView mTxtMethodName = null;
		public TextView mTxtMethodDescription = null;
		public SDImageCheckBox mCheckSelect = null;
	}

}
