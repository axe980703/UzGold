package com.uzflsoft.uzgold;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{


    SQLiteHelper myDb;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    static double gt583, gt750, gt999;
    static double gw583, gw750, gw999;
	static int db, rb, eb;
	static int dollar, evro, rubble;
	static double usd_eur = 1;
	static int usd_rub = 1 ;
    static View rootView;
	static View rootView1;
	static boolean networkOn = false;
	
	

    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new SQLiteHelper(this);
        if(myDb.isTableEmpty())
            myDb.insertDataDefault();


        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        final ActionBar actionBar = getActionBar();

        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
            @Override
            public void onPageSelected(int position)
            {
                actionBar.setSelectedNavigationItem(position);

            }
        });

        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++)
        {

            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setIcon(mAppSectionsPagerAdapter.getPageIcon(i))
                            .setTabListener(this));
        }
        
		
		if(isNetworkAvailable()) {
			new GetStringFromUrl().execute("http://onlygold.com/m/Prices/AllTheGoldInTheWorld.asp");
			new GetStringFromUrl().execute("http://cbu.uz/ru/arkhiv-kursov-valyut/xml/");
            new GetStringFromUrl().execute("https://mute.000webhostapp.com/get_data_json.php");
            new GetStringFromUrl().execute("http://uzkurs.pro/");
		}






	}




    public void onUpdate(View view) {
        new GetStringFromUrl().execute("http://onlygold.com/m/Prices/AllTheGoldInTheWorld.asp");
		new GetStringFromUrl().execute("http://cbu.uz/ru/arkhiv-kursov-valyut/xml/");
        new GetStringFromUrl().execute("https://mute.000webhostapp.com/get_data_json.php");
        new GetStringFromUrl().execute("http://uzkurs.pro/");

        Cursor curs =  myDb.getData();
        if(curs.getCount() == 0) Toast.makeText(this,"Error.Db is empty", Toast.LENGTH_SHORT);
        StringBuffer buffer = new StringBuffer();
        while(curs.moveToNext()) {
            buffer.append("gold_course: " + curs.getString(0));
            buffer.append("dollar_course: " + curs.getString(1));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("message");
        builder.setMessage(buffer.toString());
        builder.show();

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

    public static double toGramm(double once)
    {
        return once/=31.1;
    }

    public static String toStrd(double d){return String.valueOf(d);}
	
	public static String toStri(int i){return String.valueOf(i);}

    public static double toDouble(String s){return Double.valueOf(s);}
	
	public static String toStrl(long l){return String.valueOf(l);}

    public static int toInt(String s){return Integer.valueOf(s);}
	


    public static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void fill_table_world_gold(double price)
    {
		
        TextView tv1 = (TextView) rootView.findViewById(R.id.tv1);
        TextView tv3 = (TextView) rootView.findViewById(R.id.tv3);
        TextView tv5 = (TextView) rootView.findViewById(R.id.tv5);
        TextView tvg1 = (TextView) rootView.findViewById(R.id.tvg1);
		TextView tvg2 = (TextView) rootView.findViewById(R.id.tvg2);

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
		tvg1.setText(toStrd(price)+" $");
		tvg2.setText("за 1 унцию");
		
    }

	public void fill_table__world_currency(double d, double e, double r)
	{
		TextView tv11 = (TextView) rootView1.findViewById(R.id.tv11);
		TextView tv13 = (TextView) rootView1.findViewById(R.id.tv13);
		TextView tv15 = (TextView) rootView1.findViewById(R.id.tv15);

		dollar = (int) d;
		evro = (int) e;
		rubble = (int) r;

        myDb.updateData(toStrd(gw999), toStri(dollar));

		tv11.setText(toStrd(d).substring(0,toStrd(d).length()-2) + " сум");
		tv13.setText(toStrd(e).substring(0,toStrd(e).length()-2) + " сум");
		tv15.setText(toStrd(r).substring(0,toStrd(r).length()-2) + " сум");

		usd_eur = e/d;
		usd_rub = (int) (d/r);

	}
	
	public void fill_table_tashkent_gold(int gold)
	{
		
		TextView tv2 = (TextView) rootView.findViewById(R.id.tv2);
		TextView tv4 = (TextView) rootView.findViewById(R.id.tv4);
		TextView tv6 = (TextView) rootView.findViewById(R.id.tv6);

		//add to database tash_gold
        gold *= 1000;

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
	
	public void fill_table_tashkent_currency(int currency)
	{
		TextView tv12 = (TextView) rootView1.findViewById(R.id.tv12);
        TextView tv14 = (TextView) rootView1.findViewById(R.id.tv14);
        TextView tv16 = (TextView) rootView1.findViewById(R.id.tv16);

		//add to database tash_cur

		db = currency;
		eb = (int) (db*usd_eur);
		rb = (db/usd_rub);
		
		String res4 = toStri(db);
		String res5 = toStri(eb);
		String res6 = toStri(rb);
		

		tv12.setText(res4 + " сум");
		tv14.setText(res5 + " сум");
		tv16.setText(res6 + " сум");
		
	}


    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		networkOn = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

	public String getDeviceInfo() {
		String info =  (android.os.Build.VERSION.SDK + "\n" + ///тут определяем версию андроид по сдк(пробиваем по списку)
				android.os.Build.DEVICE   + "\n" + // тут модель полная нужно пробить по базе codenames написать отдельный класс -файл?
				android.os.Build.MODEL     + "\n" +  // тут модель без компании
				android.os.Build.PRODUCT ); // то же что и device только на конце версия прошивки
		return info;
	}





    public static class GoldSelected extends Fragment {

		
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
			rootView = inflater.inflate(R.layout.gold, container, false);

			final EditText enter = (EditText) rootView.findViewById(R.id.enter);
			enter.setSelection(1);




			if(!networkOn)
			{

			}

			
			
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
						TextView tv1 = (TextView)rootView.findViewById(R.id.tv1);
						TextView tv2 = (TextView)rootView.findViewById(R.id.tv2);
						TextView tv3 = (TextView)rootView.findViewById(R.id.tv3);
						TextView tv4 = (TextView)rootView.findViewById(R.id.tv4);
						TextView tv5 = (TextView)rootView.findViewById(R.id.tv5);
						TextView tv6 = (TextView)rootView.findViewById(R.id.tv6);
						
						String o = enter.getText().toString();
						if(!o.equals(""))
						{
							if(!o.matches("0"))
							{
								if(!o.endsWith(".") && !o.startsWith("00")){
							double d1 = (round(gw583*toDouble(enter.getText().toString()),2));
							long r2 = ((long)(gt583*toDouble(enter.getText().toString())));
							double d3 = (round(gw750*toDouble(enter.getText().toString()),2));
							long r4 = ((long)(gt750*toDouble(enter.getText().toString())));
							double d5 = (round(gw999*toDouble(enter.getText().toString()),2));
							long r6 = ((long)(gt999*toDouble(enter.getText().toString())));
							
							String res1="",res2="",res3="",res4="",res5="",res6="";
							if(d1>=1000)
							{
								long l = (long) d1;
								res1 = toStrl(l).substring(0, toStrl(l).length()-3)+","+toStrl(l).substring(toStrl(l).length()-3, toStrl(l).length());
							}
							else
							{
								res1 = toStrd(d1);
							}

							if(r2<1000000)
								res2 = toStrl(r2).substring(0,toStrl(r2).length()-3)+","+toStrl(r2).substring(toStrl(r2).length()-3,toStrl(r2).length())+" сум";
							else 
							{
								res2 = toStrl(r2).substring(0,toStrl(r2).length()-6)+" м "+ toStrl(r2).substring(toStrl(r2).length()-6,toStrl(r2).length()-3)+" т";
							}

							if(d3>=1000)
							{
								long l = (long) d3;
								res3 = toStrl(l).substring(0, toStrl(l).length()-3)+","+toStrl(l).substring(toStrl(l).length()-3, toStrl(l).length());
							}
							else
							{
								res3 = toStrd(d3);
							}

							if(r4<1000000)
								res4 = toStrl(r4).substring(0,toStrl(r4).length()-3)+","+toStrl(r4).substring(toStrl(r4).length()-3,toStrl(r4).length())+" сум";
							else
							{
								res4 = toStrl(r4).substring(0,toStrl(r4).length()-6)+" м "+ toStrl(r4).substring(toStrl(r4).length()-6,toStrl(r4).length()-3)+" т";
							}

							if(d5>=1000)
							{
								long l = (long) d5;
								res5 = toStrl(l).substring(0, toStrl(l).length()-3)+","+toStrl(l).substring(toStrl(l).length()-3, toStrl(l).length());
							}
							else
							{
								res5 = toStrd(d5);
							}

							if(r6<1000000)
								res6 = toStrl(r6).substring(0,toStrl(r6).length()-3)+","+toStrl(r6).substring(toStrl(r6).length()-3,toStrl(r6).length())+" сум";
							else
							{
								res6 = toStrl(r6).substring(0,toStrl(r6).length()-6)+" м "+ toStrl(r6).substring(toStrl(r6).length()-6,toStrl(r6).length()-3)+" т";
							}

							tv1.setText(res1 + " $");
							tv2.setText(res2);
							tv3.setText(res3 + " $");
							tv4.setText(res4);
							tv5.setText(res5 + " $");
							tv6.setText(res6);
							}}
						}
						else
						{
							if(!o.matches("0")){
								if(!o.endsWith(".") && !o.startsWith("00")){
								String res1 = toStrd(round(gw583,2))+" $";
								String res2 = toStrd(round(gt583,2))+" $";
								String res3 = toStrd(round(gw750,2))+" $";
								String res4 = toStrd(round(gt750,2))+" $";
								String res5 = toStrd(round(gw999,2))+" $";
								String res6 = toStrd(round(gt999,2))+" $";
							
								tv1.setText(res1);
								tv2.setText(res2.substring(0,res2.length()).substring(0,3) + "," + res2.substring(0,res2.length()).substring(3,6) + " сум");
								tv3.setText(res3);
								tv4.setText(res4.substring(0,res4.length()).substring(0,3) + "," + res4.substring(0,res4.length()).substring(3,6) + " сум");
								tv5.setText(res5);
								tv6.setText(res6.substring(0,res6.length()).substring(0,3) + "," + res6.substring(0,res6.length()).substring(3,6) + " сум");
							}}
						}
					}
				});

            return rootView;
        }
		
    }

   public static class MoneySelected extends Fragment
    {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            rootView1 = inflater.inflate(R.layout.money, container, false);
			final EditText enter1 = (EditText) rootView1.findViewById(R.id.enter1);
			enter1.setSelection(1);



			if(!networkOn)
			{

			}


			enter1.addTextChangedListener(new TextWatcher()
				{
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after)
					{

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count)
					{
						if(s.toString().matches(""))
							enter1.setGravity(Gravity.LEFT);
						else
							enter1.setGravity(Gravity.CENTER);
					}

					@Override
					public void afterTextChanged(Editable s)
					{
						TextView tv11 = (TextView)rootView1.findViewById(R.id.tv11);
						TextView tv12 = (TextView)rootView1.findViewById(R.id.tv12);
						TextView tv13 = (TextView)rootView1.findViewById(R.id.tv13);
						TextView tv14 = (TextView)rootView1.findViewById(R.id.tv14);
						TextView tv15 = (TextView)rootView1.findViewById(R.id.tv15);
						TextView tv16 = (TextView)rootView1.findViewById(R.id.tv16);

						String o = enter1.getText().toString();
						if(!o.equals(""))
						{
							if(!o.matches("0"))
							{
								if(!o.endsWith(".") && !o.startsWith("00")){
								int r1 = (int)(toDouble(o)*dollar);
								int r2 = (int)(toDouble(o)*evro);
								int r3 = (int)(toDouble(o)*rubble);
								int r4 = (int)(toDouble(o)*db);
								int r5 = (int)(toDouble(o)*eb);
								int r6 = (int)(toDouble(o)*rb);

								if(r1>=1000000)
									tv11.setText(toStri(r1).substring(0,toStri(r1).length()-6)+" м "+toStri(r1).substring(toStri(r1).length()-6,toStri(r1).length()-3)+" т");
								else
									tv11.setText(r1 + " сум");

								if(r2>=1000000)
									tv13.setText(toStri(r2).substring(0,toStri(r2).length()-6)+" м "+toStri(r2).substring(toStri(r2).length()-6,toStri(r2).length()-3)+" т");
								else
									tv13.setText(r2 + " сум");

								if(r3>=1000000)
									tv15.setText(toStri(r3).substring(0,toStri(r3).length()-6)+" м "+toStri(r3).substring(toStri(r3).length()-6,toStri(r3).length()-3)+" т");
								else
									tv15.setText(r3 + " сум");

								if(r4>=1000000)
									tv12.setText(toStri(r4).substring(0,toStri(r4).length()-6)+" м "+toStri(r4).substring(toStri(r4).length()-6,toStri(r4).length()-3)+" т");
								else
									tv12.setText(r4 + " сум");

								if(r5>=1000000)
									tv14.setText(toStri(r5).substring(0,toStri(r5).length()-6)+" м "+toStri(r5).substring(toStri(r5).length()-6,toStri(r5).length()-3)+" т");
								else
									tv14.setText(r5 + " сум");

								if(r6>=1000000)
									tv16.setText(toStri(r6).substring(0,toStri(r6).length()-6)+" м "+toStri(r6).substring(toStri(r6).length()-6,toStri(r6).length()-3)+" т");
								else
									tv16.setText(r6 + " сум");

							}
							else
							{
								tv11.setText(toStri(dollar) + " сум");
								tv12.setText(toStri(db) + " сум");
								tv13.setText(toStri(evro) + " сум");
								tv14.setText(toStri(eb) + " сум");
								tv15.setText(toStri(rubble) + " сум");
								tv16.setText(toStri(rb) + " сум");
							}
							}
						}
						else
						{
							tv11.setText(toStri(dollar) + " сум");
							tv12.setText(toStri(db) + " сум");
							tv13.setText(toStri(evro) + " сум");
							tv14.setText(toStri(eb) + " сум");
							tv15.setText(toStri(rubble) + " сум");
							tv16.setText(toStri(rb) + " сум");
						}
					}
				});



            return rootView1;
        }
    }




    private class GetStringFromUrl extends AsyncTask<String, Void, String>
    {

        ProgressDialog dialog ;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            dialog = ProgressDialog.show(MainActivity.this, null, "Обновление...");
        }

        @Override
        protected String doInBackground(String... params) 
		{
            try
			{
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(params[0]);
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();

                BufferedHttpEntity buf = new BufferedHttpEntity(entity);

                InputStream is = buf.getContent();

                BufferedReader r = new BufferedReader(new InputStreamReader(is));

                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null)
				{
                    total.append(line + "\n");
                }
                String result = total.toString();

                return result;
            } 
			catch (Exception e)
			{


            }
			
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);


            if (result != null)
            {
				if(result.substring(2, 3).equals("D"))
				{
					Double d = Double.valueOf((result.substring(result.indexOf("Based on current gold price:&nbsp;$")+35,
																result.indexOf("Based on current gold price:&nbsp;$")+43)).replaceAll("[^\\d.]", ""));
					fill_table_world_gold(d);
				}
				else if(result.substring(2,5).equals("res")) {
                    fill_table_tashkent_gold(toInt(result.substring(result.indexOf("dollar_tashkent")+18, result.indexOf("dollar_tashkent")+21)));
                }
                else if(result.indexOf("ПРОДАЖА - ")>0) {
                    fill_table_tashkent_currency(toInt(result.substring(result.indexOf("ПРОДАЖА - ") + 10, result.indexOf("ПРОДАЖА - ") + 14))-50);
                }
				else
				{
					Double usd = Double.valueOf(result.substring(result.indexOf("U.S. Dollar") + 113,result.indexOf("U.S. Dollar") + 117));
					Double eur = Double.valueOf(result.substring(result.indexOf("Euro") + 106,result.indexOf("Euro") + 110));
					Double rub = Double.valueOf(result.substring(result.indexOf("Russian Ruble") + 118,result.indexOf("Russian Ruble") + 120));
					
					fill_table__world_currency(usd,eur,rub);

				}
            }
            dialog.dismiss();
        }
    }





	
}


