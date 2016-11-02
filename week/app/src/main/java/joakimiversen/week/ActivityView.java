package joakimiversen.week;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * TODO: document your custom view class.
 */
public class ActivityView extends ViewGroup {
    private String mString = ""; // TODO: use a default from R.string...
    private String mTime = ""; // TODO: use a default from R.color...

    public ActivityView(Context context) {
        super(context);
        init(null, 0);
    }

    public ActivityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ActivityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ActivityView, defStyle, 0);

        try {
            mString = a.getString(R.styleable.ActivityView_stringField);
            mTime = a.getString(R.styleable.ActivityView_timeField);
        } finally {
            a.recycle();
        }

        mString = getResources().getString(R.string.text_placeholder);
        mTime = getResources().getString(R.string.time_placeholder);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}
