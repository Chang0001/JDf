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
import com.fanwe.p2p.ProjectDetailActivity;
import com.fanwe.p2p.R;
import com.fanwe.p2p.SearchDealsActivity;
import com.fanwe.p2p.adapter.BorrowInvestAdapter;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnRightButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.DealsActItemModel;
import com.fanwe.p2p.model.DealsSearchModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.SimpleDealsActModel;
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
 * 借款投资界面(侧滑菜单进入)
 * 
 * @author js02
 * 
 */
public class BorrowInvestFragment extends BaseFragment
{
	private static final int REQUEST_CODE_SEARCH_CONDITION = 1;

	@ViewInject(id = R.id.frag_borrow_invest_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.frag_borrow_invest_txt_empty)
	private TextView mTxtEmpty = null;

	@ViewInject(id = R.id.frag_borrow_invest_lsv_deals)
	private PullToRefreshListView mLsvDeals = null;
	private BorrowInvestAdapter mAdapter = null;
	private List<DealsActItemModel> mListModel = new ArrayList<DealsActItemModel>();

	private boolean isNeedRefresh = false;

	private DealsSearchModel mSearchModel = new DealsSearchModel();

	private int mCurrentPage = 1;
	private int mTotalPage = 0;

	private int mSelectedIndex = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag_borrow_invest, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;

	}

	private void init()
	{
		initTitle();
		initPullView();
		bindDefaultData();
	}

	private void bindDefaultData()
	{
		mAdapter = new BorrowInvestAdapter(mListModel, getActivity());
		mLsvDeals.setAdapter(mAdapter);
		mLsvDeals.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (mListModel != null && mListModel.size() > id)
				{
					if (mSelectedIndex != -1 && mSelectedIndex < mListModel.size())
					{
						mListModel.get(mSelectedIndex).setSelect(false);
					}

					mSelectedIndex = (int) id;
					mListModel.get(mSelectedIndex).setSelect(true);
					mAdapter.notifyDataSetChanged();
					Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
					intent.putExtra(ProjectDetailActivity.EXTRA_DEALS_ITEM_MODEL, (DealsActItemModel) (mAdapter.getItem((int) id)));
					startActivity(intent);
				}
			}
		});
	}

	private void initPullView()
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

	protected void requestData(final boolean isLoadMore)
	{
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "deals");
		mapData.put("page", mCurrentPage);
		mapData.put("cid", mSearchModel.getConditionCid());
		mapData.put("level", mSearchModel.getConditionLevel());
		mapData.put("interest", mSearchModel.getConditionInterest());
		mapData.put("deal_status", mSearchModel.getConditionDealStatus());

		RequestModel model = new RequestModel(mapData);
		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{
			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				SimpleDealsActModel actModel = JSON.parseObject(content, SimpleDealsActModel.class);
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
				isNeedRefresh = false;
				mLsvDeals.onRefreshComplete();
				SDViewUtil.toggleEmptyMsgByList(mListModel, mTxtEmpty);
			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				SimpleDealsActModel actModel = (SimpleDealsActModel) result;
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

	private void initTitle()
	{
		mTitle.setTitle("贷款理财");
		mTitle.setLeftButtonImage(R.drawable.ic_title_menu, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				toggleSlideMenu();
			}
		}, null);
		mTitle.setRightButtonImage(R.drawable.ic_title_search, new OnRightButtonClickListener()
		{

			@Override
			public void onRightBtnClick(View button)
			{
				// TODO:跳到搜索界面
				Intent intent = new Intent(getActivity(), SearchDealsActivity.class);
				intent.putExtra(SearchDealsActivity.EXTRA_SEALS_SEARCH_MODEL, mSearchModel);
				startActivityForResult(intent, REQUEST_CODE_SEARCH_CONDITION);
			}
		}, null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case REQUEST_CODE_SEARCH_CONDITION:

			if (data != null)
			{
				DealsSearchModel searchModel = (DealsSearchModel) data.getSerializableExtra(SearchDealsActivity.EXTRA_SEALS_SEARCH_MODEL);
				if (searchModel != null)
				{
					mSearchModel = searchModel;
					mLsvDeals.setRefreshing();
				}
			}
			break;

		default:
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (isNeedRefresh)
		{
			mLsvDeals.setRefreshing();
		}
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		if (event.getEventTagInt() == EventTag.EVENT_BID_SUCCESS)
		{
			this.isNeedRefresh = true;
		}
	}

}