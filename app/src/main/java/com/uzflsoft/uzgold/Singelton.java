package com.uzflsoft.uzgold;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singelton {

    private static Singelton singelton;
    private RequestQueue requestQueue;
    private static Context context;


    private Singelton(Context cont) {
        context = cont;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Singelton getInstance(Context cont) {
        if(singelton == null) {
            singelton = new Singelton(cont);
        }
        return singelton;
    }

    public<T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

}
