package com.fanwe.p2p.fragment;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.BidRecorderActivity;
import com.fanwe.p2p.MyInterestBidActivity;
import com.fanwe.p2p.MyInvestActivity;
import com.fanwe.p2p.PublishedBorrowActivity;
import com.fanwe.p2p.R;
import com.fanwe.p2p.RechargeLogActivity;
import com.fanwe.p2p.RepayBorrowListActivity;
import com.fanwe.p2p.UcResetpasswordActivity;
import com.fanwe.p2p.UcTransferActivity;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.common.CommonInterface;
import com.fanwe.p2p.customview.SDSimpleSetItemView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.Uc_CenterActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDViewBinder;
import com.fanwe.p2p.utils.UIHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 会员中心界面(侧滑菜单中"我的账户")
 * 
 * @author js02
 * 
 */
public class PersonalCenterFragment extends BaseFragment implements OnClickListener
{
	/** 请求更改密码 */
	public static final int REQUEST_CODE_MODIFY_PASSWORD = 1;

	@ViewInject(id = R.id.frag_personal_center_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.frag_personal_center_scroll)
	private PullToRefreshScrollView mScroll = null;

	@ViewInject(id = R.id.frag_personal_center_txt_username)
	private TextView mTxtUsername = null;

	@ViewInject(id = R.id.frag_personal_center_txt_user_level)
	private TextView mTxtUserLevel = null;

//	@ViewInject(id = R.id.frag_personal_center_txt_location_province)
//	private TextView mTxtLocationProvince = null;
//
//	@ViewInject(id = R.id.frag_personal_center_txt_location_city)
//	private TextView mTxtLocationCity = null;

	@ViewInject(id = R.id.frag_personal_center_txt_money_all)
	private TextView mTxtMoneyAll = null;

	@ViewInject(id = R.id.frag_personal_center_txt_money_can_use)
	private TextView mTxtMoneyCanUse = null;

	@ViewInject(id = R.id.frag_personal_center_txt_money_freeze)
	private TextView mTxtMoneyFreeze = null;

	@ViewInject(id = R.id.frag_personal_center_item_recharge_withdraw)
	private SDSimpleSetItemView mItemRechargeWithdraw = null;

	@ViewInject(id = R.id.frag_personal_center_item_my_invest)
	private SDSimpleSetItemView mItemMyInvest = null;

	@ViewInject(id = R.id.frag_personal_center_item_bond_transfer)
	private SDSimpleSetItemView mItemBondTransfer = null;

	@ViewInject(id = R.id.frag_personal_center_item_my_interest_bid)
	private SDSimpleSetItemView mItemMyInterestBid = null;

	@ViewInject(id = R.id.frag_personal_center_item_bid_recorder)
	private SDSimpleSetItemView mItemBidRecorder = null;

	@ViewInject(id = R.id.frag_personal_center_item_repayment_loan)
	private SDSimpleSetItemView mItemRepaymentLoan = null;

	@ViewInject(id = R.id.frag_personal_center_item_published_loan)
	private SDSimpleSetItemView mItemPublishedLoan = null;

