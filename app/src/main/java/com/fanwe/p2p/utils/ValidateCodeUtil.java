package com.fanwe.p2p.utils;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ValidateCodeUtil
{

	private static final char[] CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private static final char[] CHARS_NUMBER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private static ValidateCodeUtil bpUtil;

	public static ValidateCodeUtil getInstance()
	{
		if (bpUtil == null)
			bpUtil = new ValidateCodeUtil();
		return bpUtil;
	}

	// width="60" height="30"
	// base_padding_left="5"
	// range_padding_left="10"
	// base_padding_top="15"
	// range_padding_top="10"
	// codeLength="4"
	// line_number="3"
	// font_size="20"

	// default settings
	private static final int DEFAULT_CODE_LENGTH = 4;
	private static final int DEFAULT_FONT_SIZE = 35;
	private static final int DEFAULT_LINE_NUMBER = 0;
	private static final int BASE_PADDING_LEFT = 5, RANGE_PADDING_LEFT = 10, BASE_PADDING_TOP = 15, RANGE_PADDING_TOP = 10;
	private static final int DEFAULT_WIDTH = 80, DEFAULT_HEIGHT = 40;

	// settings decided by the layout xml
	// canvas width and height
	private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;

	// random word space and pading_top
	private int base_padding_left = BASE_PADDING_LEFT, range_padding_left = RANGE_PADDING_LEFT, base_padding_top = BASE_PADDING_TOP, range_padding_top = RANGE_PADDING_TOP;

	// number of chars, lines; font size
	private int codeLength = DEFAULT_CODE_LENGTH, line_number = DEFAULT_LINE_NUMBER, font_size = DEFAULT_FONT_SIZE;

	// variables
	private String code;
	private int padding_left, padding_top;
	private Random random = new Random();

	public Bitmap createBitmap()
	{
		padding_left = 0;

		Bitmap bp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas c = new Canvas(bp);

		code = createCode();

		c.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setTextSize(font_size);

		for (int i = 0; i < code.length(); i++)
		{
			randomTextStyle(paint);
			randomPadding();
			c.drawText(code.charAt(i) + "", i * 18, 35, paint);
		}

		for (int i = 0; i < line_number; i++)
		{
			drawLine(c, paint);
		}

		c.save(Canvas.ALL_SAVE_FLAG);// 保存
		c.restore();//
		return bp;
	}

	public String getCode()
	{
		return code;
	}

	private String createCode()
	{
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < codeLength; i++)
		{
			buffer.append(CHARS_NUMBER[random.nextInt(CHARS_NUMBER.length)]);
		}
		return buffer.toString();
	}

	private void drawLine(Canvas canvas, Paint paint)
	{
		int color = randomColor();
		int startX = random.nextInt(width);
		int startY = random.nextInt(height);
		int stopX = random.nextInt(width);
		int stopY = random.nextInt(height);
		paint.setStrokeWidth(1);
		paint.setColor(color);
		canvas.drawLine(startX, startY, stopX, stopY, paint);
	}

	private int randomColor()
	{
		return randomColor(1);
	}

	private int randomColor(int rate)
	{
		int red = random.nextInt(256) / rate;
		int green = random.nextInt(256) / rate;
		int blue = random.nextInt(256) / rate;
		return Color.rgb(red, green, blue);
	}

	private void randomTextStyle(Paint paint)
	{
		int color = randomColor();
		paint.setColor(color);
		paint.setFakeBoldText(random.nextBoolean()); // true为粗体，false为非粗体
		// float skewX = random.nextInt(11) / 10;
		// skewX = random.nextBoolean() ? skewX : -skewX;
		// paint.setTextSkewX(skewX); // float类型参数，负数表示右斜，整数左斜
		// paint.setUnderlineText(true); //true为下划线，false为非下划线
		// paint.setStrikeThruText(true); //true为删除线，false为非删除线
	}

	private void randomPadding()
	{
		padding_left += base_padding_left + random.nextInt(range_padding_left) + 1;
		padding_top = base_padding_top + random.nextInt(range_padding_top);
	}
}