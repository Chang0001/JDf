package com.fanwe.p2p;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fanwe.p2p.R.color;
import com.fanwe.p2p.constant.Constant.DealsActSearchConditionCid;
import com.fanwe.p2p.constant.Constant.DealsActSearchConditionDealStatus;
import com.fanwe.p2p.constant.Constant.DealsActSearchConditionInterest;
import com.fanwe.p2p.constant.Constant.DealsActSearchConditionLevel;
import com.fanwe.p2p.customview.FlowLayout;
import com.fanwe.p2p.customview.SDSimpleTabView;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.dao.InitActDBModelDao;
import com.fanwe.p2p.model.DealsSearchModel;
import com.fanwe.p2p.model.InitActDeal_cate_listModel;
import com.fanwe.p2p.model.act.InitActModel;
import com.fanwe.p2p.utils.SDViewNavigatorManager;
import com.fanwe.p2p.utils.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;

/**
 * 搜索界面
 * 
 * @author js02
 * 
 */
public class SearchDealsActivity extends BaseActivity implements OnClickListener
{

	public static final String EXTRA_SEALS_SEARCH_MODEL = "extra_seals_search_model";

	public static final int RESULT_CODE_SUCCESS = 1;

	@ViewInject(id = R.id.act_search_deals_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_search_deals_btn_search)
	private Button mBtnSearch = null;

	private DealsSearchModel mSearchModel = new DealsSearchModel();

	@ViewInject(id = R.id.act_search_deals_flow_level)
	private FlowLayout mFlowLevel = null; // 利率
	private SDViewNavigatorManager mLevelManager = new SDViewNavigatorManager();

	@ViewInject(id = R.id.act_search_deals_flow_rate)
	private FlowLayout mFlowRate = null; // 利率
	private SDViewNavigatorManager mRateManager = new SDViewNavigatorManager();

	@ViewInject(id = R.id.act_search_deals_flow_deal_status)
	private FlowLayout mFlowDealStatus = null; // 借款状态
	private SDViewNavigatorManager mDealStatusManager = new SDViewNavigatorManager();

	@ViewInject(id = R.id.act_search_deals_flow_cid)
	private FlowLayout mFlowCid = null; // 认证标示
	private SDViewNavigatorManager mCidManager = new SDViewNavigatorManager();

