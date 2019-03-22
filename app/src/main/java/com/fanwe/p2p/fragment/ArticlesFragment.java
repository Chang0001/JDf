package com.fanwe.p2p.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.ProjectDetailWebviewActivity;
import com.fanwe.p2p.R;
import com.fanwe.p2p.adapter.ArticlesListAdapter;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.model.Article_listActListModel;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.Article_listActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.fanwe.p2p.utils.SDViewUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 文章资信(侧滑菜单进入)
 * 
 * @author js02
 * 
 */
public class ArticlesFragment extends BaseFragment
{
	@ViewInject(id = R.id.frag_articles_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.frag_articles_lv_articles)
	private PullToRefreshListView mLvArticles = null;

	@ViewInject(id = R.id.frag_articles_txt_empty)
	private TextView mTvEmptyMsg = null;

	private List<Article_listActListModel> mListArticles = new ArrayList<Article_listActListModel>();

	private ArticlesListAdapter mAdapter = null;

	private int mCurrentPage = 1;
	private int mTotalPage = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.frag_articles, container, false);
		SDIoc.injectView(this, view);
		init();
		return view;
	}

	private void init()
	{
		initTitle();
		bindDefaultData();
		initPullListView();
	}

	private void initPullListView()
	{
		mLvArticles.setMode(Mode.BOTH);
		mLvArticles.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				refreshData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				loadMoreData();
			}

		});
		mLvArticles.setRefreshing();
		mLvArticles.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Article_listActListModel model = mAdapter.getItem((int) id);
				if (model != null)
				{
					Intent intent = new Intent(getActivity(), ProjectDetailWebviewActivity.class);
					intent.putExtra(ProjectDetailWebviewActivity.EXTRA_ARTICLE_ID, model.getId());
					startActivity(intent);
				}
			}
		});
	}

	private void refreshData()
	{
		mCurrentPage = 1;
		requestArticles(false);
	}

	private void loadMoreData()
	{
		if (mListArticles != null && mListArticles.size() > 0)
		{
			if (++mCurrentPage > mTotalPage && mTotalPage != 0)
			{
				SDToast.showToast("没有更多数据了!");
				mLvArticles.onRefreshComplete();
			} else
			{
				requestArticles(true);
			}
		} else
		{
			refreshData();
		}
	}

	private void requestArticles(final boolean isLoadMore)
	{
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "article_list");
		mapData.put("page", mCurrentPage);

		RequestModel model = new RequestModel(mapData);
		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{
			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				Article_listActModel actModel = JSON.parseObject(content, Article_listActModel.class);
				return actModel;
			}

			@Override
			public void onStartInMainThread(Object result)
			{
				showLoadingDialog("请稍候...");
			}

			@Override
			public void onFinishInMainThread(Object result)
			{
				hideLoadingDialog();
				mLvArticles.onRefreshComplete();
				SDViewUtil.toggleEmptyMsgByList(mListArticles, mTvEmptyMsg);
			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				Article_listActModel actModel = (Article_listActModel) result;
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					if (actModel.getResponse_code() == 1)
					{
						if (actModel.getPage() != null)
						{
							mCurrentPage = actModel.getPage().getPage();
							mTotalPage = actModel.getPage().getPage_total();
						}
						SDViewUtil.updateAdapterByList(mListArticles, actModel.getList(), mAdapter, isLoadMore);
					}
				}
			}

		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	private void bindDefaultData()
	{
		mAdapter = new ArticlesListAdapter(mListArticles, getActivity());
		mLvArticles.setAdapter(mAdapter);
	}

	private void initTitle()
	{
		mTitle.setTitle("文章资讯");
		mTitle.setLeftButtonImage(R.drawable.ic_title_menu, new OnLeftButtonClickListener()
		{
			@Override
			public void onLeftBtnClick(View button)
			{
				toggleSlideMenu();
			}
		}, null);
	}

}