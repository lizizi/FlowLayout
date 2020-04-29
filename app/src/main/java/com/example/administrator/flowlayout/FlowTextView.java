package com.example.administrator.flowlayout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;

public class FlowTextView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "FlowText";
    private int corner; //圆角 单位为dp
    public FlowTextView(Context context) {
        this(context, null);
    }

    public FlowTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //获取已设置的背景色
    private int getBgColor() {
        int bgColor = 0;
        Drawable drawable = getBackground();
        if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            bgColor = colorDrawable.getColor();
        }
        return bgColor;
    }

    //重新设置带圆角的背景色
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setBgColorWithCorner(int bgColor) {
        if (bgColor == 0) {
            Log.d(TAG, "setBgColorWithCorner: 背景颜色为透明色");
        }
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(bgColor);
        drawable.setCornerRadius(corner);
        setBackground(drawable);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setBgColorWithCorner(getBgColor());
    }

    public int getCorner() {
        return corner;
    }

    public void setCorner(int corner) {
        this.corner = corner;
    }
}
