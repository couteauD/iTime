package com.example.itime.ui.color;

import android.app.AlertDialog;
import android.content.DialogInterface;;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.itime.MainActivity;
import com.example.itime.R;
import com.example.itime.appThemeSaver;


public class colorFrament extends Fragment {


    private SeekBar mSbColor;
    private ColorSeekBar mColorSeekBar;
    private Toolbar toolbar;
    private appThemeSaver appThemeSaver;
    private int mColor;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_component, container, false);
        mColorSeekBar=new ColorSeekBar();
        appThemeSaver=new appThemeSaver(getContext());
        MainActivity activity = (MainActivity) getActivity();
        toolbar= (Toolbar) activity.findViewById(R.id.toolbar);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appThemeSaver.save(mColor);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int color=appThemeSaver.load();
                //菜单栏设置
                toolbar.setBackgroundColor(color);

                //状态栏设置
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setStatusBarColor(color);
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        View dialogView = View.inflate(getContext(), R.layout.fragment_color, null);
        mSbColor=dialogView.findViewById(R.id.seekbar);
        //设置SeekBar的颜色
        initColorBar();
        //当SeekBar被滑动时,获取颜色
        mSbColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float radio = (float)progress / mSbColor.getMax();
                mColor = mColorSeekBar.getColor(radio);
                mSbColor.getThumb().setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);

                //菜单栏设置
                toolbar.setBackgroundColor(mColor);

                //状态栏设置
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setStatusBarColor(mColor);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        dialog.setTitle("选择颜色");
        dialog.setView(dialogView);
        dialog.show();

        return root;
    }

    private void initColorBar(){
        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                        ColorSeekBar.PICKCOLORBAR_COLORS, ColorSeekBar.PICKCOLORBAR_POSITIONS, Shader.TileMode.REPEAT);
                return linearGradient;
            }
        };
        PaintDrawable paint = new PaintDrawable();
        paint.setShape(new RectShape());
        paint.setCornerRadius(10);
        paint.setShaderFactory(shaderFactory);
        mSbColor.setProgressDrawable(paint);
    }
}