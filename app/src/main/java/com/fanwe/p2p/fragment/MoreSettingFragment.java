package com.fanwe.p2p.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.fanwe.p2p.LoginActivity;
import com.fanwe.p2p.ProjectDetailWebviewActivity;
import com.fanwe.p2p.R;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.ImageLoaderManager;
import com.fanwe.p2p.customview.SDSimpleSetItemView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.dao.InitActDBModelDao;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.act.InitActModel;
import com.fanwe.p2p.service.AppUpgradeService;
import com.fanwe.p2p.utils.SDIntentUtil;
import com.fanwe.p2p.utils.SDPackageUtil;
import com.fanwe.p2p.utils.SDToast;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;

import de.greenrobot.event.EventBus;

public class MoreSettingFragment extends BaseFragment implements OnClickListener
{

	@ViewInject(id = R.id.frag_more_setting_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.frag_more_setting_item_clear_cache)
	private SDSimpleSetItemView mItemClearCache = null;

	@ViewInject(id = R.id.frag_more_setting_item_servic_phone)
	private SDSimpleSetItemView mItemServicePhone = null;

	@ViewInject(id = R.id.frag_more_setting_item_service_email)
	private SDSimpleSetItemView mItemServiceEmail = null;

	@ViewInject(id = R.id.frag_more_setting_item_about_us)
	private SDSimpleSetItemView mItemAboutUs = null;

	@ViewInject(id = R.id.frag_more_setting_item_check_version)
	private SDSimpleSetItemView mItemCheckVersion = null;

	@ViewInject(id = R.id.frag_more_setting_item_exit_account)
	private SDSimpleSetItemView mItemExitAccount = null;

