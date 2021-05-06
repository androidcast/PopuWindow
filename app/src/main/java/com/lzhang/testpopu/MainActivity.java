package com.lzhang.testpopu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.lzhang.popup.IphonePopupWindow;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        optionList.clear();
        switch (v.getId()){
            case R.id.btn_one:
                optionList.add("转发");
                showPopupWindow1(v);
                break;
            case R.id.btn_two:
                optionList.add("分享");
                optionList.add("转发");
                showPopupWindow1(v);
                break;
            case R.id.btn_three:
                optionList.add("分享");
                optionList.add("转发");
                optionList.add("引用");
                showPopupWindow1(v);
                break;
            case R.id.btn_four:
                optionList.add("分享");
                optionList.add("转发");
                optionList.add("引用");
                optionList.add("收藏");
                optionList.add("复制");
                optionList.add("删除");
                optionList.add("备忘");
                optionList.add("撤回");
                showPopupWindow1(v);
                break;
            case R.id.btn_five:
                optionList.add("分享");
                optionList.add("转发");
                optionList.add("引用");
                optionList.add("收藏");
                optionList.add("识别图片二维码");
                optionList.add("识别发票");
                optionList.add("复制");
                optionList.add("撤回");
                showPopupWindow1(v);
                break;
        }
    }
    List<String> optionList=new ArrayList<>();
    IphonePopupWindow popuWindow;
    private void     showPopupWindow1(View v){
        if(popuWindow==null){
            popuWindow=new IphonePopupWindow(this);
        }
        popuWindow.setMenuItems(optionList);
        popuWindow.show(v);
    }
}
