package com.fanwe.p2p;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.p2p.R.color;
import com.fanwe.p2p.customview.SDSimpleProjectDetailItemView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnRightButtonClickListener;
import com.fanwe.p2p.model.TransferActItemModel;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDUIUtil;
import com.fanwe.p2p.utils.SDViewUtil;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
/**
 * 确认转让(已被购买)
 * @author js02
 *
 */
public class ConfirmTransferBondCantBuyActivity extends BaseActivity
{
	public static final String EXTRA_BOND_DETAIL_ITEM_MODEL = "extra_bond_detail_item_model";

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_txt_name)
	private TextView mTxtName = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_sdview_remain_time)
	private SDSimpleProjectDetailItemView mSdviewRemainTime = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_sdview_repay_time)
	private SDSimpleProjectDetailItemView mSdviewRepayTime = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_sdview_next_repay_time)
	private SDSimpleProjectDetailItemView mSdviewNextRepayTime = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_sdview_transfer_amount)
	private SDSimpleProjectDetailItemView mSdviewTransferAmount = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_sdview_left_benjin)
	private SDSimpleProjectDetailItemView mSdviewLeftBenjin = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_sdview_left_lixi)
	private SDSimpleProjectDetailItemView mSdviewLeftLixi = null;

	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_sdview_transfer_income)
	private SDSimpleProjectDetailItemView mSdviewTransferIncome = null;
	
	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_sdview_buyer)
	private SDSimpleProjectDetailItemView mSdviewBuyer = null;
	
	@ViewInject(id = R.id.act_confirm_trnasfer_bond_cant_buy_sdview_buy_time)
	private SDSimpleProjectDetailItemView mSdviewBuyTime = null;



	@ViewInject(id = R.id.act_confirm_trnasfer_bond_btn_confirm_buy)
	private Button mBtnConfirmBuy = null;

	private TransferActItemModel mModel = null;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setSdContentView(R.layout.act_confirm_trnasfer_bond_cant_buy);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initIntentData();
		initTitle();
		initItems();


	}

	private void initIntentData()
	{
		mModel = (TransferActItemModel) getIntent().getSerializableExtra(EXTRA_BOND_DETAIL_ITEM_MODEL);
	}

	private void initItems()
	{
		if (mModel != null)
		{
			// 名字
			SDViewUtil.measureView(mTxtName);
			int width = mTxtName.getMeasuredWidth();
			if ((width + 10) > SDUIUtil.getScreenWidth(getApplicationContext()))
			{
				mTxtName.setGravity(Gravity.LEFT);
			} else
			{
				mTxtName.setGravity(Gravity.CENTER);
			}
			if (mModel.getName() != null)
			{
				mTxtName.setText(mModel.getName());
			} else
			{
				mTxtName.setText(getString(R.string.data_not_found));
			}

			mSdviewRemainTime.setTV_Left("剩余时间");
			mSdviewRepayTime.setTV_Left("总期限");
			if (mModel.getRepay_time() != null && mModel.getRepay_time_type_format() != null)
			{
				mSdviewRepayTime.setTV_Right(mModel.getRepay_time() + mModel.getRepay_time_type_format());
				mSdviewRemainTime.setTV_Right(mModel.getRepay_time() + mModel.getRepay_time_type_format());
				
			} else
			{
				mSdviewRepayTime.setTV_Right(getString(R.string.data_not_found));
				mSdviewRemainTime.setTV_Right(getString(R.string.data_not_found));
			}
			
			mSdviewNextRepayTime.setTV_Left("下还款日");
			mSdviewNextRepayTime.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getNear_repay_time_format() != null)
			{
				mSdviewNextRepayTime.setTV_Right(mModel.getNear_repay_time_format());
			} else
			{
				mSdviewNextRepayTime.setTV_Right(getString(R.string.data_not_found));
			}
			
			mSdviewTransferAmount.setTV_Left("转让金额");
			mSdviewTransferAmount.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
			if (mModel.getTransfer_amount_format() != null)
			{
				mSdviewTransferAmount.setTV_Right(mModel.getTransfer_amount_format());
			} else
			{
				mSdviewTransferAmount.setTV_Right(getString(R.string.data_not_found));
			}
			
			mSdviewLeftBenjin.setTV_Left("剩余本金");
			mSdviewLeftBenjin.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getLeft_benjin_format() != null)
			{
				mSdviewLeftBenjin.setTV_Right(mModel.getLeft_benjin_format());
			} else
			{
				mSdviewLeftBenjin.setTV_Right(getString(R.string.data_not_found));
			}
			
			mSdviewLeftLixi.setTV_Left("剩余利息");
			mSdviewLeftLixi.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_black_deep));
			if (mModel.getLeft_lixi_format() != null)
			{
				mSdviewLeftLixi.setTV_Right(mModel.getLeft_lixi_format());
			} else
			{
				mSdviewLeftLixi.setTV_Right(getString(R.string.data_not_found));
			}
			
			mSdviewTransferIncome.setTV_Left("受让收益");
			mSdviewTransferIncome.inverstdetail_item_tv_right.setTextColor(getResources().getColor(color.text_orange));
			if (mModel.getTransfer_income_format() != null)
			{
				mSdviewTransferIncome.setTV_Right(mModel.getTransfer_income_format());
			} else
			{
				mSdviewTransferIncome.setTV_Right(getString(R.string.data_not_found));
			}
			
			mSdviewBuyer.setTV_Left("承接人");
			if (mModel.getTuser() != null && mModel.getTuser().getUser_name() != null)
			{
				mSdviewBuyer.setTV_Right(mModel.getTuser().getUser_name());
			} else
			{
				mSdviewBuyer.setTV_Right(getString(R.string.data_not_found));
			}
			
			mSdviewBuyTime.setTV_Left("承接时间");
			if (mModel.getTransfer_time_format() != null)
			{
				mSdviewBuyTime.setTV_Right(mModel.getTransfer_time_format());
			} else
			{
				mSdviewBuyTime.setTV_Right(getString(R.string.data_not_found));
			}
			

			

		}

	}

	private void initTitle()
	{
		mTitle.setTitle("确认转让");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);
		mTitle.setRightButtonText("详情", new OnRightButtonClickListener()
		{
			@Override
			public void onRightBtnClick(View button)
			{
				// TODO 跳到webview详情页面
				if (mModel != null && !TextUtils.isEmpty(mModel.getApp_url()))
				{
					Intent intent = new Intent(ConfirmTransferBondCantBuyActivity.this, ProjectDetailWebviewActivity.class);
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "详情");
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, mModel.getApp_url());
					startActivity(intent);
				}else
				{
					SDToast.showToast("无详情链接");
				}
			}
		}, R.drawable.bg_title_my_interest_cancel, null);
	}


	
	
	
	
	
	

	

}