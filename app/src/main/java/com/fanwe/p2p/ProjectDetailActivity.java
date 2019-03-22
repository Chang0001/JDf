package com.fanwe.p2p;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.R.color;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.constant.Constant.DealStatus;
import com.fanwe.p2p.customview.SDSimpleProjectDetailItemView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnRightButtonClickListener;
import com.fanwe.p2p.event.EventTag;
import com.fanwe.p2p.model.DealsActItemModel;
import com.fanwe.p2p.model.LocalUserModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.BaseActModel;
import com.fanwe.p2p.model.act.Deal_CollectActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDUIUtil;
import com.fanwe.p2p.utils.SDViewUtil;
import com.sunday.busevent.SDBaseEvent;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 项目详情界面
 * 
 * @author js02
 * 
 */
public class ProjectDetailActivity extends BaseActivity implements OnClickListener
{
	private static final int REQUEST_CODE_LOGIN_FOR_FAVED = 1;
	private static final int REQUEST_CODE_LOGIN_FOR_INVEST = 2;
	public static final String EXTRA_DEALS_ITEM_MODEL = "extra_deals_item_model";

	@ViewInject(id = R.id.act_project_detail_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_project_detail_tv_title)
	private TextView mTvTitle = null;

	@ViewInject(id = R.id.act_project_detail_tv_num)
	private TextView mTvNum = null;

	@ViewInject(id = R.id.act_project_detail_sdview_borrow_amount)
	private SDSimpleProjectDetailItemView mSdviewBorrowAmount = null;

	@ViewInject(id = R.id.act_project_detail_sdview_need_money)
	private SDSimpleProjectDetailItemView mSdviewNeedMoney = null;

	@ViewInject(id = R.id.act_project_detail_sdview_min_loan_money)
	private SDSimpleProjectDetailItemView mSdviewMinLoanMoney = null;

	@ViewInject(id = R.id.act_project_detail_sdview_rate)
	private SDSimpleProjectDetailItemView mSdviewRate = null;

	@ViewInject(id = R.id.act_project_detail_sdview_repay_time)
	private SDSimpleProjectDetailItemView mSdviewRepayTime = null;

	@ViewInject(id = R.id.act_project_detail_sdview_repay_method)
	private SDSimpleProjectDetailItemView mSdviewRepayMethod = null;

	@ViewInject(id = R.id.act_project_detail_sdview_risk_rank)
	private SDSimpleProjectDetailItemView mSdviewRiskRank = null;

	@ViewInject(id = R.id.act_project_detail_sdview_remain_time)
	private SDSimpleProjectDetailItemView mSdviewRemainTime = null;

	@ViewInject(id = R.id.act_project_detail_btn_look_detail)
	private Button mBtnLookDetail = null;

	@ViewInject(id = R.id.act_project_detail_btn_invest)
	private Button mBtnInvest = null;

