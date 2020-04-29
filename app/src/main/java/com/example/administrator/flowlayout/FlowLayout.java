package com.example.administrator.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Build;

import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    /**
     * 用来保存每行views的列表
     */
    private List<List<View>> mViewLinesList = new ArrayList<>();
    /**
     * 用来保存行高的列表
     */
    private List<Integer> mLineHeights = new ArrayList<>();

    private List<String> strings; //数据

    private OnItemClickListener onItemClickListener;

    private int lineSpae; //行间距
    private int leftMargin; //左间距
    private int rightMargin; //右间距
    private float corner; //圆角 单位为dp
    private int flowTextColor; //字体颜色
    private int flowTextBgColor; //背景颜色
    private float flowTextSize; //字体大小 单位为sp
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributeSet(context, attrs);
    }

    private void getAttributeSet(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        lineSpae = (int) array.getDimension(R.styleable.FlowLayout_lineSpace, Utils.dp2px(getContext(), 10));
        corner = (int) array.getDimension(R.styleable.FlowLayout_flowTextCorner, Utils.dp2px(getContext(), 5));
        leftMargin = (int) array.getDimension(R.styleable.FlowLayout_flowTextLeftMargin, Utils.dp2px(getContext(), 10));
        rightMargin = (int) array.getDimension(R.styleable.FlowLayout_flowTextRightMargin, Utils.dp2px(getContext(), 10));
        flowTextColor = array.getColor(R.styleable.FlowLayout_flowTextColor, Color.parseColor("#ff0000"));
        flowTextBgColor = array.getColor(R.styleable.FlowLayout_flowTextBgColor, Color.parseColor("#cfcfcf"));
        flowTextSize = array.getDimension(R.styleable.FlowLayout_flowTextSize, Utils.sp2px(getContext(), 12));
        paddingLeft = (int) array.getDimension(R.styleable.FlowLayout_flowTextPaddingLeft, Utils.dp2px(getContext(), 10));
        paddingRight = (int) array.getDimension(R.styleable.FlowLayout_flowTextPaddingRight, Utils.dp2px(getContext(), 10));
        paddingTop = (int) array.getDimension(R.styleable.FlowLayout_flowTextPaddingTop, Utils.dp2px(getContext(), 5));
        paddingBottom = (int) array.getDimension(R.styleable.FlowLayout_flowTextPaddingBottom, Utils.dp2px(getContext(), 5));
        array.recycle();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: 调用");

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);


        int measuredWith = 0; //flowLayout的最终测量宽度
        int measuredHeight = 0; //flowLayout的最终测量高度
        int currentLineWidth = 0; //当前行宽度
        int currentLineHeight = 0; //当前行高度
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            Log.d(TAG, "宽高确定");
            measuredWith = widthSpecSize;
            measuredHeight = heightSpecSize;
        } else {
            Log.d(TAG, "宽高不确定,重新测量");
            mViewLinesList.clear();
            mLineHeights.clear();
            int childWidth; //子view的宽度
            int childHeight; //子view的高度
            int childCount = getChildCount(); //flowLayout的子view个数
            List<View> viewList = new ArrayList<>(); //存放子view的集合
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                childWidth = childView.getMeasuredWidth() + layoutParams.leftMargin +
                        layoutParams.rightMargin;
                childHeight = childView.getMeasuredHeight() + layoutParams.topMargin +
                        layoutParams.bottomMargin;


                if (currentLineWidth + childWidth > widthSpecSize) { //需要换行
                    /**1、记录当前行的信息***/

                    //1、记录当前行的最大宽度，高度累加
                    measuredWith = Math.max(measuredWith, currentLineWidth);
                    measuredHeight = (int) (measuredHeight + currentLineHeight + lineSpae);
                    //2、将当前行的viewList添加至总的mViewsList，将行高添加至总的行高List
                    mViewLinesList.add(viewList);
                    mLineHeights.add(currentLineHeight);

                    /**2、记录新一行的信息***/
                    //1、重新赋值新一行的宽、高
                    currentLineWidth = childWidth;
                    currentLineHeight = childHeight;

                    // 2、新建一行的viewlist，添加新一行的view
                    viewList = new ArrayList<>();
                    viewList.add(childView);
                } else { //不需要换行
                    // 记录某行内的消息
                    //1、行内宽度的叠加、高度比较
                    currentLineWidth += childWidth;
                    currentLineHeight = Math.max(currentLineHeight, childHeight);

                    // 2、添加至当前行的viewList中
                    viewList.add(childView);
                }

                if (i == childCount - 1) { //如果是最后一个子view,不管需不需要换行,都需要把信息存储到集合中
                    //1、记录当前行的最大宽度，高度累加
                    measuredWith = Math.max(measuredWith, currentLineWidth);
                    measuredHeight = (int) (measuredHeight + currentLineHeight + lineSpae);

                    //2、将当前行的viewList添加至总的mViewsList，将行高添加至总的行高List
                    mViewLinesList.add(viewList);
                    mLineHeights.add(currentLineHeight);
                }
            }
        }
        if (getPaddingLeft() != 0 || getPaddingRight() != 0 || getPaddingTop() != 0 || getPaddingBottom() != 0) {
            //加上viewgroup的pading
            measuredWith = measuredWith + getPaddingLeft() + getPaddingRight();
            measuredHeight = measuredHeight + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(measuredWith, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "onLayout调用");
        int left, top, right, bottom;
        int curTop = getPaddingTop();
        int curLeft = getPaddingLeft();
        int lineCount = mViewLinesList.size();
        for (int i = 0; i < lineCount; i++) {
            List<View> viewList = mViewLinesList.get(i);
            int lineViewSize = viewList.size();
            for (int j = 0; j < lineViewSize; j++) {
                View childView = viewList.get(j);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

                left = curLeft + layoutParams.leftMargin;
                top = curTop + layoutParams.topMargin;
                right = left + childView.getMeasuredWidth();
                bottom = top + childView.getMeasuredHeight();
                childView.layout(left, top, right, bottom);
                curLeft += childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }
            curLeft = getPaddingLeft(); //重置左边距
            curTop += mLineHeights.get(i) + lineSpae;
        }
        mViewLinesList.clear();
        mLineHeights.clear();
    }

    public void setData(List<String> data) {
        this.strings = data;
        if (strings != null && !strings.isEmpty()) {
            for (int i = 0; i < strings.size(); i++) {
                FlowTextView flowTextView = new FlowTextView(getContext());
                flowTextView.setText(strings.get(i));
                flowTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, flowTextSize);
                flowTextView.setTextColor(flowTextColor);
                flowTextView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
                flowTextView.setCorner((int) corner);
                flowTextView.setBackgroundColor(flowTextBgColor);
                flowTextView.setIncludeFontPadding(false);
                flowTextView.setGravity(Gravity.CENTER);
                final int finalI = i;
                flowTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(v, finalI);
                        }
                    }
                });
                MarginLayoutParams marginLayoutParams = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
                marginLayoutParams.setMargins(leftMargin, 0, rightMargin, 0);
                addView(flowTextView, marginLayoutParams);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int index);
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
