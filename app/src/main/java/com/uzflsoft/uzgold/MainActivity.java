package com.uzflsoft.uzgold;


import static com.uzflsoft.uzgold.Calc.*;
import static com.uzflsoft.uzgold.Vars.*;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;



public class MainActivity extends AppCompatActivity implements ActionBar.TabListener
{

    SQLiteHelper myDb;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
	boolean NETWORK_ON = false;
    String requestUrl = "https://mute.000webhostapp.com/get_data.php";
    GoldSelected goldActivity;
    MoneySelected moneyActivity;

	

    public void onCreate(Bundle savedInstanceState)
    {

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


        if(isNetworkAvailable())
            goRequest();



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


	public int[] readDB() {
        Cursor curs =  myDb.getData("courses");
        curs.moveToFirst();
        int arr[] = new int[8];
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
        gt999 = gt583*999/583;
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

	public String getDeviceInfo() {
		String info =  (android.os.Build.VERSION.SDK + "\n" + ///тут определяем версию андроид по сдк(пробиваем по списку)
				android.os.Build.DEVICE   + "\n" + // тут модель полная нужно пробить по базе codenames написать отдельный класс -файл?
				android.os.Build.MODEL     + "\n" +  // тут модель без компании
				android.os.Build.PRODUCT ); // то же что и device только на конце версия прошивки
		return info;
	}


}


