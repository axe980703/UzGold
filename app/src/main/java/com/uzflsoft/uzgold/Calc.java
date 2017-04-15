package com.uzflsoft.uzgold;



public class Calc {


    public static double toGramm(double once)
    {
        return once/=31.1;
    }

    public static String toStrd(double d){return String.valueOf(d);}

    public static String toStri(int i){return String.valueOf(i);}

    public static double toDouble(String s){return Double.valueOf(s);}

    public static String toStrl(long l){return String.valueOf(l);}

    public static int toInt(String s){return Integer.valueOf(s);}

    public static int getInt(double d) {return Double.valueOf(d).intValue();}



    public static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


}
