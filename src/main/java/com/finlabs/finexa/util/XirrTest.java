package com.finlabs.finexa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

    public class XirrTest {

    public static final double tol = 0.001;    

    public static double dateDiff(Date d1, Date d2){
        long day = 24*60*60*1000;

        return (d1.getTime() - d2.getTime())/day;
    }

    public static double f_xirr(double p, Date dt, Date dt0, double x) {        
        return p * Math.pow((1.0 + x), (dateDiff(dt0,dt) / 365.0));
    }

    public static double df_xirr(double p, Date dt, Date dt0, double x) {        
        return (1.0 / 365.0) * dateDiff(dt0,dt) * p * Math.pow((x + 1.0), ((dateDiff(dt0,dt) / 365.0) - 1.0));
    }

    public static double total_f_xirr(double[] payments, Date[] days, double x) {
        double resf = 0.0;

        for (int i = 0; i < payments.length; i++) {
            resf = resf + f_xirr(payments[i], days[i], days[0], x);
        }

        return resf;
    }

    public static double total_df_xirr(double[] payments, Date[] days, double x) {
        double resf = 0.0;

        for (int i = 0; i < payments.length; i++) {
            resf = resf + df_xirr(payments[i], days[i], days[0], x);
        }

        return resf;
    }

    public static double Newtons_method(double guess, double[] payments, Date[] days) {
        double x0 = guess;
        double x1 = 0.0;
        double err = 1e+100;

        while (err > tol) {
            x1 = x0 - total_f_xirr(payments, days, x0) / total_df_xirr(payments, days, x0);
            err = Math.abs(x1 - x0);
            x0 = x1;
        }

        return x0;
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static Date strToDate(String str){
        try {
            return sdf.parse(str);
        } catch (ParseException ex) {
            return null;
        }
    }

    public double findXirr(double[] payments, Date[] days) {
    	 double xirr = Newtons_method(0.1, payments, days);
    	 xirr = xirr * 100.0;
         System.out.println("XIRR value is " + xirr);  
         return xirr;
    }
    public static void main(String... args) {
        //double[] payments = {-1151250,232912,233123,233336,233551,233768}; // payments
        double[] payments = {1000,-550,3000,-1150,650,-3375}; // payments
    	Date[] days = {strToDate("01/01/2018"),strToDate("25/03/2019"),strToDate("04/04/2019"),strToDate("30/04/2019"),strToDate("01/06/2019"),strToDate("12/08/2019")}; // days of payment (as day of year)
        double xirr = Newtons_method(0.1, payments, days);

        System.out.println("XIRR value is " + (xirr*100));    
    }}