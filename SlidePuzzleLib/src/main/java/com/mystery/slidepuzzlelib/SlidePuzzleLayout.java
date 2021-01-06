package com.mystery.slidepuzzlelib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.Random;

@SuppressLint("ClickableViewAccessibility,Recycle")
@RequiresApi(api = Build.VERSION_CODES.O)
public class SlidePuzzleLayout extends ConstraintLayout implements View.OnClickListener, View.OnTouchListener {

    private Context mContext;

    private ImageView puzzleLeftItemView;
    private ImageView puzzleRightItemView;
    private ImageView puzzleMiddleItemView;
    private ImageView puzzleUnkView;

    private int downX, downY;
    private int screenHeight;
    private int screenWidth;
    private int checkL, checkM, checkR;
    private boolean LDone, RDone, MDone;

    private final int[] puzzleViews = {
            R.drawable.ic_puzzle_01, R.drawable.ic_puzzle_02,
            R.drawable.ic_puzzle_03, R.drawable.ic_puzzle_04,
            R.drawable.ic_puzzle_05, R.drawable.ic_puzzle_06,
            R.drawable.ic_puzzle_07, R.drawable.ic_puzzle_08};
    private final int[][] puzzleRow = {{4, 1, 6}, {2, 3, 0}, {4, 5, 0}, {2, 7, 6}};
    private final int[][] puzzleColumn = {{0, 1, 2}, {0, 2, 1}, {1, 2, 0}, {1, 0, 2}, {2, 0, 1}, {2, 1, 0}};
    private int puzzleItemColor;

    public SlidePuzzleLayout(@NonNull Context context) {
        super(context);
    }