	private DealsActItemModel mModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_project_detail);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initIntentData();
		initTitle();
		initItems();
		registeClick();

	}

	private void initItems()
	{
		if (mModel != null)
		{

			if (mModel.getName() != null) // 项目名称
			{
				mTvTitle.setText(mModel.getName());
			} else
			{
				mTvTitle.setText(getString(R.string.data_not_found));
			}
			SDViewUtil.measureView(mTvTitle);
			int width = mTvTitle.getMeasuredWidth();
			if ((width + 10) > SDUIUtil.getScreenWidth(getApplicationContext()))
			{
				mTvTitle.setGravity(Gravity.LEFT);
			} else
			{
				mTvTitle.setGravity(Gravity.CENTER);
			}

			if (mModel.getId() != null) // 编号id
			{
				mTvNum.setText("借款编号 " + mModel.getId());
			} else
			{
				mTvNum.setText(getString(R.string.data_not_found));
			}

			mSdviewBorrowAmount.setTV_Left("借款金额"); // 借款金额
			mSdviewBorrowAmount.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getBorrow_amount_format() != null)
			{
				mSdviewBorrowAmount.setTV_Right(mModel.getBorrow_amount_format());
			} else
			{
				mSdviewBorrowAmount.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewNeedMoney.setTV_Left("可投金额"); // 可投金额
			mSdviewNeedMoney.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
			if (mModel.getNeed_money() != null)
			{
				mSdviewNeedMoney.setTV_Right(mModel.getNeed_money());
			} else
			{
				mSdviewNeedMoney.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewMinLoanMoney.setTV_Left("最低金额"); // 最低金额
			mSdviewMinLoanMoney.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getMin_loan_money_format() != null)
			{
				mSdviewMinLoanMoney.setTV_Right(mModel.getMin_loan_money_format());
			} else
			{
				mSdviewMinLoanMoney.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewRate.setTV_Left("年利率"); // 年利率
			mSdviewRate.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
			if (mModel.getRate() != null)
			{
				mSdviewRate.setTV_Right(mModel.getRate() + "%");
			} else
			{
				mSdviewRate.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewRepayTime.setTV_Left("期限"); // 期限
			mSdviewRepayTime.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getRepay_time() != null && mModel.getRepay_time_type_format() != null)
			{
				mSdviewRepayTime.setTV_Right(mModel.getRepay_time() + mModel.getRepay_time_type_format());
			} else
			{
				mSdviewRepayTime.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewRepayMethod.setTV_Left("还款方式"); // 还款方式
			if (mModel.getLoantype() != null && mModel.getLoantype_format() != null)
			{
				mSdviewRepayMethod.setTV_Right(mModel.getLoantype_format());
			} else
			{
				mSdviewRepayMethod.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewRiskRank.setTV_Left("风险等级"); // 风险等级
			if (mModel.getRisk_rank() != null && mModel.getRisk_rank_format() != null)
			{
				mSdviewRiskRank.setTV_Right(mModel.getRisk_rank_format());
			} else
			{
				mSdviewRiskRank.setTV_Right(getString(R.string.data_not_found));
			}

			mSdviewRemainTime.setTV_Left("剩余时间"); // 剩余时间
			if (mModel.getRemain_time_format() != null)
			{
				mSdviewRemainTime.setTV_Right(mModel.getRemain_time_format());
			} else
			{
				mSdviewRemainTime.setTV_Right(getString(R.string.data_not_found));
			}

		}

	}

	private void initIntentData()
	{
		Serializable model = getIntent().getSerializableExtra(EXTRA_DEALS_ITEM_MODEL);
		if (model != null)
		{
			mModel = (DealsActItemModel) model;
		}
	}

	private void initTitle()
	{
		mTitle.setTitle("项目详情");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);
		mTitle.setRightButtonText("未知", new OnRightButtonClickListener()
		{
			@Override
			public void onRightBtnClick(View button)
			{
				requestIsFaved();
			}
		}, R.drawable.bg_btn_project_detail_title_right_not_faved, null);

		requestIsFaved();
	}

	private void requestIsFaved()
	{
		if (mModel != null)
		{
			LocalUserModel user = App.getApplication().getmLocalUser();
			if (user == null)
			{
				setTitleRightState(false);
			} else
			// 已经登录
			{
				// TODO:请求是否已经关注该标接口
				Map<String, Object> mapData = new HashMap<String, Object>();
				mapData.put("act", "deal_collect");
				mapData.put("email", user.getUserName());
				mapData.put("pwd", user.getUserPassword());
				mapData.put("id", mModel.getId());
				RequestModel model = new RequestModel(mapData);
				SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
				{
					private Dialog nDialog = null;

					@Override
					public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
					{
						Deal_CollectActModel model = JSON.parseObject(content, Deal_CollectActModel.class);
						return model;
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
							nDialog.cancel();
						}
					}

					@Override
					public void onFailureInMainThread(Throwable e, String responseBody, Object result)
					{
						mTitle.setRightButtonText("未知", new OnRightButtonClickListener()
						{
							@Override
							public void onRightBtnClick(View button)
							{
								requestIsFaved();
							}
						}, R.drawable.bg_btn_project_detail_title_right_not_faved, null);
					}

					@Override
					public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
					{
						Deal_CollectActModel actModel = (Deal_CollectActModel) result;
						if (!SDInterfaceUtil.isActModelNull(actModel))
						{
							if (actModel.getResponse_code() == 1)
							{
								LocalUserModel user = App.getApplication().getmLocalUser();
								if (user != null)
								{
									user.setUserMoney(actModel.getUser_money());
									user.setUserMoneyFormat(actModel.getUser_money_format());
									App.getApplication().setmLocalUser(user);
								}
								if (actModel.getIs_faved().equals("0")) // 未关注
								{
									setTitleRightState(false);
								} else
								{
									setTitleRightState(true);
								}

							}
						}
					}

				};
				InterfaceServer.getInstance().requestInterface(model, handler, true);
			}
		}
	}

	private void setTitleRightState(boolean isFaved)
	{
		if (isFaved)
		{
			mTitle.setRightButtonText("已关注", new OnRightButtonClickListener()
			{
				@Override
				public void onRightBtnClick(View button)
				{

				}
			}, R.drawable.bg_btn_project_detail_title_right_faved, null);
		} else
		{
			if (App.getApplication().getmLocalUser() == null) // 未登录
			{
				mTitle.setRightButtonText("关注", new OnRightButtonClickListener()
				{
					@Override
					public void onRightBtnClick(View button)
					{
						Intent intent = new Intent(ProjectDetailActivity.this, LoginActivity.class);
						startActivityForResult(intent, REQUEST_CODE_LOGIN_FOR_FAVED);
					}
				}, R.drawable.bg_btn_project_detail_title_right_not_faved, null);
			} else
			{
				mTitle.setRightButtonText("关注", new OnRightButtonClickListener()
				{
					@Override
					public void onRightBtnClick(View button)
					{
						// TODO:请求关注接口
						requestFaved();
					}
				}, R.drawable.bg_btn_project_detail_title_right_not_faved, null);
			}
		}
	}

	private void requestFaved()
	{
		LocalUserModel user = App.getApplication().getmLocalUser();
		if (user == null)
		{
			return;
		}
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "uc_do_collect");
		mapData.put("email", user.getUserName());
		mapData.put("pwd", user.getUserPassword());
		mapData.put("id", mModel.getId());
		RequestModel model = new RequestModel(mapData);
		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{
			private Dialog nDialog = null;

			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				Deal_CollectActModel model = JSON.parseObject(content, Deal_CollectActModel.class);
				return model;
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
					nDialog.cancel();
				}
			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				BaseActModel actModel = (BaseActModel) result;
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					if (actModel.getResponse_code() == 1) // 关注成功
					{
						setTitleRightState(true);
					} else
					{
						if (actModel.getShow_err() == null)
						{
							SDToast.showToast("关注失败!");
						}
					}
				}
			}

		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
		case REQUEST_CODE_LOGIN_FOR_FAVED:
			if (App.getApplication().getmLocalUser() != null)
			{
				requestIsFaved();
			} else
			{
				SDToast.showToast("您未成功登录!");
			}
			break;
		case REQUEST_CODE_LOGIN_FOR_INVEST:
			if (App.getApplication().getmLocalUser() != null)
			{
				requestIsFaved();
			} else
			{
				SDToast.showToast("您未成功登录!");
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void registeClick()
	{

		mBtnLookDetail.setOnClickListener(this);
		mBtnInvest.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{

		case R.id.act_project_detail_btn_look_detail:
			clickLookDetail();
			break;

		case R.id.act_project_detail_btn_invest:
			clickInvest();
			break;

		default:
			break;
		}
	}

	private void clickInvest()
	{
		if (App.getApplication().getmLocalUser() == null)
		{
			Intent intent = new Intent(ProjectDetailActivity.this, LoginActivity.class);
			startActivityForResult(intent, REQUEST_CODE_LOGIN_FOR_INVEST);
		} else
		{
			if (mModel != null && mModel.getDeal_status() != null && !mModel.getDeal_status().equals(DealStatus.LOANING))
			{
				SDToast.showToast("当前标不可投!");
				return;
			}
			if (App.getApplication().getmLocalUser().getId().equals(mModel.getUser_id()))
			{
				SDToast.showToast("您不能给自己投标!");
			} else
			{
				Intent intent = new Intent(ProjectDetailActivity.this, ConfirmBidActivity.class);
				intent.putExtra(ConfirmBidActivity.EXTRA_DEALS_ITEM_DETAIL_MODEL, mModel);
				startActivity(intent);
			}
		}

	}

	/**
	 * webview打开详细页面
	 */
	private void clickLookDetail()
	{
		if (mModel != null && mModel.getApp_url() != null)
		{
			Intent intent = new Intent(ProjectDetailActivity.this, ProjectDetailWebviewActivity.class);
			intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, mModel.getApp_url());
			intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "全部详情");
			startActivity(intent);
		} else
		{
			SDToast.showToast("没有详情!");
		}
	}

	@Override
	public void onBackPressed()
	{
		finish();
		super.onBackPressed();
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		if (event.getEventTagInt() == EventTag.EVENT_BID_SUCCESS)
		{
			finish();
		}

	}

}