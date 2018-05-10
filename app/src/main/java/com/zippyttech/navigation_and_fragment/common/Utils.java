package com.zippyttech.navigation_and_fragment.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zippyttech on 11/04/18.
 */

public abstract class Utils {
    public final static String TAG = "Utils";

    private static final String[] formats = {
            "yyyy-MM-dd'T'HH:mm:ss'Z'",   "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd'T'HH:mm:ss",      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss",
            "MM/dd/yyyy HH:mm:ss",        "MM/dd/yyyy'T'HH:mm:ss.SSS'Z'",
            "MM/dd/yyyy'T'HH:mm:ss.SSSZ", "MM/dd/yyyy'T'HH:mm:ss.SSS",
            "MM/dd/yyyy'T'HH:mm:ssZ",     "MM/dd/yyyy'T'HH:mm:ss",
            "yyyy:MM:dd HH:mm:ss",        "yyyyMMdd",
            "dd/MM/yyyy HH:mm", "dd/MM/yyyy HH:mm:ss",
            "dd-MM-yyyy HH:mm", "dd-MM-yyyy HH:mm:ss"
    };

    /**
     * Formatter Date
     * @param date
     * @param formatDateout
     * @return
     */

    public static String dateFormater(String date, String formatDateout) {
        try {
          String formattIn = parse(date);
          if(formattIn == null) return null;

            DateFormat format = new SimpleDateFormat(formattIn);
            Date date1 = format.parse(date);
            DateFormat format1 = new SimpleDateFormat(formatDateout);
            String date2 = format1.format(date1);
            return date2;
        } catch (Exception e) {
            Log.e(TAG, "error in date formatter");
            e.printStackTrace();
        }
        return null;

    }


    public static String parse(String d) {
      String format=null;
        if (d != null) {
            for (String parse : formats) {
                SimpleDateFormat sdf = new SimpleDateFormat(parse);
                try {
                    sdf.parse(d);
                    format = parse;
                    return parse;
                } catch (ParseException e) {
                  //  return null;
                }
            }
        }
        return format;
    }

    /**
     * Check regular expresions
     * @param regex
     * @param exp
     * @return
     */

    public  static  boolean isMatchRegex(String regex, String exp){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(exp);
        boolean b = m.matches();
        return b;
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Encode image Bitmap to Base64
     *
     * @param bm
     * @return
     */
    public static String encodeImage(Bitmap bm) {
        Log.w("encodeImage","image bounds: "+ bm.getWidth()+", "+bm.getHeight());

        if (bm.getHeight() <= 400 && bm.getWidth() <= 400) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return (Base64.encodeToString(b, Base64.DEFAULT)).replaceAll("\\n", "");
        }
        int mHeight=400;
        int mWidth=400;

        if(bm.getHeight()>bm.getWidth()){
            float div=(float)bm.getWidth()/((float) bm.getHeight());
            float auxW=div*480;
            mHeight=480;
            mWidth=Math.round(auxW);
            Log.w("encodeImage","new high: "+mHeight+" width: "+mWidth);
        }
        else{
            float div= ((float) bm.getHeight())/(float)bm.getWidth();
            float auxH=div*480;
            mWidth=480;
            mHeight=360;
            mHeight=Math.round(auxH);
            Log.w("encodeImage","new high: "+mHeight+" width: "+mWidth);
        }

        bm = Bitmap.createScaledBitmap(bm, mWidth, mHeight, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return (Base64.encodeToString(b, Base64.DEFAULT)).replaceAll("\\n", "");
    }

    public static Bitmap Base64ToBitmap(String encodedImagebase64) {
        //  String pureBase64Encoded=p;
        if (encodedImagebase64 != null) {
            try {
                String   pureBase64Encoded = encodedImagebase64.substring(encodedImagebase64.indexOf(",") + 1);
                byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                return decodedByte;
            }
            catch (Exception e){
                Log.e("ERROR UTILS","base64image "+e.getLocalizedMessage());
                e.printStackTrace();

            }

        }

        return null;
    }


    public static String RegexReplaceSimbol(String values){
        String   regex = "/^<([a-z]+)([^<]+)*(?:>(.*)<\\/\\1>|\\s+\\/>)$/";


        if (values!=null){
                values.replaceAll(regex, "");
            return values;
        }
        else return null;


    }
    public static String ReplaceSimbol(String values){
        String   regex = "/^<([a-z]+)([^<]+)*(?:>(.*)<\\/\\1>|\\s+\\/>)$/";


        if (values!=null){
            values.replace("<p>", "")
                    .replace("</p>", "")
                    .replace("<a>", "")
                    .replace("</a>", "")
                    .replace("/","")
                    .replace("[&hellip;]","")
                    .replace("</p>","")
                    .replace("&#8220;","")
                    .replace("&#8221;","");
            return values;
        }
        else return null;


    }

    public static String cientificMethod(float rep) {
        String aux="",r;
        double val=0;
        if (rep>0 && rep<1000) aux="";
        if (rep>1000 && rep<1000000)aux="k";  val=rep/1000;
        if (rep>0 && rep<1000) aux="M";  val=rep/1000000;
        if (rep>0 && rep<1000) aux="G";  val=rep/1000000000;

        return (""+ aux +""+ val);
    }

    /***
     * call api generics *
     */
/*
    public static class GetData extends AsyncTask<String,String,String> {
        private ApiCall call;
        private String URL;

        public GetData(Context context, String url){
            this.call = new ApiCall(context);
            this.URL=url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String  resp = call.callGet(URL);
            return resp;
        }


        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);

            try {
                Log.i("NavigationActivity","hay");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
*/
/***
 * call api generics *
 */

}