package com.fanwe.p2p.adapter;

import java.util.List;

import com.fanwe.p2p.R;
import com.fanwe.p2p.constant.Constant;
import com.fanwe.p2p.model.DealsActItemModel;
import com.fanwe.p2p.model.TransferModel;
import com.fanwe.p2p.model.act.Uc_TransferActModel;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Title:债券转让适配器
 * 
 * @author: yhz CreateTime：2014-6-16 下午2:22:17
 */
public class UcTransferFragLeftAdapter extends BaseAdapter
{

	private Activity mActivity = null;
	private List<TransferModel> mListModel = null;

	public UcTransferFragLeftAdapter(Activity activity, List<TransferModel> listModel)
	{
		this.mActivity = activity;
		this.mListModel = listModel;
	}

	public void updateListViewData(List<TransferModel> listModel)
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
	public View getView(int position, View convertView, ViewGroup arg2)
	{
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null)
		{
			holder = new ViewHolder();
			convertView = mActivity.getLayoutInflater().inflate(R.layout.act_transfer_listitem, null);
			holder.actTrans_tv_subname = (TextView) convertView.findViewById(R.id.actTrans_tv_subname);
			holder.actTrans_tv_monthAndrepayTime = (TextView) convertView.findViewById(R.id.actTrans_tv_monthAndrepayTime);
			holder.actTrans_tv_left_benjin_format = (TextView) convertView.findViewById(R.id.actTrans_tv_left_benjin_format);
			holder.actTrans_tv_left_lixi_format = (TextView) convertView.findViewById(R.id.actTrans_tv_left_lixi_format);
			holder.actTrans_tv_transfer_amount_format = (TextView) convertView.findViewById(R.id.actTrans_tv_transfer_amount_format);
			holder.actTrans_tv_final_repay_time_format = (TextView) convertView.findViewById(R.id.actTrans_tv_final_repay_time_format);
			holder.actTrans_tv_tras_status_format = (TextView) convertView.findViewById(R.id.actTrans_tv_tras_status_format);
			convertView.setTag(holder);
		} else
		{
			holder = (ViewHolder) convertView.getTag();
		}

		TransferModel model = (TransferModel) getItem(position);
		if (model != null)
		{
			if (model.getSub_name() != null)
			{
				holder.actTrans_tv_subname.setText(model.getSub_name());
			}
			if (model.getHow_much_month() != null && model.getRepay_time() != null)
			{
				holder.actTrans_tv_monthAndrepayTime.setText(model.getHow_much_month() + "/" + model.getRepay_time());
			}
			if (model.getLeft_benjin_format() != null)
			{
				holder.actTrans_tv_left_benjin_format.setText(model.getLeft_benjin_format());

			}
			if (model.getLeft_lixi_format() != null)
			{
				holder.actTrans_tv_left_lixi_format.setText(model.getLeft_lixi_format());

			}
			if (model.getTransfer_amount_format() != null)
			{
				holder.actTrans_tv_transfer_amount_format.setText(model.getTransfer_amount_format());
			}
			if (model.getFinal_repay_time_format() != null)
			{
				holder.actTrans_tv_final_repay_time_format.setText(model.getFinal_repay_time_format());

			}
			if (model.getTras_status_format() != null)
			{
				if (model.getTras_status_op() != null)
				{
					switch (Integer.valueOf(model.getTras_status_op()))
					{
					case Constant.Tras_Status_Op.Tras_Status_Op_1:
						holder.actTrans_tv_tras_status_format.setBackgroundResource(R.drawable.tv_tras_status_null_format_act_transfer_listitem);
						break;
					case Constant.Tras_Status_Op.Tras_Status_Op_4:
						holder.actTrans_tv_tras_status_format.setBackgroundResource(R.drawable.tv_tras_status_null_format_act_transfer_listitem);
						break;
					case Constant.Tras_Status_Op.Tras_Status_Op_6:
						holder.actTrans_tv_tras_status_format.setBackgroundResource(R.drawable.tv_tras_status_0_format_act_transfer_listitem);
						break;
					default:
						holder.actTrans_tv_tras_status_format.setBackgroundResource(R.drawable.tv_tras_status_1_format_act_transfer_listitem);
						break;
					}
				}
				holder.actTrans_tv_tras_status_format.setText(model.getTras_status_format());
			}
		}

		return convertView;
	}

	static class ViewHolder
	{
		TextView actTrans_tv_tras_status;
		TextView actTrans_tv_subname;
		TextView actTrans_tv_monthAndrepayTime;
		TextView actTrans_tv_left_benjin_format;
		TextView actTrans_tv_left_lixi_format;
		TextView actTrans_tv_transfer_amount_format;
		TextView actTrans_tv_final_repay_time_format;
		TextView actTrans_tv_tras_status_format;
	}
}
