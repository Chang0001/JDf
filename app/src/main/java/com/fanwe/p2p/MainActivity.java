package com.fanwe.p2p;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.constant.Constant.SlidingMenuItem;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.fragment.ArticlesFragment;
import com.fanwe.p2p.fragment.BaseFragment;
import com.fanwe.p2p.fragment.BondTransferFragment;
import com.fanwe.p2p.fragment.BorrowInvestFragment;
import com.fanwe.p2p.fragment.MainPageNewFragment;
import com.fanwe.p2p.fragment.MoreSettingFragment;
import com.fanwe.p2p.fragment.PersonalCenterFragment;
import com.fanwe.p2p.fragment.SlidingMenuLeftFragment;
import com.fanwe.p2p.fragment.SlidingMenuLeftFragment.SlidingMenuLeftFragmentListener;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.UIHelper;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.sunday.busevent.SDBaseEvent;

/**
 * 主界面
 * 
 * @author js02
 * 
 */
public class MainActivity extends BaseActivity
{


	private RadioGroup rg_tab_bar;
	private RadioButton rb_channel;
	//Fragment Object
	private MainPageNewFragment fg1;
	private BorrowInvestFragment fg2;
	private PersonalCenterFragment fg4;
	private BondTransferFragment fg3;
	private FragmentManager fManager;


	private SlidingMenu mSlidingMenu = null;

	private SlidingMenuLeftFragment mFragLeftMenu = null;

	
	private PersonalCenterFragment mFragPersonalCenter = null;
	private MainPageNewFragment mFragMainPage = null;
	private BorrowInvestFragment mFragBorrowInvest = null;
	private BondTransferFragment mFragBondTransfer = null;
	private ArticlesFragment mFragArticles = null;
	private MoreSettingFragment mFragMoreSetting = null;

	private long mExitTime = 0;
	
	private BaseFragment mFragLast = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_main);
		init();

	}

	private void init()
	{
		mIsNeedFinishWhenLogout = false;
		App.getApplication().getmRuntimeConfig().setMainActivityStarted(true);
		initSlidingMenu();

	}

	private void initSlidingMenu()
	{
		// SlidingMenu配置
		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setMode(SlidingMenu.LEFT);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		mSlidingMenu.setShadowDrawable(R.drawable.shadow_slidingmenu_left);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		mSlidingMenu.setMenu(R.layout.view_sliding_menu_left);
		// 动画配置
//		mSlidingMenu.setBehindCanvasTransformer(new CanvasTransformer()
//		{
//			@Override
//			public void transformCanvas(Canvas canvas, float percentOpen)
//			{
//				canvas.drawColor(MainActivity.this.getResources().getColor(R.color.transparent));
//				float scale = (float) (percentOpen*0.25 + 0.75);
//				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
//			}
//		});
		
		mFragLeftMenu = new SlidingMenuLeftFragment();
		replaceFragment(mFragLeftMenu, R.id.view_sliding_menu_left_frame_menu_container); // 左边菜单添加

		mFragMainPage = new MainPageNewFragment();
		replaceFragment(mFragMainPage, R.id.act_main_frame_container_main_content); // 内容视图添加

		mFragLeftMenu.setmListener(new MainActivity_SlidingMenuLeftFragmentListener());

		
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		App.getApplication().getmRuntimeConfig().setMainActivityStarted(false);
	}

	class MainActivity_SlidingMenuLeftFragmentListener implements SlidingMenuLeftFragmentListener
	{

		@Override
		public void onMenuItemSelected(View v, int index)
		{

			switch (index)
			{

			case SlidingMenuItem.MY_ACCOUNT: // 我的账户(登录/注册)
				clickMyAccount(v);
				break;
			case SlidingMenuItem.HOME: // 网站首页
				clickHome(v);
				break;
			case SlidingMenuItem.BORROW_INVEST: // 借款投资
				clickBorrowInvest(v);
				break;
			case SlidingMenuItem.BOND_TRANSFER: // 债券转让
				clickBondTransfer(v);
				break;
			case SlidingMenuItem.ARTICLE: // 文章资信
				clickArticle(v);
				break;
			case SlidingMenuItem.MORE_SETTING: // 更多设置
				clickMoreSetting(v);
				break;

			default:
				break;
			}

		}

		@Override
		public void onClickLoginState(View v)
		{
		}

	}
	
	public SlidingMenuLeftFragment getSlidingMenuFragment()
	{
		return mFragLeftMenu;
	}
	
	public SlidingMenu getSlidingMenu()
	{
		return mSlidingMenu;
	}



	private void clickMyAccount(View v)
	{
		if (App.getApplication().getmLocalUser() != null) // 已经登录
		{
			if (mFragPersonalCenter == null)
			{
				mFragPersonalCenter = new PersonalCenterFragment();
				replaceFragment(mFragPersonalCenter, R.id.act_main_frame_container_personal_center);
			}
			toggleFragment(mFragPersonalCenter);
		} else
		{
			UIHelper.showNormal(MainActivity.this, LoginActivity.class, false);
		}
	}

	private void clickHome(View v)
	{
		if (mFragMainPage == null)
		{
			mFragMainPage = new MainPageNewFragment();
			replaceFragment(mFragMainPage, R.id.act_main_frame_container_home);
		}
		toggleFragment(mFragMainPage);
		
	}

	private void clickBorrowInvest(View v)
	{
		if (mFragBorrowInvest == null)
		{
			mFragBorrowInvest = new BorrowInvestFragment();
			replaceFragment(mFragBorrowInvest, R.id.act_main_frame_container_borrow_invest);
		}
		toggleFragment(mFragBorrowInvest);
		
	}

	private void clickBondTransfer(View v)
	{
		if (mFragBondTransfer == null)
		{
			mFragBondTransfer = new BondTransferFragment();
			replaceFragment(mFragBondTransfer, R.id.act_main_frame_container_bond_transfer);
		}
		toggleFragment(mFragBondTransfer);

	}
	
	public void clickArticle(View v)
	{
		if (mFragArticles == null)
		{
			mFragArticles = new ArticlesFragment();
			replaceFragment(mFragArticles, R.id.act_main_frame_container_articles);
		}
		toggleFragment(mFragArticles);

	}

	private void clickMoreSetting(View v)
	{
		if (mFragMoreSetting == null)
		{
			mFragMoreSetting = new MoreSettingFragment();
			replaceFragment(mFragMoreSetting, R.id.act_main_frame_container_more_setting);
		}
		toggleFragment(mFragMoreSetting);

	}

	public void toggleSlideMenu()
	{
		mSlidingMenu.toggle();
	}
	
	
	
	
	@Override
	public void onEvent(SDBaseEvent event)
	{
		switch (event.getEventTagInt())
		{
		case EventTag.EVENT_LOGOUT_SUCCESS:
			mFragPersonalCenter = null;
			break;
		default:
			break;
		}
	}

	private void toggleFragment(BaseFragment fragment)
	{
		if (mFragLast != null)
		{
			hideFragment(mFragLast);
		}
		showFragment(fragment);
		mFragLast = fragment;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0)
			{
				this.exitApp();
			}
			return true;
		}

		switch (keyCode)
		{
		case KeyEvent.KEYCODE_MENU:
			toggleSlideMenu();
			return true;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exitApp()
	{
		if (System.currentTimeMillis() - mExitTime > 2000)
		{
			SDToast.showToast("再按一次退出!");
		} else
		{
			App.getApplication().exitApp(true);
		}
		mExitTime = System.currentTimeMillis();
	}

}
