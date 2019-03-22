package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.R.color;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.customview.SDSimpleTabView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.fragment.PayOfflineFragment;
import com.fanwe.p2p.fragment.PayOnlineFragment;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.Uc_InchargeActBelow_paymentModel;
import com.fanwe.p2p.model.Uc_InchargeActPayment_listModel;
import com.fanwe.p2p.model.act.Uc_InchargeActModel;
import com.fanwe.p2p.model.act.Uc_Save_InchargeActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDUIUtil;
import com.fanwe.p2p.utils.SDViewNavigatorManager;
import com.fanwe.p2p.utils.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 充值界面
 * @author js02
 *
 */
public class RechargeActivity extends BaseActivity implements OnClickListener
{
	private static final int TEXT_SIZE_TAB = 18;

	@ViewInject(id = R.id.act_recharge_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_recharge_tab_pay_online)
	private SDSimpleTabView mTabPayOnline = null;

	@ViewInject(id = R.id.act_recharge_tab_pay_offline)
	private SDSimpleTabView mTabPayOffline = null;

	@ViewInject(id = R.id.act_recharge_edt_money_recharge)
	private ClearEditText mEdtMoneyRecharge = null;

	@ViewInject(id = R.id.act_recharge_frame_container_pay_online)
	private FrameLayout mFrameContainerPayOnline = null;

	@ViewInject(id = R.id.act_recharge_frame_container_pay_offline)
	private FrameLayout mFrameContainerPayOffline = null;

	@ViewInject(id = R.id.act_recharge_btn_recharge)
	private Button mBtnRecharge = null;

	private SDViewNavigatorManager mNavigator = new SDViewNavigatorManager();

	private PayOnlineFragment mFragPayOnline = null;

	private PayOfflineFragment mFragPayOffline = null;

	private String mStrRechargeMoney = null;

