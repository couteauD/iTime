package com.example.itime.ui.color;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.itime.R;
import com.example.itime.appThemeSaver;


public class colorFrament extends Fragment implements View.OnClickListener{

    private Button buttonBlue,buttonPink,buttonRed,buttonGreen,buttonYellow,buttonPurple,buttonGray,buttonBlack;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_color, container, false);

        buttonBlue=root.findViewById(R.id.button_blue);
        buttonPink=root.findViewById(R.id.button_pink);
        buttonRed=root.findViewById(R.id.button_red);
        buttonGreen=root.findViewById(R.id.button_green);
        buttonYellow=root.findViewById(R.id.button_yellow);
        buttonPurple=root.findViewById(R.id.button_purple);
        buttonGray=root.findViewById(R.id.button_gray);
        buttonBlack=root.findViewById(R.id.button_black);

        buttonBlue.setOnClickListener(this);
        buttonPink.setOnClickListener(this);
        buttonRed.setOnClickListener(this);
        buttonGreen.setOnClickListener(this);
        buttonYellow.setOnClickListener(this);
        buttonPurple.setOnClickListener(this);
        buttonGray.setOnClickListener(this);
        buttonBlack.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        Myapp app = (Myapp)getActivity().getApplication();
        appThemeSaver appthemeSaver=new appThemeSaver(getContext());
        switch (view.getId()) {
            case R.id.button_blue:
                app.theme = R.style.AppTheme;
                appthemeSaver.save(app.theme);
                getActivity().recreate();
                break;
            case R.id.button_pink:
                app.theme = R.style.pink;
                appthemeSaver.save(app.theme);
                getActivity().recreate();
                break;
            case R.id.button_red:
                app.theme = R.style.red;
                appthemeSaver.save(app.theme);
                getActivity().recreate();
                break;
            case R.id.button_green:
                app.theme = R.style.green;
                appthemeSaver.save(app.theme);
                getActivity().recreate();
                break;
            case R.id.button_yellow:
                app.theme = R.style.yellow;
                appthemeSaver.save(app.theme);
                getActivity().recreate();
                break;
            case R.id.button_purple:
                app.theme = R.style.purple;
                appthemeSaver.save(app.theme);
                getActivity().recreate();
                break;
            case R.id.button_gray:
                app.theme = R.style.gray;
                appthemeSaver.save(app.theme);
                getActivity().recreate();
                break;
            case R.id.button_black:
                app.theme = R.style.black;
                appthemeSaver.save(app.theme);
                getActivity().recreate();
                break;

        }
    }
}