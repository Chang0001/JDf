package com.fanwe.p2p;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.customview.SDSimpleTitleView;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnLeftButtonClickListener;
import com.fanwe.p2p.customview.SDSimpleTitleView.OnRightButtonClickListener;
import com.fanwe.p2p.model.RequestModel;
import com.fanwe.p2p.model.act.Show_ArticleActModel;
import com.fanwe.p2p.server.InterfaceServer;
import com.fanwe.p2p.utils.SDInterfaceUtil;
import com.fanwe.p2p.utils.SDToast;
import com.sunday.ioc.SDIoc;
import com.sunday.ioc.annotation.ViewInject;
import com.ta.sunday.http.impl.SDAsyncHttpResponseHandler;

/**
 * 项目详情web界面
 * 
 * @author js02
 * 
 */
public class ProjectDetailWebviewActivity extends BaseActivity
{
	/** webview 要加载的链接 */
	public static final String EXTRA_URL = "extra_url";
	/** webview 界面标题 */
	public static final String EXTRA_TITLE = "extra_title";
	/** 要显示的文章的ID */
	public static final String EXTRA_ARTICLE_ID = "extra_article_id";
	/** 要显示的HTML内容 */
	public static final String EXTRA_HTML_CONTENT = "extra_html_content";

	@ViewInject(id = R.id.act_project_detail_webview_title)
	private SDSimpleTitleView mTitle = null;

	@ViewInject(id = R.id.act_project_detail_webview_pgb_progress)
	private ProgressBar mPgbProgress = null;

	@ViewInject(id = R.id.act_project_detail_webview_web)
	private WebView mWeb = null;

	private String mStrUrl = null;
	private String mStrTitle = null;
	private String mStrArticleId = null;
	private String mStrHtmlContent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_project_detail_webview);
		SDIoc.injectView(this);
		init();
	}

	private void init()
	{
		App.getApplication().getmRuntimeConfig().setProjectDetailWebviewActivity(this);
		initIntentData();
		initTitle();
		initWebView();
		startLoadData();
	}

	private void startLoadData()
	{
		if (mStrHtmlContent != null)
		{
			loadHtmlContent(mStrHtmlContent);
			return;
		}

		if (mStrArticleId != null)
		{
			loadArticleDetail(mStrArticleId);
			return;
		}

		if (mStrUrl != null)
		{
			loadUrl(mStrUrl);
			return;
		}

	}

	private void initIntentData()
	{
		mStrUrl = getIntent().getStringExtra(EXTRA_URL);
		mStrTitle = getIntent().getStringExtra(EXTRA_TITLE);
		mStrArticleId = getIntent().getStringExtra(EXTRA_ARTICLE_ID);
		mStrHtmlContent = getIntent().getStringExtra(EXTRA_HTML_CONTENT);
	}

	private void initWebView()
	{
		WebSettings settings = mWeb.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		// settings.setUseWideViewPort(true);
		// settings.setLoadWithOverviewMode(true);
		mWeb.setWebViewClient(new ProjectDetailWebviewActivity_WebViewClient());
		mWeb.setWebChromeClient(new ProjectDetailWebviewActivity_WebChromeClient());
	}

	private void loadHtmlContent(String htmlContent)
	{
		if (htmlContent != null)
		{
			mWeb.loadDataWithBaseURL("about:blank", htmlContent, "text/html", "utf-8", null);
		}
	}

	private void loadUrl(String url)
	{
		if (!TextUtils.isEmpty(url))
		{
			Log.i("ProjectDetailWebviewActivity", url);
			mWeb.loadUrl(mStrUrl);
		}
	}

	protected void loadArticleDetail(String articleId)
	{
		Map<String, Object> mapData = new HashMap<String, Object>();
		mapData.put("act", "show_article");
		mapData.put("id", articleId);
		RequestModel model = new RequestModel(mapData);
		SDAsyncHttpResponseHandler handler = new SDAsyncHttpResponseHandler()
		{
			@Override
			public Object onSuccessInRequestThread(int statusCode, Header[] headers, String content)
			{
				Show_ArticleActModel actModel = JSON.parseObject(content, Show_ArticleActModel.class);
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
			}

			@Override
			public void onSuccessInMainThread(int statusCode, Header[] headers, String content, Object result)
			{
				Show_ArticleActModel actModel = (Show_ArticleActModel) result;
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					if (actModel.getResponse_code() == 1)
					{
						if (TextUtils.isEmpty(actModel.getContent()))
						{
							SDToast.showToast("没有文章详情!");
						} else
						{
							loadHtmlContent(actModel.getContent());
						}

						if (!TextUtils.isEmpty(actModel.getTitle()))
						{
							mTitle.setTitle(actModel.getTitle());
						} else
						{
							if (!TextUtils.isEmpty(mStrTitle))
							{
								mTitle.setTitle(mStrTitle);
							}
						}

					} else
					{
						SDToast.showToast("获取文章详情失败!");
					}
				}
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler, true);
	}

	class ProjectDetailWebviewActivity_WebViewClient extends WebViewClient
	{

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{

			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{

			super.onPageFinished(view, url);
		}
	}

	class ProjectDetailWebviewActivity_WebChromeClient extends WebChromeClient
	{

		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			if (newProgress == 100)
			{
				mPgbProgress.setVisibility(View.GONE);
			} else
			{
				if (mPgbProgress.getVisibility() == View.GONE)
				{
					mPgbProgress.setVisibility(View.VISIBLE);
				}
				mPgbProgress.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWeb.canGoBack())
		{
			mWeb.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initTitle()
	{
		if (mStrTitle != null)
		{
			mTitle.setTitle(mStrTitle);
		}

		mTitle.setLeftButton("返回", R.drawable.ic_header_left, new OnLeftButtonClickListener()
		{

			@Override
			public void onLeftBtnClick(View button)
			{
				finishActivity(true);
			}
		}, null);

		mTitle.setRightButtonText("刷新", new OnRightButtonClickListener()
		{

			@Override
			public void onRightBtnClick(View button)
			{
				startLoadData();
			}
		}, R.drawable.bg_btn_project_detail_title_right_not_faved, null);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// App.getApplication().getmRuntimeConfig().setProjectDetailWebviewActivity(null);
	}

}