	private String mStrServiceNumber = null;
	private String mStrServiceEmail = null;
	private String mStrAboutUs = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag_more_setting, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;
	}

	private void init()
	{
		initTitle();
		initItems();
		initItemsData();
		registeClick();

	}

	/**
	 * 初始化设置项上面的数据
	 */
	private void initItemsData()
	{

		InitActModel model = InitActDBModelDao.readInitDB();
		if (model != null)
		{
			if (model.getKf_phone() != null)
			{
				mStrServiceNumber = model.getKf_phone();
				mItemServicePhone.setTitleSubText(model.getKf_phone());
			} else
			{
				mItemServicePhone.setTitleSubText("未找到");
			}

			if (model.getKf_email() != null)
			{
				mStrServiceEmail = model.getKf_email();
				mItemServiceEmail.setTitleSubText(model.getKf_email());
			} else
			{
				mItemServiceEmail.setTitleSubText("未找到");
			}

			mStrAboutUs = model.getAbout_info();

		}

		PackageInfo pi = SDPackageUtil.getCurrentAppPackageInfo(App.getApplication(), getActivity().getPackageName());
		mItemCheckVersion.setTitleSubText("V" + pi.versionName);

	}

	/**
	 * 初始化设置项的状态
	 */
	private void initItems()
	{
		mItemClearCache.setTitleImage(R.drawable.ic_recharge_withdrawals);
		mItemClearCache.setTitleText("清除缓存");
		mItemClearCache.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemClearCache.setBackgroundImage(R.drawable.selector_single_item_top);

		mItemServicePhone.setTitleImage(R.drawable.ic_my_investment);
		mItemServicePhone.setTitleText("客服电话");
		mItemServicePhone.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemServicePhone.setBackgroundImage(R.drawable.selector_single_item_middle);
		
		mItemServiceEmail.setTitleImage(R.drawable.ic_bond_transfer);
		mItemServiceEmail.setTitleText("客服邮箱");
		mItemServiceEmail.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemServiceEmail.setBackgroundImage(R.drawable.selector_single_item_middle);

		mItemAboutUs.setTitleImage(R.drawable.ic_my_interest_bid);
		mItemAboutUs.setTitleText("关于我们");
		mItemAboutUs.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemAboutUs.setBackgroundImage(R.drawable.selector_single_item_middle);

		mItemCheckVersion.setTitleImage(R.drawable.ic_bid_recorder);
		mItemCheckVersion.setTitleText("版本检测");
		mItemCheckVersion.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemCheckVersion.setBackgroundImage(R.drawable.selector_single_item_bottom);

		mItemExitAccount.setTitleImage(R.drawable.ic_repay_loans);
		mItemExitAccount.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemExitAccount.setBackgroundImage(R.drawable.selector_single_item);
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null)
		{
			mItemExitAccount.setTitleText("退出账号");
		} else
		{
			mItemExitAccount.setTitleText("登录");
		}
	}

	private void initTitle()
	{
		mTitle.setTitle("更多设置");
		mTitle.setLeftButtonImage(R.drawable.ic_title_menu, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				toggleSlideMenu();
			}
		}, null);
	}

	private void registeClick()
	{
		mItemClearCache.setOnClickListener(this);
		mItemServicePhone.setOnClickListener(this);
		mItemServiceEmail.setOnClickListener(this);
		mItemAboutUs.setOnClickListener(this);
		mItemCheckVersion.setOnClickListener(this);
		mItemExitAccount.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.frag_more_setting_item_clear_cache:
			clickClearCache();
			break;
		case R.id.frag_more_setting_item_servic_phone:
			clickServicePhone();
			break;
		case R.id.frag_more_setting_item_service_email:
			clickServiceEmail();
			break;
		case R.id.frag_more_setting_item_about_us:
			clickAboutUs();
			break;
		case R.id.frag_more_setting_item_check_version:
			clickCheckVersion();
			break;
		case R.id.frag_more_setting_item_exit_account:
			clickExitAccount();
			break;

		default:
			break;
		}
	}

	/**
	 * 清除缓存
	 */
	private void clickClearCache()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				ImageLoaderManager.getImageLoader().getDiscCache().clear();
				SDToast.showToast("清除完毕");
			}
		}).start();
	}

	/**
	 * 客服电话
	 */
	private void clickServicePhone()
	{
		final Dialog dialog = getBaseActivity().mDialogUtil.confirm("提示", "确定拨打客服电话?", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				if (mStrServiceNumber != null)
				{
					startActivity(SDIntentUtil.getCallNumberIntent(mStrServiceNumber));
				} else
				{
					SDToast.showToast("未找到客服电话");
				}
			}
		}, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
	}

	/**
	 * 客服邮箱
	 */
	private void clickServiceEmail()
	{
		if (mStrServiceEmail != null)
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("plain/text");
			intent.putExtra(Intent.EXTRA_EMAIL, mStrServiceEmail);
			intent.putExtra(Intent.EXTRA_SUBJECT, "问题反馈");
			startActivity(Intent.createChooser(intent, ""));
		}
	}

	/**
	 * 关于我们
	 */
	private void clickAboutUs()
	{
		if (mStrAboutUs != null)
		{
			Intent intent = new Intent(getActivity(), ProjectDetailWebviewActivity.class);
//			intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "关于我们");
			intent.putExtra(ProjectDetailWebviewActivity.EXTRA_ARTICLE_ID, mStrAboutUs);
			startActivity(intent);
		} else
		{
			SDToast.showToast("暂无信息");
		}
	}

	/**
	 * 版本检测
	 */
	private void clickCheckVersion()
	{
		Intent updateIntent = new Intent(getActivity(), AppUpgradeService.class);
		updateIntent.putExtra(AppUpgradeService.EXTRA_SERVICE_START_TYPE, 1); // 1代表该service被用户手动启动检测版本
		getActivity().startService(updateIntent);
	}

	/**
	 * 退出账户
	 */
	private void clickExitAccount()
	{
		if (App.getApplication().getmLocalUser() != null) // 已登录
		{
			final Dialog dialog = getBaseActivity().mDialogUtil.confirm("提示", "确定要退出账户?", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
//					if (getMainActivity() != null && getMainActivity().getSlidingMenuFragment() != null)
//					{
//						getMainActivity().getSlidingMenuFragment().setSelectIndex(SlidingMenuItem.HOME, null, true, true);
//						getMainActivity().getSlidingMenuFragment().changeUserLoginState();
//						EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_LOGOUT_SUCCESS));
//						SDToast.showToast("成功退出帐号!");
//					}
					EventBus.getDefault().post(new SDBaseEvent(null, EventTag.EVENT_LOGOUT_SUCCESS));
					SDToast.showToast("成功退出帐号!");
				}
			}, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		} else
		{
			startActivity(new Intent(getActivity(), LoginActivity.class));
		}
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		switch (event.getEventTagInt())
		{
		case EventTag.EVENT_LOGIN_SUCCESS:
			mItemExitAccount.setTitleText("退出账号");
			break;
		case EventTag.EVENT_REGISTER_AND_LOGIN_SUCCESS:
			mItemExitAccount.setTitleText("退出账号");
			break;
		case EventTag.EVENT_LOGOUT_SUCCESS:
			mItemExitAccount.setTitleText("登录");
			break;

		default:
			break;
		}
		super.onEventMainThread(event);
	}

}