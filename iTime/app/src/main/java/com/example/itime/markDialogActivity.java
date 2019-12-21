package com.example.itime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itime.model.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class markDialogActivity extends AppCompatActivity {

    private FlowLayout layout;
    private FlowLayout.MarginLayoutParams params;
    private appThemeSaver appthemeSaver;
    private Button buttonDelete,buttonCancel,buttonOk;
    private int curIndex,lastIndex;
    private TextView Tag;
    //存放标签和标签选择状态
    final List<TextView> tagView=new ArrayList<>();
    final List<Boolean> tagViewState=new ArrayList<>();
    final ArrayList<String> selected=new ArrayList<>();

    private int themeColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appthemeSaver=new appThemeSaver(this);
        themeColor=appthemeSaver.load();

        setContentView(R.layout.activity_mark_dialog_activity);

        layout=(FlowLayout) findViewById(R.id.mark_container);

        params=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(30,0,30,10);

        buttonDelete=(Button)findViewById(R.id.button_delete);
        buttonCancel=(Button)findViewById(R.id.button_cancel);
        buttonOk=(Button)findViewById(R.id.button_ok);

       //创建默认标签
        initData();

        //创建编辑中的标签
        final EditText editText=new EditText(getApplicationContext());
        editText.setHint("添加标签");
        //设置固定宽度
        editText.setMinEms(4);
        editText.setTextSize(12);
        //设置shape
        editText.setBackgroundResource(R.drawable.mark_edit);
        editText.setHintTextColor(Color.parseColor("#b4b4b4"));
        editText.setTextColor(themeColor);
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
                                        temp.setBackgroundResource(themeColor);
                                        temp.setTextColor(getResources().getColor(themeColor));
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
                int index=0;
                //选中状态下的标签删除
                while(index<tagView.size()) {
                    if (tagViewState.get(index)) {
                        Tag = tagView.get(index);
                        tagView.remove(Tag);
                        tagViewState.remove(index);
                        layout.removeView(Tag);
                    }
                    index++;
                }
                Toast.makeText(markDialogActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
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
                load(tagView,tagViewState);
            }
        });
    }

    /**
     * 设置默认标签
     */

    private void initData(){
        final List<String> label_list=new ArrayList<>();
        //初始化标签
        label_list.add("生日");
        label_list.add("学习");
        label_list.add("工作");
        label_list.add("节假日");

        for (int i = 0; i < label_list.size() ; i++) {
            final TextView textView = new TextView(getApplicationContext());
            textView .setText(label_list.get(i));
            textView .setTextSize(12);
            //设置shape
            textView .setBackgroundResource(R.drawable.mark_normal);
            textView .setTextColor(Color.parseColor("#b4b4b4"));
            textView .setLayoutParams(params);

            tagView.add(textView);
            tagViewState.add(false);

            //添加点击事件，点击变成选中状态，选中状态下被点击则恢复未选中状态
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    curIndex = tagView.indexOf(textView);
                    if (!tagViewState.get(curIndex)) {
                        //显示 √选中
                        textView.setText(textView.getText() + " √");
                        textView.setBackgroundColor(themeColor);
                        textView.setTextColor(themeColor);
                        //修改选中状态
                        tagViewState.set(curIndex, true);
                    } else {
                        //删除 √取消选中
                        textView.setText(textView.getText().toString().replace("√", ""));
                        textView.setBackgroundResource(R.drawable.mark_normal);
                        textView.setTextColor(Color.parseColor("#b4b4b4"));
                        //修改未选中状态
                        tagViewState.set(curIndex, false);
                    }
                }
            });

            //添加到layout中
            layout.addView(textView);
        }

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
     * 用子进程获取选中的标签列表并传递数据
     */
    public void load(final List<TextView> tagView, final List<Boolean> tagViewState) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int index=0;
                while(index<tagView.size()) {
                    Tag = tagView.get(index);
                    if (tagViewState.get(index)) {
                        selected.add(Tag.getText().toString().replace("√",""));
                    }
                    index++;

                }
                Intent intent=new Intent();
                intent.putStringArrayListExtra("mark",selected);
                setResult(RESULT_OK, intent);
                finish();
            }
        }).start();
    }




}
