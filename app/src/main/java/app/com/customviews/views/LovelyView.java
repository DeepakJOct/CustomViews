package app.com.customviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import app.com.customviews.R;

public class LovelyView extends View {

    private int circleCol, labelCol;
    private String circleText;
    private Paint circlePaint;

    public LovelyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        circlePaint = new Paint();

        //Typed array will provide access to attribute values.
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LovelyView, 0, 0);
        try {
            circleText = a.getString(R.styleable.LovelyView_circleLabel);
            circleCol = a.getInteger(R.styleable.LovelyView_circleColor, 0);
            labelCol = a.getInteger(R.styleable.LovelyView_labelColor, 0);
        } finally {
            a.recycle();
        }

    }

    public void setCircleCol(int circleCol) {
        this.circleCol = circleCol;
        invalidate();
        requestLayout();
    }

    public void setLabelCol(int labelCol) {
        this.labelCol = labelCol;
        invalidate();
        requestLayout();
    }

    public void setCircleText(String circleText) {
        this.circleText = circleText;
        invalidate();
        requestLayout();
    }

    public void setCirclePaint(Paint circlePaint) {
        this.circlePaint = circlePaint;
    }

    public int getCircleCol() {
        return circleCol;
    }

    public int getLabelCol() {
        return labelCol;
    }

    public String getCircleText() {
        return circleText;
    }

    public Paint getCirclePaint() {
        return circlePaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewHalfWidth = this.getMeasuredWidth() / 2;
        int viewHalfHeight = this.getMeasuredHeight() / 2;

        //get the radius as half of the width or height, smaller one.
        //substract by ten so as it have some space around it.
        int radius = 0;
        if(viewHalfWidth > viewHalfHeight)
            radius = viewHalfHeight - 20;
        else
            radius = viewHalfWidth - 20;

        //set properties for painting with
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(circleCol);
        canvas.drawCircle(viewHalfWidth, viewHalfHeight, radius, circlePaint);
        circlePaint.setColor(labelCol);
        circlePaint.setTextAlign(Paint.Align.CENTER);
        circlePaint.setTextSize(50);
        canvas.drawText(circleText, viewHalfWidth, viewHalfHeight, circlePaint);
    }
}
