package com.fanwe.p2p.common;

import java.util.List;

import com.fanwe.p2p.R;
import com.fanwe.p2p.adapter.PopSelectBankAdapter;
import com.fanwe.p2p.application.App;
import com.fanwe.p2p.model.act.Uc_Add_BankActItemModel;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ViewHelper
{

	public static View getPopSimpleBankListView(Activity activity, List<Uc_Add_BankActItemModel> listModel, OnItemClickListener itemClickListener)
	{
		View view = LayoutInflater.from(App.getApplication()).inflate(R.layout.pop_lsv, null);
		ListView lsv = (ListView) view.findViewById(R.id.pop_lsv_lsv_content);
		PopSelectBankAdapter adapter = new PopSelectBankAdapter(listModel, activity);
		lsv.setAdapter(adapter);
		lsv.setOnItemClickListener(itemClickListener);
		return view;
	}

}
