package com.lzhang.popup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Administrator on 2021/4/28.
 */

public class PathView extends View {
    private Paint mPaint;
    private Path mPath;

    public PathView(Context context,Path mPath,Paint mPaint){
        super(context, null);
        this.mPath=mPath;
        this.mPaint=mPaint;
    }

    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.popu_backgrounp));
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(8);
        mPath = new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(32,0);
        mPath.lineTo(16,16);
        mPath.close();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

}
