package com.fanwe.p2p.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.model.Article_listActListModel;
import com.fanwe.p2p.utils.SDViewBinder;
import com.fanwe.p2p.utils.ViewHolder;

public class ArticlesListAdapter extends SDBaseAdapter<Article_listActListModel>
{

	public ArticlesListAdapter(List<Article_listActListModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getItemView(int position, View convertView, ViewGroup parent, Article_listActListModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_lsv_articles, null);
		}
		TextView tvTitle = ViewHolder.get(convertView, R.id.item_lsv_articles_tv_title);

		if (model != null)
		{
			SDViewBinder.setTextView(tvTitle, model.getTitle(), App.getStringById(R.string.not_found));
		}

		return convertView;
	}

}
