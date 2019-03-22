package com.fanwe.p2p;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.common.ImageLoaderManager;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.FeeStageModel;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.Uc_BankActFee_configModel;
import com.fanwe.p2p.model.Uc_BankActItemModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.server.SDFeeCalculater;
import com.fanwe.p2p.utils.SDFormatUtil;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDTypeParseUtil;
import com.fanwe.p2p.utils.SDUIUtil;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 申请提现界面
 * 
 * @author js02
 * 
 */
public class ApplyWithdrawActivity extends BaseActivity implements OnClickListener
{
	public static final String EXTRA_SELECT_BANK = "extra_select_bank";
	public static final String EXTRA_SELECT_BANK_FEE_LIST = "extra_select_bank_fee_list";

	@ViewInject(id = R.id.act_apply_withdraw_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_apply_withdraw_txt_bank_card_number)
	private TextView mTxtBankCardNumber = null;

	@ViewInject(id = R.id.act_apply_withdraw_img_bank_card)
	private ImageView mImgBankCard = null;

	@ViewInject(id = R.id.act_apply_withdraw_txt_bank_name)
	private TextView mTxtBankName = null;

	@ViewInject(id = R.id.act_apply_withdraw_txt_bank_real_name)
	private TextView mTxtBankRealName = null;

	@ViewInject(id = R.id.act_apply_withdraw_tv_money_can_use)
	private TextView mTvMoneyCanUse = null;

	@ViewInject(id = R.id.act_apply_withdraw_tv_fee)
	private TextView mTvFee = null;

	@ViewInject(id = R.id.act_apply_withdraw_tv_money_real_pay)
	private TextView mTvMoneyRealPay = null;

	@ViewInject(id = R.id.act_apply_withdraw_edt_money)
	private ClearEditText mEdtMoney = null;

	@ViewInject(id = R.id.act_apply_withdraw_edt_pay_password)
	private ClearEditText mEdtPayPassword = null;

	@ViewInject(id = R.id.act_apply_withdraw_btn_apply_withdraw)
	private Button mBtnApplyWithdraw = null;

	private Uc_BankActItemModel mSelectBank = null;
	private List<Uc_BankActFee_configModel> mListFeeConfig = null;
	private String mStrMoneyInput = null;
	private String mStrPayPassword = null;

