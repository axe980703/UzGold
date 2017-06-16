package com.uzflsoft.uzgold;


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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;



public class GoldSelected extends Fragment {

    View rootView;
    Spinner spiner_location;
    TextView gold1, gold2, gold3, tv1, tv2, tv3;
    EditText enter;
    RadioButton rb1, rb2;
    RadioGroup radioGroup;
    Typeface fonter;
    ArrayAdapter<?> spinnerAdapter;
    String PREFERENCE_RADIO = "radio_checked";
    String PREFERENCE_SPINNER = "spinner_checked";



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



        spiner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {
                switch (selectedItemPosition) {
                    case 0:
                        LOCATION_STATUS = "world";
                        SavePreferences(PREFERENCE_SPINNER, "0");
                        break;
                    case 1:
                        LOCATION_STATUS = "tashkent";
                        SavePreferences(PREFERENCE_SPINNER, "1");
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
                if(rb1.getId() == checkedId) CUR_SUM = true;
                else CUR_SUM = false;
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
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });




        return rootView;
    }



    private void LoadPreferences(String kies[]) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int savedRadioIndex = sharedPreferences.getInt(kies[0], 0);
        if(savedRadioIndex == rb1.getId())
            rb1.setChecked(true);
        if(savedRadioIndex == rb2.getId())
            rb2.setChecked(true);

        String savedSpinnerPosition = sharedPreferences.getString(kies[1], "0");
        spiner_location.setSelection(Integer.valueOf(savedSpinnerPosition));

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