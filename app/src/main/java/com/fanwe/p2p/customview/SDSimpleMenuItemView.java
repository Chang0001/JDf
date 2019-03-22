package com.fanwe.p2p.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.p2p.R;
import com.fanwe.p2p.R.color;

public class SDSimpleMenuItemView extends LinearLayout
{

	private View mView = null;
	private ImageView mImgTitle = null;
	private TextView mTxtTitle = null;

	private int mColorTitleTextSelect = getResources().getColor(color.text_menu_select);
	private int mColorTitleTextNormal = getResources().getColor(color.text_menu_normal);

	private int mImageTitleNormalId = 0;
	private int mImageTitleSelectId = 0;

	private int mBackgroundImageSelectId = R.drawable.bg_menu_item_select;
	private int mBackgroundImageNormalId = R.drawable.bg_transparent;

	private int mSizeTitleText = 0;

	private boolean mSelected = false;

	public boolean ismSelected()
	{
		return mSelected;
	}

	public int getmColorTitleTextSelect()
	{
		return mColorTitleTextSelect;
	}

	public void setmColorTitleTextSelect(int mColorTitleTextSelect)
	{
		this.mColorTitleTextSelect = mColorTitleTextSelect;
	}

	public int getmColorTitleTextNormal()
	{
		return mColorTitleTextNormal;
	}

	public void setmColorTitleTextNormal(int mColorTitleTextNormal)
	{
		this.mColorTitleTextNormal = mColorTitleTextNormal;
	}

	public int getmImageTitleNormalId()
	{
		return mImageTitleNormalId;
	}

	public void setmImageTitleNormalId(int mImageTitleNormalId)
	{
		this.mImageTitleNormalId = mImageTitleNormalId;
	}

	public int getmImageTitleSelectId()
	{
		return mImageTitleSelectId;
	}

	public void setmImageTitleSelectId(int mImageTitleSelectId)
	{
		this.mImageTitleSelectId = mImageTitleSelectId;
	}

	public int getmBackgroundImageSelectId()
	{
		return mBackgroundImageSelectId;
	}

	public void setmBackgroundImageSelectId(int mBackgroundImageSelectId)
	{
		this.mBackgroundImageSelectId = mBackgroundImageSelectId;
	}

	public int getmBackgroundImageNormalId()
	{
		return mBackgroundImageNormalId;
	}

	public void setmBackgroundImageNormalId(int mBackgroundImageNormalId)
	{
		this.mBackgroundImageNormalId = mBackgroundImageNormalId;
	}

	public int getmSizeTitleText()
	{
		return mSizeTitleText;
	}

	public void setmSizeTitleText(int mSizeTitleText)
	{
		this.mSizeTitleText = mSizeTitleText;
		this.mTxtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSizeTitleText);
	}

	public SDSimpleMenuItemView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		mView = LayoutInflater.from(getContext()).inflate(R.layout.view_simple_menu_item, null);
		mImgTitle = (ImageView) mView.findViewById(R.id.view_simple_menu_item_img_title);
		mTxtTitle = (TextView) mView.findViewById(R.id.view_simple_menu_item_txt_title);
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		mTxtTitle.setTextColor(mColorTitleTextNormal);
		mImgTitle.setImageResource(mImageTitleNormalId);
		if (mSizeTitleText != 0)
		{
			mTxtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, mSizeTitleText);
		}

		mView.setBackgroundResource(mBackgroundImageNormalId);

		this.addView(mView, params);
	}

	public void setTitleText(String text)
	{
		if (mTxtTitle != null && text != null)
		{
			mTxtTitle.setText(text);
		}
	}

	public void setSelectState(boolean select)
	{
		if (select)
		{
			if (mImageTitleSelectId != 0) // 设置菜单图标
			{
				mImgTitle.setImageResource(mImageTitleSelectId);
			}
			if (mColorTitleTextSelect != 0) // 设置菜单文字
			{
				mTxtTitle.setTextColor(mColorTitleTextSelect);
			}
			if (mBackgroundImageSelectId != 0) // 设置背景
			{
				mView.setBackgroundResource(mBackgroundImageSelectId);
			}

		} else
		{
			if (mImageTitleNormalId != 0) // 设置菜单图标
			{
				mImgTitle.setImageResource(mImageTitleNormalId);
			}
			if (mColorTitleTextNormal != 0) // 设置菜单文字
			{
				mTxtTitle.setTextColor(mColorTitleTextNormal);
			}
			if (mBackgroundImageNormalId != 0) // 设置背景
			{
				mView.setBackgroundResource(mBackgroundImageNormalId);
			}
		}
		mSelected = select;

	}

	public void toggleState()
	{
		setSelectState(mSelected);
	}

}
