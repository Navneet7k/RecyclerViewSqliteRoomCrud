package zingmyorder.kitchen.mobile.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import zingmyorder.kitchen.mobile.R;


public class MyCustomButton extends AppCompatButton {

    String defaultFont = "hevetica.ttf";

    public MyCustomButton(Context context) {
        super(context);
        initAttributes(null);
    }

    public MyCustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
    }

    public MyCustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);
    }

    public void initAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MyCustomButton);
            String fontName = a.getString(R.styleable.MyCustomButton_fontface);
            try {
                if (fontName != null) {
                    Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                    setTypeface(myTypeface);
                } else {
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
