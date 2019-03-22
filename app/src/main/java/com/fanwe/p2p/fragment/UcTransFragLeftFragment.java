package com.fanwe.p2p.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.DialogInterface;
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
import com.fanwe.p2p.R;
import com.fanwe.p2p.UcTransferActivity;
import com.fanwe.p2p.UcWantTransferActivity;
import com.fanwe.p2p.adapter.UcTransferFragLeftAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.constant.Constant;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.TransferModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.Uc_TransferActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.DialogUtil;
import com.fanwe.p2p.utils.SDCollectionUtil;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

import de.greenrobot.event.EventBus;

/**
 * 个人中心债券转让债权转让
 * 
 * @author yhz
 */
public class UcTransFragLeftFragment extends BaseFragment
{
	@ViewInject(id = R.id.fragUcTransferLeft_list)
	private PullToRefreshListView mList = null;

	@ViewInject(id = R.id.fragUcTransferLeft_tv_error)
	private TextView mTvError = null;

	private List<TransferModel> mListModel = null;

	private UcTransferFragLeftAdapter mAdapter = null;

	private int mCurrentPage = 1;

	private int mTotalPage = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.act_uc_trans_frag_left, container, false);
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
		mAdapter = new UcTransferFragLeftAdapter(getActivity(), mListModel);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
			{
				// TODO Auto-generated method stub
				int curListItem = position - 1;
				if (mListModel.get(curListItem).getTras_status_op() != null)
				{
					tras_Status_Op(Integer.valueOf(mListModel.get(curListItem).getTras_status_op()), curListItem);
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
				refreshData(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				loadMoreData();
			}
		});
		mList.setRefreshing();
	}

	private void refreshData(boolean isReturnHead)
	{
		mCurrentPage = 1;
		requestDataUc_transfer(false, isReturnHead);
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
				requestDataUc_transfer(true, false);
			}
		} else
		{
			refreshData(false);
		}
	}

	protected void requestDataUc_transfer(final boolean isLoadMore, final boolean isReturnHeda)
	{
		Map<String, Object> mMapData = new HashMap<String, Object>();
		mMapData.put("act", "uc_transfer");
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
				if (nDialog != null)
				{
					nDialog.dismiss();
				}
				mList.onRefreshComplete();
				if (isReturnHeda)
					mList.getRefreshableView().setSelection(0);
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

	protected void requestDataUc_do_Reback(String dlitid)
	{
		Map<String, Object> mMapData = new HashMap<String, Object>();
		mMapData.put("act", "uc_do_reback");
		mMapData.put("email", App.getApplication().getmLocalUser().getUserName());
		mMapData.put("pwd", App.getApplication().getmLocalUser().getUserPassword());
		mMapData.put("dltid", dlitid);
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
				BaseActModel model = null;
				try
				{
					model = JSON.parseObject(content, BaseActModel.class);
				} catch (Exception e)
				{
					return null;
				}

				return model;
			}

			@Override
			public void onFinishInMainThread(Object result)
			{

				if (nDialog != null)
				{
					nDialog.dismiss();
				}

			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				BaseActModel actModel = (BaseActModel) result;
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					if (actModel.getResponse_code() == 1)
					{
						refreshData(true);
					} else
					{
						if (actModel.getShow_err() == null)
						{
							SDToast.showToast("撤销失败!");
						}
					}

				}
			}

		};

		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	private void tras_Status_Op(int tras_status_op, final int curListItem)
	{
		switch (tras_status_op)
		{
		case Constant.Tras_Status_Op.Tras_Status_Op_0:
			break;
		case Constant.Tras_Status_Op.Tras_Status_Op_1:
			Intent intentOp_1 = new Intent(getActivity(), UcWantTransferActivity.class);
			intentOp_1.putExtra(UcWantTransferActivity.TRA_DLID, mListModel.get(curListItem).getDlid());
			startActivity(intentOp_1);
			break;
		case Constant.Tras_Status_Op.Tras_Status_Op_2:
			break;
		case Constant.Tras_Status_Op.Tras_Status_Op_3:
			break;
		case Constant.Tras_Status_Op.Tras_Status_Op_4:
			Intent intentOp_4 = new Intent(getActivity(), UcWantTransferActivity.class);
			intentOp_4.putExtra(UcWantTransferActivity.TRA_DLID, mListModel.get(curListItem).getDlid());
			startActivity(intentOp_4);
			break;
		case Constant.Tras_Status_Op.Tras_Status_Op_5:
			break;
		case Constant.Tras_Status_Op.Tras_Status_Op_6:
			getBaseActivity().mDialogUtil = new DialogUtil(getActivity());
			getBaseActivity().mDialogUtil.confirm("确认撤销", null, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					// TODO Auto-generated method stub
					requestDataUc_do_Reback(mListModel.get(curListItem).getDltid());
				}
			}, new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int arg1)
				{
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		if (event.getEventTagInt() == EventTag.EVENT_TRANSFER_SUCCESS)
		{
			refreshData(true);
		}
	}

}