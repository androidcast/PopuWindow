package com.lzhang.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2021/4/29.
 */

public class IphonePopupWindow extends PopupWindow implements View.OnClickListener{
    PopupLinearLayout ll_popup_window;
    PathView path_view;
    View popupWindowView;
    private Context mContext;
    private float scale;
    private Map<String,OptionPopuMenu> mapOptions=new HashMap<>();
    public IphonePopupWindow(Context context){
        super(context);
        mContext=context;
        scale = context.getResources().getDisplayMetrics().density;

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popu_background));
        // 使其聚集
        setFocusable(true);
        // 设置允许在外点击消失
        setOutsideTouchable(true);
        initView(context);
    }

    /**
     * 初始化view
     * @param context
     */
    private void initView(Context context){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindowView = layoutInflater.inflate(R.layout.popu_view, null);
        //popupWindowView.setBackground(mContext.getResources().getDrawable(R.color.popu_transfer));
        ll_popup_window= popupWindowView.findViewById(R.id.ll_popup_window);
        path_view= popupWindowView.findViewById(R.id.path_view);

        setContentView(popupWindowView);
        ll_popup_window.setOnDataChangedListener(new PopupLinearLayout.OnDataChangedListener() {
            @Override
            public void onDataChanged(int type) {
                switch (type){
                    case PopupLinearLayout.MOVE_LEFT:
                        if(position==options.size()-1){
                            return;
                        }
                        position++;
                        changeUI();
                        break;
                    case PopupLinearLayout.MOVE_RIGHT:
                        if(position==0){
                            return;
                        }
                        position--;
                        changeUI();
                        break;
                }
            }
        });
    }
    List<List<OptionPopuMenu>> options=new ArrayList<>();
    public void setMenuItems(List<String> list){
        options.clear();
        if(list==null){
            return;
        }
        List<OptionPopuMenu> temp=null;
        OptionPopuMenu menu=null;
        int weightSum=0;
        for (int i = 0; i < list.size(); i++) {

            if(temp==null){
                temp=new ArrayList<>();
                options.add(temp);
            }
            weightSum+=list.get(i).length();
            if(temp.size()==5||weightSum>15){
                weightSum=list.get(i).length();
                temp=new ArrayList<>();
                options.add(temp);
            }
            if(mapOptions.containsKey(list.get(i))){
                menu=mapOptions.get(list.get(i));
            }else{
                menu=new OptionPopuMenu(list.get(i));
            }

            temp.add(menu);

        }

        if(ll_popup_window==null||options==null||options.size()==0){
            return;
        }

        //初始化数据
        ll_popup_window.removeAllViews();
        LinearLayout.LayoutParams popu_params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ll_popup_window.setLayoutParams(popu_params);
        List<OptionPopuMenu> optionPopuMenus = options.get(0);
        TextView itemView=null;
        for (int i=0;i<optionPopuMenus.size();i++
                ) {
            if(optionPopuMenus.get(i).itemView!=null){
                itemView=mapOptions.get(optionPopuMenus.get(i).name).itemView;
                LinearLayout.LayoutParams item_params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                item_params.weight=0;
                itemView.setLayoutParams(item_params);
            }else{
                itemView= (TextView) View.inflate(mContext,R.layout.item_popu_view,null);
                optionPopuMenus.get(i).itemView=itemView;
            }
            if(i==0){
                if(i!=optionPopuMenus.size()-1){
                    itemView.setBackgroundResource(R.drawable.btn_item_left_selector);
                }else{
                    itemView.setBackgroundResource(R.drawable.btn_item_selector);
                }

            }else{
                if(i==optionPopuMenus.size()-1){
                    if(options.size()==1){
                        itemView.setBackgroundResource(R.drawable.btn_item_right_selector);
                    }else{
                        itemView.setBackgroundResource(R.drawable.btn_item_defualt_selector);
                    }
                }else{
                    itemView.setBackgroundResource(R.drawable.btn_item_defualt_selector);
                }
            }

            ll_popup_window.addView(itemView);
            itemView.setText(optionPopuMenus.get(i).name);
            itemView.setOnClickListener(this);

            if(i!=optionPopuMenus.size()-1){
                //添加分割线
                addViewLine();
            }

            if(i==optionPopuMenus.size()-1){
                if(options.size()==1){
                    break;
                }
                //添加分割线
                addViewLine();

                PathView pathView=new PathView(mContext,getNextPath(),getPaint());
                pathView.setTag("next");
                pathView.setBackground(mContext.getResources().getDrawable(R.drawable.btn_item_right_selector));
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
                pathView.setLayoutParams(params);
                ll_popup_window.addView(pathView);
                pathView.setOnClickListener(this);
            }


        }
    }
    /**
     * 弹出popu
     * @param v
     */
    public void show(View v){
        position=0;
        popupWindowView.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        View contentView = getContentView();
        contentView.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //屏幕的高宽
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int myScreenHeight = metrics.heightPixels;
        int myScreenWidth = metrics.widthPixels;
        //popup view的高宽
        int measuredHeight = ll_popup_window.getMeasuredHeight();
        int measuredWidth = ll_popup_window.getMeasuredWidth();
        //控件相对屏幕的位置
        int [] location=new int[2];
        v.getLocationOnScreen(location);

        //控件的高宽
        int width = v.getWidth();
        int height = v.getHeight();

        //popup平移的距离
        int translationX=getTranslationX(myScreenWidth,measuredWidth,width,location[0]);
        int translationY=-height-measuredHeight;


        //popup 底部箭头的位置
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(32, 16);
        params.leftMargin=getTriangleLeftMargin(measuredWidth,width,translationX,32);
        path_view.setLayoutParams(params);

        showAsDropDown(v,translationX,translationY);
    }


    /**
     * 小箭头平移位置
     * @param popupWidth
     * @param viewWidth
     * @param translationX
     * @param triangleWidth
     * @return
     */
    private int getTriangleLeftMargin(int popupWidth,int viewWidth,int translationX,int triangleWidth){
        if(popupWidth<viewWidth){
            return popupWidth/2-triangleWidth/2;
        }
        int i = viewWidth / 2;

        return i-translationX-triangleWidth/2;
    };

    /**
     * 获取平移距离
     * @param screenWidth
     * @param popupWidth
     * @param viewWidth
     * @param viewX
     * @return
     */
    private int getTranslationX(int screenWidth,int popupWidth,int viewWidth,int viewX){
        int i = screenWidth - viewX;
        if(popupWidth<viewWidth){
            //居中
            return viewWidth/2-popupWidth/2;
        }
        //左右距离屏幕边框的最小距离
        int dip2px = dip2px( 20);
        if(i>popupWidth){
            //剩余x距离大于popu宽度，先直接作普通平移，后续优化
            if(viewX<dip2px){
                //如果左边距已经小于最小边距，不做平移
                return 0;
            }
            return -dip2px;
        }else{
            int temp=popupWidth-i;
            //剩余x距离消息popu宽度，需要平移否则会变成系统默认
            return -temp-dip2px;
        }
    }

    /**
     * 上一页
     * @return
     */
    private Path getBackPath(){
        int height1 = dip2px(32);
        Path mPath = new Path();
        mPath.moveTo(60,30);
        mPath.lineTo(30,height1/2);
        mPath.lineTo(60,height1-30);
        mPath.close();
        return mPath;
    }

    /**
     * 下一页
     * @return
     */
    private Path getNextPath(){
        int height1 = dip2px(32);
        Path mPath = new Path();
        mPath.moveTo(30,30);
        mPath.lineTo(60,height1/2);
        mPath.lineTo(30,height1-30);
        mPath.close();
        return mPath;
    }

    private Paint getPaint(){
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        return mPaint;
    }
    /**
     *
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px( float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {
        if(v instanceof CheckedTextView){
            dismiss();
            String s = ((CheckedTextView) v).getText().toString();

        }else if(v instanceof PathView){
            changeUiData(v);
        }
    }






    private int position;
    private void changeUiData(View v){

        Object tag = v.getTag();
        if(tag!=null&&tag.equals("next")){
            position++;
        }else{
            position--;
        }
        changeUI();
    }

    private void changeUI(){
        int width = ll_popup_window.getWidth();
        int height = ll_popup_window.getHeight();
        ll_popup_window.removeAllViews();
        //换数据
        LinearLayout.LayoutParams popu_params=new LinearLayout.LayoutParams(width, height);
        ll_popup_window.setLayoutParams(popu_params);

        List<OptionPopuMenu> optionPopuMenus=options.get(position);
        TextView itemView=null;

        for (int i=0;i<optionPopuMenus.size();i++
                ) {
            if(i==0&&position!=0){
                PathView pathView=new PathView(mContext,getBackPath(),getPaint());
                pathView.setTag("top");
                pathView.setBackground(mContext.getResources().getDrawable(R.drawable.btn_item_left_selector));
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
                pathView.setLayoutParams(params);
                ll_popup_window.addView(pathView);
                pathView.setOnClickListener(this);
                //添加分割线
                addViewLine();
            }
            if(optionPopuMenus.get(i).itemView!=null){
                itemView=optionPopuMenus.get(i).itemView;
            }else{
                itemView= (TextView) View.inflate(mContext,R.layout.item_popu_view,null);
                optionPopuMenus.get(i).itemView=itemView;
            }

            if(i==0&&position==0){
                itemView.setBackgroundResource(R.drawable.btn_item_left_selector);
            }else{
                if(i==optionPopuMenus.size()-1){
                    if(options.size()-1==position){
                        itemView.setBackgroundResource(R.drawable.btn_item_right_selector);
                    }else{
                        itemView.setBackgroundResource(R.drawable.btn_item_defualt_selector);
                    }
                }else{
                    itemView.setBackgroundResource(R.drawable.btn_item_defualt_selector);
                }
            }

            LinearLayout.LayoutParams item_params=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            item_params.weight=optionPopuMenus.get(i).name.length()+2;
            itemView.setLayoutParams(item_params);
            ll_popup_window.addView(itemView);
            itemView.setText(optionPopuMenus.get(i).name);
            itemView.setOnClickListener(this);

            if(i!=optionPopuMenus.size()-1){
                //添加分割线
                addViewLine();
            }

            if(i==optionPopuMenus.size()-1&&position<options.size()-1){
                //添加分割线
                addViewLine();

                PathView pathView=new PathView(mContext,getNextPath(),getPaint());
                pathView.setTag("next");
                pathView.setBackgroundResource(R.drawable.btn_item_right_selector);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(80, ViewGroup.LayoutParams.MATCH_PARENT);
                pathView.setLayoutParams(params);
                ll_popup_window.addView(pathView);
                pathView.setOnClickListener(this);
            }


        }
    }

    private void addViewLine(){
        //添加分割线
        View view_line=new View(mContext);
        view_line.setBackground(mContext.getResources().getDrawable(R.color.popu_view_line));
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT);
        view_line.setLayoutParams(params);
        ll_popup_window.addView(view_line);
    }



}
