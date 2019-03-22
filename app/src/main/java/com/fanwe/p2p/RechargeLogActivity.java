package com.fanwe.p2p;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.adapter.RechargeLogAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.Uc_Money_LogActItemModel;
import com.fanwe.p2p.model.act.Uc_Money_LogActModel;
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
 * 充值日志界面(会员中心-充值提现进入)
 * @author js02
 *
 */
public class RechargeLogActivity extends BaseActivity implements OnClickListener
{

	@ViewInject(id = R.id.act_recharge_log_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_recharge_log_lsv_recharge_log)
	private PullToRefreshListView mLsvRechargeLog = null;

	@ViewInject(id = R.id.act_recharge_log_btn_recharge)
	private Button mBtnRecharge = null;

	@ViewInject(id = R.id.act_recharge_log_txt_empty)
	private TextView mTxtEmpty = null;

	@ViewInject(id = R.id.act_recharge_log_btn_withdraw)
	private Button mBtnWithdraw = null;

	private List<Uc_Money_LogActItemModel> mListModel = new ArrayList<Uc_Money_LogActItemModel>();
	private RechargeLogAdapter mAdapter = null;

	private int mCurrentPage = 0;
	private int mTotalPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_recharge_log);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initTitle();
		initPullListView();
		bindDefaultData();
		registeClick();
		// requestData(false);
	}

	private void bindDefaultData()
	{
		mAdapter = new RechargeLogAdapter(mListModel, this);
		mLsvRechargeLog.setAdapter(mAdapter);
	}

	private void initPullListView()
	{
		mLsvRechargeLog.setMode(Mode.BOTH);
		mLsvRechargeLog.setOnRefreshListener(new OnRefreshListener2<ListView>()
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
					mLsvRechargeLog.onRefreshComplete();
				} else
				{
					requestData(true);
				}
			}

		});
		mLsvRechargeLog.setRefreshing();
	}

	private void requestData(final boolean isLoadMore)
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && user.getUserName() != null && user.getUserPassword() != null)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_money_log");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			mapData.put("page", mCurrentPage);

			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				private Dialog nDialog = null;

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					Uc_Money_LogActModel actModel = JSON.parseObject(content, Uc_Money_LogActModel.class);
					return actModel;
				}

				@Override
				public void onStartInMainThread(Object result)
				{
					nDialog = mDialogUtil.showLoading("正在请求数据...");
				}

				@Override
				public void onFinishInMainThread(Object result)
				{
					if (nDialog != null)
					{
						nDialog.dismiss();
					}
					mLsvRechargeLog.onRefreshComplete();
					toggleEmptyMsg();
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					Uc_Money_LogActModel actModel = (Uc_Money_LogActModel) result;
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

	}

	private void initTitle()
	{
		mTitle.setTitle("账户日志");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{

			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);

	}

	private void registeClick()
	{
		mBtnRecharge.setOnClickListener(this);
		mBtnWithdraw.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{

		case R.id.act_recharge_log_btn_recharge:
			clickRecharge();
			break;

		case R.id.act_recharge_log_btn_withdraw:
			clickWithdraw();
			break;

		default:
			break;
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

	private void clickRecharge()
	{
		startActivity(new Intent(this, RechargeActivity.class));
	}

	private void clickWithdraw()
	{
		startActivity(new Intent(this, WithdrawSelectBankCardActivity.class));
	}

}