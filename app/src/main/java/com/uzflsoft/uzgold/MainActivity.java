package com.uzflsoft.uzgold;

import static com.uzflsoft.uzgold.calc.*;
import android.app.ActionBar;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{

    int PARSE_STATUS = 1;
    SQLiteHelper myDb;
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;
    static double gt583, gt750, gt999;
    static double gw583, gw750, gw999;
	static int db, rb, eb;
	static int dollar, evro, rubble;
	static double usd_eur = 1;
    static double usd_rub = 1;
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
            new ParseSite().execute("http://onlygold.com/m/Prices/AllTheGoldInTheWorld.asp" + toStri(PARSE_STATUS)); PARSE_STATUS ++;
            new ParseSite().execute("http://cbu.uz/ru/arkhiv-kursov-valyut/xml" + toStri(PARSE_STATUS)); PARSE_STATUS ++;
            new ParseSite().execute("https://mute.000webhostapp.com/get_data_json.php" + toStri(PARSE_STATUS));

		}





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

                case 1  : return new MoneySelected();
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

        dollar = toInt(String.valueOf(dollar).replaceAll("[^\\d.]",""));
        evro = toInt(String.valueOf(evro).replaceAll("[^\\d.]",""));
        rubble = toInt(String.valueOf(rubble).replaceAll("[^\\d.]",""));

        myDb.updateData(toStrd(gw999), toStri(dollar));

		tv11.setText(toStrd(d).substring(0,toStrd(d).length()-2) + " сум");
		tv13.setText(toStrd(e).substring(0,toStrd(e).length()-2) + " сум");
		tv15.setText(toStrd(r).substring(0,toStrd(r).length()-2) + " сум");

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

	public void errorToast() {
        Toast.makeText(MainActivity.this, "Ошибка подключения! Перезапустите приложение", Toast.LENGTH_SHORT);
    }



    private class ParseSite extends AsyncTask<String, Void, String>
    {
        ProgressDialog dialog ;
        Document doc = null;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

           //dialog = ProgressDialog.show(MainActivity.this, null, "Обновление...");
        }

        @Override
        protected String doInBackground(String... params)
        {
            try {
                doc = Jsoup.connect(params[0].substring(0, params[0].length()-1)).get();
                return params[0].substring(params[0].length()-1);
            }
            catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if(toInt(result) == 2)
                new ParseSite().execute("https://dollar-uz.ru/" + toStri(4));

            switch (toInt(result)) {
                case 1: parseGoldWorld(doc); break;
                case 2: parseCurrencyWorld(doc); break;
                case 3: parseGoldTashkent(doc); break;
                case 4: parseCurrencyTashkent(doc); break;
            }


            //dialog.dismiss();
        }
    }

    public void parseGoldWorld(Document doc) {
		try {
			Element element = doc.getElementsByClass("spPrimaryDescription").first();
			String s = element.text();
			fill_table_world_gold(toDouble(s.substring(s.length() - 8, s.length()).replaceAll(",", "")));
		}
		catch (Exception e ) {
			errorToast();
		}
    }
    public void parseCurrencyWorld(Document doc) {
		try {

			Element d = doc.select("CcyNtry[ID=840]").first().select("Rate").first();
			Element e = doc.select("CcyNtry[ID=978]").first().select("Rate").first();
			Element r = doc.select("CcyNtry[ID=643]").first().select("Rate").first();
			fill_table__world_currency(getInt(toDouble(d.text())), getInt(toDouble(e.text())), getInt(toDouble(r.text())));
		}
		catch (Exception e ) {
			errorToast();
		}
    }
	public void parseCurrencyTashkent(Document doc) {
        try {
            int d1 = toInt(doc.getElementsByClass("pokupka left_ram").first().text().replaceAll("[^\\d.]", "").trim());
            int d2 = toInt(doc.getElementsByClass("prodaja left_ram").first().text().replaceAll("[^\\d.]", "").trim());
            fill_table_tashkent_currency((d1 + d2) / 2);
        }
        catch (Exception e ) {
            errorToast();
        }
	}
    public void parseGoldTashkent(Document doc) {
        try {
            fill_table_tashkent_gold(1000 * toInt(doc.text().substring(doc.text().indexOf("gold_tashkent") + 16, doc.text().indexOf("gold_tashkent") + 19)));
        }
        catch (Exception e) {
            errorToast();
        }
    }

}


