package com.zxs.ptrmenulistview;

import com.lishate.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgress extends View{

	private Paint mRingPaint;
	private Paint mRingBgPaint;
	private Paint mDescTextPaint;
	
	private int mRingColor;
	private int mRingBgColor;
	// 半径
	private float mRadius;
	// 圆环半径
	private float mRingRadius;
	// 圆环宽度
	private float mStrokeWidth;
	// 圆心x坐标
	private int mXCenter;
	// 圆心y坐标
	private int mYCenter;
	// 总进度
	private int mTotalProgress = 100;
	// 当前进度
	private float mProgress;
	private String mTitle;
	private int mDescTextColor;
	private float mOuterStrokeWidth;
	private float mOuterRingRadius;
	
	
	public CircleProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttrs(context, attrs);
		initVariable();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,R.styleable.GZSportTasksView, 0, 0);
		
		mRadius = typeArray.getDimension(R.styleable.GZSportTasksView_gz_sport_radius, 144);
		mOuterStrokeWidth = typeArray.getDimension(R.styleable.GZSportTasksView_gz_sport_outerStrokeWidth, 24);
		mStrokeWidth = typeArray.getDimension(R.styleable.GZSportTasksView_gz_sport_strokeWidth, 8);
		
		mRingBgColor = typeArray.getColor(R.styleable.GZSportTasksView_gz_sport_ringBgColor, 0xfff2f2f2);
		mRingColor = typeArray.getColor(R.styleable.GZSportTasksView_gz_sport_ringColor, 0xff3998ed);
		
		mDescTextColor = typeArray.getColor(R.styleable.GZSportTasksView_gz_sport_descTextColor, 0xff3998ed);
		
		mRingRadius = (mRadius + mOuterStrokeWidth) - mStrokeWidth / 2;
		mOuterRingRadius = mRadius + mOuterStrokeWidth / 2;
	}

	private void initVariable() {                		
		mRingBgPaint = new Paint();
		mRingBgPaint.setAntiAlias(true);
		mRingBgPaint.setColor(mRingBgColor);
		mRingBgPaint.setStyle(Paint.Style.STROKE);
		mRingBgPaint.setStrokeWidth(mOuterStrokeWidth);
		
		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);
		
		mDescTextPaint = new Paint();
		mDescTextPaint.setAntiAlias(true);
		mDescTextPaint.setStyle(Paint.Style.FILL);
		mDescTextPaint.setTextAlign(Paint.Align.CENTER);
		mDescTextPaint.setColor(mDescTextColor);
		mDescTextPaint.setTextSize(mRadius / 3);
		
		setTitle(getResources().getString(R.string.config_title));
		setProgress(0);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;
		
		canvas.drawCircle(mXCenter, mYCenter, mOuterRingRadius, mRingBgPaint);
		
		if (mProgress >= 0 ) {
			RectF oval = new RectF();
			oval.left = (mXCenter - mRingRadius);
			oval.top = (mYCenter - mRingRadius);
			oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
			oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
			canvas.drawArc(oval, -90, mProgress * 360 / mTotalProgress , false, mRingPaint);
		}
		
		if(mTitle != null) {
			FontMetrics fm = mDescTextPaint.getFontMetrics();
			float mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
			canvas.drawText(mTitle, mXCenter, mYCenter + mTxtHeight / 4, mDescTextPaint);
		}
	}
	
	public void setProgress(float progress) {
		mProgress = progress;
		postInvalidate();
	}
	
	public void setTitle(String title){
		mTitle = title;
		postInvalidate();
	}
	
}
