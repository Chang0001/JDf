package com.fanwe.p2p;

import android.app.Dialog;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.fanwe.p2p.customview.SDSlidingFinishLayout;
import com.fanwe.p2p.customview.SDSlidingFinishLayout.SDSlidingFinishLayoutListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.fragment.BaseFragment;
import com.fanwe.p2p.utils.DialogUtil;
import com.fanwe.p2p.utils.SDPackageUtil;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.busevent.SDEvent;
import com.ta.util.netstate.TANetChangeObserver;
import com.ta.util.netstate.TANetWorkUtil.netType;
import com.ta.util.netstate.TANetworkStateReceiver;

import de.greenrobot.event.EventBus;

public class BaseActivity extends FragmentActivity implements TANetChangeObserver, SDEvent
{
	
	public static final String EXTRA_IS_START_BY_NOTIFICATION = "extra_is_start_by_notification";

	public DialogUtil mDialogUtil = null;
	public Dialog mBaseDialog = null;
	
	private boolean mIsStartByNotification = false;
	private boolean mIsFinishByUser = false;
	protected boolean mIsNeedFinishWhenLogout = true;

	protected SDSlidingFinishLayout mSDFinishLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		baseInit();
	}

	public void setSdContentView(int resViewId)
	{
		super.setContentView(resViewId);
//		setSlidingFinishLayout(resViewId);
	}
	
	private void setSlidingFinishLayout(int resViewId)
	{
		if (mSDFinishLayout == null)
		{
			mSDFinishLayout = new SDSlidingFinishLayout(getApplicationContext());
		} else
		{
			mSDFinishLayout.removeAllViews();
		}
		View view = getLayoutInflater().inflate(resViewId, null);
		RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mSDFinishLayout.addView(view, params);
		super.setContentView(mSDFinishLayout, params);
		mSDFinishLayout.setmListener(new SDSlidingFinishLayoutListener()
		{
			@Override
			public void onFinish()
			{
				finish();
			}
		});
	}

	private void baseInit()
	{
		initIntentData();
		mDialogUtil = new DialogUtil(this);
		TANetworkStateReceiver.registerObserver(this);
		EventBus.getDefault().register(this);
	}

	private void initIntentData()
	{
		mIsStartByNotification = getIntent().getBooleanExtra(EXTRA_IS_START_BY_NOTIFICATION, false);
		
	}

	@Override
	public void onConnect(netType type)
	{

	}

	@Override
	public void onDisConnect()
	{

	}

	public void addFragment(BaseFragment fragment, int containerId)
	{
		getSupportFragmentManager().beginTransaction().add(containerId, fragment).commitAllowingStateLoss();
	}

	public void replaceFragment(BaseFragment fragment, int containerId)
	{
		getSupportFragmentManager().beginTransaction().replace(containerId, fragment).commitAllowingStateLoss();
	}

	public void showFragment(BaseFragment fragment)
	{
		getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
		fragment.onShowView();
	}

	public void hideFragment(BaseFragment fragment)
	{
		getSupportFragmentManager().beginTransaction().hide(fragment).commitAllowingStateLoss();
		fragment.onHideView();
	}

	public void showLoadingDialog(String msg)
	{
		if (msg != null)
		{
			if (mBaseDialog != null && mBaseDialog.isShowing())
			{
				mBaseDialog.dismiss();
				mBaseDialog = null;
			}
			mBaseDialog = mDialogUtil.showLoading(msg);
		}
	}

	public void hideLoadingDialog()
	{
		if (mBaseDialog != null && mBaseDialog.isShowing())
		{
			mBaseDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		TANetworkStateReceiver.removeRegisterObserver(this);
		EventBus.getDefault().unregister(this);
		super.onDestroy();
		
		if (mIsStartByNotification && mIsFinishByUser)
		{
//			startActivity(new Intent(getApplicationContext(), InitActivity.class));
			SDPackageUtil.startCurrentApp();
		}
		
	}

	@Override
	public void onEvent(SDBaseEvent event)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		// TODO Auto-generated method stub
		switch (event.getEventTagInt())
		{
		case EventTag.EVENT_EXIT_APP:
			finish();
			break;
		case EventTag.EVENT_LOGOUT_SUCCESS:
			if (mIsNeedFinishWhenLogout)
			{
				finish();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onEventBackgroundThread(SDBaseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onEventAsync(SDBaseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		finishActivity(true);
		super.onBackPressed();
		
	}
	
	protected void finishActivity(boolean isFinishByUser)
	{
		this.mIsFinishByUser = isFinishByUser;
		finish();
	}

}
