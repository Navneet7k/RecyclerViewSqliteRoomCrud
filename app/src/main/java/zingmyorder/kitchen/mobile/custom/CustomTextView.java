package zingmyorder.kitchen.mobile.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import zingmyorder.kitchen.mobile.R;


public class CustomTextView extends AppCompatTextView {

    String defaultFont = "hevetica.ttf";


    public CustomTextView(Context context) {
        super(context);
        initAttributes(null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);
    }

    public void initAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString(R.styleable.CustomTextView_tv_fontface);
            try {
                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                    setTypeface(myTypeface);
                }
                else{
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + defaultFont);
                    setTypeface(myTypeface);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            a.recycle();

        }

    }

}
