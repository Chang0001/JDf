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
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.R;
import com.fanwe.p2p.RepayBorrowRepayActivity;
import com.fanwe.p2p.UcRepayBorrowAdvanceActivity;
import com.fanwe.p2p.adapter.RepayBorrowFragAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.Uc_RefundActItemModel;
import com.fanwe.p2p.model.act.Uc_RepayBorrowListFraModel;
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
 * 个人中心偿还贷款还款列表
 * 
 * @author yhz
 */
public class RepayBorrowListFragment extends BaseFragment
{

	public static final int REQUEST_CODE_REPAY = 1;
	public static final int REQUEST_CODE_REPAY_ADVANCE = 2;

	@ViewInject(id = R.id.frag_repay_borrow_list_lsv_content)
	private PullToRefreshListView mLsvContent = null;

	@ViewInject(id = R.id.frag_repay_borrow_list_txt_empty)
	private TextView mTxtEmpty = null;

	private List<Uc_RefundActItemModel> mListModel = null;

	private RepayBorrowFragAdapter mAdapter = null;

	private int mCurrentPage = 1;

	private int mTotalPage = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.uc_frag_repay_borrow_list, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;
	}

	private void init()
	{
		bindDefaultData();
		initPullListView();
	}

	private void bindDefaultData()
	{
		// TODO Auto-generated method stub
		mListModel = new ArrayList<Uc_RefundActItemModel>();
		mAdapter = new RepayBorrowFragAdapter(RepayBorrowListFragment.this, mListModel);
		mLsvContent.setAdapter(mAdapter);
	}

	private void initPullListView()
	{
		mLsvContent.setMode(Mode.BOTH);
		mLsvContent.setOnRefreshListener(new OnRefreshListener2<ListView>()
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
		mLsvContent.setRefreshing(false);
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
			mCurrentPage++;
			if (mCurrentPage > mTotalPage && mTotalPage != 0)
			{
				SDToast.showToast("没有更多数据了!");
				mLsvContent.onRefreshComplete();
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
		if (user != null && user.getUserName() != null && user.getUserPassword() != null)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_refund");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			mapData.put("page", mCurrentPage);
			mapData.put("status", "0");
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				private Dialog nDialog = null;

				@Override
				public void onFailureInMainThread(Throwable error, String content, Object result)
				{
					// TODO Auto-generated method stub
					super.onFailureInMainThread(error, content, result);
				}

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					Uc_RepayBorrowListFraModel model = null;
					try
					{
						model = JSON.parseObject(content, Uc_RepayBorrowListFraModel.class);
					} catch (Exception e)
					{
						return null;
					}
					return model;
				}

				@Override
				public void onStartInMainThread(Object result)
				{
					nDialog = getBaseActivity().mDialogUtil.showLoading("请稍候...");
				}

				@Override
				public void onFinishInMainThread(Object result)
				{
					if (nDialog != null)
					{
						nDialog.cancel();
					}
					mLsvContent.onRefreshComplete();
					toggleEmptyMsg();
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					Uc_RepayBorrowListFraModel fragModel = (Uc_RepayBorrowListFraModel) result;
					if (!SDInterfaceUtil.isActModelNull(fragModel))
					{
						if (fragModel.getPage() != null)
						{
							mCurrentPage = fragModel.getPage().getPage();
							mTotalPage = fragModel.getPage().getPage_total();
						}
						if (fragModel.getItem() != null && fragModel.getItem().size() > 0)
						{
							if (!isLoadMore)
							{
								mListModel.clear();
							}
							mListModel.addAll(fragModel.getItem());
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case REQUEST_CODE_REPAY:
			if (resultCode == RepayBorrowRepayActivity.RESULT_CODE_REPAY_SUCCESS)
			{
				mLsvContent.setRefreshing();
			}
			break;
		case REQUEST_CODE_REPAY_ADVANCE:
			if (resultCode == UcRepayBorrowAdvanceActivity.RESULT_CODE_REPAY_ADVANCE_SUCCESS)
			{
				mLsvContent.setRefreshing();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}