	private Uc_InchargeActPayment_listModel mSelectItemOnline = null;
	private Uc_InchargeActBelow_paymentModel mSelectItemOffline = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_recharge);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initTitle();
		initTabs();
		registeClick();
		requestData();
		bindData();
	}

	private void bindData()
	{

		mFragPayOffline = new PayOfflineFragment();
		addFragment(mFragPayOffline, R.id.act_recharge_frame_container_pay_offline);

		mFragPayOnline = new PayOnlineFragment();
		addFragment(mFragPayOnline, R.id.act_recharge_frame_container_pay_online);

		mNavigator.setSelectIndex(0, mTabPayOnline, true);
	}

	/**
	 * 加载充值列表接口
	 */
	private void requestData()
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && user.getUserName() != null && user.getUserPassword() != null)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_incharge");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				private Dialog nDialog = null;

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					Uc_InchargeActModel model = JSON.parseObject(content, Uc_InchargeActModel.class);
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
					Uc_InchargeActModel actModel = (Uc_InchargeActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						if (actModel.getResponse_code() == 1)
						{
							if (actModel.getPayment_list() != null && actModel.getPayment_list().size() > 0)
							{
								mFragPayOnline.setmListModel(actModel.getPayment_list());
							}
							if (actModel.getBelow_payment() != null && actModel.getBelow_payment().size() > 0)
							{
								// TODO:给fragment传递数据
								mFragPayOffline.setmListModel(actModel.getBelow_payment());
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

	private void initTitle()
	{
		mTitle.setTitle("充值");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{

			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);
	}

	private void initTabs()
	{
		mTabPayOnline.setmBackgroundImageNormal(R.drawable.rec_tab_left_normal);
		mTabPayOnline.setmBackgroundImageSelect(R.drawable.rec_tab_left_press);
		mTabPayOnline.setmTextColorNormal(getResources().getColor(color.bg_title));
		mTabPayOnline.setmTextColorSelect(getResources().getColor(color.white));
		mTabPayOnline.setTabName("线上支付");
		mTabPayOnline.mTxtTabName.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_TAB);

		mTabPayOffline.setmBackgroundImageNormal(R.drawable.rec_tab_right_normal);
		mTabPayOffline.setmBackgroundImageSelect(R.drawable.rec_tab_right_press);
		mTabPayOffline.setmTextColorNormal(getResources().getColor(color.bg_title));
		mTabPayOffline.setmTextColorSelect(getResources().getColor(color.white));
		mTabPayOffline.setTabName("线下支付");
		mTabPayOffline.mTxtTabName.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_TAB);

		SDSimpleTabView[] items = new SDSimpleTabView[] { mTabPayOnline, mTabPayOffline };

		mNavigator.setItems(items);
		mNavigator.setmListener(new SDViewNavigatorManagerListener()
		{

			@Override
			public void onItemClick(View v, int index)
			{
				switch (index)
				{
				case 0:
					showFragment(mFragPayOnline);
					hideFragment(mFragPayOffline);
					break;
				case 1:
					showFragment(mFragPayOffline);
					hideFragment(mFragPayOnline);
					break;

				default:
					break;
				}
			}
		});
	}

	private void registeClick()
	{
		mBtnRecharge.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_recharge_btn_recharge:
			clickRecharge();
			break;

		default:
			break;
		}
	}

	/**
	 * 访问充值接口
	 */
	private void clickRecharge()
	{
		if (validateParam())
		{
			LocalUserModel user = App.getApplication().getmLocalUser();
			if (user != null && user.getUserName() != null && user.getUserPassword() != null)
			{
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("act", "uc_save_incharge");
				mapData.put("email", user.getUserName());
				mapData.put("pwd", user.getUserPassword());
				switch (mNavigator.getmCurrentIndex())
				{
				case 0: //线上支付
					mapData.put("payment_id", mSelectItemOnline.getId());
					break;
				case 1:
					mapData.put("payment_id", mSelectItemOffline.getPay_id());
					mapData.put("bank_id", mSelectItemOffline.getBank_id());
					mapData.put("memo", mFragPayOffline.getLeaveMsg());
					break;
				default:
					break;
				}
				mapData.put("money", mStrRechargeMoney);

				RequestModel model = new RequestModel(mapData);
				SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
				{
					private Dialog nDialog = null;

					@Override
					public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
					{
						Uc_Save_InchargeActModel model = JSON.parseObject(content, Uc_Save_InchargeActModel.class);
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
						Uc_Save_InchargeActModel actModel = (Uc_Save_InchargeActModel) result;
						if (!SDInterfaceUtil.isActModelNull(actModel))
						{
							if (actModel.getResponse_code() == 1)
							{
								if (actModel.getPay_type() != null)
								{
									if ("1".equals(actModel.getPay_type())) // 线上支付
									{
										if (actModel.getPay_wap() != null)
										{
											Intent intent = new Intent(RechargeActivity.this, ProjectDetailWebviewActivity.class);
											intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, actModel.getPay_wap());
											intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "支付");
											startActivity(intent);
											finish();
										}
									} else if ("2".equals(actModel.getPay_type())) // 线下支付
									{
										finish();
									}
								}
							} else
							{

							}
						}
					}

					@Override
					public void onFailureInMainThread(Throwable error, String content, Object result)
					{
						// TODO Auto-generated method stub
						super.onFailureInMainThread(error, content, result);
					}

				};
				InterfaceServer.getInstance().requestInterface(model, handler, true);
			}
		}

	}

	private boolean validateParam()
	{
		mStrRechargeMoney = mEdtMoneyRecharge.getText().toString();
		if (TextUtils.isEmpty(mStrRechargeMoney))
		{
			SDToast.showToast("请输入充值金额!");
			SDUIUtil.showInputMethod(getApplicationContext(), mEdtMoneyRecharge, true);
			return false;
		}

		switch (mNavigator.getmCurrentIndex())
		{
		case 0: // 线上支付
			if (mFragPayOnline.getSelectItem() == null)
			{
				SDToast.showToast("请选择支付方式!");
				return false;
			} else
			{
				mSelectItemOnline = mFragPayOnline.getSelectItem();
			}
			break;
		case 1: // 线下支付
			if (mFragPayOffline.getSelectItem() == null)
			{
				SDToast.showToast("请选择银行卡!");
				return false;
			} else
			{
				mSelectItemOffline = mFragPayOffline.getSelectItem();
			}
			break;

		default:
			break;
		}
		return true;
	}

}