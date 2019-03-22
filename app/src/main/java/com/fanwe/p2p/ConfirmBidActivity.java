package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.R.color;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.customview.SDSimpleProjectDetailItemView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.DealsActItemModel;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDUIUtil;
import com.fanwe.p2p.utils.SDViewUtil;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

import de.greenrobot.event.EventBus;

/**
 * 确认投标界面
 * 
 * @author js02
 * 
 */
public class ConfirmBidActivity extends BaseActivity implements OnClickListener
{
	public static final String EXTRA_DEALS_ITEM_DETAIL_MODEL = "extra_deals_item_detail_model";

	@ViewInject(id = R.id.act_confirm_bid_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_confirm_bid_tv_title)
	private TextView mTvTitle = null;

	@ViewInject(id = R.id.act_confirm_bid_sdview_borrow_amount)
	private SDSimpleProjectDetailItemView mSdviewBorrowAmount = null;

	@ViewInject(id = R.id.act_confirm_bid_sdview_progress)
	private SDSimpleProjectDetailItemView mSdviewProgress = null;

	@ViewInject(id = R.id.act_confirm_bid_sdview_rate)
	private SDSimpleProjectDetailItemView mSdviewRate = null;

	@ViewInject(id = R.id.act_confirm_bid_sdview_repay_time)
	private SDSimpleProjectDetailItemView mSdviewRepayTime = null;

	@ViewInject(id = R.id.act_confirm_bid_sdview_loan_type)
	private SDSimpleProjectDetailItemView mSdviewLoanType = null;

	@ViewInject(id = R.id.act_confirm_bid_sdview_need_money)
	private SDSimpleProjectDetailItemView mSdviewNeedMoney = null;

	@ViewInject(id = R.id.act_confirm_bid_txt_money_can_use)
	private TextView mTxtMoneyCanUse = null;

	@ViewInject(id = R.id.act_confirm_bid_txt_i_want_recharge)
	private TextView mTxtIWantRecharge = null;

	@ViewInject(id = R.id.act_confirm_bid_edt_invest_money_amount)
	private ClearEditText mEdtInvestMoneyAmount = null;

	@ViewInject(id = R.id.act_confirm_bid_edt_pay_password)
	private ClearEditText mEdtPayPassword = null;

	@ViewInject(id = R.id.act_confirm_bid_btn_confirm_invest)
	private Button mBtnConfirmInvest = null;

	private DealsActItemModel mModel = null;

