package app.com.customviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import app.com.customviews.R;

public class StyledTextView extends AppCompatTextView {

    private int borderCol, textCol;
    private String text;
    private Paint rectPaint;

    public StyledTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public StyledTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StyledTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        rectPaint = new Paint();
        if(attrs != null) {
            try {

                TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StyledTextView);
                Typeface mTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + a.getString(R.styleable.StyledTextView_textViewFont));
                setTypeface(mTypeFace);
                setBackground(a.getDrawable(R.styleable.StyledTextView_drawableBackground));
                setTextColor(a.getColor(R.styleable.StyledTextView_textCol, 0));
                setText(a.getString(R.styleable.StyledTextView_text));

            } catch (Exception e) {
                e.printStackTrace();
            }



            /*borderCol = a.getColor(R.styleable.StyledTextView_borderCol, 0);
            text = a.getString(R.styleable.StyledTextView_text);
            textCol = a.getColor(R.styleable.StyledTextView_textCol, 0);*/
        }
    }

    /*public void setBorderCol(int borderCol) {
        this.borderCol = borderCol;
        invalidate();
        requestLayout();
    }

    public void setTextCol(int textCol) {
        this.textCol = textCol;
        invalidate();
        requestLayout();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
        requestLayout();
    }*/

    /*@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth() / 2;
        int height = getMeasuredHeight() / 2;


        rectPaint.setColor(borderCol);
        rectPaint.setStrokeWidth(4);
        rectPaint.setStyle(Paint.Style.STROKE);
        //get dimensions for rect
        canvas.drawRect(canvas.getHeight(), canvas.getWidth(), canvas.getHeight(), canvas.getWidth(), rectPaint);
        rectPaint.setColor(textCol);
        canvas.drawText(text, width, height, rectPaint);



    }*/
}
