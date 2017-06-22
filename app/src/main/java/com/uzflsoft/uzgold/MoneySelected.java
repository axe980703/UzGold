package com.uzflsoft.uzgold;

import static com.uzflsoft.uzgold.Vars.rootView1;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;




public class MoneySelected extends Fragment
{

    Spinner spiner_metall, spiner_currency;
    EditText enter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        rootView1 = inflater.inflate(R.layout.money, container, false);
        enter = (EditText) rootView1.findViewById(R.id.enter1);
        enter.setSelection(1);





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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });

        return rootView1;
    }
}