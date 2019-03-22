package com.fanwe.p2p;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.fanwe.p2p.R.color;
import com.fanwe.p2p.customview.SDSimpleTabView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.fragment.RepayBorrowListFinishFragment;
import com.fanwe.p2p.fragment.RepayBorrowListFragment;
import com.fanwe.p2p.utils.SDViewNavigatorManager;
import com.fanwe.p2p.utils.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
/**
 * 还款界面
 * @author js02
 *
 */
public class RepayBorrowListActivity extends BaseActivity
{
	private static final int TEXT_SIZE_TAB = 18;

	@ViewInject(id = R.id.act_repay_borrow_list_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_repay_borrow_list_tab_repay_borrow_list)
	private SDSimpleTabView mTabRepayBorrowList = null;

	@ViewInject(id = R.id.act_repay_borrow_list_tab_repay_borrow_list_finish)
	private SDSimpleTabView mTabRepayBorrowListFinish = null;

	@ViewInject(id = R.id.act_repay_borrow_list_frame_container_repay_borrow_list)
	private FrameLayout mFrameContainerRepayBorrowList = null;

	@ViewInject(id = R.id.act_repay_borrow_list_frame_container_repay_borrow_list_finish)
	private FrameLayout mFrameContainerRepayBorrowListFinish = null;

	private SDViewNavigatorManager mNavigator = new SDViewNavigatorManager();

	private RepayBorrowListFragment mFragRepayBorrowList = null;
	private RepayBorrowListFinishFragment mFragRepayBorrowListFinish = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_repay_borrow_list);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initTitle();
		initTabs();
		addFragments();
	}

	private void addFragments()
	{
		mFragRepayBorrowList = new RepayBorrowListFragment();
		mFragRepayBorrowListFinish = new RepayBorrowListFinishFragment();

		addFragment(mFragRepayBorrowList, R.id.act_repay_borrow_list_frame_container_repay_borrow_list);
		addFragment(mFragRepayBorrowListFinish, R.id.act_repay_borrow_list_frame_container_repay_borrow_list_finish);

		mNavigator.setSelectIndex(0, mTabRepayBorrowList, true);
	}

	private void initTitle()
	{
		mTitle.setTitle("还款");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{

			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);
	}

	private void initTabs()
	{
		mTabRepayBorrowList.setmBackgroundImageNormal(R.drawable.rec_tab_left_normal);
		mTabRepayBorrowList.setmBackgroundImageSelect(R.drawable.rec_tab_left_press);
		mTabRepayBorrowList.setmTextColorNormal(getResources().getColor(color.bg_title));
		mTabRepayBorrowList.setmTextColorSelect(getResources().getColor(color.white));
		mTabRepayBorrowList.setTabName("还款列表");
		mTabRepayBorrowList.mTxtTabName.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_TAB);

		mTabRepayBorrowListFinish.setmBackgroundImageNormal(R.drawable.rec_tab_right_normal);
		mTabRepayBorrowListFinish.setmBackgroundImageSelect(R.drawable.rec_tab_right_press);
		mTabRepayBorrowListFinish.setmTextColorNormal(getResources().getColor(color.bg_title));
		mTabRepayBorrowListFinish.setmTextColorSelect(getResources().getColor(color.white));
		mTabRepayBorrowListFinish.setTabName("已还清借款");
		mTabRepayBorrowListFinish.mTxtTabName.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_TAB);

		SDSimpleTabView[] items = new SDSimpleTabView[] { mTabRepayBorrowList, mTabRepayBorrowListFinish };

		mNavigator.setItems(items);
		mNavigator.setmListener(new SDViewNavigatorManagerListener()
		{

			@Override
			public void onItemClick(View v, int index)
			{
				switch (index)
				{
				case 0:
					showFragment(mFragRepayBorrowList);
					hideFragment(mFragRepayBorrowListFinish);
					break;
				case 1:
					showFragment(mFragRepayBorrowListFinish);
					hideFragment(mFragRepayBorrowList);
					break;

				default:
					break;
				}
			}
		});

	}

}