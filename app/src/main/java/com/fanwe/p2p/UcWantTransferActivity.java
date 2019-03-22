package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.customview.SDSimpleProjectDetailItemView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.BondBuyModel;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.BondBuyActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

import de.greenrobot.event.EventBus;

/**
 * 个人中心债券转让页面
 * 
 * @author yhz
 */
public class UcWantTransferActivity extends BaseActivity implements OnClickListener
{
	@ViewInject(id = R.id.act_wantTran_tv_title)
	private SDSimpleTitleView mVTitle = null;

	@ViewInject(id = R.id.act_wantTran_tv_name)
	private TextView mVname = null;

	@ViewInject(id = R.id.act_wantTran_sd_next_repay_time_format)
	private SDSimpleProjectDetailItemView mDRepay = null;

	@ViewInject(id = R.id.act_wantTran_sd_monthAndrepay)
	private SDSimpleProjectDetailItemView mMonthAndReapy = null;

	@ViewInject(id = R.id.act_wantTran_sd_left_benjin)
	private SDSimpleProjectDetailItemView mDLeftBenjin = null;

	@ViewInject(id = R.id.act_wantTran_sd_left_lixi_format)
	private SDSimpleProjectDetailItemView mDLeftLixiFormat = null;

	@ViewInject(id = R.id.act_wantTran_sd_transfer_amount_format)
	private SDSimpleProjectDetailItemView mDTransferAmountFormat = null;

	@ViewInject(id = R.id.act_wantTran_et_trans_amount)
	private ClearEditText mTtrans = null;

	@ViewInject(id = R.id.act_wantTran_et_pwd)
	private ClearEditText mTPwd = null;

	@ViewInject(id = R.id.act_wantTran_btn_buy)
	private Button mTnBuy = null;

	public static final String TRA_DLID = "dlid";

	private LocalUserModel user = App.getApplication().getmLocalUser();

	private String mDlid = null;

	private String mDltid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_want_transfer);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		String dlid = initIntentData();
		registeClick();
		ininTitle();
		initItems();
		requestDataOne(dlid);
	}

	private String initIntentData()
	{
		String dlid = getIntent().getStringExtra(TRA_DLID);
		if (null != dlid)
		{
			return dlid;
		}
		return null;
	}

	private void registeClick()
	{
		mVTitle.setOnClickListener(this);
		mVname.setOnClickListener(this);
		mDLeftBenjin.setOnClickListener(this);
		mDLeftLixiFormat.setOnClickListener(this);
		mDTransferAmountFormat.setOnClickListener(this);
		mTPwd.setOnClickListener(this);
		mTnBuy.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_wantTran_btn_buy:
			clickWantTranBtnBuy();
			break;

		default:
			break;
		}
	}

	private void ininTitle()
	{
		mVTitle.setTitle("确认转让");
		mVTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
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
		mDRepay.setTV_Left("下个还款日");
		mMonthAndReapy.setTV_Left("待还/总期数");
		mDLeftBenjin.setTV_Left("剩余本金");
		mDLeftLixiFormat.setTV_Left("剩余利息");
		mDTransferAmountFormat.setTV_Left("最大转让金额");

	}

	protected void requestDataOne(String dlid)
	{
		Map<String, Object> mMapData = new HashMap<String, Object>();
		mMapData.put("act", "uc_to_transfer");
		mMapData.put("id", dlid);
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
				BondBuyActModel actModel = null;
				try
				{
					actModel = JSON.parseObject(content, BondBuyActModel.class);
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
			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				BondBuyActModel actModel = (BondBuyActModel) result;
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					updateUI(actModel);
				}
			}
		};

		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	protected void requestDataTwo(String transfer_money, String paypassword)
	{
		Map<String, Object> mMapData = new HashMap<String, Object>();
		mMapData.put("act", "uc_do_transfer");
		mMapData.put("email", App.getApplication().getmLocalUser().getUserName());
		mMapData.put("pwd", App.getApplication().getmLocalUser().getUserPassword());
		mMapData.put("dlid", mDlid);
		mMapData.put("dltid", mDltid);
		mMapData.put("transfer_money", transfer_money);
		mMapData.put("paypassword", paypassword);
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
						EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_TRANSFER_SUCCESS));
						CommonInterface.refreshLocalUser();
						finish();
					} else
					{
						if (actModel.getShow_err() == null)
						{
							SDToast.showToast("转让失败!");
						}
					}

				}
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	private void updateUI(BondBuyActModel actModel)
	{
		BondBuyModel bondModel = actModel.getTransfer();
		if (bondModel != null)
		{
			if (bondModel.getNext_repay_time_format() != null)
			{
				mDRepay.setTV_Right(bondModel.getNext_repay_time_format());
			}
			if (bondModel.getHow_much_month() != null && bondModel.getRepay_time() != null)
			{
				mMonthAndReapy.setTV_Right(bondModel.getHow_much_month() + "/" + bondModel.getRepay_time());
			}
			if (bondModel.getName() != null)
			{
				mVname.setText(actModel.getTransfer().getName());
			}
			if (bondModel.getLeft_benjin_format() != null)
			{
				mDLeftBenjin.setTV_Right(actModel.getTransfer().getLeft_benjin_format());
			}
			if (bondModel.getLeft_lixi_format() != null)
			{
				mDLeftLixiFormat.setTV_Right(bondModel.getLeft_lixi_format());
			}
			if (bondModel.getTransfer_amount_format() != null)
			{
				mDTransferAmountFormat.setTV_Right(bondModel.getTransfer_amount_format());
			}
			if (bondModel.getDlid() != null)
			{
				mDlid = bondModel.getDlid();
			}
			if (bondModel.getDltid() != null)
			{
				mDltid = bondModel.getDltid();
			}
		}
	}

	private void clickWantTranBtnBuy()
	{
		String transfer_money = mTtrans.getText().toString();
		String paypassword = mTPwd.getText().toString();
		if (transfer_money.equals(""))
		{
			mTtrans.setError("不能为空");
			return;
		}
		if (paypassword.equals(""))
		{
			mTPwd.setError("不能为空");
			return;
		}
		requestDataTwo(transfer_money, paypassword);
	}
}