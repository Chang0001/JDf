package com.fanwe.p2p.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.ConfirmTransferBondActivity;
import com.fanwe.p2p.ConfirmTransferBondCantBuyActivity;
import com.fanwe.p2p.R;
import com.fanwe.p2p.adapter.BondTransferAdapter;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.BondTransferSearchModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.TransferActItemModel;
import com.fanwe.p2p.model.act.TransferActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDViewUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 债券转让界面(侧滑菜单进入)
 * 
 * @author js02
 * 
 */
public class BondTransferFragment extends BaseFragment
{
	@ViewInject(id = R.id.frag_bond_transfer_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.frag_bond_transfer_lsv_bonds)
	private PullToRefreshListView mLsvBonds = null;

	@ViewInject(id = R.id.frag_bond_transfer_txt_empty)
	private TextView mTxtEmpty = null;
	private BondTransferSearchModel mSearchModel = new BondTransferSearchModel();
	private List<TransferActItemModel> mListModel = new ArrayList<TransferActItemModel>();

	private BondTransferAdapter mAdapter = null;

	private int mCurrentPage = 1;
	private int mTotalPage = 0;

	private boolean isNeedRefresh = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag_bond_transfer, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;
	}

	private void init()
	{
		initTitle();
		bindDefaultData();
		initPullListView();

	}

	private void initPullListView()
	{
		mLsvBonds.setMode(Mode.BOTH);
		mLsvBonds.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				refreshData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				loadMoreData();
			}
		});
		mLsvBonds.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				TransferActItemModel model = mListModel.get((int) id);
				if (model != null)
				{
					Intent intent = null;
					if (!model.getT_user_id().equals("0")) // 该债券已经被购买
					{
						intent = new Intent(getActivity(), ConfirmTransferBondCantBuyActivity.class);
					} else
					{
						intent = new Intent(getActivity(), ConfirmTransferBondActivity.class);
					}
					intent.putExtra(ConfirmTransferBondActivity.EXTRA_BOND_DETAIL_ITEM_MODEL, mListModel.get((int) id));
					startActivity(intent);
				}
			}
		});
		mLsvBonds.setRefreshing();
	}

	private void refreshData()
	{
		mCurrentPage = 1;
		requestData(false);
	}

	private void loadMoreData()
	{
		if (mListModel != null && mListModel.size() > 0)
		{
			if (++mCurrentPage > mTotalPage && mTotalPage != 0)
			{
				SDToast.showToast("没有更多数据了!");
				mLsvBonds.onRefreshComplete();
			} else
			{
				requestData(true);
			}
		} else
		{
			refreshData();
		}
	}

	protected void requestData(final boolean isLoadMore)
	{
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "transfer");
		mapData.put("page", mCurrentPage);
		mapData.put("cid", mSearchModel.getConditionCid());
		mapData.put("level", mSearchModel.getConditionLevel());
		mapData.put("interest", mSearchModel.getConditionInterest());
		mapData.put("months", mSearchModel.getConditionMonths());
		mapData.put("lefttime", mSearchModel.getConditionLeftTime());

		RequestModel model = new RequestModel(mapData);
		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{
			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				TransferActModel actModel = JSON.parseObject(content, TransferActModel.class);
				return actModel;
			}

			@Override
			public void onStartInMainThread(Object result)
			{
				showLoadingDialog("请稍候...");
			}

			@Override
			public void onFinishInMainThread(Object result)
			{
				isNeedRefresh = false;
				hideLoadingDialog();
				mLsvBonds.onRefreshComplete();
				SDViewUtil.toggleEmptyMsgByList(mListModel, mTxtEmpty);
			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				TransferActModel actModel = (TransferActModel) result;
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					if (actModel.getPage() != null)
					{
						mCurrentPage = actModel.getPage().getPage();
						mTotalPage = actModel.getPage().getPage_total();
					}
					SDViewUtil.updateAdapterByList(mListModel, actModel.getItem(), mAdapter, isLoadMore);
				}
			}

		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	private void bindDefaultData()
	{
		mAdapter = new BondTransferAdapter(mListModel, getActivity());
		mLsvBonds.setAdapter(mAdapter);
	}

	private void initTitle()
	{
		mTitle.setTitle("分销分佣");
		mTitle.setLeftButtonImage(R.drawable.ic_title_menu, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				toggleSlideMenu();
			}
		}, null);
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		if (event.getEventTagInt() == EventTag.EVENT_BUY_BOND_TRANSFER_SUCCESS)
		{
			this.isNeedRefresh = true;
		}
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		if (isNeedRefresh)
		{
			mLsvBonds.setRefreshing();
		}
	}

}