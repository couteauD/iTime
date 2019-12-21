package com.example.itime.ui.color;

import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.itime.R;
import com.example.itime.appThemeSaver;


public class colorFrament extends DialogFragment {

    private SeekBar mSbColor;
    private ColorSeekBar mColorSeekBar;
    private appThemeSaver appThemeSaver;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_color, container, false);
        mSbColor=root.findViewById(R.id.seekbar);
        mColorSeekBar=new ColorSeekBar();
        appThemeSaver=new appThemeSaver(getContext());
        //设置SeekBar的颜色
        initColorBar();

       //当SeekBar被滑动时,获取颜色
        mSbColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float radio = (float)progress / mSbColor.getMax();
                int mColor = mColorSeekBar.getColor(radio);
                mSbColor.getThumb().setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
                //储存颜色
                appThemeSaver.save(mColor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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