	private String mStrBidMoney = null;
	private String mStrBidPayPassword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_confirm_bid);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initIntentData();
		initTitle();
		initItems();
		registeClick();

	}

	private void initTitle()
	{
		mTitle.setTitle("确认投标");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{

			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);

	}

	private void initIntentData()
	{
		mModel = (DealsActItemModel) getIntent().getSerializableExtra(EXTRA_DEALS_ITEM_DETAIL_MODEL);

	}

	private void initItems()
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && user.getUserMoneyFormat() != null)
		{
			mTxtMoneyCanUse.setText(user.getUserMoneyFormat());
		}

		if (mModel != null)
		{

			if (mModel.getName() != null) // 项目名称
			{
				mTvTitle.setText(mModel.getName());
			} else
			{
				mTvTitle.setText("未找到数据");
			}
			SDViewUtil.measureView(mTvTitle);
			int width = mTvTitle.getMeasuredWidth();
			if ((width + 10) > SDUIUtil.getScreenWidth(getApplicationContext()))
			{
				mTvTitle.setGravity(Gravity.LEFT);
			} else
			{
				mTvTitle.setGravity(Gravity.CENTER);
			}

			mSdviewBorrowAmount.setTV_Left("借款金额"); // 借款金额
			mSdviewBorrowAmount.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getBorrow_amount_format() != null)
			{
				mSdviewBorrowAmount.setTV_Right(mModel.getBorrow_amount_format());
			} else
			{
				mSdviewBorrowAmount.setTV_Right("未找到数据");
			}

			mSdviewProgress.setTV_Left("完成进度"); // 完成进度
			mSdviewProgress.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getProgress_point_format() != null)
			{
				mSdviewProgress.setTV_Right(mModel.getProgress_point_format());
			} else
			{
				mSdviewProgress.setTV_Right("未找到数据");
			}

			mSdviewRate.setTV_Left("年利率"); // 年利率
			mSdviewRate.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
			if (mModel.getRate() != null)
			{
				mSdviewRate.setTV_Right(mModel.getRate() + "%");
			} else
			{
				mSdviewRate.setTV_Right("未找到数据");
			}

			mSdviewRepayTime.setTV_Left("期限　　"); // 期限
			mSdviewRepayTime.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getRepay_time() != null && mModel.getRepay_time_type_format() != null)
			{
				mSdviewRepayTime.setTV_Right(mModel.getRepay_time() + mModel.getRepay_time_type_format());
			} else
			{
				mSdviewRepayTime.setTV_Right("未找到数据");
			}

			mSdviewLoanType.setTV_Left("还款方式"); // 还款方式
			if (mModel.getLoantype() != null && mModel.getLoantype_format() != null)
			{
				mSdviewLoanType.setTV_Right(mModel.getLoantype_format());
			} else
			{
				mSdviewLoanType.setTV_Right("未找到数据");
			}

			mSdviewNeedMoney.setTV_Left("可投金额"); // 可投金额
			mSdviewNeedMoney.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
			if (mModel.getNeed_money() != null)
			{
				mSdviewNeedMoney.setTV_Right(mModel.getNeed_money());
			} else
			{
				mSdviewNeedMoney.setTV_Right("未找到数据");
			}

		}

	}

	private void registeClick()
	{
		mTxtIWantRecharge.setOnClickListener(this);
		mBtnConfirmInvest.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_confirm_bid_txt_i_want_recharge:
			// TODO:跳到充值界面
			startActivity(new Intent(ConfirmBidActivity.this, RechargeActivity.class));
			break;

		case R.id.act_confirm_bid_btn_confirm_invest:
			clickConfirmInvest();
			break;

		default:
			break;
		}
	}

	private boolean validateParams()
	{
		if (App.getApplication().getmLocalUser() == null)
		{
			return false;
		}
		mStrBidMoney = mEdtInvestMoneyAmount.getText().toString().trim();
		mStrBidPayPassword = mEdtPayPassword.getText().toString();

		if (TextUtils.isEmpty(mStrBidMoney))
		{
			SDToast.showToast("投标金额不能为空!");
			SDUIUtil.showInputMethod(getApplicationContext(), mEdtInvestMoneyAmount, true);
			return false;
		}
		try
		{
			double moneyCanUse = Double.parseDouble(App.getApplication().getmLocalUser().getUserMoney());
			double moneyBid = Double.parseDouble(mStrBidMoney);
			double moneyMinLoan = Double.parseDouble(mModel.getMin_loan_money());
			if (moneyBid < moneyMinLoan)
			{
				SDToast.showToast("您的投资金额小于最低起投金额:" + mModel.getMin_loan_money() + "元");
				return false;
			}

			if (moneyBid > moneyCanUse)
			{
				SDToast.showToast("您的可用余额不足!");
				SDUIUtil.showInputMethod(getApplicationContext(), mEdtInvestMoneyAmount, true);
				return false;
			}
		} catch (Exception e)
		{
			return false;
		}

		if (TextUtils.isEmpty(mStrBidPayPassword))
		{
			SDToast.showToast("支付密码不能为空!");
			SDUIUtil.showInputMethod(getApplicationContext(), mEdtPayPassword, true);
			return false;
		}
		return true;
	}

	/**
	 * 请求投标接口
	 */
	private void clickConfirmInvest()
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && validateParams())
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "deal_dobid");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			mapData.put("id", mModel.getId());
			mapData.put("bid_money", mStrBidMoney);
			mapData.put("bid_paypassword", mStrBidPayPassword);
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				private Dialog nDialog = null;

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					BaseActModel model = JSON.parseObject(content, BaseActModel.class);
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
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					BaseActModel actModel = (BaseActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						if (actModel.getResponse_code() == 1)
						{
							EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_BID_SUCCESS));
							CommonInterface.refreshLocalUser();
							finish();
						} else
						{
							if (actModel.getShow_err() == null)
							{
								SDToast.showToast("投标失败!");
							}
						}
					}
				}

			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}

	}

}