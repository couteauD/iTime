package com.example.itime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.DialogCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class markDialogActivity extends AppCompatActivity {

    private FlowLayout layout;
    private FlowLayout.MarginLayoutParams params;
    private Button buttonDelete,buttonCancel,buttonOk;
    private int curIndex,lastIndex,index;
    private TextView Tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_dialog_activity);

        layout=(FlowLayout) findViewById(R.id.mark_container);

        params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(30,0,30,10);

        buttonDelete=(Button)findViewById(R.id.button_delete);
        buttonCancel=(Button)findViewById(R.id.button_cancel);
        buttonOk=(Button)findViewById(R.id.button_ok);

        //存放标签和标签选择状态
        final List<TextView> tagView=new ArrayList<>();
        final List<Boolean> tagViewState=new ArrayList<>();

        //创建编辑中的标签
        final EditText editText=new EditText(getApplicationContext());
        editText.setHint("添加标签");
        //设置固定宽度
        editText.setMinEms(4);
        editText.setTextSize(12);
        //设置shape
        editText.setBackgroundResource(R.drawable.mark_edit);
        editText.setHintTextColor(Color.parseColor("#b4b4b4"));
        editText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        editText.setLayoutParams(params);

        //添加到layout中
        layout.addView(editText);

        //对软键盘的Enter键监听
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (KeyEvent.ACTION_DOWN == event.getAction()) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            String editTextContent = editText.getText().toString();
                            //判断输入是否为空
                            if (editTextContent.equals(""))
                                return true;
                            //判断标签是否重复添加
                            for (TextView tag : tagView) {
                                String tempStr = tag.getText().toString();
                                if (tempStr.equals(editTextContent)) {
                                    Toast.makeText(markDialogActivity.this, "重复添加", Toast.LENGTH_SHORT).show();
                                    editText.setText("");
                                    editText.requestFocus();
                                    return true;
                                }
                            }
                            //添加标签
                            final TextView temp = getTag(editText.getText().toString());
                            tagView.add(temp);
                            tagViewState.add(false);
                            //添加点击事件，点击变成选中状态，选中状态下被点击则恢复未选中状态
                            temp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    curIndex = tagView.indexOf(temp);
                                    if (!tagViewState.get(curIndex)) {
                                        //显示 √选中
                                        temp.setText(temp.getText() + " √");
                                        temp.setBackgroundResource(R.drawable.mark_selected);
                                        temp.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                        //修改选中状态
                                        tagViewState.set(curIndex, true);
                                    } else {
                                        //删除 √取消选中
                                        temp.setText(temp.getText().toString().replace("√", ""));
                                        temp.setBackgroundResource(R.drawable.mark_normal);
                                        temp.setTextColor(Color.parseColor("#b4b4b4"));
                                        //修改未选中状态
                                        tagViewState.set(curIndex, false);
                                    }
                                }
                            });
                            layout.addView(temp);
                            //让编辑框在最后一个位置上
                            editText.bringToFront();
                            //清空编辑框
                            editText.setText("");
                            editText.requestFocus();
                            return true;
                    }
                }
                return false;
            }

        });

        /**
         * 设置“删除选中标签”按钮点击事件
         */
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastIndex = tagView.size() - 1;
                //没有添加标签则不继续执行
                if (lastIndex < 0)
                    return;
                index=0;
                //选中状态下的标签删除
                while(index<tagView.size()) {
                    if (tagViewState.get(index)) {
                        Tag = tagView.get(index);
                        tagView.remove(Tag);
                        tagViewState.remove(index);
                        layout.removeView(Tag);
                        Toast.makeText(markDialogActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        index++;
                    }
                }
            }
        });

        /**
         * 设置“取消”按钮点击事件
         */
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
         * 设置“确定”按钮点击事件
         */
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(markDialogActivity.this, newScheduleActivity.class);
                index=0;
                ArrayList<String> selected=new ArrayList<>();
                while(index<tagView.size()) {
                    Tag = tagView.get(index);
                    if (tagViewState.get(index)) {
                        selected.add(Tag.getText().toString().replace("√",""));
                        index++;
                    }
                }
                intent.putStringArrayListExtra("mark",selected);
                startActivityForResult(intent,200); ;
                finish();
            }
        });
    }

    /**
     * 创建一个正常状态的标签
     * @param tag
     * @return
     */
    private  TextView getTag(String tag){
        TextView textView=new TextView(getApplicationContext());
        textView.setTextSize(12);
        textView.setBackgroundResource(R.drawable.mark_normal);
        textView.setTextColor(Color.parseColor("#b4b4b4"));
        textView.setText(tag);
        textView.setLayoutParams(params);
        return  textView;
    }

    /**
     * 点击空白处收起键盘
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (markDialogActivity.this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(markDialogActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

}