	private List<InitActDeal_cate_listModel> mListDealCate = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search_deals);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		initDealCateList();
		initTitle();
		getDataFromIntent();
		initLevelCondition(); // 等级
		initRateCondition(); // 利率
		initDealStatusCondition(); // 借款状态
		initCidCondition(); // 认证标识
		registeClick();

	}

	private void initDealCateList()
	{
		InitActModel initActModel = InitActDBModelDao.readInitDB();
		if (initActModel != null)
		{
			mListDealCate = initActModel.getDeal_cate_list();
		}
	}

	private void getDataFromIntent()
	{
		DealsSearchModel searchModel = (DealsSearchModel) getIntent().getSerializableExtra(EXTRA_SEALS_SEARCH_MODEL);
		if (searchModel != null)
		{
			mSearchModel = searchModel;
			Log.i("lll", mSearchModel.getConditionCid());
		}
	}

	/**
	 * 等级
	 */
	private void initLevelCondition()
	{

		SDSimpleTabView tabLevelAll = createTabView("不限", DealsActSearchConditionLevel.ALL);
		mFlowLevel.addView(tabLevelAll);

		SDSimpleTabView tabLevelB = createTabView("B级以上", DealsActSearchConditionLevel.B);
		mFlowLevel.addView(tabLevelB);

		SDSimpleTabView tabLevelC = createTabView("C级以上", DealsActSearchConditionLevel.C);
		mFlowLevel.addView(tabLevelC);

		SDSimpleTabView tabLevelD = createTabView("D级以上", DealsActSearchConditionLevel.D);
		mFlowLevel.addView(tabLevelD);

		SDSimpleTabView tabLevelE = createTabView("E级以上", DealsActSearchConditionLevel.E);
		mFlowLevel.addView(tabLevelE);

		SDSimpleTabView[] arrItems = new SDSimpleTabView[] { tabLevelAll, tabLevelB, tabLevelC, tabLevelD, tabLevelE };
		mLevelManager.setItems(arrItems);
		String level = mSearchModel.getConditionLevel();
		if (level.equals(DealsActSearchConditionLevel.ALL))
		{
			mLevelManager.setSelectIndex(0, tabLevelAll, true);
		} else if (level.equals(DealsActSearchConditionLevel.B))
		{
			mLevelManager.setSelectIndex(1, tabLevelB, true);
		} else if (level.equals(DealsActSearchConditionLevel.C))
		{
			mLevelManager.setSelectIndex(2, tabLevelC, true);
		} else if (level.equals(DealsActSearchConditionLevel.D))
		{
			mLevelManager.setSelectIndex(3, tabLevelD, true);
		} else if (level.equals(DealsActSearchConditionLevel.E))
		{
			mLevelManager.setSelectIndex(4, tabLevelE, true);
		}

		mLevelManager.setmListener(new SDViewNavigatorManagerListener()
		{
			@Override
			public void onItemClick(View v, int index)
			{
				String data = (String) v.getTag();
				if (data != null)
				{
					mSearchModel.setConditionLevel(data);
				}
			}
		});
	}

	/**
	 * 利率
	 */
	private void initRateCondition()
	{

		SDSimpleTabView tabRateAll = createTabView("不限", DealsActSearchConditionInterest.ALL);
		mFlowRate.addView(tabRateAll);

		SDSimpleTabView tabRateTen = createTabView("10%以上", DealsActSearchConditionInterest.TEN);
		mFlowRate.addView(tabRateTen);

		SDSimpleTabView tabRateTwelve = createTabView("12%以上", DealsActSearchConditionInterest.TWELVE);
		mFlowRate.addView(tabRateTwelve);

		SDSimpleTabView tabRateFifteen = createTabView("15%以上", DealsActSearchConditionInterest.FIFTEEN);
		mFlowRate.addView(tabRateFifteen);

		SDSimpleTabView tabRateEighteen = createTabView("18%以上", DealsActSearchConditionInterest.EIGHTEEN);
		mFlowRate.addView(tabRateEighteen);

		SDSimpleTabView[] arrItems = new SDSimpleTabView[] { tabRateAll, tabRateTen, tabRateTwelve, tabRateFifteen, tabRateEighteen };
		mRateManager.setItems(arrItems);
		if (mSearchModel.getConditionInterest().equals(DealsActSearchConditionInterest.ALL))
		{
			mRateManager.setSelectIndex(0, tabRateAll, true);
		} else if (mSearchModel.getConditionInterest().equals(DealsActSearchConditionInterest.TEN))
		{
			mRateManager.setSelectIndex(1, tabRateTen, true);
		} else if (mSearchModel.getConditionInterest().equals(DealsActSearchConditionInterest.TWELVE))
		{
			mRateManager.setSelectIndex(2, tabRateFifteen, true);
		} else if (mSearchModel.getConditionInterest().equals(DealsActSearchConditionInterest.FIFTEEN))
		{
			mRateManager.setSelectIndex(3, tabRateFifteen, true);
		} else if (mSearchModel.getConditionInterest().equals(DealsActSearchConditionInterest.EIGHTEEN))
		{
			mRateManager.setSelectIndex(4, tabRateFifteen, true);
		}

		mRateManager.setmListener(new SDViewNavigatorManagerListener()
		{
			@Override
			public void onItemClick(View v, int index)
			{
				String data = (String) v.getTag();
				if (data != null)
				{
					mSearchModel.setConditionInterest(data);
				}
			}
		});
	}


	/**
	 * 借款状态
	 */
	private void initDealStatusCondition()
	{

		SDSimpleTabView tabDealStatusAll = createTabView("不限", DealsActSearchConditionDealStatus.ALL);
		mFlowDealStatus.addView(tabDealStatusAll);

		SDSimpleTabView tabDealStatusLoaning = createTabView("进行中", DealsActSearchConditionDealStatus.LOANING);
		mFlowDealStatus.addView(tabDealStatusLoaning);

		SDSimpleTabView tabDealStatusFull = createTabView("满标", DealsActSearchConditionDealStatus.FULL);
		mFlowDealStatus.addView(tabDealStatusFull);

		SDSimpleTabView tabDealStatusFail = createTabView("流标", DealsActSearchConditionDealStatus.FAIL);
		mFlowDealStatus.addView(tabDealStatusFail);

		SDSimpleTabView tabDealStatusRepaymenting = createTabView("还款中", DealsActSearchConditionDealStatus.REPAYMENTING);
		mFlowDealStatus.addView(tabDealStatusRepaymenting);

		SDSimpleTabView tabDealStatusRepaymented = createTabView("已还清", DealsActSearchConditionDealStatus.REPAYMENTED);
		mFlowDealStatus.addView(tabDealStatusRepaymented);

		SDSimpleTabView[] arrItems = new SDSimpleTabView[] { tabDealStatusAll, tabDealStatusLoaning, tabDealStatusFull, tabDealStatusFail, tabDealStatusRepaymenting, tabDealStatusRepaymented };
		mDealStatusManager.setItems(arrItems);
		if (mSearchModel.getConditionDealStatus().equals(DealsActSearchConditionDealStatus.ALL))
		{
			mDealStatusManager.setSelectIndex(0, tabDealStatusAll, true);
		} else if (mSearchModel.getConditionDealStatus().equals(DealsActSearchConditionDealStatus.LOANING))
		{
			mDealStatusManager.setSelectIndex(1, tabDealStatusLoaning, true);
		} else if (mSearchModel.getConditionDealStatus().equals(DealsActSearchConditionDealStatus.FULL))
		{
			mDealStatusManager.setSelectIndex(2, tabDealStatusFull, true);
		} else if (mSearchModel.getConditionDealStatus().equals(DealsActSearchConditionDealStatus.FAIL))
		{
			mDealStatusManager.setSelectIndex(2, tabDealStatusFail, true);
		} else if (mSearchModel.getConditionDealStatus().equals(DealsActSearchConditionDealStatus.REPAYMENTING))
		{
			mDealStatusManager.setSelectIndex(2, tabDealStatusRepaymenting, true);
		} else if (mSearchModel.getConditionDealStatus().equals(DealsActSearchConditionDealStatus.REPAYMENTED))
		{
			mDealStatusManager.setSelectIndex(2, tabDealStatusRepaymented, true);
		}

		mDealStatusManager.setmListener(new SDViewNavigatorManagerListener()
		{
			@Override
			public void onItemClick(View v, int index)
			{
				String data = (String) v.getTag();
				if (data != null)
				{
					mSearchModel.setConditionDealStatus(data);
				}
			}
		});

	}

	/**
	 * 认证标识
	 */
	private void initCidCondition()
	{
		if (mListDealCate != null && mListDealCate.size() > 0)
		{
			SDSimpleTabView[] arrItems = new SDSimpleTabView[mListDealCate.size() + 1];
			SDSimpleTabView tabViewAll = createTabView("不限", DealsActSearchConditionCid.ALL);
			mFlowCid.addView(tabViewAll);
			arrItems[0] = tabViewAll;
			for (int i = 0; i < mListDealCate.size(); i++)
			{
				InitActDeal_cate_listModel item = mListDealCate.get(i);
				SDSimpleTabView tabView = getTabViewByInitActDeal_cate_listModel(item);
				if (tabView != null)
				{
					arrItems[i + 1] = tabView;
					mFlowCid.addView(tabView);
				}
			}
			mCidManager.setItems(arrItems);
			mCidManager.setmListener(new SDViewNavigatorManagerListener()
			{
				@Override
				public void onItemClick(View v, int index)
				{
					Object object = v.getTag();
					if (object instanceof String)
					{
						String data = (String) object;
						mSearchModel.setConditionCid(data);
					} else if (object instanceof InitActDeal_cate_listModel)
					{
						InitActDeal_cate_listModel model = (InitActDeal_cate_listModel) object;
						if (model != null && model.getId() != null)
						{
							mSearchModel.setConditionCid(model.getId());
						}
					}
				}
			});

			boolean isMatch = false;
			for (int i = 1; i < arrItems.length; i++)
			{
				if (mSearchModel.getConditionCid().equals(mListDealCate.get(i - 1).getId()))
				{
					mCidManager.setSelectIndex(i, arrItems[i], true);
					isMatch = true;
					break;
				}
			}
			if (!isMatch)
			{
				mCidManager.setSelectIndex(0, tabViewAll, true);
			}
		} else
		{
			SDSimpleTabView tabView = createTabView("不限", DealsActSearchConditionCid.ALL);
			mFlowCid.addView(tabView);
			SDSimpleTabView[] arrItems = new SDSimpleTabView[] { tabView };
			mCidManager.setItems(arrItems);
			mCidManager.setmListener(new SDViewNavigatorManagerListener()
			{
				@Override
				public void onItemClick(View v, int index)
				{
					String data = (String) v.getTag();
					if (data != null)
					{
						mSearchModel.setConditionCid(data);
					}
				}
			});
			mCidManager.setSelectIndex(0, tabView, true);
		}
	}

	private SDSimpleTabView getTabViewByInitActDeal_cate_listModel(InitActDeal_cate_listModel model)
	{
		if (model != null && model.getId() != null && model.getName() != null)
		{
			SDSimpleTabView tab = new SDSimpleTabView(getApplicationContext());
			setTabDefaultState(tab, model.getName());
			tab.setTag(model);
			return tab;
		} else
		{
			return null;
		}
	}

	private SDSimpleTabView setTabDefaultState(SDSimpleTabView tab, String tabName)
	{
		if (tab != null && tabName != null)
		{
			tab.setmBackgroundImageNormal(R.drawable.bg_search_condition_normal);
			tab.setmBackgroundImageSelect(R.drawable.bg_search_condition_select);
			tab.setmTextColorNormal(getResources().getColor(color.text_black));
			tab.setmTextColorSelect(getResources().getColor(color.white));
			tab.setTabName(tabName);
		}
		return tab;
	}

	private SDSimpleTabView createTabView(String tabName, Object viewTagData)
	{
		if (tabName != null)
		{
			SDSimpleTabView tab = new SDSimpleTabView(getApplicationContext());
			tab.setmBackgroundImageNormal(R.drawable.bg_search_condition_normal);
			tab.setmBackgroundImageSelect(R.drawable.bg_search_condition_select);
			tab.setmTextColorNormal(getResources().getColor(color.text_black));
			tab.setmTextColorSelect(getResources().getColor(color.white));
			tab.setTabName(tabName);
			tab.setTag(viewTagData);
			return tab;
		} else
		{
			return null;
		}

	}

	private void initTitle()
	{
		mTitle.setTitle("搜索");
		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				finish();
			}
		}, null);

	}

	private void registeClick()
	{
		mBtnSearch.setOnClickListener(this);

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{

		case R.id.act_search_deals_btn_search:
			clickSearch();
			break;

		default:
			break;
		}
	}

	private void clickSearch()
	{
		Intent intentFinish = new Intent();
		intentFinish.putExtra(EXTRA_SEALS_SEARCH_MODEL, mSearchModel);
		setResult(RESULT_CODE_SUCCESS, intentFinish);
		finish();
	}

}