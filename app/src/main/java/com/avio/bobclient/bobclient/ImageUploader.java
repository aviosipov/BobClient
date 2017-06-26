package com.avio.bobclient.bobclient;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

/**
 * Created by avi on 23/06/2017.
 */

public class ImageUploader {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    public static String generateRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }



    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }



    public File createFileFromInputStream(InputStream inputStream) {

        try{

            File f = File.createTempFile("tmp",".jpg");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            Log.d(MainActivity.TAG,"file is ready");

            return f;
        }catch (IOException e) {
            Log.d(MainActivity.TAG,"cant convert stream to file");
            e.printStackTrace();
            //Logging exception
        }
        Log.d(MainActivity.TAG,"no file blat") ;
        return null;
    }

    public  void uploadImage(File file) {

        Log.d(MainActivity.TAG,"upload file");


        RequestParams params = new RequestParams();

        try {

            params.put("file", file );
            AsyncHttpClient client = new AsyncHttpClient();



            client.post("http://192.168.1.21:5000/upload", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d(MainActivity.TAG,"post success");

                    String str = null; // for UTF-8 encoding
                    try {
                        str = new String(responseBody, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d(MainActivity.TAG, str );
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d(MainActivity.TAG,"post failure");
                    Log.d(MainActivity.TAG, String.valueOf(statusCode));
                    Log.d(MainActivity.TAG, String.valueOf(responseBody));
                }
            }) ;


        } catch(FileNotFoundException e) {
            Log.d(MainActivity.TAG,"bad bad bad") ;
        }

    }




}



