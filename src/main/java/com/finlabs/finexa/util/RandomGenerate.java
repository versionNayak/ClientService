package com.finlabs.finexa.util;

import java.util.Random;

public class RandomGenerate {
	 private static final Random r = new Random();
	 
	 public static String getPan() {
    	 StringBuilder b1 = new StringBuilder();
         for(int i = 0; i < 5; i++){
             b1.append((char)((byte) 'A' +r.nextInt(25)));
         }
         StringBuilder b2 = new StringBuilder();
         for(int i=0; i < 4; i++){
        	 b2.append((char)((byte) '0' +r.nextInt(9)));
         }
         StringBuilder b3 = new StringBuilder();
         int i1= 0;
         if(i1< 1) {
               b3.append((char)((byte) 'A' +r.nextInt(25)));
           }
           return "" + b1 + b2 + b3;
         
    } 
	 
	 public static String getEmail() {
	        String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	        //String CHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
	        StringBuilder em = new StringBuilder();
	        while (em.length() < 10) { 
	            int index = (int) (r.nextFloat() * CHARS.length());
	            em.append(CHARS.charAt(index));
	        }
	        String emStr = em.toString();
	        return emStr;

	    }
	 
	 public static long getAadhar() {
	  	  long aadhar = (long)(r.nextDouble()*1000000000000L);
	      return aadhar;
	  }
	 
	 public static long getMobile() {
   	  long mob = (long)(r.nextDouble()*10000000000L);
   	  return mob;
     }
	 
	 /** Random Distributor Code generated */
	    public  static String getRandomDistributorCode() {
	        return "ARN-" + r.nextInt(999999);
	    }
}
