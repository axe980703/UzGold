package com.uzflsoft.uzgold;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoggingActivity extends AppCompatActivity {

    final String URL = "https://mute.000webhostapp.com/logging.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        Intent intent = getIntent();
        letsReportLog(intent.getStringExtra("text"));
    }


    public void letsReportLog(final String sss) {

        final ProgressDialog progDailog = new ProgressDialog(LoggingActivity.this);
        progDailog.setMessage("Отправка ошибки разработчику...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progDailog.dismiss();
                        System.exit(1);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("log_text", sss);
                return params;
            }
        };

        Singelton.getInstance(LoggingActivity.this).addToRequestQueue(stringRequest);

    }
}
