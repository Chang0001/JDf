package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.customview.SDSimpleProjectDetailItemView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.UcReapyBorrowAdvanceActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * Title:个人中心提前还款
 * 
 * @author: yhz CreateTime：2014-6-16 下午2:22:17
 */
public class UcRepayBorrowAdvanceActivity extends BaseActivity implements OnClickListener
{
	public static final int RESULT_CODE_REPAY_ADVANCE_SUCCESS = 1;

	@ViewInject(id = R.id.act_ucRepBorAdv_sd_title)
	private SDSimpleTitleView mSdTitle = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_tv_name)
	private TextView mTvName = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_borrow_amount_format)
	private SDSimpleProjectDetailItemView mSdBorrowAountFormat = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_rate)
	private SDSimpleProjectDetailItemView mSdRate = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_repay_money)
	private SDSimpleProjectDetailItemView mSdReapyMoney = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_repay_time)
	private SDSimpleProjectDetailItemView mSdRepayTime = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_month_manage_money_format)
	private SDSimpleProjectDetailItemView mSdMonthManageMoneyFormat = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_impose_money_format)
	private SDSimpleProjectDetailItemView mSdImposeMoneyFormat = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_total_repay_money_format)
	private SDSimpleProjectDetailItemView mSdTotalRepayMoneyFormat = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_true_total_repay_money_format)
	private TextView mTvTrueTotalRepayMoneyFormat = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_btn_pay)
	private Button mBtnPay = null;

	@ViewInject(id = R.id.act_ucRepBorAdv_btn_repay)
	private Button mBtnRepay = null;

	public static final String EXTRA_DEAL_ID = "extra_deal_id";

	private String id = null;

	private boolean mIsRepaySuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_uc_repay_borrow_advance);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initIntentData();
		registeClick();
		ininTitle();
		initItems();
		requestDataOne();
	}

	private void initIntentData()
	{
		if (getIntent().getStringExtra(EXTRA_DEAL_ID) != null)
			id = getIntent().getStringExtra(EXTRA_DEAL_ID);
	}

	private void registeClick()
	{
		mBtnPay.setOnClickListener(this);
		mBtnRepay.setOnClickListener(this);
	}

	private void ininTitle()
	{
		mSdTitle.setTitle("提前还款");
		mSdTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				// TODO Auto-generated method stub
				finish();
			}
		}, null);
	}

	private void initItems()
	{
		mSdBorrowAountFormat.setTV_Left("借款金额");
		mSdRate.setTV_Left("年利率");
		mSdReapyMoney.setTV_Left("已还总额");
		mSdRepayTime.setTV_Left("期限");
		mSdMonthManageMoneyFormat.setTV_Left("管理费");
		mSdImposeMoneyFormat.setTV_Left("罚息");
		mSdTotalRepayMoneyFormat.setTV_Left("应还本息");
		initItemsColors();
	}

	private void initItemsColors()
	{
		mTvName.setTextColor(getResources().getColor(R.color.text_black_deep));
		mSdRate.inverstdetail_item_tv_right.setTextColor(getResources().getColor(R.color.text_orange));
		mSdReapyMoney.inverstdetail_item_tv_right.setTextColor(getResources().getColor(R.color.text_orange));
		mSdTotalRepayMoneyFormat.inverstdetail_item_tv_right.setTextColor(getResources().getColor(R.color.text_orange));
	}

	protected void requestDataOne()
	{
		if (id == null)
		{
			return;
		}
		Map<String, Object> mMapData = new HashMap<String, Object>();
		mMapData.put("act", "uc_inrepay_refund");
		mMapData.put("id", id);
		mMapData.put("email", App.getApplication().getmLocalUser().getUserName());
		mMapData.put("pwd", App.getApplication().getmLocalUser().getUserPassword());
		RequestModel model = new RequestModel(mMapData);
		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{
			private Dialog nDialog = null;

			@Override
			public void onStartInMainThread(Object result)
			{
				nDialog = mDialogUtil.showLoading("正在请求数据...");
			}

			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				UcReapyBorrowAdvanceActModel model = null;
				try
				{
					model = JSON.parseObject(content, UcReapyBorrowAdvanceActModel.class);
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

				UcReapyBorrowAdvanceActModel actModel = (UcReapyBorrowAdvanceActModel) result;
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					updateUI(actModel);
				}
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	/**
	 * 确认还款
	 */
	protected void requestDataTwo()
	{
		if (id == null)
		{
			return;
		}
		Map<String, Object> mMapData = new HashMap<String, Object>();
		mMapData.put("act", "uc_do_inrepay_refund");
		mMapData.put("id", id);
		mMapData.put("email", App.getApplication().getmLocalUser().getUserName());
		mMapData.put("pwd", App.getApplication().getmLocalUser().getUserPassword());
		RequestModel model = new RequestModel(mMapData);
		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{
			private Dialog nDialog = null;

			@Override
			public void onStartInMainThread(Object result)
			{
				nDialog = mDialogUtil.showLoading("正在请求数据...");
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
						mIsRepaySuccess = true;
						finish();
					}

				}
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.act_ucRepBorAdv_btn_pay:
			clickRecharge();
			break;
		case R.id.act_ucRepBorAdv_btn_repay:
			requestDataTwo();
			break;
		}
	}

	private void updateUI(UcReapyBorrowAdvanceActModel model)
	{
		if (model.getDeal().getName() != null)
		{
			mTvName.setText(model.getDeal().getName());
		}
		if (model.getDeal().getBorrow_amount_format() != null)
		{
			mSdBorrowAountFormat.setTV_Right(model.getDeal().getBorrow_amount_format());
		}
		if (model.getDeal().getRate() != null)
		{
			mSdRate.setTV_Right(model.getDeal().getRate() + "%");
		}
		if (model.getDeal().getRepay_money() != null)
		{
			mSdReapyMoney.setTV_Right("¥ " + model.getDeal().getRepay_money());
		}
		if (model.getDeal().getRepay_time() != null && model.getDeal().getRepay_time_type() != null)
		{
			switch (Integer.valueOf(model.getDeal().getRepay_time_type()))
			{
			case 0:
				mSdRepayTime.setTV_Right(model.getDeal().getRepay_time() + "天");
				break;
			case 1:
				mSdRepayTime.setTV_Right(model.getDeal().getRepay_time() + "个月");
				break;
			}
		}
		if (model.getDeal().getMonth_manage_money_format() != null)
		{
			mSdMonthManageMoneyFormat.setTV_Right(model.getDeal().getMonth_manage_money_format());
		}
		if (model.getImpose_money_format() != null)
		{
			mSdImposeMoneyFormat.setTV_Right(model.getImpose_money_format());
		}
		if (model.getTotal_repay_money_format() != null)
		{
			mSdTotalRepayMoneyFormat.setTV_Right(model.getTotal_repay_money_format());
		}
		if (model.getTrue_total_repay_money_format() != null)
		{
			mTvTrueTotalRepayMoneyFormat.setText(model.getTrue_total_repay_money_format());
		}

	}

	private void clickRecharge()
	{
		startActivity(new Intent(getApplicationContext(), RechargeActivity.class));
	}

	@Override
	public void finish()
	{
		if (mIsRepaySuccess)
		{
			setResult(RESULT_CODE_REPAY_ADVANCE_SUCCESS);
		}
		super.finish();
	}

}