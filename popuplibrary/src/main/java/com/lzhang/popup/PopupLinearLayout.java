package com.lzhang.popup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2021/5/6.
 */

public class PopupLinearLayout extends LinearLayout {
    public static final int MOVE_LEFT=0;
    public static final int MOVE_RIGHT=1;
    public PopupLinearLayout(Context context) {
        super(context);
    }

    public PopupLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    float downX;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
       // Log.i("onTouch","onTouch============"+ev.getX());
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:

                downX=ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:


                break;
            case MotionEvent.ACTION_UP:
                float moveX = ev.getX();
                if(moveX-downX>100){
                   // Log.i("onTouch","up============"+ev.getX());
                    if(listener!=null){
                        listener.onDataChanged(MOVE_RIGHT);
                        return true;
                    }

                }else if(moveX-downX<-100){
                    //Log.i("onTouch","up============"+ev.getX());
                    if(listener!=null){
                        listener.onDataChanged(MOVE_LEFT);
                        return true;
                    }

                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    interface OnDataChangedListener{
        void onDataChanged(int type);
    }

    private OnDataChangedListener listener;



    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.listener = listener;
    }
}
