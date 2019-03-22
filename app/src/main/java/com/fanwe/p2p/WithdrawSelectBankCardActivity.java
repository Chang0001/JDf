package com.fanwe.p2p;

import java.io.Serializable;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.adapter.WithdrawBankAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnRightButtonClickListener;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.Uc_BankActFee_configModel;
import com.fanwe.p2p.model.Uc_BankActItemModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.Uc_BankActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDCollectionUtil;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDViewUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;
/**
 * 选择银行界面(提现的时候)
 * @author js02
 *
 */
public class WithdrawSelectBankCardActivity extends BaseActivity implements OnClickListener
{
	public static final int REQUEST_CODE_ADD_BANK_CARD = 1;

	@ViewInject(id = R.id.act_withdraw_select_bank_card_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_withdraw_select_bank_card_txt_empty)
	private TextView mTxtEmpty = null;

	@ViewInject(id = R.id.act_withdraw_select_bank_card_scroll)
	private PullToRefreshScrollView mScroll = null;

	@ViewInject(id = R.id.act_withdraw_select_bank_card_lsv_bank_card)
	private ListView mLsvBankCard = null;

	@ViewInject(id = R.id.act_withdraw_select_bank_card_rla_add_bank_card)
	private RelativeLayout mRlaAddBankCard = null;

	@ViewInject(id = R.id.act_withdraw_select_bank_card_lin_bank_card)
	private LinearLayout mLinBankCard = null;

	@ViewInject(id = R.id.act_withdraw_select_bank_card_lin_bottom_delete)
	private LinearLayout mLinDeleteBankCard = null;

	@ViewInject(id = R.id.act_withdraw_select_bank_card_txt_delete)
	private TextView mTxtDelete = null;

	private List<Uc_BankActItemModel> mListModel = new ArrayList<Uc_BankActItemModel>();

	private List<Uc_BankActFee_configModel> mListFeeConfig = new ArrayList<Uc_BankActFee_configModel>();

	private WithdrawBankAdapter mAdapter = null;

