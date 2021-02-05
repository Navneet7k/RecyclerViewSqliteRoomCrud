package zingmyorder.kitchen.mobile.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import zingmyorder.kitchen.mobile.R;

public class TextDrawable extends Drawable {
    private final int mIntrinsicSize;
    private final TextView mTextView;

    public TextDrawable(Context context, CharSequence text) {
        mIntrinsicSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DRAWABLE_SIZE,
                context.getResources().getDisplayMetrics());
        mTextView = createTextView(context, text);
        mTextView.setWidth(mIntrinsicSize);
        mTextView.setHeight(mIntrinsicSize);
        mTextView.measure(mIntrinsicSize, mIntrinsicSize);
        mTextView.layout(0, 0, mIntrinsicSize, mIntrinsicSize);
    }

    private TextView createTextView(Context context, CharSequence text) {
        TextView textView = new TextView(context);
//        textView.setId(View.generateViewId()); // API 17+
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.notif_green);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TEXT_SIZE);
        textView.setText(text);
        return textView;
    }

    public void setText(CharSequence text) {
        mTextView.setText(text);
        invalidateSelf();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mTextView.draw(canvas);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    @Override
    public int getIntrinsicWidth() {
        return mIntrinsicSize;
    }

    @Override
    public int getIntrinsicHeight() {
        return mIntrinsicSize;
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter filter) {
    }

    private static final int DRAWABLE_SIZE = 32; // device-independent pixels (DP)
    private static final int DEFAULT_TEXT_SIZE = 12; // device-independent pixels (DP)
}
