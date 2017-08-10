package com.uzflsoft.uzgold;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class ExceptionHandler implements UncaughtExceptionHandler {


    private UncaughtExceptionHandler defaultUEH;
    private String localPath;
    private Context context;


    public ExceptionHandler(String localPath, Context cont) {
        this.localPath = localPath;
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.context = cont;
    }


    public void uncaughtException(Thread t, Throwable e) {

        final Writer stringBuffSync = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringBuffSync);
        e.printStackTrace(printWriter);
        String stacktrace = stringBuffSync.toString();
        printWriter.close();

        if (localPath != null) {
            writeToFile(stacktrace);

        }

        Intent intent = new Intent(context, LoggingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("text", stacktrace);
        context.startActivity(intent);


        defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String currentStacktrace) {
        try {

            File dir = new File(Environment.getExternalStorageDirectory(),
                    "Crash_Reports");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
            Date date = new Date();
            String filename = dateFormat.format(date) + ".txt";

            File reportFile = new File(dir, filename);
            FileWriter fileWriter = new FileWriter(reportFile);
            fileWriter.append(currentStacktrace);
            fileWriter.flush();
            fileWriter.close();

        } catch (Exception e) {
            Log.e("ExceptionHandler", e.getMessage());
        }
    }



}