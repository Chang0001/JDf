package com.fanwe.p2p.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.LoginActivity;
import com.fanwe.p2p.ProjectDetailActivity;
import com.fanwe.p2p.ProjectDetailWebviewActivity;
import com.fanwe.p2p.R;
import com.fanwe.p2p.adapter.BorrowInvestAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.constant.Constant;
import com.fanwe.p2p.constant.Constant.SlidingMenuItem;
import com.fanwe.p2p.customview.AbSlidingPlayView;
import com.fanwe.p2p.customview.AbSlidingPlayView.AbOnItemClickListener;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnRightButtonClickListener;
import com.fanwe.p2p.dao.InitActDBModelDao;
import com.fanwe.p2p.model.DealsActItemModel;
import com.fanwe.p2p.model.InitActAdv_ListModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.InitActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDViewBinder;
import com.fanwe.p2p.utils.UIHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 首页
 * 
 * @author yhz
 */
public class MainPageNewFragment extends BaseFragment
{
	@ViewInject(id = R.id.frag_main_page_new_sd_title)
	private SDSimpleTitleView mSdTitle = null;

	@ViewInject(id = R.id.frag_main_page_new_ap_ad)
	private AbSlidingPlayView mApAd = null;

	@ViewInject(id = R.id.frag_main_page_new_tv_virtual_money_1)
	private TextView mTvVirtualMoney1 = null;

	@ViewInject(id = R.id.frag_main_page_new_tv_virtual_money_2)
	private TextView mTvVirtualMoney2 = null;

	@ViewInject(id = R.id.frag_main_page_new_tv_virtual_money_3)
	private TextView mTvVirtualMoney3 = null;

	@ViewInject(id = R.id.frag_main_page_new_ll_center)
	private LinearLayout mLlCenter = null;

	@ViewInject(id = R.id.fragMainPage_fl_head)
	private LinearLayout mFlHead = null;

	@ViewInject(id = R.id.frag_main_page_new_ptrsv_all)
	private PullToRefreshScrollView mPtrsvAll = null;

	private InitActModel mInitActModel = null;

	private List<DealsActItemModel> mListDealsModel = null;

