package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.customview.SDSendValidateButton;
import com.fanwe.p2p.customview.SDSendValidateButton.SDSendValidateButtonListener;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.RegisterActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDUIUtil;
import com.fanwe.p2p.utils.SDValidateUtil;
import com.fanwe.p2p.utils.UIHelper;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

import de.greenrobot.event.EventBus;

/**
 * 注册界面
 * 
 * @author js02
 * 
 */
public class RegisteActivity extends BaseActivity implements OnClickListener
{
	@ViewInject(id = R.id.act_registe_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_registe_edt_username)
	private ClearEditText mEdtUsername = null;

	@ViewInject(id = R.id.act_registe_edt_password)
	private ClearEditText mEdtPassword = null;

	@ViewInject(id = R.id.act_registe_edt_confirm_password)
	private ClearEditText mEdtConfirmPassword = null;

	@ViewInject(id = R.id.act_registe_edt_cellphone_number)
	private ClearEditText mEdtCellphoneNumber = null;

	@ViewInject(id = R.id.act_registe_edt_validate_code)
	private ClearEditText mEdtValidateCode = null;

	@ViewInject(id = R.id.act_registe_btn_registe)
	private Button mBtnRegiste = null;

	@ViewInject(id = R.id.act_registe_btn_sand_validate)
	private SDSendValidateButton mBtnSendValidate = null;

	private String mStrUsername = null;
	private String mStrPassword = null;
	private String mStrConfirmPassword = null;
	private String mStrCellphoneNumber = null;
	private String mStrValidateCode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_registe);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initTitle();
		initSendValidateButton();
		registeClick();

	}

	/**
	 * 初始化发送验证码按钮
	 */
	private void initSendValidateButton()
	{
		mBtnSendValidate.setmEnableString("获取验证码");
		mBtnSendValidate.setmEnableColor(Color.WHITE);
		mBtnSendValidate.setmDisableColor(Color.WHITE);
		mBtnSendValidate.setmListener(new SDSendValidateButtonListener()
		{

			@Override
			public void onTick()
			{
			}

			@Override
			public void onClickSendValidateButton()
			{
				requestValidateCode();
			}
		});

	}

	/**
	 * 请求验证码接口
	 */
	protected void requestValidateCode()
	{
		if (validateSendValidateCodeParams())
		{

			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "send_register_code");
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
						switch (actModel.getResponse_code())
						{
						case 1:
							mBtnSendValidate.startTickWork();
							break;
						case 0:
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

	private void initTitle()
	{
		mTitle.setTitle("账户注册");
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
		mBtnRegiste.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_registe_btn_registe:
			clickRegiste();
			break;

		default:
			break;
		}
	}

	/**
	 * 点击注册按钮
	 */
	private void clickRegiste()
	{
		if (validateRegisteParams())
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "register");
			mapData.put("user_name", mStrUsername);
			mapData.put("user_pwd", mStrPassword);
			mapData.put("user_pwd_confirm", mStrConfirmPassword);
			mapData.put("mobile", mStrCellphoneNumber);
			mapData.put("mobile_code", mStrValidateCode);
			RequestModel model = new RequestModel(mapData);

			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				private Dialog nDialog = null;

				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					try
					{
						RegisterActModel actModel = JSON.parseObject(content, RegisterActModel.class);
						return actModel;
					} catch (Exception e)
					{
						return null;
					}
				}

				@Override
				public void onStartInMainThread(Object result)
				{
					nDialog = mDialogUtil.showLoading("注册中...");
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
					RegisterActModel actModel = (RegisterActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						switch (actModel.getResponse_code())
						{
						case 0:

							break;
						case 1:
							// TODO:注册成功并已经登录处理
							dealRegisteSuccess(actModel);
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

	private void dealRegisteSuccess(RegisterActModel actModel)
	{
		LocalUserModel user = new LocalUserModel();
		user.setId(actModel.getId());
		user.setUserName(actModel.getUser_name());
		user.setUserPassword(mStrPassword);
		user.setUserMoneyFormat(actModel.getUser_money_format());
		user.setUserMoney(actModel.getUser_money());
		App.getApplication().setmLocalUser(user);
		EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_REGISTER_AND_LOGIN_SUCCESS));
		CommonInterface.submitRegistrationID();
		UIHelper.showMain(this,true);
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

	/**
	 * 检查注册参数是否合法
	 * 
	 * @return
	 */
	private boolean validateRegisteParams()
	{
		mStrCellphoneNumber = mEdtCellphoneNumber.getText().toString();
		mStrConfirmPassword = mEdtConfirmPassword.getText().toString();
		mStrPassword = mEdtPassword.getText().toString();
		mStrUsername = mEdtUsername.getText().toString();
		mStrValidateCode = mEdtValidateCode.getText().toString();

		if (TextUtils.isEmpty(mStrUsername))
		{
			SDToast.showToast("用户名不能为空!");
			SDUIUtil.showInputMethod(this, mEdtUsername, true);
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
			SDUIUtil.showInputMethod(this, mEdtConfirmPassword, true);
			return false;
		}
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
		if (!mStrPassword.equals(mStrConfirmPassword))
		{
			SDToast.showToast("两次密码不一致!");
			SDUIUtil.showInputMethod(this, mEdtConfirmPassword, true);
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