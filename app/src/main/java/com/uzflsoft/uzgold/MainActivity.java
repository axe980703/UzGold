package com.uzflsoft.uzgold;

import static com.uzflsoft.uzgold.Calc.*;
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
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ActionBar.TabListener
{

    SQLiteHelper myDb;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    double gt583, gt750, gt999;
    double gw583, gw750, gw999;
	int db, rb, eb;
	double dollar, evro, rubble;
	double usd_eur = 1;
    double usd_rub = 1;
    View rootView;
	View rootView1;
	boolean NETWORK_ON = false;
    String requestUrl = "https://mute.000webhostapp.com/script.php";
	
	

    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDb = new SQLiteHelper(this);
        if(myDb.isTableEmpty())
            myDb.insertDataDefault();

        tabsInitialize();

        courseRequest();

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


	public String readFromdLocalDB() {
        Cursor curs =  myDb.getData();
        StringBuffer buffer = new StringBuffer();
        while(curs.moveToNext()) {
            buffer.append("gold_course: " + curs.getString(0));
            buffer.append("dollar_course: " + curs.getString(1));
        }
        return buffer.toString();
	}


    public void onUpdate(View view) {
        courseRequest();
    }

    String res;
    public String courseRequest() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        res = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Singelton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        return res;
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



    public void fill_table_world_gold(double price)
    {
		
        TextView tv1 = (TextView) rootView.findViewById(R.id.tv1);
        TextView tv3 = (TextView) rootView.findViewById(R.id.tv3);
        TextView tv5 = (TextView) rootView.findViewById(R.id.tv5);

		//add to database world_gold

        gw999 = toGramm(price);
        gw750 = gw999*750/999;
        gw583 = gw999*583/999;

		String res1 = toStrd(round(gw583,2));
		String res2 = toStrd(round(gw750,2));
		String res3 = toStrd(round(gw999,2));
		
        tv1.setText(res1+" $");
        tv3.setText(res2+" $");
        tv5.setText(res3+" $");
		
    }

	public void fill_table__world_currency(double d, double e, double r)
	{
		TextView tv11 = (TextView) rootView1.findViewById(R.id.tv11);
		TextView tv13 = (TextView) rootView1.findViewById(R.id.tv13);
		TextView tv15 = (TextView) rootView1.findViewById(R.id.tv15);

		dollar = d;
		evro =  e;
		rubble = r;

        ///////////// edited by me after changing parsing from cbu

        myDb.updateData(toStrd(gw999), toStrd(dollar));

		tv11.setText(getInt(d));
		tv13.setText(getInt(e));
		tv15.setText(getInt(r));

		usd_eur = e/d;
        usd_rub = r/d;

	}
	
	public void fill_table_tashkent_gold(int gold)
	{
		
		TextView tv2 = (TextView) rootView.findViewById(R.id.tv2);
		TextView tv4 = (TextView) rootView.findViewById(R.id.tv4);
		TextView tv6 = (TextView) rootView.findViewById(R.id.tv6);

		//add to database tash_gold

		gt583 = gold;
		gt750 = (int) gt583*750/583;
		gt999 = (int) gt583*999/583;
		
		String res1 = toStrd(gt583);
		String res2 = toStrd(gt750);
		String res3 = toStrd(gt999);
		
		
		tv2.setText(res1.substring(0,res1.length()).substring(0,3) + "," + res1.substring(0,res1.length()).substring(3,6) + " сум") ;
		tv4.setText(res2.substring(0,res2.length()).substring(0,3) + "," + res2.substring(0,res2.length()).substring(3,6) + " сум") ;
		tv6.setText(res3.substring(0,res3.length()).substring(0,3) + "," + res3.substring(0,res3.length()).substring(3,6) + " сум") ;
		
		
	}
	
	public void fill_table_tashkent_currency(int d)
	{
		TextView tv12 = (TextView) rootView1.findViewById(R.id.tv12);
        TextView tv14 = (TextView) rootView1.findViewById(R.id.tv14);
        TextView tv16 = (TextView) rootView1.findViewById(R.id.tv16);

		//add to database tash_cur

		db = d;
		eb = (int) (db*usd_eur);
		rb = (int) (db*usd_rub);
		
		String res4 = toStri(db);
		String res5 = toStri(eb);
		String res6 = toStri(rb);
		

		tv12.setText(res4 + " сум");
		tv14.setText(res5 + " сум");
		tv16.setText(res6 + " сум");
		
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

	public void goText(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public class Course {
        String dollar_tashkent_course;
        String euro_tashkent_course;
        String ruble_tashkent_course;
        String dollar_world_course;
        String euro_world_course;
        String ruble_world_course;
        String gold_tashkent_course;
        String gold_world_course;
    }




}


