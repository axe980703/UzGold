package com.uzflsoft.uzgold;


import android.view.View;

public class Vars {

    static String LOCATION_STATUS = "world";
    static boolean CUR_SUM = true;
    static String CUR_PREFIX = " сум";
    static View rootView;
    static View rootView1;
    static double dTash, eTash, rTash, dWorld, eWorld, rWorld, gw999, gw750, gw583;
    static long gt999, gt750, gt583;
    static double NUM = 1;
    static boolean CONNECTION_ON;



    public double getCurGoldVars(int i) {
        long gtArr[] = {gt583, gt750, gt999};
        double gwArr[] = {gw583, gw750, gw999};
        if(LOCATION_STATUS.equals("world")) {
            if (CUR_SUM)
                return getSumValue(gwArr[i]);
            else
                return gwArr[i];
        }
        else {
            if (CUR_SUM)
                return gtArr[i];
            else
                return getDolValue(gtArr[i]);
        }
    }

    public double getDolValue(long curr) {
        return (curr/dTash);
    }

    public long getSumValue(double curr) {
        return (long) (curr*dTash);
    }


}


