package com.fanwe.p2p;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.R.color;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.ViewHelper;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.Uc_Add_BankActItemModel;
import com.fanwe.p2p.model.act.Uc_Add_BankActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDUIUtil;
import com.fanwe.p2p.utils.SDViewUtil;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 添加银行卡界面
 * 
 * @author js02
 * 
 */
public class AddBankCardActivity extends BaseActivity implements OnClickListener
{

	public static final int RESULT_CODE_ADD_BANK_CARD_SUCCESS = 1;
	public static final int RESULT_CODE_ADD_BANK_CARD_FAIL = 2;

	@ViewInject(id = R.id.act_add_bank_card_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_add_bank_card_txt_account_name)
	private TextView mTxtAccountRealName = null;

	@ViewInject(id = R.id.act_add_bank_card_lin_select_bank)
	private LinearLayout mLinSelectBank = null;

	@ViewInject(id = R.id.act_add_bank_card_rla_more_bank)
	private RelativeLayout mRlaMoreBank = null;

	@ViewInject(id = R.id.act_add_bank_card_txt_select_bank)
	private TextView mTxtSelectBankName = null;

	@ViewInject(id = R.id.act_add_bank_card_edt_open_account_bank)
	private ClearEditText mEdtOpenAccountBank = null;

	@ViewInject(id = R.id.act_add_bank_card_edt_bank_card_number)
	private ClearEditText mEdtBankCardNumber = null;

	@ViewInject(id = R.id.act_add_bank_card_btn_cancel)
	private Button mBtnCancel = null;

	@ViewInject(id = R.id.act_add_bank_card_btn_add)
	private Button mBtnAdd = null;

	private PopupWindow mPopSelectBankCard = null;

	private Uc_Add_BankActItemModel mSelectBankModel = null;

	private String mStrOpenAccountBankName = null;
	private String mStrBankCardNumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_add_bank_card);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initTitle();
		registeClick();
		SDViewUtil.measureView(mLinSelectBank);
	}

	private void initTitle()
	{
		mTitle.setTitle("添加银行卡");
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
		mLinSelectBank.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		mBtnAdd.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_add_bank_card_lin_select_bank:
			clickSelectBankCard(v);
			break;

		case R.id.act_add_bank_card_btn_cancel:
			finish();
			break;

		case R.id.act_add_bank_card_btn_add:
			clickAddBankCard();
			break;

		default:
			break;
		}
	}

	/**
	 * 请求银行卡列表接口
	 */
	private void clickSelectBankCard(final View v)
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && user.getUserName() != null && user.getUserPassword() != null)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_add_bank");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				private Dialog nDialog = null;

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					Uc_Add_BankActModel model = JSON.parseObject(content, Uc_Add_BankActModel.class);
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
					Uc_Add_BankActModel actModel = (Uc_Add_BankActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						if (actModel.getResponse_code() == 1)
						{
							if (actModel.getItem() != null && actModel.getItem().size() > 0)
							{
								showPopSelectBankCard(actModel.getItem(), v);
							} else
							{
								SDToast.showToast("未找到银行列表数据!");
							}
							if (actModel.getReal_name() != null)
							{
								mTxtAccountRealName.setText(actModel.getReal_name());
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

	private void showPopSelectBankCard(final List<Uc_Add_BankActItemModel> listModel, View v)
	{

		View view = ViewHelper.getPopSimpleBankListView(this, listModel, new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (listModel != null && position < listModel.size())
				{
					Uc_Add_BankActItemModel model = listModel.get(position);
					mSelectBankModel = model;
					if (model != null && model.getName() != null)
					{
						mTxtSelectBankName.setTextColor(getResources().getColor(color.text_black));
						mTxtSelectBankName.setText(model.getName());
						if (mPopSelectBankCard != null)
						{
							mPopSelectBankCard.dismiss();
						}
					}
				}
			}
		});
		mPopSelectBankCard = new PopupWindow(view, mLinSelectBank.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopSelectBankCard.setFocusable(true);
		mPopSelectBankCard.setOutsideTouchable(true);
		mPopSelectBankCard.setBackgroundDrawable(new ColorDrawable());
		mPopSelectBankCard.showAsDropDown(v, 0, 10);
	}

	/**
	 * 请求添加银行卡接口
	 */
	private void clickAddBankCard()
	{
		if (validateParams())
		{
			LocalUserModel user = App.getApplication().getmLocalUser();
			if (user != null && user.getUserName() != null && user.getUserPassword() != null)
			{
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("act", "uc_save_bank");
				mapData.put("email", user.getUserName());
				mapData.put("pwd", user.getUserPassword());
				mapData.put("bank_id", mSelectBankModel.getId());
				mapData.put("bankzone", mStrOpenAccountBankName);
				mapData.put("bankcard", mStrBankCardNumber);
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
								setResult(RESULT_CODE_ADD_BANK_CARD_SUCCESS);
								finish();
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

	private boolean validateParams()
	{
		if (mSelectBankModel == null)
		{
			SDToast.showToast("请选择银行!");
			return false;
		}

		mStrOpenAccountBankName = mEdtOpenAccountBank.getText().toString();
		if (TextUtils.isEmpty(mStrOpenAccountBankName))
		{
			SDToast.showToast("开户行不能为空!");
			SDUIUtil.showInputMethod(getApplicationContext(), mEdtOpenAccountBank, true);
			return false;
		}

		mStrBankCardNumber = mEdtBankCardNumber.getText().toString();
		if (TextUtils.isEmpty(mStrBankCardNumber))
		{
			SDToast.showToast("银行卡号不能为空!");
			SDUIUtil.showInputMethod(getApplicationContext(), mEdtBankCardNumber, true);
			return false;
		}

		return true;
	}

	@Override
	public void onBackPressed()
	{
		if (mPopSelectBankCard != null && mPopSelectBankCard.isShowing())
		{
			mPopSelectBankCard.dismiss();
		} else
		{
			super.onBackPressed();
		}

	}

}