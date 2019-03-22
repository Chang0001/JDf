package com.fanwe.p2p.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.adapter.PayOfflineAdapter;
import com.fanwe.p2p.adapter.PayOfflineAdapter.PayOfflineAdapterListener;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.model.Uc_InchargeActBelow_paymentModel;
import com.fanwe.p2p.utils.SDCollectionUtil;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
/**
 * 充值界面中的线下支付fragment
 * @author js02
 *
 */
public class PayOfflineFragment extends BaseFragment
{
	@ViewInject(id = R.id.frag_pay_offline_lsv_content)
	private ListView mLsvContent = null;

	@ViewInject(id = R.id.frag_pay_offline_et_leave_msg)
	private ClearEditText mEtLeaveMsg = null;

	@ViewInject(id = R.id.frag_pay_offline_txt_empty)
	private TextView mTxtEmpty = null;

	private List<Uc_InchargeActBelow_paymentModel> mListModel = new ArrayList<Uc_InchargeActBelow_paymentModel>();

	private PayOfflineAdapter mAdapter = null;

	public String getLeaveMsg()
	{
		return mEtLeaveMsg.getText().toString();
	}
	
	public void setmListModel(List<Uc_InchargeActBelow_paymentModel> listModel)
	{
		if (listModel != null)
		{

			this.mListModel = listModel;
			if (mListModel.size() > 0)
			{
				mListModel.get(0).setSelect(true);
			}
			mAdapter.updateListViewData(mListModel);
			toggleEmptyMsg();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag_pay_offline, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;
	}

	private void init()
	{
		bindDefaultData();
	}

	private void bindDefaultData()
	{

		mAdapter = new PayOfflineAdapter(mListModel, getActivity(), new PayOfflineAdapterListener()
		{
			@Override
			public void onItemSelect(Uc_InchargeActBelow_paymentModel selectOfflineItem)
			{
				
			}
		});
		mLsvContent.setAdapter(mAdapter);
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		toggleEmptyMsg();

	}

	protected void toggleEmptyMsg()
	{
		if (SDCollectionUtil.isListHasData(mListModel))
		{
			if (mTxtEmpty.getVisibility() == View.VISIBLE)
			{
				mTxtEmpty.setVisibility(View.GONE);
			}
		} else
		{
			if (mTxtEmpty.getVisibility() == View.GONE)
			{
				mTxtEmpty.setVisibility(View.VISIBLE);
			}
		}
	}

	public Uc_InchargeActBelow_paymentModel getSelectItem()
	{
		return mAdapter.getSelectItem();
	}

}