	private SDFeeCalculater mFeeCalculater = new SDFeeCalculater();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_apply_withdraw);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		CommonInterface.refreshLocalUser();
		initIntentData();
		initTitle();
		initItmes();
		initEdt();
		registeClick();

	}

	private void initEdt()
	{
		mEdtMoney.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				if (s.toString().equals(""))
				{
					mTvFee.setText("￥0.00");
					mTvMoneyRealPay.setText("￥0.00");
				} else
				{
					try
					{
						Double fee = mFeeCalculater.getFee(Float.parseFloat(s.toString()));
						Double money = SDTypeParseUtil.getDoubleFromString(s.toString(), 0);
						if (fee != null)
						{
							mTvFee.setText("￥" + fee);
							String formatMoney = SDFormatUtil.formatMoneyChina(money + fee);
							mTvMoneyRealPay.setText(formatMoney);
						}
					} catch (Exception e)
					{
						// TODO: handle exception
					}
				}

			}
		});

	}

	@SuppressWarnings("unchecked")
	private void initIntentData()
	{
		mSelectBank = (Uc_BankActItemModel) getIntent().getSerializableExtra(EXTRA_SELECT_BANK);
		mListFeeConfig = (List<Uc_BankActFee_configModel>) getIntent().getSerializableExtra(EXTRA_SELECT_BANK_FEE_LIST);
		if (mSelectBank != null && mListFeeConfig != null)
		{
			int i = 0;
			for (Uc_BankActFee_configModel fee : mListFeeConfig)
			{
				if (fee != null)
				{
					FeeStageModel feeStageModel = new FeeStageModel();
					try
					{
						if (i == 0)
						{
							i++;
							continue;
						}

						feeStageModel.setFee(Float.parseFloat(fee.getFee()));
						feeStageModel.setFeeFormat(fee.getFee_format());
						feeStageModel.setMax(Float.parseFloat(fee.getMax_price()));
						feeStageModel.setMin(Float.parseFloat(fee.getMin_price()));
						mFeeCalculater.addFeeStageModel(feeStageModel);

					} catch (Exception e)
					{
						SDToast.showToast("服务器返回数组出错!", 1);
					}

				}
			}
		}
	}

	private void initTitle()
	{
		mTitle.setTitle("申请提现");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);
	}

	private void initItmes()
	{
		if (mSelectBank != null && mListFeeConfig != null)
		{
			if (mSelectBank.getBankcard() != null)
			{
				mTxtBankCardNumber.setText(mSelectBank.getBankcard());
			}
			if (mSelectBank.getImg() != null)
			{
				ImageLoaderManager.getImageLoader().displayImage(mSelectBank.getImg(), mImgBankCard, ImageLoaderManager.getOptions());
			}
			if (mSelectBank.getBank_name() != null)
			{
				mTxtBankName.setText(mSelectBank.getBank_name());
			}
			if (mSelectBank.getReal_name() != null)
			{
				mTxtBankRealName.setText(mSelectBank.getReal_name());
			}

			if (App.getApplication().getmLocalUser() != null)
			{
				mTvMoneyCanUse.setText(App.getApplication().getmLocalUser().getUserMoneyFormat());
			}

			if (App.getApplication().getmLocalUser() != null)
			{
				mTvFee.setText("￥0.00");
			}

			if (App.getApplication().getmLocalUser() != null)
			{
				mTvMoneyRealPay.setText("￥0.00");
			}

		}

	}

	private void registeClick()
	{
		mBtnApplyWithdraw.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_apply_withdraw_btn_apply_withdraw:
			clickApplyWithdraw();
			break;

		default:
			break;
		}
	}

	private void clickApplyWithdraw()
	{
		if (validateParam())
		{
			LocalUserModel user = App.getApplication().getmLocalUser();
			if (user != null && user.getUserName() != null && user.getUserPassword() != null)
			{
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("act", "uc_save_carry");
				mapData.put("email", user.getUserName());
				mapData.put("pwd", user.getUserPassword());
				mapData.put("bid", mSelectBank.getId());
				mapData.put("paypassword", mStrPayPassword);
				mapData.put("amount", mStrMoneyInput);
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
		// TODO Auto-generated method stub
		mStrMoneyInput = mEdtMoney.getText().toString();
		mStrPayPassword = mEdtPayPassword.getText().toString();

		if (TextUtils.isEmpty(mStrMoneyInput))
		{
			SDToast.showToast("请输入金额!");
			SDUIUtil.showInputMethod(getApplicationContext(), mEdtMoney, true);
			return false;
		}

		if (TextUtils.isEmpty(mStrPayPassword))
		{
			SDToast.showToast("请输入支付密码!");
			SDUIUtil.showInputMethod(getApplicationContext(), mEdtPayPassword, true);
			return false;
		}

		try
		{
			Float userMoney = Float.parseFloat(App.getApplication().getmLocalUser().getUserMoney());
			Float moneyInput = Float.parseFloat(mStrMoneyInput);
			if (moneyInput > userMoney)
			{
				SDToast.showToast("提现金额大于可用余额!");
				SDUIUtil.showInputMethod(getApplicationContext(), mEdtMoney, true);
				return false;
			}
		} catch (Exception e)
		{
			return false;
		}

		return true;
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (event.getEventTagInt())
		{
		case EventTag.EVENT_REFRESH_USER_SUCCESS:
			if (App.getApplication().getmLocalUser() != null)
			{
				mTvMoneyCanUse.setText(App.getApplication().getmLocalUser().getUserMoneyFormat());
			}
			break;

		default:
			break;
		}
	}

}