	@ViewInject(id = R.id.frag_personal_center_item_modify_password)
	private SDSimpleSetItemView mItemModifyPassword = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag_personal_center, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;

	}

	private void init()
	{
		initTitle();
		initScroll();
		bindDatas();
		requestData();
		initItems();
		registeClick();

	}

	private void initScroll()
	{
		mScroll.setMode(Mode.PULL_FROM_START);
		mScroll.setOnRefreshListener(new OnRefreshListener2<ScrollView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				requestData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{

			}

		});
		mScroll.setRefreshing();
	}

	private void bindDatas()
	{
//		if (App.getApplication().getmLocalUser() != null) // 用户名
//		{
//			if (App.getApplication().getmLocalUser().getUserName() != null)
//			{
//				mTxtUsername.setText(App.getApplication().getmLocalUser().getUserName());
//			} else
//			{
//				mTxtUsername.setText("未找到");
//			}
//		} else
//		{
//			mTxtUsername.setText("未找到");
//		}
	}

	/**
	 * 请求用户信息
	 */
	private void requestData()
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user != null && user.getUserName() != null && user.getUserPassword() != null)
		{
			Map<String, Object> mapData = new HashMap<String, Object>();
			mapData.put("act", "uc_center");
			mapData.put("email", user.getUserName());
			mapData.put("pwd", user.getUserPassword());
			RequestModel model = new RequestModel(mapData);
			SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
			{
				@Override
				public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
				{
					Uc_CenterActModel model = JSON.parseObject(content, Uc_CenterActModel.class);
					return model;
				}

				@Override
				public void onStartInMainThread(Object result)
				{
					getBaseActivity().showLoadingDialog("请稍候...");
				}

				@Override
				public void onFinishInMainThread(Object result)
				{
					getBaseActivity().hideLoadingDialog();
					mScroll.onRefreshComplete();
				}

				@Override
				public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
				{
					Uc_CenterActModel actModel = (Uc_CenterActModel) result;
					if (!SDInterfaceUtil.isActModelNull(actModel))
					{
						if (actModel.getResponse_code() == 1)
						{
							SDViewBinder.setTextView(mTxtUsername, actModel.getUser_name(), "未找到"); //设置用户名
							SDViewBinder.setTextView(mTxtUserLevel, actModel.getGroup_name(), "未找到"); // 设置用户等级
							SDViewBinder.setTextView(mTxtMoneyAll, actModel.getTotal_money_format(), "未找到"); // 设置资金金额
							SDViewBinder.setTextView(mTxtMoneyCanUse, actModel.getMoney_format(), "未找到"); // 设置可用金额
							SDViewBinder.setTextView(mTxtMoneyFreeze, actModel.getLock_money_format(), "未找到"); // 设置冻结金额
							CommonInterface.refreshLocalUser();
						} 
					}
				}

			};
			InterfaceServer.getInstance().requestInterface(model, handler, true);
		}

	}

	private void initItems()
	{

		mItemRechargeWithdraw.setTitleImage(R.drawable.ic_recharge_withdrawals);
		mItemRechargeWithdraw.setTitleText("充值提现");
		mItemRechargeWithdraw.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemRechargeWithdraw.setBackgroundImage(R.drawable.selector_single_item);

		mItemMyInvest.setTitleImage(R.drawable.ic_my_investment);
		mItemMyInvest.setTitleText("我的投资");
		mItemMyInvest.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemMyInvest.setBackgroundImage(R.drawable.selector_single_item_top);

		mItemBondTransfer.setTitleImage(R.drawable.ic_bond_transfer);
		mItemBondTransfer.setTitleText("债权转让");
		mItemBondTransfer.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemBondTransfer.setBackgroundImage(R.drawable.selector_single_item_middle);

		mItemMyInterestBid.setTitleImage(R.drawable.ic_my_interest_bid);
		mItemMyInterestBid.setTitleText("我关注的标");
		mItemMyInterestBid.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemMyInterestBid.setBackgroundImage(R.drawable.selector_single_item_middle);

		mItemBidRecorder.setTitleImage(R.drawable.ic_bid_recorder);
		mItemBidRecorder.setTitleText("投标记录");
		mItemBidRecorder.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemBidRecorder.setBackgroundImage(R.drawable.selector_single_item_bottom);

		mItemRepaymentLoan.setTitleImage(R.drawable.ic_repay_loans);
		mItemRepaymentLoan.setTitleText("偿还贷款");
		mItemRepaymentLoan.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemRepaymentLoan.setBackgroundImage(R.drawable.selector_single_item_top);

		mItemPublishedLoan.setTitleImage(R.drawable.ic_published_loan);
		mItemPublishedLoan.setTitleText("已发布的贷款");
		mItemPublishedLoan.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemPublishedLoan.setBackgroundImage(R.drawable.selector_single_item_bottom);

		mItemModifyPassword.setTitleImage(R.drawable.ic_modify_password);
		mItemModifyPassword.setTitleText("修改密码");
		mItemModifyPassword.setTitleImageArrowRight(R.drawable.ic_arrow_right_more);
		mItemModifyPassword.setBackgroundImage(R.drawable.selector_single_item);
	}

	private void initTitle()
	{
		mTitle.setTitle("我的");

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
		mTxtUsername.setOnClickListener(this);
		mTxtUserLevel.setOnClickListener(this);
		mTxtMoneyAll.setOnClickListener(this);
		mTxtMoneyCanUse.setOnClickListener(this);
		mTxtMoneyFreeze.setOnClickListener(this);
		mItemRechargeWithdraw.setOnClickListener(this);
		mItemMyInvest.setOnClickListener(this);
		mItemBondTransfer.setOnClickListener(this);
		mItemMyInterestBid.setOnClickListener(this);
		mItemBidRecorder.setOnClickListener(this);
		mItemRepaymentLoan.setOnClickListener(this);
		mItemPublishedLoan.setOnClickListener(this);
		mItemModifyPassword.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.frag_personal_center_txt_username:

			break;

		case R.id.frag_personal_center_txt_user_level:

			break;

		case R.id.frag_personal_center_txt_money_all:

			break;

		case R.id.frag_personal_center_txt_money_can_use:

			break;

		case R.id.frag_personal_center_txt_money_freeze:

			break;

		case R.id.frag_personal_center_item_recharge_withdraw:
			clickRechargeWithDraw();
			break;

		case R.id.frag_personal_center_item_my_invest:
			clickMyInvest();
			break;

		case R.id.frag_personal_center_item_bond_transfer:
			clickMyTransfer();
			break;

		case R.id.frag_personal_center_item_my_interest_bid:
			clickMyInterestBid();
			break;

		case R.id.frag_personal_center_item_bid_recorder:
			clickBidRecorder();
			break;

		case R.id.frag_personal_center_item_repayment_loan:
			clickRepayLoan();
			break;

		case R.id.frag_personal_center_item_published_loan:
			clickPublishedLoan();
			break;

		case R.id.frag_personal_center_item_modify_password:
			clickModifyPassword();
			break;

		default:
			break;
		}
	}

	/**
	 * 投标记录
	 */
	private void clickBidRecorder()
	{
		startActivity(new Intent(getActivity(), BidRecorderActivity.class));
	}

	/**
	 * 已发布贷款
	 */
	private void clickPublishedLoan()
	{
		startActivity(new Intent(getActivity(), PublishedBorrowActivity.class));
	}

	/**
	 * 我的投资
	 */
	private void clickMyInvest()
	{
		startActivity(new Intent(getActivity(), MyInvestActivity.class));
	}

	/**
	 * 偿还贷款
	 */
	private void clickRepayLoan()
	{
		startActivity(new Intent(getActivity(), RepayBorrowListActivity.class));
	}

	/**
	 * 充值提现
	 */
	private void clickRechargeWithDraw()
	{
		startActivity(new Intent(getActivity(), RechargeLogActivity.class));
	}

	/**
	 * 我关注的标
	 */
	private void clickMyInterestBid()
	{
		UIHelper.showNormal(getActivity(), MyInterestBidActivity.class, false);
	}

	/**
	 * 债券转让
	 */
	private void clickMyTransfer()
	{
		UIHelper.showNormal(getActivity(), UcTransferActivity.class, false);
	}

	/**
	 * 修改密码
	 */
	private void clickModifyPassword()
	{
		Intent intent = new Intent(getActivity(), UcResetpasswordActivity.class);
		startActivity(intent);
	}

}