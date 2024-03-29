package com.fanwe.p2p.customview;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.fanwe.p2p.R;
import com.fanwe.p2p.R.color;

/**
 * ��iphone���ȵĽ�������̰߳�ȫ��View����ֱ�����߳��и��½��
 * 
 * @author xiaanming
 * 
 */
public class RoundProgressBar extends View
{
	private DecimalFormat mFormat = new DecimalFormat("#0.00");

	/**
	 * ���ʶ��������
	 */
	private Paint paint;

	/**
	 * Բ������ɫ
	 */
	private int roundColor;

	/**
	 * Բ����ȵ���ɫ
	 */
	private int roundProgressColor;

	/**
	 * �м��Ȱٷֱȵ��ַ����ɫ
	 */
	private int textColor;

	/**
	 * �м��Ȱٷֱȵ��ַ������
	 */
	private float textSize;

	/**
	 * Բ���Ŀ��
	 */
	private float roundWidth;

	/**
	 * �����
	 */
	private float max;

	/**
	 * ��ǰ���
	 */
	private float progress;
	/**
	 * �Ƿ���ʾ�м�Ľ��
	 */
	private boolean textIsDisplayable = true;

	/**
	 * ��ȵķ��ʵ�Ļ��߿���
	 */
	private int style;

	public static final int STROKE = 0;
	public static final int FILL = 1;

	public RoundProgressBar(Context context)
	{
		this(context, null);
	}

	public RoundProgressBar(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		paint = new Paint();

		// TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
		// R.styleable.RoundProgressBar);

		// ��ȡ�Զ������Ժ�Ĭ��ֵ
		// roundColor =
		// mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor,
		// Color.RED);
		// roundProgressColor =
		// mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor,
		// Color.GREEN);
		// textColor =
		// mTypedArray.getColor(R.styleable.RoundProgressBar_textColor,
		// Color.GREEN);
		// textSize =
		// mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
		// roundWidth =
		// mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
		// max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		// textIsDisplayable =
		// mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable,
		// true);
		// style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);
		//
		// mTypedArray.recycle();

		style = STROKE;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		/**
		 * �������Ĵ�Բ��
		 */
		int centre = getWidth() / 2; // ��ȡԲ�ĵ�x���
		int radius = (int) (centre - roundWidth / 2); // Բ���İ뾶
		paint.setColor(roundColor); // ����Բ������ɫ
		paint.setStyle(Paint.Style.STROKE); // ���ÿ���

		paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
		paint.setAntiAlias(true); // �����
		canvas.drawCircle(centre, centre, radius, paint); // ����Բ��

		/**
		 * ����Ȱٷֱ�
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); // ��������
		double percent = (progress / max) * 100; // �м�Ľ�Ȱٷֱȣ���ת����float�ڽ��г����㣬��Ȼ��Ϊ0
		String strPercent = mFormat.format(percent) + "%";

		float textWidth = paint.measureText(strPercent); // ���������ȣ�������Ҫ�������Ŀ��������Բ���м�

		if (textIsDisplayable && style == STROKE)
		{
			canvas.drawText(strPercent, centre - textWidth / 2, centre + textSize / 2, paint); // ������Ȱٷֱ�
		}

		/**
		 * ��Բ�� ����Բ���Ľ��
		 */

		// ���ý����ʵ�Ļ��ǿ���
		paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
		paint.setColor(roundProgressColor); // ���ý�ȵ���ɫ
		RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���

		switch (style)
		{
		case STROKE:
		{
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 270, 360 * progress / max, false, paint); // ��ݽ�Ȼ�Բ��
			break;
		}
		case FILL:
		{
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (progress != 0)
				canvas.drawArc(oval, 270, 360 * progress / max, true, paint); // ��ݽ�Ȼ�Բ��
			break;
		}
		}

	}

	public synchronized float getMax()
	{
		return max;
	}

	/**
	 * ���ý�ȵ����ֵ
	 * 
	 * @param max
	 */
	public synchronized void setMax(int max)
	{
		if (max < 0)
		{
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * ��ȡ���.��Ҫͬ��
	 * 
	 * @return
	 */
	public synchronized float getProgress()
	{
		return progress;
	}

	/**
	 * ���ý�ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ��
	 * ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(float progress)
	{
		if (progress < 0)
		{
			throw new IllegalArgumentException("progress not less than 0");
		}
		if (progress > max)
		{
			progress = max;
		}
		if (progress <= max)
		{
			this.progress = progress;
			postInvalidate();
		}

	}

	public int getCricleColor()
	{
		return roundColor;
	}

	public void setCricleColor(int cricleColor)
	{
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor()
	{
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor)
	{
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor()
	{
		return textColor;
	}

	public void setTextColor(int textColor)
	{
		this.textColor = textColor;
	}

	public float getTextSize()
	{
		return textSize;
	}

	public void setTextSize(float textSize)
	{
//		textSize = SDUIUtil.dp2px(getContext(), textSize);
		this.textSize = textSize;
	}

	public float getRoundWidth()
	{
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth)
	{
//		roundWidth = SDUIUtil.dp2px(getContext(), roundWidth);
		this.roundWidth = roundWidth;
	}

	public void setDefaultConfig()
	{
		setCricleColor(getResources().getColor(color.bg_pgb_round_normal));
		setMax(100);
		setRoundWidth(getResources().getDimension(R.dimen.width_pgb_round));
		setProgress(0);
		setTextColor(getResources().getColor(color.text_black));
		setTextSize(getResources().getDimension(R.dimen.text_pgb_round));
		setCricleProgressColor(getResources().getColor(color.bg_pgb_round_progress_orange));
	}

}
