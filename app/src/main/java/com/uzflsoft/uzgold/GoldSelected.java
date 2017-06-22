package com.uzflsoft.uzgold;


import static com.uzflsoft.uzgold.Calc.*;
import static com.uzflsoft.uzgold.Vars.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;



public class GoldSelected extends Fragment {

    Spinner spiner_location;
    TextView gold1, gold2, gold3, tv1, tv2, tv3;
    EditText enter;
    RadioButton rb1, rb2;
    RadioGroup radioGroup;
    ProgressBar pb1, pb2, pb3;
    Typeface fonter;
    ArrayAdapter<?> spinnerAdapter;
    String PREFERENCE_RADIO = "radio_checked";
    String PREFERENCE_SPINNER = "spinner_checked";
    Vars vars;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.gold, container, false);


        enter = (EditText) rootView.findViewById(R.id.enter);
        enter.setSelection(1);

        spiner_location = (Spinner) rootView.findViewById(R.id.spinner_currency);


        gold1 = (TextView) rootView.findViewById(R.id.gold1);
        gold2 = (TextView) rootView.findViewById(R.id.gold2);
        gold3 = (TextView) rootView.findViewById(R.id.gold3);
        tv1 = (TextView) rootView.findViewById(R.id.tv1);
        tv2 = (TextView) rootView.findViewById(R.id.tv2);
        tv3 = (TextView) rootView.findViewById(R.id.tv3);
        rb1 = (RadioButton) rootView.findViewById(R.id.rb1);
        rb2 = (RadioButton) rootView.findViewById(R.id.rb2);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        if(!CONNECTION_ON)
            showPBars(false);


        spiner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                switch (selectedItemPosition) {
                    case 0:
                        LOCATION_STATUS = "world";
                        SavePreferences(PREFERENCE_SPINNER, "0");
                        fill_table_gold();

                        break;
                    case 1:
                        LOCATION_STATUS = "tashkent";
                        SavePreferences(PREFERENCE_SPINNER, "1");
                        fill_table_gold();

                        break;
                }

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.location, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner_location.setAdapter(spinnerAdapter);

        fonter = Typeface.createFromAsset(getContext().getAssets(), "fonts/Helvetica/HelveticaRegular.ttf");
        gold1.setTypeface(fonter);
        gold2.setTypeface(fonter);
        gold3.setTypeface(fonter);
        tv1.setTypeface(fonter);
        tv2.setTypeface(fonter);
        tv3.setTypeface(fonter);

        LoadPreferences(new String[] {PREFERENCE_RADIO, PREFERENCE_SPINNER});

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(rb1.getId() == checkedId) {
                    CUR_SUM = true;
                    CUR_PREFIX = " сум";
                    fill_table_gold();
                }
                else {
                    CUR_SUM = false;
                    CUR_PREFIX = " $";
                    fill_table_gold();
                }
                SavePreferences(PREFERENCE_RADIO, checkedId);
            }
        });


        enter.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString().matches(""))
                    enter.setGravity(Gravity.LEFT);
                else
                    enter.setGravity(Gravity.CENTER);
                if(!enter.getText().toString().equals("") && Double.valueOf(enter.getText().toString()) != 0) {
                    NUM = Double.valueOf(enter.getText().toString());
                    fill_table_gold();
                }
                else {
                    NUM = 1;
                    fill_table_gold();
                }


            }

        @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        fill_table_gold();


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars();


    }

    public void showPBars(boolean status) {
        pb1 = (ProgressBar) rootView.findViewById(R.id.pb1);
        pb2 = (ProgressBar) rootView.findViewById(R.id.pb2);
        pb3 = (ProgressBar) rootView.findViewById(R.id.pb3);
        int stat;
        if(status) stat = View.VISIBLE;
        else stat = View.INVISIBLE;
        pb1.setVisibility(stat);
        pb2.setVisibility(stat);
        pb3.setVisibility(stat);
    }

    public void fadeAnimTv() {
        tv1 = (TextView) rootView.findViewById(R.id.tv1);
        tv2 = (TextView) rootView.findViewById(R.id.tv2);
        tv3 = (TextView) rootView.findViewById(R.id.tv3);
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
        tv1.startAnimation(fadeIn);
        tv2.startAnimation(fadeIn);
        tv3.startAnimation(fadeIn);
        fadeIn.setDuration(800);
        fadeIn.setFillAfter(true);
    }

    public void fill_table_gold()
    {
        vars = new Vars();
        double g583 = vars.getCurGoldVars(0);
        double g750 = vars.getCurGoldVars(1);
        double g999 = vars.getCurGoldVars(2);

        TextView tv1 = (TextView) rootView.findViewById(R.id.tv1);
        TextView tv2 = (TextView) rootView.findViewById(R.id.tv2);
        TextView tv3 = (TextView) rootView.findViewById(R.id.tv3);

        if(!CUR_SUM) {
            g583 = round(g583*NUM,2);
            g750 = round(g750*NUM,2);
            g999 = round(g999*NUM,2);

            if(g583 > 1000) g583 = getInt(g583);
            if(g750 > 1000) g750 = getInt(g750);
            if(g999 > 1000) g999 = getInt(g999);

            tv1.setText(String.format("%1$,.2f", g583) + CUR_PREFIX);
            tv2.setText(String.format("%1$,.2f", g750) + CUR_PREFIX);
            tv3.setText(String.format("%1$,.2f", g999) + CUR_PREFIX);
        }
        else {
            long gg583 = (long) (g583*NUM);
            long gg750 = (long) (g750*NUM);
            long gg999 = (long) (g999*NUM);

            tv1.setText(String.format("%,d", gg583) + CUR_PREFIX);
            tv2.setText(String.format("%,d", gg750) + CUR_PREFIX);
            tv3.setText(String.format("%,d", gg999) + CUR_PREFIX);
        }


    }



    private void LoadPreferences(String kies[]) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int savedRadioIndex = sharedPreferences.getInt(kies[0], 0);
        if(savedRadioIndex == rb1.getId()) {
            rb1.setChecked(true);
            CUR_SUM = true;
            CUR_PREFIX = " сум";
        }
        if(savedRadioIndex == rb2.getId()) {
            rb2.setChecked(true);
            CUR_SUM = false;
            CUR_PREFIX = " $";
        }

        String savedSpinnerPosition = sharedPreferences.getString(kies[1], "0");
        int pos = Integer.valueOf(savedSpinnerPosition);
        spiner_location.setSelection(pos);

        if(pos == 0) LOCATION_STATUS = "world";
        else  LOCATION_STATUS = "tashkent";


    }

    private void SavePreferences(String key, int value) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


}