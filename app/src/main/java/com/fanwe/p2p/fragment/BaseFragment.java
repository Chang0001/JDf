package com.fanwe.p2p.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;


import com.fanwe.p2p.BaseActivity;
import com.fanwe.p2p.MainActivity;
import com.fanwe.p2p.listener.IVisibleStateListener;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.busevent.SDEvent;
import com.ta.util.netstate.TANetChangeObserver;
import com.ta.util.netstate.TANetWorkUtil.netType;
import com.ta.util.netstate.TANetworkStateReceiver;

import de.greenrobot.event.EventBus;

public class BaseFragment extends Fragment implements TANetChangeObserver, SDEvent, IVisibleStateListener
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initBaseFragment();
	}

	private void initBaseFragment()
	{
		TANetworkStateReceiver.registerObserver(this);
		EventBus.getDefault().register(this);
	}

	@Override
	public void onConnect(netType type)
	{
	}

	@Override
	public void onDisConnect()
	{
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		TANetworkStateReceiver.removeRegisterObserver(this);
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
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

	public BaseActivity getBaseActivity()
	{
		return (BaseActivity) getActivity();
	}

	public MainActivity getMainActivity()
	{
		BaseActivity activity = getBaseActivity();
		if (activity != null && (activity instanceof MainActivity))
		{
			return ((MainActivity) activity);
		} else
		{
			return null;
		}
	}

	public void toggleSlideMenu()
	{
		MainActivity mainActivity = getMainActivity();
		if (mainActivity != null)
		{
			mainActivity.toggleSlideMenu();
		}
	}

	public void showLoadingDialog(String msg)
	{
		BaseActivity activity = getBaseActivity();
		if (activity != null)
		{
			activity.showLoadingDialog(msg);
		}
	}

	public void hideLoadingDialog()
	{
		BaseActivity activity = getBaseActivity();
		if (activity != null)
		{
			activity.hideLoadingDialog();
		}
	}

	@Override
	public void onShowView()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onHideView()
	{
		// TODO Auto-generated method stub

	}

}
