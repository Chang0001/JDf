package com.fanwe.p2p.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.constant.Constant.SlidingMenuItem;
import com.fanwe.p2p.customview.SDSimpleMenuItemViewNew;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.utils.SDViewNavigatorManager;
import com.fanwe.p2p.utils.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;

/**
 * 侧滑菜单界面
 * 
 * @author js02
 * 
 */
public class SlidingMenuLeftFragment extends BaseFragment
{
	private static final int DEFAULT_SELECT_MENU_ITEM_INDEX = SlidingMenuItem.HOME;

	private SlidingMenuLeftFragmentListener mListener = null;

	@ViewInject(id = R.id.frag_sliding_menu_left_login_state)
	private TextView mTxtLoginState = null;

	@ViewInject(id = R.id.frag_sliding_menu_left_item_my_account)
	private SDSimpleMenuItemViewNew mItemMyAccount = null;

	@ViewInject(id = R.id.frag_sliding_menu_left_item_hom)
	private SDSimpleMenuItemViewNew mItemHome = null;

	@ViewInject(id = R.id.frag_sliding_menu_left_item_borrow_invest)
	private SDSimpleMenuItemViewNew mItemBorrowInvest = null;

	@ViewInject(id = R.id.frag_sliding_menu_left_item_bond_transfer)
	private SDSimpleMenuItemViewNew mItemBondTransfer = null;

	@ViewInject(id = R.id.frag_sliding_menu_left_item_article)
	private SDSimpleMenuItemViewNew mItemArticle = null;
	
	@ViewInject(id = R.id.frag_sliding_menu_left_item_more_setting)
	private SDSimpleMenuItemViewNew mItemMoreSetting = null;

	private int mLastIndex = DEFAULT_SELECT_MENU_ITEM_INDEX;

	private SDViewNavigatorManager mNavigatorManager = new SDViewNavigatorManager();

	private boolean toggleMenu = true;

	public SlidingMenuLeftFragmentListener getmListener()
	{
		return mListener;
	}

	public void setmListener(SlidingMenuLeftFragmentListener mListener)
	{
		this.mListener = mListener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag_sliding_menu_left, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;
	}

	private void init()
	{
		initLoginState();
		initMenuItems();

	}

	private void initLoginState()
	{
		if (App.getApplication().getmLocalUser() != null)
		{
			mTxtLoginState.setText(App.getApplication().getmLocalUser().getUserName() + "，您好!");
		}
		mTxtLoginState.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mListener != null)
				{
					mListener.onClickLoginState(v);
				}
			}
		});
	}

	private void initMenuItems()
	{
		mItemMyAccount.setmImageTitleNormalId(R.drawable.ic_menu_login_or_registe_normal);
		mItemMyAccount.setmImageTitleSelectId(R.drawable.ic_menu_login_or_registe_select);
		if (App.getApplication().getmLocalUser() != null)
		{
			mItemMyAccount.setTitleText("我的账户");
		} else
		{
			mItemMyAccount.setTitleText("登录/注册");
		}

		mItemHome.setmImageTitleNormalId(R.drawable.ic_menu_home_normal);
		mItemHome.setmImageTitleSelectId(R.drawable.ic_menu_home_select);
		mItemHome.setTitleText("首页");

		mItemBorrowInvest.setmImageTitleNormalId(R.drawable.ic_menu_i_want_invest_normal);
		mItemBorrowInvest.setmImageTitleSelectId(R.drawable.ic_menu_i_want_invest_select);
		mItemBorrowInvest.setTitleText("贷款理财");

		mItemBondTransfer.setmImageTitleNormalId(R.drawable.ic_menu_i_want_borrow_normal);
		mItemBondTransfer.setmImageTitleSelectId(R.drawable.ic_menu_i_want_borrow_select);
		mItemBondTransfer.setTitleText("分销分佣");
		
		mItemArticle.setmImageTitleNormalId(R.drawable.ic_menu_article_normal);
		mItemArticle.setmImageTitleSelectId(R.drawable.ic_menu_article_select);
		mItemArticle.setTitleText("文章资讯");

		mItemMoreSetting.setmImageTitleNormalId(R.drawable.ic_menu_setting_normal);
		mItemMoreSetting.setmImageTitleSelectId(R.drawable.ic_menu_setting_select);
		mItemMoreSetting.setTitleText("更多设置");

		SDSimpleMenuItemViewNew[] arrItems = new SDSimpleMenuItemViewNew[] { mItemMyAccount, mItemHome, mItemBorrowInvest, mItemBondTransfer, mItemArticle, mItemMoreSetting };

		mNavigatorManager.setItems(arrItems);
		mNavigatorManager.setmListener(new SDViewNavigatorManagerListener()
		{

			@Override
			public void onItemClick(View v, int index)
			{
				if (App.getApplication().getmLocalUser() == null) // 未登录
				{
					if (index != SlidingMenuItem.MY_ACCOUNT)
					{
						mLastIndex = index;
					} else
					{
						mNavigatorManager.setSelectIndex(mLastIndex, null, false);
						toggleMenu = false;
					}
				}

				if (mListener != null)
				{
					mListener.onMenuItemSelected(v, index);
				}
				if (toggleMenu)
				{
					toggleSlideMenu();
				}
				toggleMenu = true;
			}
		});
		mNavigatorManager.setSelectIndex(DEFAULT_SELECT_MENU_ITEM_INDEX, null, true);
	}

	public void setSelectIndex(int index, View v, boolean notifyListener, boolean toggleMenu)
	{
		this.toggleMenu = toggleMenu;
		if (mNavigatorManager != null)
		{
			mNavigatorManager.setSelectIndex(index, v, notifyListener);
		}
	}
	
	

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (event.getEventTagInt())
		{
		case EventTag.EVENT_LOGOUT_SUCCESS:
			App.getApplication().clearAppsLocalUserModel();
			mNavigatorManager.setSelectIndex(SlidingMenuItem.HOME, null, true);
			changeUserLoginState();
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume()
	{
		changeUserLoginState();
		super.onResume();
	}

	private void changeUserLoginState()
	{
		LocalUserModel model = App.getApplication().getmLocalUser();
		if (model != null && !TextUtils.isEmpty(model.getUserName()))
		{
			mTxtLoginState.setText(model.getUserName() + ", 您好!");
			mItemMyAccount.setTitleText("我的账户");

		} else
		{
			mTxtLoginState.setText("亲，您还没有登录哦");
			mItemMyAccount.setTitleText("登录/注册");
		}
	}

	public interface SlidingMenuLeftFragmentListener
	{
		public void onMenuItemSelected(View v, int index);

		public void onClickLoginState(View v);
	}

}