	private List<InitActAdv_ListModel> mListActAdvModel = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag_main_page_new, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;
	}

	private void init()
	{

		initRefreshLL();
		initInfo();

	}

	private void initInfo()
	{
		initInitActModel();
		if (mInitActModel != null)
		{
			initTitle();
			initAdViewPager();
			initCenterViewPager();
			addFootView();
			addIgnoredView();
		}
	}

	private void initRefreshLL()
	{
		mPtrsvAll.setOnRefreshListener(new OnRefreshListener<ScrollView>()
		{
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				// TODO Auto-generated method stub
				requestInitInterface();
			}
		});
	}

	private void initInitActModel()
	{
		mInitActModel = InitActDBModelDao.readInitDB();
	}

	private void initTitle()
	{

		if (mInitActModel.getProgram_title() != null)
		{
			mSdTitle.setTitle(mInitActModel.getProgram_title());
		}

		mSdTitle.setLeftButtonImage(R.drawable.ic_title_menu, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				toggleSlideMenu();
			}
		}, null);

		mSdTitle.setRightButtonImage(R.drawable.ic_main_title_right_btn, new OnRightButtonClickListener()
		{
			@Override
			public void onRightBtnClick(View button)
			{
				// TODO:跳到界面
				if (App.getApplication().getmLocalUser() != null) // 已经登录
				{
					if (getMainActivity() != null && getMainActivity().getSlidingMenuFragment() != null)
					{
						getMainActivity().getSlidingMenuFragment().setSelectIndex(SlidingMenuItem.MY_ACCOUNT, null, true, false);
					}
				} else
				{
					UIHelper.showNormal(getActivity(), LoginActivity.class, false);
				}
			}
		}, null);

		mSdTitle.setTitleBottom(mInitActModel.getSite_domain());
	}

	private void initAdViewPager()
	{
		mApAd.removeAllViews();
		mApAd.setNavHorizontalGravity(Gravity.CENTER);

		mListActAdvModel = mInitActModel.getIndex_list().getAdv_list();

		if (mInitActModel != null && mInitActModel.getIndex_list() != null)
		{
			for (int i = 0; i < mInitActModel.getIndex_list().getAdv_list().size(); i++)
			{
				InitActAdv_ListModel model = mInitActModel.getIndex_list().getAdv_list().get(i);
				ImageView img = new ImageView(App.getApplication());
				img.setScaleType(ScaleType.CENTER_INSIDE);
				mApAd.addView(img);
				if (i == 0)
				{
					SDViewBinder.setImageFillScreenWidthByScale(img, mFlHead, model.getImg());
				} else
				{
					SDViewBinder.setImageView(img, model.getImg());
				}
			}
		}
		mApAd.setOnItemClickListener(abOnItemClickListenerHead);
	}

	private AbOnItemClickListener abOnItemClickListenerHead = new AbOnItemClickListener()
	{

		@Override
		public void onClick(int position)
		{
			// TODO Auto-generated method stub
			String type = mListActAdvModel.get(position).getType();
			String data = mListActAdvModel.get(position).getData();
			String open_rul_type = mListActAdvModel.get(position).getOpen_url_type();
			clickListener(type, data, open_rul_type, position);
		}
	};

	private void clickListener(String type, String data, String open_rul_type, int position)
	{
		if (Integer.valueOf(type) == Constant.Init_Adv_List.Init_Adv_List_Type_1)
		{
			Intent intent = new Intent(getActivity(), ProjectDetailWebviewActivity.class);
			intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "");
			intent.putExtra(ProjectDetailWebviewActivity.EXTRA_ARTICLE_ID, data);
			getActivity().startActivity(intent);

		} else if (Integer.valueOf(type) == Constant.Init_Adv_List.Init_Adv_List_Type_2)
		{
			if (Integer.valueOf(open_rul_type) == Constant.Init_Adv_List.Init_Adv_List_Open_Url_Type_0)
			{
				Intent intent = new Intent(getActivity(), ProjectDetailWebviewActivity.class);
				intent.putExtra(ProjectDetailWebviewActivity.EXTRA_TITLE, "");
				intent.putExtra(ProjectDetailWebviewActivity.EXTRA_URL, data);
				getActivity().startActivity(intent);
			}
			if (Integer.valueOf(open_rul_type) == Constant.Init_Adv_List.Init_Adv_List_Open_Url_Type_1)
			{
				UIHelper.showHTML(getActivity(), data);
			}
		} else
		{
			SDToast.showToast("error:type错误");
		}

	}

	private void initCenterViewPager()
	{
		mListDealsModel = mInitActModel.getIndex_list().getDeal_list();

		if (mInitActModel != null && mInitActModel.getIndex_list() != null && mInitActModel.getIndex_list().getDeal_list() != null)
		{
			mLlCenter.removeAllViews();

			BorrowInvestAdapter adapter = new BorrowInvestAdapter(mInitActModel.getIndex_list().getDeal_list(), getActivity());
			for (int i = 0; i < mInitActModel.getIndex_list().getDeal_list().size(); i++)
			{
				View v = adapter.getView(i, null, null);
				v.setTag(adapter.getItem(i));
				v.setOnClickListener(new CerterDealOnClickListener());
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				params.topMargin = 20;
				mLlCenter.addView(v, params);
			}
		}
	}

	class CerterDealOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			DealsActItemModel model = (DealsActItemModel) v.getTag();
			if (model != null)
			{
				Intent intent = new Intent(getActivity(), ProjectDetailActivity.class);
				intent.putExtra(ProjectDetailActivity.EXTRA_DEALS_ITEM_MODEL, model);
				getActivity().startActivity(intent);
			}
		}

	}

	private void addFootView()
	{
		if (mInitActModel.getVirtual_money_1() != null)
			mTvVirtualMoney1.setText(mInitActModel.getVirtual_money_1());
		if (mInitActModel.getVirtual_money_2() != null)
			mTvVirtualMoney2.setText(mInitActModel.getVirtual_money_2());
		if (mInitActModel.getVirtual_money_3() != null)
			mTvVirtualMoney3.setText(mInitActModel.getVirtual_money_3());
	}

	/**
	 * 请求初始化接口
	 */
	private void requestInitInterface()
	{
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "init");
		RequestModel model = new RequestModel(mapData);

		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{

			@Override
			public void onStartInMainThread(Object result)
			{
				// TODO Auto-generated method stub
				super.onStartInMainThread(result);

			}

			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				try
				{

					InitActModel model = JSON.parseObject(content, InitActModel.class);
					return model;
				} catch (Exception e)
				{
					return null;
				}

			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				InitActDBModelDao.saveInitActModel(content);
				InitActModel model = (InitActModel) result;
				if (!SDInterfaceUtil.isActModelNull(model))
				{
					// TODO:对初始化返回结果进行处理
					switch (model.getResponse_code())
					{
					case 1:
						initInfo();
						break;

					default:
						break;
					}
				}
			}

			@Override
			public void onFailureInMainThread(Throwable error, String content, Object result)
			{
				mPtrsvAll.onRefreshComplete();
			}

			@Override
			public void onFinishInMainThread(Object result)
			{
				mPtrsvAll.onRefreshComplete();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		removeIgnoredView();
	}

	@Override
	public void onShowView()
	{
		// TODO Auto-generated method stub
		super.onShowView();
		addIgnoredView();
	}

	@Override
	public void onHideView()
	{
		// TODO Auto-generated method stub
		super.onHideView();
		removeIgnoredView();
	}

	private void addIgnoredView()
	{
		if (getMainActivity() != null)
		{
			getMainActivity().getSlidingMenu().addIgnoredView(mFlHead);
		}
	}

	private void removeIgnoredView()
	{
		if (getMainActivity() != null)
		{
			getMainActivity().getSlidingMenu().removeIgnoredView(mFlHead);

		}
	}
}