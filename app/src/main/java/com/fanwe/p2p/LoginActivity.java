package com.fanwe.p2p;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.LoginActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.UIHelper;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

import de.greenrobot.event.EventBus;

/**
 * 登录界面
 * 
 * @author js02
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int RESULT_CODE_LOGIN_SUCCESS = 1;

	@ViewInject(id = R.id.act_login_SDTitle)
	private SDSimpleTitleView mSdtitle = null;

	@ViewInject(id = R.id.act_login_et_username)
	private ClearEditText mEtUsername = null;

	@ViewInject(id = R.id.act_login_et_password)
	private ClearEditText mEtPassword = null;

	@ViewInject(id = R.id.act_login_btn_login)
	private Button mBtnLogin = null;

	@ViewInject(id = R.id.act_login_tv_forget)
	private TextView mTvForget = null;

	@ViewInject(id = R.id.act_login_tv_reg)
	private TextView mTvReg = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_login);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		mIsNeedFinishWhenLogout = false;
		registeClick();
		ininCustomview();
	}

	private void ininCustomview()
	{
		mSdtitle.setTitle("登录");
		mSdtitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{

			@Override
			public void onLeftBtnClick(View button)
			{
				// TODO Auto-generated method stub
				finish();
			}
		}, null);
	}

	private void requestLogin()
	{
		String email = mEtUsername.getText().toString();
		final String pwd = mEtPassword.getText().toString();
		if (TextUtils.isEmpty(email))
		{
			SDToast.showToast("帐号不能为空");
			return;
		}
		if (TextUtils.isEmpty(pwd))
		{
			SDToast.showToast("密码不能为空");
			return;
		}
		
		
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "login");
		mapData.put("email", email);
		mapData.put("pwd", pwd);
		RequestModel model = new RequestModel(mapData);

		SDAsyncHttpResponseHandler sdAsync = new SDAsyncHttpResponseHandler()
		{
			private Dialog nDialog = null;

			@Override
			public void onStartInMainThread(Object result)
			{
				nDialog = mDialogUtil.showLoading("登陆中...");
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
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				try
				{
					LoginActModel actModel = JSON.parseObject(content, LoginActModel.class);
					return actModel;
				} catch (Exception e)
				{
					return null;
				}
			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				LoginActModel actModel = (LoginActModel) result;
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					switch (actModel.getResponse_code())
					{
					case 0:
						break;
					case 1:
						dealLoginSuccess(actModel, pwd);
						break;

					default:
						break;
					}
				}
			}
		};
		InterfaceServer.getInstance().requestInterface(model, sdAsync, true);
	}

	private void dealLoginSuccess(LoginActModel actModel, String password)
	{
		LocalUserModel user = new LocalUserModel();
		user.setId(actModel.getId());
		user.setUserName(actModel.getUser_name());
		user.setUserPassword(password);
		user.setUserMoneyFormat(actModel.getUser_money_format());
		user.setUserMoney(actModel.getUser_money());
		App.getApplication().setmLocalUser(user);
		EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_LOGIN_SUCCESS));
		CommonInterface.submitRegistrationID();
		setResult(RESULT_CODE_LOGIN_SUCCESS);
		finish();
	}

	private void registeClick()
	{
		mSdtitle.setOnClickListener(this);
		mEtUsername.setOnClickListener(this);
		mEtPassword.setOnClickListener(this);
		mBtnLogin.setOnClickListener(this);
		mTvForget.setOnClickListener(this);
		mTvReg.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_login_SDTitle:

			break;

		case R.id.act_login_et_username:

			break;

		case R.id.act_login_et_password:

			break;

		case R.id.act_login_btn_login:
			requestLogin();
			break;

		case R.id.act_login_tv_forget:
			UIHelper.showNormal(LoginActivity.this, ResetPasswordActivity.class, false);
			break;

		case R.id.act_login_tv_reg:
			UIHelper.showNormal(LoginActivity.this, RegisteActivity.class, false);
			break;

		default:
			break;
		}
	}

}