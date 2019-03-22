package com.fanwe.p2p.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class SDBottomNavigatorView extends LinearLayout
{

	private SDBottomNavigatorBaseItem[] mItems = null;

	private int mCurrentIndex = -1;

	private int mDefaltIndex = 0;

	private SDBottomNavigatorViewListener mListener = null;

	public SDBottomNavigatorViewListener getmListener()
	{
		return mListener;
	}

	public void setmListener(SDBottomNavigatorViewListener mListener)
	{
		this.mListener = mListener;
	}

	public SDBottomNavigatorView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		this.setOrientation(LinearLayout.HORIZONTAL);

	}

	public void setItems(SDBottomNavigatorBaseItem[] items)
	{
		if (items != null && items.length > 0)
		{
			mItems = items;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
			for (int i = 0; i < mItems.length; i++)
			{
				mItems[i].setId(i);
				mItems[i].setOnClickListener(new SDBottomNavigatorView_listener());
				mItems[i].setSelectedState(false);
				addView(mItems[i], params);
			}
		}
	}

	class SDBottomNavigatorView_listener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			setSelectIndex(v.getId(), v, true);
		}

	}

	public boolean setSelectIndex(int index, View v, boolean notifyListener)
	{
		if (mItems != null && mItems.length > 0 && index < mItems.length)
		{
			if (index != mCurrentIndex)
			{
				mItems[index].setSelectedState(true);
				if (mCurrentIndex != -1)
				{
					mItems[mCurrentIndex].setSelectedState(false);
				}
				mCurrentIndex = index;
				if (mListener != null && notifyListener)
				{
					mListener.onItemClick(v, v.getId());
				}
				return true;
			}
		}
		return false;
	}

	public interface SDBottomNavigatorViewListener
	{
		public void onItemClick(View v, int index);
	}

}
