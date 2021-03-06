package net.xamous.bezierinterpolatordemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import net.xamous.bezierinterpolator.BezierInterpolator;




public class SampleActivity extends AppCompatActivity implements BezierView.OnCurveChangedListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    protected ImageView mActor;
    protected BezierView mBezierView;
    protected SeekBar mSeekBar;
    protected TextView mDurationText;
    protected TextView mParamText;

    protected SeekBar seekBarScaleStart;
    protected TextView textViewScaleStart;

    protected SeekBar seekBarScaleEnd;
    protected TextView textViewScaleEnd;

    protected SeekBar seekBarAlphaStart;
    protected TextView textViewAlphaStart;

    protected SeekBar seekBarAlphaEnd;
    protected TextView textViewAlphaEnd;

    //    protected ValueAnimator mActorAnim;
    protected Interpolator mInterpolator;
    protected long mDuration;

    protected float startScale;
    protected float endScale;

    protected float startAlpha;
    protected float endAlpha;

    protected final static float DEFAULT_X1 = 0;
    protected final static float DEFAULT_Y1 = .5f;
    protected final static float DEFAULT_X2 = .5f;
    protected final static float DEFAULT_Y2 = 1;
    protected final static int DEFAULT_DURATION = 500;

    protected final static int REPEAT_DELAY = 500;
    protected final static int MIN_DURATION = 100;
    protected final static int MAX_DURATION = 1000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mActor = (ImageView) findViewById(R.id.actor);
        mDurationText = (TextView) findViewById(R.id.duration);
        mParamText = (TextView) findViewById(R.id.parameters);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(this);
        mSeekBar.setMax(MAX_DURATION - MIN_DURATION);
        mSeekBar.setProgress(DEFAULT_DURATION - MIN_DURATION);

        textViewScaleStart = (TextView) findViewById(R.id.scaleStart);

        seekBarScaleStart = (SeekBar) findViewById(R.id.seekBarScaleStart);
        seekBarScaleStart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewScaleStart.setText("시작 크기 : " + progress + "%");
                startScale = (float)progress / 100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarScaleStart.setMax(200);
        seekBarScaleStart.setProgress(50);

        textViewScaleEnd = (TextView) findViewById(R.id.scaleEnd);

        seekBarScaleEnd = (SeekBar) findViewById(R.id.seekBarScaleEnd);
        seekBarScaleEnd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewScaleEnd.setText("종료 크기 : " + progress + "%");
                endScale = (float)progress / 100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarScaleEnd.setMax(200);
        seekBarScaleEnd.setProgress(150);

        textViewAlphaStart = (TextView) findViewById(R.id.alphaStart);

        seekBarAlphaStart = (SeekBar) findViewById(R.id.seekBarAlphaStart);
        seekBarAlphaStart.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAlphaStart.setText("시작 투명도 : " + progress + "%");
                startAlpha = (float)progress / 100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarAlphaStart.setMax(100);
        seekBarAlphaStart.setProgress(50);

        textViewAlphaEnd = (TextView) findViewById(R.id.alphaEnd);

        seekBarAlphaEnd = (SeekBar) findViewById(R.id.seekBarAlphaEnd);
        seekBarAlphaEnd.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAlphaEnd.setText("종료 투명도 : " + progress + "%");
                endAlpha = (float)progress / 100f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarAlphaEnd.setMax(100);
        seekBarAlphaEnd.setProgress(100);

        mBezierView = (BezierView) findViewById(R.id.bezierView);
        mBezierView.setCurve(DEFAULT_X1, DEFAULT_Y1, DEFAULT_X2, DEFAULT_Y2);
        mBezierView.setOnCurveChangedListener(this);

        onCurveChanged(DEFAULT_X1, DEFAULT_Y1, DEFAULT_X2, DEFAULT_Y2);

        int actorWidth = mActor.getDrawable().getIntrinsicWidth();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mActor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(mActor, "scaleX", startScale, endScale);
                scaleDownX.setInterpolator(mInterpolator);
                ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(mActor, "scaleY", startScale, endScale);
                scaleDownY.setInterpolator(mInterpolator);
                ObjectAnimator alpha = ObjectAnimator.ofFloat(mActor, "alpha", startAlpha, endAlpha);
                scaleDownY.setInterpolator(mInterpolator);

                ValueAnimator mIndicatorAnim = ObjectAnimator.ofFloat(mBezierView, "indicatorPos", 0, 1);
                mIndicatorAnim.setInterpolator(new LinearInterpolator());

                AnimatorSet mAnimatorSet = new AnimatorSet();
                mAnimatorSet.playTogether(scaleDownX, scaleDownY, alpha, mIndicatorAnim);
                mAnimatorSet.setDuration(mDuration);
                mAnimatorSet.start();
            }
        });

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBarScaleStart.setProgress(50);
                seekBarScaleEnd.setProgress(150);
                seekBarAlphaStart.setProgress(50);
                seekBarAlphaEnd.setProgress(100);
                mSeekBar.setProgress(DEFAULT_DURATION);
                mBezierView.setCurve(DEFAULT_X1, DEFAULT_Y1, DEFAULT_X2, DEFAULT_Y2);
            }
        });
    }

    @Override
    public void onCurveChanged(float x1, float y1, float x2, float y2) {
        mInterpolator = createInterpolator(x1, y1, x2, y2);
        mParamText.setText(String.format(getString(R.string.parameters), x1, y1, x2, y2));
    }

    private Interpolator createInterpolator(float x1, float y1, float x2, float y2) {
        return PathInterpolatorCompat.create(x1, y1, x2, y2);
    }

    private void setDuration(int duration) {
        mDuration = duration;
        mDurationText.setText(String.format(getString(R.string.duration), mDuration));
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setDuration(MIN_DURATION + progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mBezierView.usePathInterpolator(isChecked);
    }
}