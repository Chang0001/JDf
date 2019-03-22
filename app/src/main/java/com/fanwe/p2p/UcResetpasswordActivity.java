package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.customview.ClearEditText;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDUIUtil;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 个人中心修改密码
 * 
 * @author yhz
 */
public class UcResetpasswordActivity extends BaseActivity implements OnClickListener
{
	@ViewInject(id = R.id.act_resetPassword_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.actPassword_edt_oldPassword)
	private ClearEditText mOldPassword = null;

	@ViewInject(id = R.id.actPassword_edt_newPassword1)
	private ClearEditText mNewPassword1 = null;

	@ViewInject(id = R.id.actPassword_edt_newPassword2)
	private ClearEditText mNewPassword2 = null;

	@ViewInject(id = R.id.actPassword_btn_submit)
	private Button mSubmit = null;

	private String mPwd = null;

	private String mUserPwd = null;

	private String mUserPwdConfirm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uc_act_resetpassword);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		registeClick();
		initTitle();
	}

	private void registeClick()
	{
		mSubmit.setOnClickListener(this);
	}

	private void initTitle()
	{
		mTitle.setTitle("修改密码");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{

			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);
	}

	private void clicksubmit()
	{
		if (!isGetInfo())
			return;
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "uc_save_pwd");
		mapData.put("email", App.getApplication().getmLocalUser().getUserName());
		mapData.put("pwd", mPwd);
		mapData.put("user_pwd", mUserPwd);
		mapData.put("user_pwd_confirm", mUserPwdConfirm);
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
				nDialog = mDialogUtil.showLoading("加载中...");
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
				if (actModel != null)
				{
					switch (actModel.getResponse_code())
					{
					case 0:
						SDToast.showToast(actModel.getShow_err());
						break;
					case 1:
						// TODO:修改密码成功
						LocalUserModel user = App.getApplication().getmLocalUser();
						if (user != null)
						{
							user.setUserPassword(mUserPwd);
							App.getApplication().setmLocalUser(user);
						}
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

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.actPassword_btn_submit:
			clicksubmit();
			break;
		default:
			break;
		}
	}

	private boolean isGetInfo()
	{
		mPwd = mOldPassword.getText().toString();
		mUserPwd = mNewPassword1.getText().toString();
		mUserPwdConfirm = mNewPassword2.getText().toString();

		if (TextUtils.isEmpty(mPwd))
		{
			SDToast.showToast("旧密码不能为空");
			SDUIUtil.showInputMethod(this, mOldPassword, true);
			return false;
		}
		if (TextUtils.isEmpty(mUserPwd))
		{
			SDToast.showToast("新密码不能为空");
			SDUIUtil.showInputMethod(this, mNewPassword1, true);
			return false;
		}
		if (TextUtils.isEmpty(mUserPwdConfirm))
		{
			SDToast.showToast("再次输入新密码不能为空");
			SDUIUtil.showInputMethod(this, mNewPassword2, true);
			return false;
		}
		if (!mUserPwd.equals(mUserPwdConfirm))
		{
			SDToast.showToast("两次新密码不一致");
			return false;
		}
		return true;

	}

}