	private boolean isEditMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_withdraw_select_bank_card);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initTitle();
		initScroll();
		registeClick();
		bindDefaultData();
		// requestData();

	}

	private void initScroll()
	{
		mScroll.setMode(Mode.PULL_FROM_START);
		mScroll.setOnRefreshListener(new OnRefreshListener2<ScrollView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				requestData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{

			}
		});
		mScroll.setRefreshing();
	}

	private void initTitle()
	{
		mTitle.setTitle("选择银行");
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
		if (isEditMode) // 切换到编辑模式
		{
			mLinDeleteBankCard.setVisibility(View.VISIBLE);
			mAdapter.setEditMode(true);
			mTitle.mTxtRight.setText("取消");
			mScroll.setMode(Mode.DISABLED);
			mLsvBankCard.setOnItemClickListener(null);
		} else
		{
			mLinDeleteBankCard.setVisibility(View.GONE);
			mAdapter.setEditMode(false);
			mAdapter.setItemsSelectState(false);
			mTitle.mTxtRight.setText("编辑");
			mScroll.setMode(Mode.PULL_FROM_START);
			mLsvBankCard.setOnItemClickListener(new BankCardItemClickListener());
		}
	}

	private void bindDefaultData()
	{
		mAdapter = new WithdrawBankAdapter(mListModel, this);
		mLsvBankCard.setAdapter(mAdapter);
		if (mListModel != null && mListModel.size() <= 0)
		{
			mLinBankCard.setVisibility(View.GONE);
		} else
		{
			mLinBankCard.setVisibility(View.VISIBLE);
		}
		mLsvBankCard.setOnItemClickListener(new BankCardItemClickListener());
	}

	/**
	 * 银行卡listview item点击操作
	 * 
	 * @author js02
	 * 
	 */
	class BankCardItemClickListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			if (id < mListModel.size())
			{
				Intent intent = new Intent(WithdrawSelectBankCardActivity.this, ApplyWithdrawActivity.class);
				Uc_BankActItemModel selectBank = mListModel.get((int) id);
				if (selectBank != null && mListModel != null && mListModel.size() > 0)
				{
					intent.putExtra(ApplyWithdrawActivity.EXTRA_SELECT_BANK, selectBank);
					intent.putExtra(ApplyWithdrawActivity.EXTRA_SELECT_BANK_FEE_LIST, (Serializable) mListFeeConfig);
					startActivity(intent);
				} else
				{
					SDToast.showToast("服务器返回数据出错!");
				}
			}
		}
	}

	/**
	 * 请求银行卡列表接口
	 */
	private void requestData()
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && user.getUserName() != null && user.getUserPassword() != null)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_bank");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				private Dialog nDialog = null;

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					Uc_BankActModel model = JSON.parseObject(content, Uc_BankActModel.class);
					return model;
				}

				@Override
				public void onStartInMainThread(Object result)
				{
					nDialog = mDialogUtil.showLoading("请稍候...");
				}

				@Override
				public void onFinishInMainThread(Object result)
				{
					if (nDialog != null)
					{
						nDialog.cancel();
					}
					mScroll.onRefreshComplete();
					if (mListModel != null && mListModel.size() <= 0)
					{
						mLinBankCard.setVisibility(View.GONE);
					} else
					{
						mLinBankCard.setVisibility(View.VISIBLE);
					}
					SDViewUtil.resetListViewHeightBasedOnChildren(mLsvBankCard);
					toggleEmptyMsg();
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					Uc_BankActModel actModel = (Uc_BankActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						if (actModel.getResponse_code() == 1)
						{
							if (actModel.getItem() != null && actModel.getItem().size() > 0)
							{
								mListModel.clear();
								mListModel.addAll(actModel.getItem());
								mAdapter.notifyDataSetChanged();
							} else
							{
								SDToast.showToast("未找到数据!");
							}
							if (actModel.getFee_config() != null)
							{
								mListFeeConfig = actModel.getFee_config();
							}

						} else
						{

						}
					}
				}

			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}
	}

	private void registeClick()
	{
		mRlaAddBankCard.setOnClickListener(this);
		mTxtDelete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_withdraw_select_bank_card_rla_add_bank_card:
			clickAddBankCard();
			break;
		case R.id.act_withdraw_select_bank_card_txt_delete:
			clickDeleteBankCard();
			break;

		default:
			break;
		}
	}

	/**
	 * 删除选中的银行卡接口
	 */
	private void clickDeleteBankCard()
	{
		List<Uc_BankActItemModel> listSelect = mAdapter.getSelectItems();
		if (SDCollectionUtil.isListHasData(listSelect))
		{
			RequestModel model = createRequestModel(listSelect);
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
							requestData();
						} else
						{

						}
					}
				}

			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);

		} else
		{
			SDToast.showToast("请选择要删除的银行卡!");
		}

	}

	private RequestModel createRequestModel(List<Uc_BankActItemModel> listSelect)
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "uc_del_bank");
		mapData.put("email", user.getUserName());
		mapData.put("pwd", user.getUserPassword());
		StringBuilder builderBankId = new StringBuilder();
		for (Uc_BankActItemModel model : listSelect)
		{
			builderBankId.append(model.getId());
			builderBankId.append(",");
		}
		String bankId = builderBankId.toString();
		bankId = bankId.substring(0, bankId.length() - 1);
		mapData.put("id", bankId);
		return new RequestModel(mapData);
	}

	private void clickAddBankCard()
	{
		Intent intent = new Intent(this, AddBankCardActivity.class);
		startActivityForResult(intent, REQUEST_CODE_ADD_BANK_CARD);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case REQUEST_CODE_ADD_BANK_CARD:
			if (resultCode == AddBankCardActivity.RESULT_CODE_ADD_BANK_CARD_SUCCESS)
			{
				requestData();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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