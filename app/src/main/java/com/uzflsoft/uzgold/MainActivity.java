package com.uzflsoft.uzgold;


import static com.uzflsoft.uzgold.Calc.*;
import static com.uzflsoft.uzgold.Vars.*;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener
{

    SQLiteHelper myDb;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
	boolean NETWORK_ON = false;
    String requestUrl = "https://mute.000webhostapp.com/get_data.php";
    String UPDATE_USERS = "https://mute.000webhostapp.com/update_users.php";
    GoldSelected goldActivity;
    MoneySelected moneyActivity;



    public void onCreate(Bundle savedInstanceState)
    {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(
                "/mnt/sdcard/", this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goldActivity = new GoldSelected();
        moneyActivity = new MoneySelected();


        myDb = new SQLiteHelper(this);
        if(myDb.isTableEmpty("courses")) {
            myDb.insertDataDefault("courses");

        }

        tabsInitialize();

        CONNECTION_ON = isNetworkAvailable();

        initVars(readDB());


        if(isNetworkAvailable()) {
            goRequest();
            getDeviceInfo();
        }


    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {

    }

    public void letsReportLog(final String sss) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://mute.000webhostapp.com/logging.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

    }

	public int[] readDB() {
        Cursor curs =  myDb.getData("courses");
        curs.moveToFirst();
        int arr[] = new int[9];
        for(int i = 0; i < arr.length; i++)
            arr[i] = Integer.parseInt(curs.getString(i));
        curs.close();
        return arr;
	}

    public void onUpdate(View view) {
        if(isNetworkAvailable()) {
            goldActivity.showPBars(true);
            moneyActivity.showPBars(true);
            goRequest();
        }
    }


     public void goRequest() {
         StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl,
                 new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {

                         String courses[] = response.split(" ");
                         myDb.updateDataCourses(courses);
                         goldActivity.fadeAnimTv();
                         moneyActivity.fadeAnimTv();
                         initVars(readDB());
                         goldActivity.fill_table_gold();
                         moneyActivity.fill_table_curr();
                         goldActivity.showPBars(false);
                         moneyActivity.showPBars(false);


                     }
                 },
                 new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {

                     }
                 });

         Singelton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
     }


    public void initVars(int arr[]) {
        dTash = arr[0];
        eTash = arr[1];
        rTash = arr[2];
        dWorld = arr[3];
        eWorld = arr[4];
        rWorld = arr[5];
        gt583 = arr[6]*1000;
        gt750 = gt583*750/583;
        gt999 = arr[8];
        gw999 = toGramm(arr[7]);
        gw750 = gw999*750/999;
        gw583 = gw999*583/999;
    }


    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter
    {

        public AppSectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int i)
        {
            switch (i)
			{
	
                case 0: return new GoldSelected();

                case 1: return new MoneySelected();
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return 2;
        }


        public CharSequence getPageTitle(int position)
        {
            if(position==0)
                return " Металлы";
            if(position==1)
                return " Валюта";
            else return "";
        }

        public int getPageIcon(int position)
        {
            if(position==0)
                return R.drawable.gold_ingots_stack;
            if(position==1)
                return R.drawable.dollar_coins_stack;
            else return 0;
        }
    }


	public void tabsInitialize() {
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {

            actionBar.setHomeButtonEnabled(false);
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mAppSectionsPagerAdapter);

            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);

                }
            });

            for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {

                actionBar.addTab(
                        actionBar.newTab()
                                .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                                .setIcon(mAppSectionsPagerAdapter.getPageIcon(i))
                                .setTabListener(this));
            }
        }
    }




    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		NETWORK_ON = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void getDeviceInfo() {
        String andVersion =  android.os.Build.VERSION.SDK, ///тут определяем версию андроид по сдк(пробиваем по списку)
                model = android.os.Build.DEVICE, // тут модель полная нужно пробить по базе codenames
                device = android.os.Build.MODEL;     // тут модель без компании

        getVersionName(andVersion, device);

    }

    public void getVersionName(String andVer, String device) {
        String version = "";
        switch (andVer) {
            case "14": version = "Android 4.0"; break;
            case "15": version = "Android 4.0.3"; break;
            case "16": version = "Android 4.1"; break;
            case "17": version = "Android 4.2"; break;
            case "18": version = "Android 4.3"; break;
            case "19": version = "Android 4.4"; break;
            case "20": version = "Android 4.4W"; break;
            case "21": version = "Android 5.0"; break;
            case "22": version = "Android 5.1"; break;
            case "23": version = "Android 6.0"; break;
            case "24": version = "Android 7.0"; break;
            case "25": version = "Android 7.1"; break;
            case "26": version = "Android 8.0"; break;
        }

        saveInfoDB(new String[] {version, device});
    }



    public void saveInfoDB(final String info[]) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_USERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}

        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                String date = new SimpleDateFormat("yyyy.MM.dd, HH:mm:ss").format(Calendar.getInstance().getTime());
                String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String phoneNumber = telephonyManager.getLine1Number();
                if (phoneNumber == null || !phoneNumber.matches(".*\\d+.*")) phoneNumber = "unavailable";
                String imei = telephonyManager.getDeviceId();


                params.put("OS", info[0]);
                params.put("Device", info[1]);
                params.put("AndroidID", android_id);
                params.put("PhoneNumber", phoneNumber);
                params.put("IMEI", imei);
                params.put("Date", date);
                return params;
            }
        };

        Singelton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }







}




