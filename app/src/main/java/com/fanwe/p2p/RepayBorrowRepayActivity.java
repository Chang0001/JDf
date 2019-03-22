package com.fanwe.p2p;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.adapter.RepayBorrowRepayListAdapter;
import com.fanwe.p2p.adapter.RepayBorrowRepayListAdapter.RepayBorrowRepayListAdapterListener;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.Uc_Quick_RefundActDealModel;
import com.fanwe.p2p.model.Uc_Quick_RefundActLoan_ListModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.Uc_Quick_RefundActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDFormatUtil;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 偿还贷款界面
 * 
 * @author js02
 * 
 */
public class RepayBorrowRepayActivity extends BaseActivity implements OnClickListener
{
	public static final String EXTRA_DEAL_ID = "extra_deal_id";

	public static final int RESULT_CODE_REPAY_SUCCESS = 1;

	@ViewInject(id = R.id.act_repay_borrow_repay_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_txt_name)
	private TextView mTxtName = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_txt_borrow_amount)
	private TextView mTxtBorrowAmount = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_txt_rate)
	private TextView mTxtRate = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_txt_repay_time)
	private TextView mTxtRepayTime = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_txt_repay_money)
	private TextView mTxtRepayMoney = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_txt_need_remain_repay_money)
	private TextView mTxtNeedRemainRepayMoney = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_lsv_repay_record)
	private ListView mLsvRepayRecord = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_txt_total_repay_money)
	private TextView mTxtTotalRepayMoney = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_btn_recharge)
	private Button mBtnRecharge = null;

	@ViewInject(id = R.id.act_repay_borrow_repay_btn_confirm_repay)
	private Button mBtnConfirmRepay = null;

	private int mDealId = -1;

	private Uc_Quick_RefundActDealModel mDealModel = null;

	private List<Uc_Quick_RefundActLoan_ListModel> mListLoanListModel = new ArrayList<Uc_Quick_RefundActLoan_ListModel>();
	private RepayBorrowRepayListAdapter mAdapter = null;
	private String mIds = null;
	private float mTotalMoneyNeedpay = 0;

	private boolean mIsRepaySuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_repay_borrow_repay);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initIntentData();
		initTitle();
		bindDefaultData();
		requestData(false);
		registeClick();

	}

	private void bindDefaultData()
	{
		mAdapter = new RepayBorrowRepayListAdapter(mListLoanListModel, this, new RepayBorrowRepayActivity_RepayBorrowRepayListAdapterListener());
		mLsvRepayRecord.setAdapter(mAdapter);
	}

	private void initIntentData()
	{
		String strId = getIntent().getStringExtra(EXTRA_DEAL_ID);
		if (!TextUtils.isEmpty(strId))
		{
			try
			{
				mDealId = Integer.parseInt(strId);
			} catch (Exception e)
			{
				// TODO: handle exception
			}
		}
	}

	private void requestData(final boolean isRepaySuccess)
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && user.getUserName() != null && user.getUserPassword() != null && mDealId != -1)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_quick_refund");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			mapData.put("id", mDealId);
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{

				@Override
				public void onStartInMainThread(Object result)
				{
					showLoadingDialog("请稍候...");
				}

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					Uc_Quick_RefundActModel model = null;
					try
					{
						model = JSON.parseObject(content, Uc_Quick_RefundActModel.class);
					} catch (Exception e)
					{
						return null;
					}
					return model;
				}

				@Override
				public void onFinishInMainThread(Object result)
				{
					hideLoadingDialog();
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					Uc_Quick_RefundActModel actModel = (Uc_Quick_RefundActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						if (actModel.getDeal() != null)
						{
							mDealModel = actModel.getDeal();
							bindTopData(mDealModel);
						}
						if (actModel.getLoan_list() != null && actModel.getLoan_list().size() > 0)
						{
							mListLoanListModel = actModel.getLoan_list();
							mAdapter.updateListViewData(mListLoanListModel);
						}
					}
				}

				@Override
				public void onFailureInMainThread(Throwable e, String responseBody, Object result)
				{
					if (isRepaySuccess)
					{
						finish();
					}
				}

			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}
	}

	protected void bindTopData(Uc_Quick_RefundActDealModel dealModel)
	{
		if (dealModel != null)
		{
			if (dealModel.getName() != null)
			{
				mTxtName.setText(dealModel.getName());
			} else
			{
				mTxtName.setText("未找到");
			}

			if (dealModel.getBorrow_amount_format() != null)
			{
				mTxtBorrowAmount.setText(dealModel.getBorrow_amount_format());
			} else
			{
				mTxtBorrowAmount.setText("未找到");
			}

			if (dealModel.getRate() != null)
			{
				mTxtRate.setText(dealModel.getRate() + "%");
			} else
			{
				mTxtRate.setText("未找到");
			}

			if (dealModel.getRepay_time_format() != null)
			{
				mTxtRepayTime.setText(dealModel.getRepay_time_format());
			} else
			{
				mTxtRepayTime.setText("未找到");
			}

			if (dealModel.getRepay_money_format() != null)
			{
				mTxtRepayMoney.setText(dealModel.getRepay_money_format());
			} else
			{
				mTxtRepayMoney.setText("未找到");
			}

			if (dealModel.getNeed_remain_repay_money_format() != null)
			{
				mTxtNeedRemainRepayMoney.setText(dealModel.getNeed_remain_repay_money_format());
			} else
			{
				mTxtNeedRemainRepayMoney.setText("未找到");
			}

		}

	}

	private void initTitle()
	{
		mTitle.setTitle("偿还贷款");
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
		mBtnConfirmRepay.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_repay_borrow_repay_btn_recharge:
			clickRecharge();
			break;

		case R.id.act_repay_borrow_repay_btn_confirm_repay:
			clickConfirmRepay();
			break;

		default:
			break;
		}
	}

	private void clickConfirmRepay()
	{
		if (validateParam())
		{
			LocalUserModel user = App.getApplication().getmLocalUser();
			if (user != null && user.getUserName() != null && user.getUserPassword() != null)
			{
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("act", "uc_do_quick_refund");
				mapData.put("email", user.getUserName());
				mapData.put("pwd", user.getUserPassword());
				mapData.put("id", mDealId);
				mapData.put("ids", mIds);
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
								CommonInterface.refreshLocalUser();
								requestData(true);
								mIsRepaySuccess = true;
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

	private boolean validateParam()
	{
		if (TextUtils.isEmpty(mIds) || mTotalMoneyNeedpay <= 0 && mDealId != -1)
		{
			return false;
		}
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user == null)
		{
			return false;
		}
		try
		{
			float userMoney = Float.parseFloat(user.getUserMoney());
			if (userMoney < mTotalMoneyNeedpay)
			{
				SDToast.showToast("您的余额不足");
				return false;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}

		return true;
	}

	private void clickRecharge()
	{
		startActivity(new Intent(getApplicationContext(), RechargeActivity.class));
	}

	class RepayBorrowRepayActivity_RepayBorrowRepayListAdapterListener implements RepayBorrowRepayListAdapterListener
	{
		@Override
		public void onCheckedChange(String ids, float totalMoneyNeedpay)
		{
			mIds = ids;
			mTotalMoneyNeedpay = totalMoneyNeedpay;
			try
			{
				mTxtTotalRepayMoney.setText(SDFormatUtil.formatMoneyChina(String.valueOf(totalMoneyNeedpay)));
			} catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	@Override
	public void finish()
	{
		if (mIsRepaySuccess)
		{
			setResult(RESULT_CODE_REPAY_SUCCESS);
		}
		super.finish();
	}

}