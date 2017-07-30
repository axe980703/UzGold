package com.uzflsoft.uzgold;


import android.view.View;

public class Vars {

    static String LOCATION_STATUS = "world";
    static String CURRENT_COURSE = "gos";
    static boolean CUR_SUM = true;
    static String CUR_PREFIX = " сум";
    static View rootView;
    static View rootView1;
    static double  gw999, gw750, gw583;
    static long gt999, gt750, gt583, dTash, eTash, rTash, dWorld, eWorld, rWorld;
    static double NUM = 1;
    static double NUM_CURR = 1;
    static boolean CONNECTION_ON;


    public long getCurCurrVars(int i) {
        long currTash[] = {dTash, eTash, rTash};
        long currWorld[] = {dWorld, eWorld, rWorld};
        boolean confirm = CURRENT_COURSE.equals("gos");
        if(confirm)
            return currWorld[i];
        else
            return currTash[i];
    }

    public double getCurGoldVars(int i) {
        double gtArr[] = {gt583, gt750, gt999};
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

    public double getDolValue(double curr) {
        return (curr/dTash);
    }

    public long getSumValue(double curr) {
        return (long) (curr*dTash);
    }


}


