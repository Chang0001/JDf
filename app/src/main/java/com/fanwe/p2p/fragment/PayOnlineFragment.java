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
import com.fanwe.p2p.adapter.PayOnlineAdapter;
import com.fanwe.p2p.model.Uc_InchargeActPayment_listModel;
import com.fanwe.p2p.utils.SDCollectionUtil;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
/**
 * 充值界面中的线上支付fragment
 * @author js02
 *
 */
public class PayOnlineFragment extends BaseFragment
{
	@ViewInject(id = R.id.frag_pay_online_lsv_content)
	private ListView mLsvContent = null;

	@ViewInject(id = R.id.frag_pay_online_txt_empty)
	private TextView mTxtEmpty = null;

	private List<Uc_InchargeActPayment_listModel> mListModel = new ArrayList<Uc_InchargeActPayment_listModel>();

	private PayOnlineAdapter mAdapter = null;

	public void setmListModel(List<Uc_InchargeActPayment_listModel> listModel)
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
		View view = inflater.inflate(R.layout.frag_pay_online, container, false);
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
		mAdapter = new PayOnlineAdapter(mListModel, getActivity());
		mLsvContent.setAdapter(mAdapter);
	}

	public Uc_InchargeActPayment_listModel getSelectItem()
	{
		return mAdapter.getSelectItem();
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();

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

}