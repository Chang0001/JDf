package com.fanwe.p2p.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.model.TransferActItemModel;
import com.fanwe.p2p.model.TransferActTuserModel;
import com.fanwe.p2p.model.TransferActUserModel;
import com.fanwe.p2p.utils.SDViewBinder;
import com.fanwe.p2p.utils.ViewHolder;

public class BondTransferAdapter extends SDBaseAdapter<TransferActItemModel>
{

	private String mStrNotHave = null;
	private String mStrHasTransfer = null;
	private String mStrLeftTimeColon = null;

	public BondTransferAdapter(List<TransferActItemModel> listModel, Activity activity)
	{
		super(listModel, activity);
		mStrNotHave = App.getStringById(R.string.not_have);
		mStrHasTransfer = App.getStringById(R.string.has_transfer);
		mStrLeftTimeColon = App.getStringById(R.string.left_time_colon);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent, TransferActItemModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_lsv_bond_transfer, null);
		}
		TextView tvTransferUserName = ViewHolder.get(convertView, R.id.item_lsv_bond_transfer_txt_user_transfer); // 转让者名字
		TextView tvDealsNameReal = ViewHolder.get(convertView, R.id.item_lsv_bond_transfer_txt_deals_name_real); // 转让项目名字
		TextView tvDealsBenjin = ViewHolder.get(convertView, R.id.item_lsv_bond_transfer_txt_benjin); // 本金
		TextView tvDealsLixi = ViewHolder.get(convertView, R.id.item_lsv_bond_transfer_txt_lixi); // 利息
		TextView tvDealsTransferMoney = ViewHolder.get(convertView, R.id.item_lsv_bond_transfer_txt_transfer_money); // 转让价
		TextView tvDealsRate = ViewHolder.get(convertView, R.id.item_lsv_bond_transfer_txt_rate); // 利率
		TextView tvDealsRemainTimeLabel = ViewHolder.get(convertView, R.id.item_lsv_bond_transfer_txt_remain_time_label); // 剩余时间
		TextView tvDealsTransferBuyUserName = ViewHolder.get(convertView, R.id.item_lsv_bond_transfer_txt_user_buy); // 承接人

		if (model != null)
		{
			TransferActUserModel user = model.getUser();
			if (user != null)
			{
				SDViewBinder.setTextView(tvTransferUserName, user.getUser_name(), App.getStringById(R.string.not_found));
			}
			SDViewBinder.setTextView(tvDealsNameReal, model.getName(), App.getStringById(R.string.not_found));
			SDViewBinder.setTextView(tvDealsBenjin, model.getLeft_benjin_format(), App.getStringById(R.string.not_found));
			SDViewBinder.setTextView(tvDealsLixi, model.getLeft_lixi_format(), App.getStringById(R.string.not_found));
			SDViewBinder.setTextView(tvDealsTransferMoney, model.getTransfer_amount_format(), App.getStringById(R.string.not_found));
			SDViewBinder.setTextView(tvDealsRate, model.getRate_format(), App.getStringById(R.string.not_found));

			if (model.getT_user_id_format_int() == 0 && model.getStatus_format_int() == 1) // 可转让才显示时间
			{
				tvDealsRemainTimeLabel.setText(mStrLeftTimeColon + model.getRemain_time_format());
			} else
			{
				tvDealsRemainTimeLabel.setText(mStrHasTransfer);
			}

			TransferActTuserModel tUser = model.getTuser();
			if (tUser != null)
			{
				SDViewBinder.setTextView(tvDealsTransferBuyUserName, tUser.getUser_name(), App.getStringById(R.string.not_found));
			} else
			{
				tvDealsTransferBuyUserName.setText(mStrNotHave);
			}
		}

		return convertView;
	}

}
