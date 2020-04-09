package com.henry.newdemo.customer.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henry on 2020/3/30.
 *
 * @author henry.zsf
 * @version 10.0.0
 */
public class TagLayout extends ViewGroup {
    private final String TAG = TagLayout.class.getSimpleName();

    private List<Rect> mRectList = new ArrayList<>();

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 父容器可用宽度，用于计算当前这行是否排的下
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        // 父容器可用高度
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 父容器最终行高，根据子容器排版情况计算得到
        int height = 0;
        // 当前这一行已使用的宽度
        int currentLineLeft = 0;
        // 当前这行的起始高度
        int currentLineTop = 0;
        // 占用宽度最多的一行值，作为容器的宽度
        int maxLineWidth = 0;
        // 占用高度最多的一行值，作为这一行的高度
        int maxLineHeight = 0;

        Log.d(TAG, "onMeasure, parentWidth=" + parentWidth + ",parentHeight=" + parentHeight);

        for (int i = 0; i < getChildCount(); i++) {
            // 测量子 view 数据
            View childView = getChildAt(i);
            String city = "empty";
            if (childView instanceof TextView) {
                TextView textView = (TextView) childView;
                city = (String) textView.getText();
            }
            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, currentLineTop);
            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            Log.d(TAG, "begin onMeasure, index=" + i
                    + ", childWidth=" + childWidth
                    + ", childHeight=" + childHeight
                    + ", currentLineLeft=" + currentLineLeft
                    + ", currentLineTop=" + currentLineTop
                    + ", city=" + city);

            // 排不下，需要换行
            if (specMode != MeasureSpec.UNSPECIFIED
                    && childWidth + currentLineLeft > parentWidth) {
                // 重置 left 值
                currentLineLeft = 0;
                // 计算新一行的 top 值(原值+上一行最大组件高度)
                currentLineTop += maxLineHeight;
                // 重置当前行最大值
                maxLineHeight = 0;
                Log.d(TAG, "change line, new line top=" + currentLineTop
                        + ",maxLineHeight=" + maxLineHeight
                        + ", city=" + city);
            }

            // 可以排在这一行
            Rect rect;
            if (i < mRectList.size()) {
                rect = mRectList.get(i);
            } else {
                rect = new Rect();
                mRectList.add(rect);
            }
            Log.d(TAG, "set rect index=" + i
                    + ", left=" + currentLineLeft
                    + ", top=" + currentLineTop
                    + ", right=" + (currentLineLeft + childWidth)
                    + ", bottom=" + (currentLineTop + childHeight)
                    + ", city=" + city);
            // 保存子 view 上下左右顶点坐标
            rect.set(currentLineLeft, currentLineTop, currentLineLeft + childWidth, currentLineTop + childHeight);

            // 使用的宽度增加
            currentLineLeft += childWidth;
            // 获取最宽的一行，最后作为父容器的宽度
            maxLineWidth = Math.max(currentLineLeft, maxLineWidth);
            // 保存当前行最高组件高度，最后作为这一行的高度
            maxLineHeight = Math.max(childHeight, maxLineHeight);

            Log.d(TAG, "one item end onMeasure"
                    + ", maxLineWidth=" + maxLineWidth
                    + ", maxLineHeight=" + maxLineHeight
                    + ", city=" + city);
        }

        // 增加
        height = currentLineTop + maxLineHeight;
        Log.d(TAG, "total end onMeasure"
                + ", maxLineWidth=" + maxLineWidth
                + ", height=" + height);

        // 设置容器
        setMeasuredDimension(maxLineWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            Rect rect = mRectList.get(i);
            if (rect != null) {
                childView.layout(rect.left, rect.top, rect.right, rect.bottom);
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
