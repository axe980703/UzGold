package com.uzflsoft.uzgold;

import static com.uzflsoft.uzgold.Vars.*;
import static com.uzflsoft.uzgold.Calc.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;



public class MoneySelected extends Fragment
{

    TextView tv1, tv2, tv3;
    Spinner spinner_currency;
    ProgressBar pb1, pb2, pb3;
    ArrayAdapter<?> spinnerAdapter;
    EditText enter;
    Vars vars;

    String SPINNER_SETTINGS = "spinner_course";
    String NUM_VALUE = "num_curr";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        rootView1 = inflater.inflate(R.layout.money, container, false);


        enter = (EditText) rootView1.findViewById(R.id.cur_enter);
        enter.setSelection(1);

        spinner_currency = (Spinner) rootView1.findViewById(R.id.spinner_currency);


        if(!CONNECTION_ON)
            showPBars(false);


        spinner_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                switch (selectedItemPosition) {
                    case 0:
                        CURRENT_COURSE = "gos";
                        SavePreferences(SPINNER_SETTINGS, 0);
                        fill_table_curr();

                        break;
                    case 1:
                        CURRENT_COURSE = "baz";
                        SavePreferences(SPINNER_SETTINGS, 1);
                        fill_table_curr();

                        break;
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.courses, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_currency.setAdapter(spinnerAdapter);




        enter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(""))
                    enter.setGravity(Gravity.LEFT);
                else
                    enter.setGravity(Gravity.CENTER);

                if(!enter.getText().toString().equals("") && Double.valueOf(enter.getText().toString()) != 0) {
                    NUM_CURR = Double.valueOf(enter.getText().toString());
                    fill_table_curr();
                }
                else {
                    NUM_CURR = 1;
                    fill_table_curr();
                }
                SavePreferences(NUM_VALUE, (float) NUM_CURR);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });



        LoadPreferences(new String[] {SPINNER_SETTINGS, NUM_VALUE});

        fill_table_curr();

        return rootView1;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars();

    }

    public void fill_table_curr() {
        vars = new Vars();
        long dollar = vars.getCurCurrVars(0);
        long euro = vars.getCurCurrVars(1);
        long ruble = vars.getCurCurrVars(2);

        TextView tv1 = (TextView) rootView1.findViewById(R.id.tv1);
        TextView tv2 = (TextView) rootView1.findViewById(R.id.tv2);
        TextView tv3 = (TextView) rootView1.findViewById(R.id.tv3);

        tv1.setText(String.format("%,d", (long)(NUM_CURR * dollar)) + " сум");
        tv2.setText(String.format("%,d", (long)(NUM_CURR * euro)) + " сум");
        tv3.setText(String.format("%,d", (long)(NUM_CURR * ruble)) + " сум");

    }



    public void fadeAnimTv() {
        tv1 = (TextView) rootView1.findViewById(R.id.tv1);
        tv2 = (TextView) rootView1.findViewById(R.id.tv2);
        tv3 = (TextView) rootView1.findViewById(R.id.tv3);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        tv1.startAnimation(fadeIn);
        tv2.startAnimation(fadeIn);
        tv3.startAnimation(fadeIn);
        fadeIn.setDuration(800);
        fadeIn.setFillAfter(true);
    }

    public void showPBars(boolean status) {
        pb1 = (ProgressBar) rootView1.findViewById(R.id.pb1);
        pb2 = (ProgressBar) rootView1.findViewById(R.id.pb2);
        pb3 = (ProgressBar) rootView1.findViewById(R.id.pb3);
        int stat;
        if(status) stat = View.VISIBLE;
        else stat = View.INVISIBLE;
        pb1.setVisibility(stat);
        pb2.setVisibility(stat);
        pb3.setVisibility(stat);
    }





    private void LoadPreferences(String kies[]) {
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        int pos = sp.getInt(kies[0], 0);
        if(pos == 0)
            CURRENT_COURSE = "gos";
        else
            CURRENT_COURSE = "baz";
        spinner_currency.setSelection(pos);

        NUM_CURR  = sp.getFloat(kies[1], 0.0f);
        if(NUM_CURR == (int)NUM_CURR)
            enter.setText(toStri((int)(NUM_CURR)));
        else
            enter.setText(toStrd(NUM_CURR));


    }


    private void SavePreferences(String key, int value) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void SavePreferences(String key, float value) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

}