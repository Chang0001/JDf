package com.fanwe.p2p.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.model.act.Uc_Add_BankActItemModel;

public class PopSelectBankAdapter extends BaseAdapter
{

	private List<Uc_Add_BankActItemModel> mListModel = null;
	private Activity mActivity = null;

	public PopSelectBankAdapter(List<Uc_Add_BankActItemModel> listModel, Activity activity)
	{
		this.mListModel = listModel;
		this.mActivity = activity;
	}

	public void updateListViewData(List<Uc_Add_BankActItemModel> listModel)
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
			convertView = mActivity.getLayoutInflater().inflate(R.layout.item_simple_pop_bank_list, null);
			holder.mTxtContent = (TextView) convertView.findViewById(R.id.item_simple_pop_bank_list_txt_content);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		Uc_Add_BankActItemModel model = (Uc_Add_BankActItemModel) getItem(position);
		if (model != null)
		{
			if (model.getName() != null)
			{
				holder.mTxtContent.setText(model.getName());
			}

		}

		return convertView;
	}

	static class ViewHolder
	{
		public TextView mTxtContent = null;
	}

}
