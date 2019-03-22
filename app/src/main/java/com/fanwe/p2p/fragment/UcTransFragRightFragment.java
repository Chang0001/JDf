package com.fanwe.p2p.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.ProjectDetailWebviewActivity;
import com.fanwe.p2p.R;
import com.fanwe.p2p.adapter.UcTransferFragRightAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.TransferModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.Uc_TransferActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDCollectionUtil;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 个人中心偿债权转让购买记录
 * 
 * @author yhz
 */
public class UcTransFragRightFragment extends BaseFragment
{
	@ViewInject(id = R.id.fragUcTransferRight_list)
	private PullToRefreshListView mList = null;

	@ViewInject(id = R.id.fragUcTransferRight_tv_error)
	private TextView mTvError = null;

	private List<TransferModel> mListModel = null;

	private UcTransferFragRightAdapter mAdapter = null;

	private int mCurrentPage = 1;

	private int mTotalPage = 0;

	private boolean isNeedRefresh = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.act_uc_trans_frag_right, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;
	}

	private void init()
	{
		bindDefaultData();
		initPullView();
	}

	private void bindDefaultData()
	{
		mListModel = new ArrayList<TransferModel>();
		mAdapter = new UcTransferFragRightAdapter(getActivity(), mListModel);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				// TODO Auto-generated method stub
				int curListItem = position - 1;

				String url = mListModel.get(curListItem).getApp_url();
				if (url != null)
				{

					Intent intent = new Intent(getActivity(), ProjectDetailWebviewActivity.class);
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "详情");
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, url);
					startActivity(intent);
				}
			}

		});

	}

	private void initPullView()
	{
		mList.setMode(Mode.BOTH);
		mList.setOnRefreshListener(new OnRefreshListener2<ListView>()
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

		mList.setRefreshing();
	}

	private void refreshData()
	{
		mCurrentPage = 1;
		requestDataUc_transfer(false);
	}

	private void loadMoreData()
	{
		if (mListModel != null && mListModel.size() > 0)
		{
			mCurrentPage++;
			if (mCurrentPage > mTotalPage && mTotalPage != 0)
			{
				SDToast.showToast("没有更多数据了!");
				mList.onRefreshComplete();
			} else
			{
				requestDataUc_transfer(true);
			}
		} else
		{
			refreshData();
		}
	}

	protected void requestDataUc_transfer(final boolean isLoadMore)
	{
		Map<String, Object> mMapData = new HashMap<String, Object>();
		mMapData.put("act", "uc_transfer_buys");
		mMapData.put("page", mCurrentPage);
		mMapData.put("email", App.getApplication().getmLocalUser().getUserName());
		mMapData.put("pwd", App.getApplication().getmLocalUser().getUserPassword());

		RequestModel model = new RequestModel(mMapData);
		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{
			private Dialog nDialog = null;

			@Override
			public void onStartInMainThread(Object result)
			{
				nDialog = getBaseActivity().mDialogUtil.showLoading("正在请求数据...");
			}

			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				Uc_TransferActModel actModel = null;
				try
				{
					actModel = JSON.parseObject(content, Uc_TransferActModel.class);

				} catch (Exception e)
				{
					return null;
				}
				return actModel;
			}

			@Override
			public void onFinishInMainThread(Object result)
			{
				isNeedRefresh = false;
				if (nDialog != null)
				{
					nDialog.dismiss();
				}
				mList.onRefreshComplete();
				toggleEmptyMsg();
			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				Uc_TransferActModel actModel = (Uc_TransferActModel) result;
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					if (actModel.getPage() != null)
					{
						mCurrentPage = actModel.getPage().getPage();
						mTotalPage = actModel.getPage().getPage_total();
					}
					if (actModel.getItem() != null && actModel.getItem().size() > 0)
					{
						if (!isLoadMore)
						{
							mListModel.clear();
						}
						mListModel.addAll(actModel.getItem());
						mAdapter.updateListViewData(mListModel);
					} else
					{
						SDToast.showToast("未找到数据!");
					}
				}
			}

		};

		InterfaceServer.getInstance().requestInterface(model, handler, true);

	}

	protected void toggleEmptyMsg()
	{
		if (SDCollectionUtil.isListHasData(mListModel))
		{
			if (mTvError.getVisibility() == View.VISIBLE)
			{
				mTvError.setVisibility(View.GONE);
			}
		} else
		{
			if (mTvError.getVisibility() == View.GONE)
			{
				mTvError.setVisibility(View.VISIBLE);
			}
		}
	}
}