package com.fanwe.p2p;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.adapter.BorrowInvestAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.constant.Constant.MyInvestConditionMode;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.DealsActItemModel;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.InvestActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDCollectionUtil;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;
/**
 * 我都投资界面(会员中心)
 * @author js02
 *
 */
public class MyInvestActivity extends BaseActivity
{
	@ViewInject(id = R.id.act_my_invest_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_my_invest_lsv_deals)
	private PullToRefreshListView mLsvDeals = null;

	@ViewInject(id = R.id.act_my_invest_txt_empty)
	private TextView mTxtEmpty = null;

	private BorrowInvestAdapter mAdapter = null;
	private List<DealsActItemModel> mListModel = new ArrayList<DealsActItemModel>();

	private int mCurrentPage = 1;

	private int mTotalPage = 0;
	
	private int mSelectedIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_my_invest);
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
		mAdapter = new BorrowInvestAdapter(mListModel, this);
		mLsvDeals.setAdapter(mAdapter);
		mLsvDeals.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				// TODO:跳到web详情页
				if (SDCollectionUtil.isListHasData(mListModel) && id < mListModel.size())
				{
					DealsActItemModel model = mListModel.get((int) id);
					if (model != null && model.getApp_url() != null)
					{
						if (mSelectedIndex != -1 && mSelectedIndex < mListModel.size())
						{
							mListModel.get(mSelectedIndex).setSelect(false);
						}
						
						mSelectedIndex = (int) id;
						mListModel.get(mSelectedIndex).setSelect(true);
						mAdapter.notifyDataSetChanged();
						Intent intent = new Intent(MyInvestActivity.this, ProjectDetailWebviewActivity.class);
						intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "详情");
						intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, model.getApp_url());
						startActivity(intent);
					} else
					{
						SDToast.showToast("无详情链接!");
					}
				}
			}

		});
	}

	private void initTitle()
	{
		mTitle.setTitle("我的投资");
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
		Log.e("MlistModel",mListModel.toString());
		if (mListModel != null && mListModel.size() > 0)
		{
			mCurrentPage++;
			if (mCurrentPage > mTotalPage && mTotalPage != 0)
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
			mapData.put("act", "uc_invest");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			mapData.put("page", mCurrentPage);
			mapData.put("mode", MyInvestConditionMode.INDEX);
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					InvestActModel actModel = JSON.parseObject(content, InvestActModel.class);
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
					toggleEmptyMsg();
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					InvestActModel actModel = (InvestActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						if (actModel.getPage() != null)
						{
							mCurrentPage = actModel.getPage().getPage();
							mTotalPage = actModel.getPage().getPage_total();
						}
						if (!isLoadMore)
						{
							mListModel.clear();
						}
						if (actModel.getItem() != null)
						{
							mListModel.addAll(actModel.getItem());
						}
						mAdapter.updateListViewData(mListModel);
					}
				}
			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}

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