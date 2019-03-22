package com.fanwe.p2p;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fanwe.p2p.R.color;
import com.fanwe.p2p.customview.SDSimpleTabView;
import com.fanwe.p2p.fragment.UcTransFragLeftFragment;
import com.fanwe.p2p.fragment.UcTransFragRightFragment;
import com.fanwe.p2p.utils.SDViewNavigatorManager;
import com.fanwe.p2p.utils.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;

public class UcTransferActivity extends BaseActivity implements OnClickListener
{
	private static final int TEXT_SIZE_TAB = 18;

	@ViewInject(id = R.id.actUcTransfer_ll_finish)
	private LinearLayout mFinish = null;

	@ViewInject(id = R.id.actUcTransfer_sd_tabLeft)
	private SDSimpleTabView mTabLeft = null;

	@ViewInject(id = R.id.actUcTransfer_sd_tabRight)
	private SDSimpleTabView mTabRight = null;

	@ViewInject(id = R.id.actUcTransfer_frame_left)
	private FrameLayout mFrameLeft = null;

	@ViewInject(id = R.id.actUcTransfer_frame_right)
	private FrameLayout mFrameRight = null;

	private SDViewNavigatorManager mNavigator = new SDViewNavigatorManager();

	private UcTransFragLeftFragment mLeftFragment = null;

	private UcTransFragRightFragment mRightFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_uc_transfer);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		registeClick();
		initTabs();
		addFragments();

	}

	private void addFragments()
	{

		mLeftFragment = new UcTransFragLeftFragment();
		mRightFragment = new UcTransFragRightFragment();

		addFragment(mLeftFragment, R.id.actUcTransfer_frame_left);
		addFragment(mRightFragment, R.id.actUcTransfer_frame_right);

		mNavigator.setSelectIndex(0, mTabLeft, true);
	}

	private void initTabs()
	{
		mTabLeft.setmBackgroundImageNormal(R.drawable.uc_transfer_tab_left_normal);
		mTabLeft.setmBackgroundImageSelect(R.drawable.uc_transfer_tab_left_press);
		mTabLeft.setmTextColorNormal(getResources().getColor(color.gray));
		mTabLeft.setmTextColorSelect(getResources().getColor(color.white));
		mTabLeft.setTabName("债权转让");
		mTabLeft.mTxtTabName.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_TAB);

		mTabRight.setmBackgroundImageNormal(R.drawable.uc_transfer_tab_right_normal);
		mTabRight.setmBackgroundImageSelect(R.drawable.uc_transfer_tab_right_press);
		mTabRight.setmTextColorNormal(getResources().getColor(color.gray));
		mTabRight.setmTextColorSelect(getResources().getColor(color.white));
		mTabRight.setTabName("购买记录");
		mTabRight.mTxtTabName.setTextSize(TypedValue.COMPLEX_UNIT_SP, TEXT_SIZE_TAB);

		SDSimpleTabView[] items = new SDSimpleTabView[] { mTabLeft, mTabRight };

		mNavigator.setItems(items);
		mNavigator.setmListener(new SDViewNavigatorManagerListener()
		{

			@Override
			public void onItemClick(View v, int index)
			{
				switch (index)
				{
				case 0:
					showFragment(mLeftFragment);
					hideFragment(mRightFragment);
					break;
				case 1:
					showFragment(mRightFragment);
					hideFragment(mLeftFragment);
					break;

				default:
					break;
				}
			}
		});

	}



	private void registeClick()
	{
		mFinish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.actUcTransfer_ll_finish:
			finish();
			break;

		default:
			break;
		}
	}
	
}