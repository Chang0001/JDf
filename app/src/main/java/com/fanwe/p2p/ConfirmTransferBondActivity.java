package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

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
import com.fanwe.p2p.customview.SDSimpleTitleView.OnRightButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.TransferActItemModel;
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
 * 确认转让
 * 
 * @author js02
 * 
 */
public class ConfirmTransferBondActivity extends BaseActivity implements OnClickListener
{
	public static final String EXTRA_BOND_DETAIL_ITEM_MODEL = "extra_bond_detail_item_model";

	public static final int REQUEST_CODE_LOGIN_FOR_CONFIRM_TRANSFER_BOND = 1;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_txt_name)
	private TextView mTxtName = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_sdview_remain_time)
	private SDSimpleProjectDetailItemView mSdviewRemainTime = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_sdview_repay_time)
	private SDSimpleProjectDetailItemView mSdviewRepayTime = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_sdview_next_repay_time)
	private SDSimpleProjectDetailItemView mSdviewNextRepayTime = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_sdview_transfer_amount)
	private SDSimpleProjectDetailItemView mSdviewTransferAmount = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_sdview_left_benjin)
	private SDSimpleProjectDetailItemView mSdviewLeftBenjin = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_sdview_left_lixi)
	private SDSimpleProjectDetailItemView mSdviewLeftLixi = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_sdview_transfer_income)
	private SDSimpleProjectDetailItemView mSdviewTransferIncome = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_txt_money_can_use)
	private TextView mTxtMoneyCanUse = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_txt_i_want_recharge)
	private TextView mTxtIWantRecharge = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_edt_pay_password)
	private ClearEditText mEdtPayPassword = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_btn_confirm_buy)
	private Button mBtnConfirmBuy = null;

	private TransferActItemModel mModel = null;

	private String mStrPayPassword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_confirm_trnasfer_bond);
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

	private void initIntentData()
	{
		mModel = (TransferActItemModel) getIntent().getSerializableExtra(EXTRA_BOND_DETAIL_ITEM_MODEL);
	}

	private void initItems()
	{
		if (mModel != null)
		{
			// 名字
			SDViewUtil.measureView(mTxtName);
			int width = mTxtName.getMeasuredWidth();
			if ((width + 10) > SDUIUtil.getScreenWidth(getApplicationContext()))
			{
				mTxtName.setGravity(Gravity.LEFT);
			} else
			{
				mTxtName.setGravity(Gravity.CENTER);
			}
			if (mModel.getName() != null)
			{
				mTxtName.setText(mModel.getName());
			} else
			{
				mTxtName.setText(getString(R.string.data_not_found));
			}

			mSdviewRemainTime.setTV_Left("剩余时间");
			if (mModel.getRemain_time_format() != null)
			{
				mSdviewRemainTime.inverstdetail_item_tv_right.setText(mModel.getRemain_time_format());
			} else
			{
				mSdviewRemainTime.inverstdetail_item_tv_right.setText(App.getApplication().getString(R.string.data_not_found));
			}
			// if (mModel.getT_user_id() != null && mModel.getStatus() != null)
			// {
			// if (mModel.getT_user_id().equals("0") &&
			// mModel.getStatus().equals("1")) // 可转让才显示时间
			// {
			// mSdviewRemainTime.inverstdetail_item_tv_right.setVisibility(View.VISIBLE);
			// if (mModel.getRemain_time_format() != null)
			// {
			// mSdviewRemainTime.inverstdetail_item_tv_right.setText(mModel.getRemain_time_format());
			// } else
			// {
			// mSdviewRemainTime.inverstdetail_item_tv_right.setText(App.getApplication().getString(R.string.data_not_found));
			// }
			// } else //已转让
			// {
			// isTransfer = true;
			// mSdviewRemainTime.inverstdetail_item_tv_right.setText("已转让");
			// }
			// }

			mSdviewRepayTime.setTV_Left("总期限");
			if (mModel.getRepay_time() != null && mModel.getRepay_time_type_format() != null)
			{
				mSdviewRepayTime.setTV_Right(mModel.getRepay_time() + mModel.getRepay_time_type_format());
			} else
			{
				mSdviewRepayTime.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewNextRepayTime.setTV_Left("下还款日");
			mSdviewNextRepayTime.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getNear_repay_time_format() != null)
			{
				mSdviewNextRepayTime.setTV_Right(mModel.getNear_repay_time_format());
			} else
			{
				mSdviewNextRepayTime.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewTransferAmount.setTV_Left("转让金额");
			mSdviewTransferAmount.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
			if (mModel.getTransfer_amount_format() != null)
			{
				mSdviewTransferAmount.setTV_Right(mModel.getTransfer_amount_format());
			} else
			{
				mSdviewTransferAmount.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewLeftBenjin.setTV_Left("剩余本金");
			mSdviewLeftBenjin.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getLeft_benjin_format() != null)
			{
				mSdviewLeftBenjin.setTV_Right(mModel.getLeft_benjin_format());
			} else
			{
				mSdviewLeftBenjin.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewLeftLixi.setTV_Left("剩余利息");
			mSdviewLeftLixi.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getLeft_lixi_format() != null)
			{
				mSdviewLeftLixi.setTV_Right(mModel.getLeft_lixi_format());
			} else
			{
				mSdviewLeftLixi.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewTransferIncome.setTV_Left("受让收益");
			mSdviewTransferIncome.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
			if (mModel.getTransfer_income_format() != null)
			{
				mSdviewTransferIncome.setTV_Right(mModel.getTransfer_income_format());
			} else
			{
				mSdviewTransferIncome.setTV_Right(getString(R.string.data_not_found));
			}

			LocalUserModel user = App.getApplication().getmLocalUser();
			if (user != null && user.getUserMoneyFormat() != null)
			{
				mTxtMoneyCanUse.setText(user.getUserMoneyFormat());
			}

		}

	}

	private void initTitle()
	{
		mTitle.setTitle("确认转让");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);
		mTitle.setRightButtonText("详情", new OnRightButtonClickListener()
		{
			@Override
			public void onRightBtnClick(View button)
			{
				// TODO 跳到webview详情页面
				if (mModel != null && !TextUtils.isEmpty(mModel.getApp_url()))
				{
					Intent intent = new Intent(ConfirmTransferBondActivity.this, ProjectDetailWebviewActivity.class);
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "详情");
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, mModel.getApp_url());
					startActivity(intent);
				} else
				{
					SDToast.showToast("无详情链接");
				}
			}
		}, R.drawable.bg_title_my_interest_cancel, null);
	}

	private void registeClick()
	{
		mTxtIWantRecharge.setOnClickListener(this);
		mBtnConfirmBuy.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_confirm_trnasfer_bond_txt_i_want_recharge:

			clickIWantRecharge();
			break;

		case R.id.act_confirm_trnasfer_bond_btn_confirm_buy:
			clickConfirmBuy();
			break;

		default:
			break;
		}
	}

	private void clickIWantRecharge()
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null)
		{
			startActivity(new Intent(ConfirmTransferBondActivity.this, RechargeActivity.class));
		} else
		{
			Intent intent = new Intent(ConfirmTransferBondActivity.this, LoginActivity.class);
			startActivityForResult(intent, REQUEST_CODE_LOGIN_FOR_CONFIRM_TRANSFER_BOND);
		}

	}

	private void clickConfirmBuy()
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null)
		{
			if (validateParams())
			{
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("act", "transfer_dobid");
				mapData.put("email", user.getUserName());
				mapData.put("pwd", user.getUserPassword());
				mapData.put("id", mModel.getId());
				mapData.put("paypassword", mStrPayPassword);
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
								EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_BUY_BOND_TRANSFER_SUCCESS));
								CommonInterface.refreshLocalUser();
								finish();
							} else
							{
								if (actModel.getShow_err() == null)
								{
									SDToast.showToast("购买失败!");
								}
							}
						}
					}

				};
				InterfaceServer.getInstance().requestInterface(model, handler, true);
			}
		} else
		{
			Intent intent = new Intent(ConfirmTransferBondActivity.this, LoginActivity.class);
			startActivityForResult(intent, REQUEST_CODE_LOGIN_FOR_CONFIRM_TRANSFER_BOND);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case REQUEST_CODE_LOGIN_FOR_CONFIRM_TRANSFER_BOND:
			if (resultCode == LoginActivity.RESULT_CODE_LOGIN_SUCCESS)
			{
				initItems();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private boolean validateParams()
	{
		if (App.getApplication().getmLocalUser() == null)
		{
			return false;
		}

		if (mModel.getUser() != null && mModel.getUser_id().equals(App.getApplication().getmLocalUser().getId()))
		{
			SDToast.showToast("不能购买自己的债券");
			return false;
		}
		if (mModel.getUser() != null && mModel.getT_user_id().equals(App.getApplication().getmLocalUser().getId()))
		{
			SDToast.showToast("您已经购买该债券");
			return false;
		}

		mStrPayPassword = mEdtPayPassword.getText().toString();
		try
		{
			float moneyCanUse = Float.parseFloat(App.getApplication().getmLocalUser().getUserMoney());
			float moneyTransfer = Float.parseFloat(mModel.getTransfer_amount());
			if (moneyTransfer > moneyCanUse)
			{
				SDToast.showToast("您的可用余额不足!");
				return false;
			}
		} catch (Exception e)
		{
			return false;
		}

		if (TextUtils.isEmpty(mStrPayPassword))
		{
			SDToast.showToast("支付密码不能为空!");
			SDUIUtil.showInputMethod(getApplicationContext(), mEdtPayPassword, true);
			return false;
		}
		return true;
	}

}