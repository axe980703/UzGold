package com.uzflsoft.uzgold;


import static com.uzflsoft.uzgold.calc.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class MoneySelected extends Fragment
{

    View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        rootView = inflater.inflate(R.layout.money, container, false);
        final EditText enter1 = (EditText) rootView.findViewById(R.id.enter1);
        enter1.setSelection(1);

        final Spinner spiner_metall = (Spinner) rootView.findViewById(R.id.spinner_metal);
        final Spinner spiner_currency = (Spinner) rootView.findViewById(R.id.spinner_currency);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.metal, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        enter1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().matches(""))
                    enter1.setGravity(Gravity.LEFT);
                else
                    enter1.setGravity(Gravity.CENTER);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });

        return rootView;
    }
}