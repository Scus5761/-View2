package com.example.scus.selfdrawbreathview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Scus on 2017/10/15.
 */

public class BreathView extends View implements ValueAnimator.AnimatorUpdateListener {

    private TypedArray typedArray;
    private int mDuration=2000;
    private int mCoreColor;
    private int mDiffusionColor;
    private float mCoreRadius = 30f;
    private float mOuterRadius = 40f;
    private int mInnerRadius;
    private int mInnerColor;
    private Paint mPaint;
    private ValueAnimator mAnimator;
    private Handler mHandler;
    private float circleX;
    private float circleY;
    private long Heart_BEAT_RATE = 3000;
    private boolean mIsDiffuse = false;
    private int color = 255;
    private float mFraction;
    private float mOuterFraction;


    public BreathView(Context context) {
        this(context, null);
    }

    public BreathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.BreathView);
        if (typedArray != null) {
            mDuration = typedArray.getInt(R.styleable.BreathView_duration, 2000);
            mCoreColor = typedArray.getColor(R.styleable.BreathView_coreColor, 0);
            mDiffusionColor = typedArray.getColor(R.styleable.BreathView_diffuseColor, 0);
            mCoreRadius = typedArray.getInt(R.styleable.BreathView_coreRadius, 40);
            mOuterRadius = typedArray.getInt(R.styleable.BreathView_outerRadius, 40);
            mInnerRadius = typedArray.getInt(R.styleable.BreathView_innerRadius, 40);
            mInnerColor = typedArray.getColor(R.styleable.BreathView_innerColor, 0);
            typedArray.recycle();
        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mAnimator = ValueAnimator.ofFloat(0, 1f).setDuration(mDuration);
        mAnimator.addUpdateListener(this);
        if (null == mHandler) {
            mHandler = new Handler();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        circleX = w / 2;
        circleY = h / 2;
    }

    public BreathView setmCoreRadius(int mCoreRadius) {
        this.mCoreRadius = mCoreRadius;
        return this;
    }

    public BreathView setOuterRadius(int outerRadius) {
        this.mOuterRadius = outerRadius;
        return this;
    }

    public BreathView setCoordinate(float x, float y) {
        circleX = x;
        circleY = y;
        return this;
    }

    public BreathView setmDiffusionColor(int mDiffusionColor) {
        this.mDiffusionColor = mDiffusionColor;
        return this;
    }

    public BreathView setmCoreColor(int mCoreColor) {
        this.mCoreColor = mCoreColor;
        return this;
    }

    public BreathView setInterval(long interval) {
        Heart_BEAT_RATE = interval;
        return this;
    }

    @Override
    public void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus()) {
            invalidate();
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            connect();
            mHandler.postDelayed(this, Heart_BEAT_RATE);
        }
    };

    public BreathView onStart() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
        return this;
    }

    public void onStop() {
        mIsDiffuse = false;
        mHandler.removeCallbacks(mRunnable);
    }

    private void connect() {
        mIsDiffuse = true;
        mAnimator.start();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mIsDiffuse) {
//            绘制最外层扩散圆
            mPaint.setColor(mInnerColor);
            mPaint.setAlpha((int) (color - color * mFraction));
            canvas.drawCircle(circleX, circleY, mCoreRadius + mOuterRadius * mFraction + mInnerRadius * mFraction, mPaint);
//            绘制扩散圆
            mPaint.setColor(mDiffusionColor);
            mPaint.setAlpha((int) (color - color * mFraction));
            canvas.drawCircle(circleX, circleY, mCoreRadius + mOuterRadius * mFraction, mPaint);
//            绘制中心圆
            mPaint.setAntiAlias(true);
            mPaint.setColor(mCoreColor);
            mPaint.setAlpha(255);
            canvas.drawCircle(circleX, circleY, mCoreRadius, mPaint);
        }
        invalidate();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mFraction = (float) animation.getAnimatedValue();
        mOuterFraction = (float) (mFraction * 1.5);
    }
}
