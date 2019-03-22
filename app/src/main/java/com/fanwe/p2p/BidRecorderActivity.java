package com.fanwe.p2p;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.adapter.BidRecorderAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.Uc_LendActItemModel;
import com.fanwe.p2p.model.act.Uc_LendActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDViewUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 我都投资界面(会员中心)
 * 
 * @author js02
 * 
 */
public class BidRecorderActivity extends BaseActivity
{
	@ViewInject(id = R.id.act_bid_recorder_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_bid_recorder_lsv_deals)
	private PullToRefreshListView mLsvDeals = null;

	@ViewInject(id = R.id.act_bid_recorder_txt_empty)
	private TextView mTxtEmpty = null;

	private BidRecorderAdapter mAdapter = null;
	private List<Uc_LendActItemModel> mListModel = new ArrayList<Uc_LendActItemModel>();

	private int mCurrentPage = 1;

	private int mTotalPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_bid_recorder);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initTitle();
		initPullListView();
		bindDefaultData();
	}

	private void bindDefaultData()
	{
		mAdapter = new BidRecorderAdapter(mListModel, this);
		mLsvDeals.setAdapter(mAdapter);
		mLsvDeals.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO:跳到web详情页
				Uc_LendActItemModel model = mAdapter.getItem((int) id);
				if (model != null && model.getApp_url() != null)
				{
					Intent intent = new Intent(BidRecorderActivity.this, ProjectDetailWebviewActivity.class);
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "详情");
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, model.getApp_url());
					startActivity(intent);
				} else
				{
					SDToast.showToast("无详情链接!");
				}
			}

		});
	}

	private void initTitle()
	{
		mTitle.setTitle("投标记录");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);
	}

	private void initPullListView()
	{
		mLsvDeals.setMode(Mode.BOTH);
		mLsvDeals.setOnRefreshListener(new OnRefreshListener2<ListView>()
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
		mLsvDeals.setRefreshing();
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
				mLsvDeals.onRefreshComplete();
			} else
			{
				requestData(true);
			}
		} else
		{
			refreshData();
		}
	}

	private void requestData(final boolean isLoadMore)
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_lend");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			mapData.put("page", mCurrentPage);
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					Uc_LendActModel actModel = JSON.parseObject(content, Uc_LendActModel.class);
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
					hideLoadingDialog();
					mLsvDeals.onRefreshComplete();
					SDViewUtil.toggleEmptyMsgByList(mListModel, mTxtEmpty);
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					Uc_LendActModel actModel = (Uc_LendActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						if (actModel.getResponse_code() == 1)
						{
							if (actModel.getPage() != null)
							{
								mCurrentPage = actModel.getPage().getPage();
								mTotalPage = actModel.getPage().getPage_total();
							}
							SDViewUtil.updateAdapterByList(mListModel, actModel.getItem(), mAdapter, isLoadMore);
						}
					}
				}
			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}

	}

}