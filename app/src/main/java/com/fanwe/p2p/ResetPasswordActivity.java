package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.customview.SDSendValidateButton;
import com.fanwe.p2p.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDUIUtil;
import com.fanwe.p2p.utils.SDValidateUtil;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;
/**
 * 找回密码界面
 * @author js02
 *
 */
public class ResetPasswordActivity extends BaseActivity implements OnClickListener
{
	@ViewInject(id = R.id.act_reset_password_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_reset_password_edt_cellphone_number)
	private ClearEditText mEdtCellphoneNumber = null;

	@ViewInject(id = R.id.act_reset_password_btn_sand_validate)
	private SDSendValidateButton mBtnSendValidate = null;

	@ViewInject(id = R.id.act_reset_password_edt_validate_code)
	private ClearEditText mEdtValidateCode = null;

	@ViewInject(id = R.id.act_reset_password_edt_password)
	private ClearEditText mEdtPassword = null;

	@ViewInject(id = R.id.act_reset_password_edt_password_confirm)
	private ClearEditText mEdtPasswordConfirm = null;

	@ViewInject(id = R.id.act_reset_password_btn_submit)
	private Button mBtnSubmit = null;

	private String mStrPassword = null;
	private String mStrConfirmPassword = null;
	private String mStrCellphoneNumber = null;
	private String mStrValidateCode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_reset_password);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initTitle();
		initSendValidateCodeButton();
		registeClick();

	}

	private void initTitle()
	{
		mTitle.setTitle("找回密码");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				finish();

			}
		}, null);
	}

	private void initSendValidateCodeButton()
	{

		mBtnSendValidate.setmEnableString("获取验证码");
		mBtnSendValidate.setmEnableColor(Color.WHITE);
		mBtnSendValidate.setmDisableColor(Color.WHITE);
		mBtnSendValidate.setmListener(new SDSendValidateButtonListener()
		{

			@Override
			public void onTick()
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onClickSendValidateButton()
			{
				clickSendValidateButton();
			}
		});

	}

	/**
	 * 发送验证码按钮被点击
	 */
	protected void clickSendValidateButton()
	{
		if (validateSendValidateCodeParams())
		{
			
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "send_reset_pwd_code");
			mapData.put("mobile", mStrCellphoneNumber);
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				private Dialog nDialog = null;

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					try
					{
						BaseActModel actModel = JSON.parseObject(content, BaseActModel.class);
						return actModel;
					} catch (Exception e)
					{
						return null;
					}
				}

				@Override
				public void onStartInMainThread(Object result)
				{
					nDialog = mDialogUtil.showLoading("正在请求验证码...");
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
							mBtnSendValidate.startTickWork();
						}
					}
				}
			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}
	}

	private boolean validateSendValidateCodeParams()
	{
		mStrCellphoneNumber = mEdtCellphoneNumber.getText().toString();
		if (TextUtils.isEmpty(mStrCellphoneNumber))
		{
			SDToast.showToast("手机号不能为空!");
			SDUIUtil.showInputMethod(this, mEdtCellphoneNumber, true);
			return false;
		}
		if (!SDValidateUtil.isCellPhoneNumber(mStrCellphoneNumber))
		{
			SDToast.showToast("手机号格式不正确!");
			SDUIUtil.showInputMethod(this, mEdtCellphoneNumber, true);
			return false;
		}
		return true;
	}

	private void registeClick()
	{
		mBtnSubmit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_reset_password_btn_submit:
			clickSubitBtn();
			break;

		default:
			break;
		}
	}

	/**
	 * 确定按钮被点击
	 */
	private void clickSubitBtn()
	{
		if (validateParams())
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "save_reset_pwd");
			mapData.put("mobile", mStrCellphoneNumber);
			mapData.put("mobile_code", mStrValidateCode);
			mapData.put("user_pwd", mStrPassword);
			mapData.put("user_pwd_confirm", mStrConfirmPassword);
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				private Dialog nDialog = null;

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					try
					{
						BaseActModel actModel = JSON.parseObject(content, BaseActModel.class);
						return actModel;
					} catch (Exception e)
					{
						return null;
					}
				}

				@Override
				public void onStartInMainThread(Object result)
				{
					nDialog = mDialogUtil.showLoading("正在重置密码...");
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
						switch (actModel.getResponse_code())
						{
						case 0:

							break;
						case 1:
							finish();
							break;

						default:
							break;
						}
					}
				}
			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}

	}

	/**
	 * 检查重置参数是否合法
	 * 
	 * @return
	 */
	private boolean validateParams()
	{
		mStrCellphoneNumber = mEdtCellphoneNumber.getText().toString();
		mStrConfirmPassword = mEdtPasswordConfirm.getText().toString();
		mStrPassword = mEdtPassword.getText().toString();
		mStrValidateCode = mEdtValidateCode.getText().toString();
		if (TextUtils.isEmpty(mStrCellphoneNumber))
		{
			SDToast.showToast("手机号不能为空!");
			SDUIUtil.showInputMethod(this, mEdtCellphoneNumber, true);
			return false;
		}
		if (!SDValidateUtil.isCellPhoneNumber(mStrCellphoneNumber))
		{
			SDToast.showToast("手机号格式不正确!");
			SDUIUtil.showInputMethod(this, mEdtCellphoneNumber, true);
			return false;
		}

		if (TextUtils.isEmpty(mStrValidateCode))
		{
			SDToast.showToast("验证码不能为空!");
			SDUIUtil.showInputMethod(this, mEdtValidateCode, true);
			return false;
		}

		if (TextUtils.isEmpty(mStrPassword))
		{
			SDToast.showToast("密码不能为空!");
			SDUIUtil.showInputMethod(this, mEdtPassword, true);
			return false;
		}
		if (TextUtils.isEmpty(mStrConfirmPassword))
		{
			SDToast.showToast("再次输入密码不能为空!");
			SDUIUtil.showInputMethod(this, mEdtPasswordConfirm, true);
			return false;
		}

		if (!mStrPassword.equals(mStrConfirmPassword))
		{
			SDToast.showToast("两次密码不一致!");
			SDUIUtil.showInputMethod(this, mEdtPasswordConfirm, true);
			return false;
		}
		return true;
	}

	@Override
	public void onBackPressed()
	{
		finish();
		super.onBackPressed();
	}

}