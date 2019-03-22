package com.fanwe.p2p;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.adapter.MyInterestBidAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnRightButtonClickListener;
import com.fanwe.p2p.model.DealsActItemModel;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.Uc_CollectActModel;
import com.fanwe.p2p.server.InterfaceServer;
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
 * 我关注的标界面
 * 
 * @author js02
 * 
 */
public class MyInterestBidActivity extends BaseActivity implements OnClickListener
{
	@ViewInject(id = R.id.act_my_interest_bid_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_my_interest_bid_lsv_bids)
	private PullToRefreshListView mLsvBids = null;

	@ViewInject(id = R.id.act_my_interest_bid_lin_bottom_delete)
	private LinearLayout mLinBottomDelete = null;

	@ViewInject(id = R.id.act_my_interest_bid_txt_delete)
	private TextView mTxtDelete = null;

	private boolean isEditMode = false;

	private List<DealsActItemModel> mListModel = new ArrayList<DealsActItemModel>();
	private MyInterestBidAdapter mAdapter = null;
	private int mCurrentPage = 1;

	private int mTotalPage = 0;

	private int mSelectedIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_my_interest_bid);
		// setContentView(R.layout.act_my_interest_bid);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initTitle();
		registeClick();
		bindDefaultData();
		initScroll();

		// requestData(false);
	}

	private void bindDefaultData()
	{
		mAdapter = new MyInterestBidAdapter(mListModel, this);
		mLsvBids.setAdapter(mAdapter);
		mLsvBids.setOnItemClickListener(new MyInterestBidListViewOnItemClickListener());
	}

	class MyInterestBidListViewOnItemClickListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			DealsActItemModel model = (DealsActItemModel) mAdapter.getItem((int) id);
			if (model != null)
			{
				if (mSelectedIndex != -1 && mSelectedIndex < mListModel.size())
				{
					mListModel.get(mSelectedIndex).setSelect(false);
				}

				mSelectedIndex = (int) id;
				mListModel.get(mSelectedIndex).setSelect(true);
				mAdapter.notifyDataSetChanged();

				// TODO:跳到web详情页
				if (model != null && model.getApp_url() != null)
				{
					if (mSelectedIndex != -1)
					{
						mListModel.get(mSelectedIndex).setSelect(false);
					}

					mSelectedIndex = (int) id;
					mListModel.get(mSelectedIndex).setSelect(true);
					mAdapter.notifyDataSetChanged();
					Intent intent = new Intent(MyInterestBidActivity.this, ProjectDetailWebviewActivity.class);
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "详情");
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, model.getApp_url());
					startActivity(intent);
				} else
				{
					SDToast.showToast("无详情链接!");
				}
			}
		}

	}

	private void initScroll()
	{
		mLsvBids.setMode(Mode.BOTH);
		mLsvBids.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				mCurrentPage = 1;
				requestData(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				mCurrentPage++;
				if (mCurrentPage > mTotalPage && mTotalPage != 0)
				{
					SDToast.showToast("没有更多数据了!");
					mLsvBids.onRefreshComplete();
				} else
				{
					requestData(true);
				}
			}
		});
		mLsvBids.setRefreshing();
	}

	private void requestData(final boolean isLoadMore)
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && user.getUserName() != null && user.getUserPassword() != null)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_collect");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			mapData.put("page", mCurrentPage);
			RequestModel model = new RequestModel(mapData);

			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					try
					{
						Uc_CollectActModel model = JSON.parseObject(content, Uc_CollectActModel.class);
						return model;
					} catch (Exception e)
					{
						return null;
					}
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
					mLsvBids.onRefreshComplete();
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					Uc_CollectActModel actModel = (Uc_CollectActModel) result;
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
						} else
						{
							mListModel.clear();
							SDToast.showToast("未找到数据!");
						}
						mAdapter.updateListViewData(mListModel);
					}
				}
			};

			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}

	}

	private void initTitle()
	{
		mTitle.setTitle("我关注的标");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);
		mTitle.setRightButtonText("编辑", new OnRightButtonClickListener()
		{
			@Override
			public void onRightBtnClick(View button)
			{
				switchMode();

			}
		}, R.drawable.bg_title_my_interest_cancel, null);
	}

	protected void switchMode()
	{
		isEditMode = !isEditMode;
		if (isEditMode)
		{
			mLsvBids.setOnItemClickListener(null);
			mTitle.mTxtRight.setText("取消");
			mLinBottomDelete.setVisibility(View.VISIBLE);
			mLsvBids.setMode(Mode.DISABLED);
			mLsvBids.getLoadingLayoutProxy();
		} else
		{
			mLsvBids.setOnItemClickListener(new MyInterestBidListViewOnItemClickListener());
			mTitle.mTxtRight.setText("编辑");
			mLinBottomDelete.setVisibility(View.GONE);
			mLsvBids.setMode(Mode.BOTH);
			mAdapter.setItemsSelectState(false);
		}
		mAdapter.setmIsDeleteMode(isEditMode);
	}

	private void registeClick()
	{
		mLinBottomDelete.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_my_interest_bid_lin_bottom_delete:
			clickDelete();
			break;

		default:
			break;
		}
	}

	/**
	 * 删除按钮被点击，请求删除接口
	 */
	private void clickDelete()
	{
		if (mAdapter.getSelectIds() == null)
		{
			SDToast.showToast("请选择要删除的项!");
			return;
		}
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && user.getUserName() != null && user.getUserPassword() != null)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_del_collect");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			mapData.put("id", mAdapter.getSelectIds());
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					BaseActModel model = JSON.parseObject(content, BaseActModel.class);
					return model;
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
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					BaseActModel actModel = (BaseActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						if (actModel.getResponse_code() == 1)
						{
							switchMode();
							mLsvBids.setRefreshing();
						} else
						{

						}
					}
				}

			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}

	}

}