    public SlidePuzzleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SlidePuzzleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.slide_puzzle_layout, this);
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,R.styleable.SlidePuzzleLayout);
        puzzleItemColor = typedArray.getColor(R.styleable.SlidePuzzleLayout_puzzleColor,getResources().getColor(R.color.teal_200));
        puzzleLeftItemView = findViewById(R.id.puzzle_l_iv);
        puzzleMiddleItemView = findViewById(R.id.puzzle_m_iv);
        puzzleRightItemView = findViewById(R.id.puzzle_r_iv);
        puzzleUnkView = findViewById(R.id.puzzle_un_iv);
        findViewById(R.id.puzzle_check_tv).setOnClickListener(this);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity(mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        setNewPuzzle();
    }

    @Override
    public void onClick(View v) {
        CheckPuzzle();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) motionEvent.getRawX();
                downY = (int) motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int newX = (int) motionEvent.getRawX();
                int newY = (int) motionEvent.getRawY();
                int dx = newX - downX;
                int dy = newY - downY;
                int top = view.getTop();
                int bottom = view.getBottom();
                int left = view.getLeft();
                int right = view.getRight();
                int newTop = top + dy;
                int newBottom = bottom + dy;
                int newLeft = left + dx;
                int newRight = right + dx;
                if ((newLeft < 0) || (newRight < 0)
                        || (newRight > screenWidth)
                        || (newBottom > screenHeight)) {
                    break;
                }
                view.layout(newLeft, newTop, newRight, newBottom);
                downX = (int) motionEvent.getRawX();
                downY = (int) motionEvent.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                checkAnim(view);
                break;
        }
        return true;
    }

    /*检查拼图对应关系是否正确*/
    private void checkAnim(final View view) {
        int puzzleItemTo = 0;
        int toTop, toLeft;
        int length = view.getWidth();
        int id = view.getId();
        if (id == R.id.puzzle_l_iv) {
            puzzleItemTo = checkTo(checkL);
        } else if (id == R.id.puzzle_m_iv) {
            puzzleItemTo = checkTo(checkM);
        } else if (id == R.id.puzzle_r_iv) {
            puzzleItemTo = checkTo(checkR);
        }
        if (puzzleItemTo == 0) {
            //目的地为左
            toTop = puzzleUnkView.getTop();
            toLeft = puzzleUnkView.getLeft();
            puzzleCheckAnim(view, toTop, toLeft);
        } else if (puzzleItemTo == 1) {
            //目的地为中
            toTop = puzzleUnkView.getTop();
            toLeft = puzzleUnkView.getLeft() + length * 2 / 3;
            puzzleCheckAnim(view, toTop, toLeft);
        } else if (puzzleItemTo == 2) {
            //目的地为右
            toTop = puzzleUnkView.getTop();
            toLeft = puzzleUnkView.getLeft() + length * 4 / 3;
            puzzleCheckAnim(view, toTop, toLeft);
        }
    }

    /*拼图贴合动画*/
    private void puzzleCheckAnim(View view, int toTop, int toLeft) {
        int dy = toTop - view.getTop();
        int dx = toLeft - view.getLeft();
        if (Math.abs(dy) < 100 && Math.abs(dx) < 100) {
            final TranslateAnimation translateAnimation = new TranslateAnimation(0, dx, 0, dy);
            translateAnimation.setDuration(618);
            translateAnimation.setFillAfter(true);
            translateAnimation.setInterpolator(new BounceInterpolator());
            getActivity(mContext).runOnUiThread(() -> view.startAnimation(translateAnimation));
            int viewId = view.getId();
            if (viewId == R.id.puzzle_l_iv) {
                LDone = true;
                puzzleLeftItemView.setOnTouchListener(null);
            } else if (viewId == R.id.puzzle_m_iv) {
                MDone = true;
                puzzleMiddleItemView.setOnTouchListener(null);
            } else if (viewId == R.id.puzzle_r_iv) {
                RDone = true;
                puzzleRightItemView.setOnTouchListener(null);
            }
        }
    }

    /*判断方块去向*/
    private int checkTo(int check) {
        if (check == 2 || check == 4) {
            return 0;
        } else if (check == 0 || check == 6) {
            return 2;
        } else {
            return 1;
        }
    }

    /*发送信息,检查拼图*/
    public void CheckPuzzle() {
        if (LDone && RDone && MDone) {
            LDone = false;
            RDone = false;
            MDone = false;
            new Thread(() -> getActivity(mContext).runOnUiThread(this::setNewPuzzle)).start();
        }
    }

    /*设置新的拼图*/
    private void setNewPuzzle() {
        puzzleLeftItemView.setOnTouchListener(this);
        puzzleMiddleItemView.setOnTouchListener(this);
        puzzleRightItemView.setOnTouchListener(this);
        puzzleRightItemView.setVisibility(View.GONE);
        puzzleMiddleItemView.setVisibility(View.GONE);
        puzzleLeftItemView.setVisibility(View.GONE);
        int puzzleRandomRow = new Random().nextInt(4);
        int puzzleRandomColumn = new Random().nextInt(6);
        checkL = puzzleRow[puzzleRandomRow][puzzleColumn[puzzleRandomColumn][0]];
        checkM = puzzleRow[puzzleRandomRow][puzzleColumn[puzzleRandomColumn][1]];
        checkR = puzzleRow[puzzleRandomRow][puzzleColumn[puzzleRandomColumn][2]];
        Drawable drawableLeft = ContextCompat.getDrawable(mContext,puzzleViews[checkL]);
        Drawable drawableMiddle = ContextCompat.getDrawable(mContext,puzzleViews[checkM]);
        Drawable drawableRight = ContextCompat.getDrawable(mContext,puzzleViews[checkR]);
        drawableLeft.setTint(puzzleItemColor);
        drawableMiddle.setTint(puzzleItemColor);
        drawableRight.setTint(puzzleItemColor);
        puzzleLeftItemView.setImageDrawable(drawableLeft);
        puzzleMiddleItemView.setImageDrawable(drawableMiddle);
        puzzleRightItemView.setImageDrawable(drawableRight);
        puzzleRightItemView.setVisibility(View.VISIBLE);
        puzzleMiddleItemView.setVisibility(View.VISIBLE);
        puzzleLeftItemView.setVisibility(View.VISIBLE);
        Animation translate = AnimationUtils.loadAnimation(mContext, R.anim.translate_anim);
        translate.setInterpolator(new BounceInterpolator());
        translate.setFillAfter(true);
        puzzleRightItemView.startAnimation(translate);
        puzzleLeftItemView.startAnimation(translate);
        puzzleMiddleItemView.startAnimation(translate);
    }

    /*由Context获取Activity*/
    private Activity getActivity(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }
}
