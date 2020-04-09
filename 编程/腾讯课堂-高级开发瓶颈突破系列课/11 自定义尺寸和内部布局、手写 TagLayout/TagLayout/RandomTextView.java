package com.henry.newdemo.customer.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Random;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by henry on 2020/4/1.
 *
 * @author henry.zsf
 * @version 10.0.0
 */
public class RandomTextView extends AppCompatTextView {
    private int[] mBgColorArray = new int[]{0xFF00FF00, 0xFFFF0000, 0xFF0000FF, 0xFF800000, 0xFF008080, 0xFF00FFFF};

    public RandomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // 文字颜色
        setTextColor(getResources().getColor(android.R.color.white));

        // padding
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float density = displayMetrics.density;
        setPadding((int) (density * 10), (int) (density * 10), (int) (density * 10), (int) (density * 10));

        // 背景色
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(mBgColorArray[new Random().nextInt(mBgColorArray.length)]);
        bg.setCornerRadius(density * 8);
        setBackgroundDrawable(bg);

        // 字号
        Random random = new Random();
        int size = random.nextInt(30) + 10;
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }
}
