package app.com.customviews.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by thrymr on 18/5/16.
 */
public class CustomImageView extends AppCompatImageView {
    /**
     * Instantiates a new Custom image view.
     *
     * @param context the context
     */
    public CustomImageView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Custom image view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Custom image view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}