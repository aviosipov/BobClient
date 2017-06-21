
package com.avio.bobclient.bobclient;



import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import kotlin.jvm.functions.Function2;


public class MainActivity extends Activity   {

    static String TAG = "my_app" ;

    private CameraPreview camPreview;
    private ImageView MyCameraPreview = null;
    private FrameLayout mainLayout;
    private int PreviewSizeWidth = 640;
    private int PreviewSizeHeight= 480;



    private File createFileFromInputStream(InputStream inputStream) {

        try{

            File f = File.createTempFile("tmp","bmp");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            Log.d(TAG,"file is ready");

            return f;
        }catch (IOException e) {
            Log.d(TAG,"cant convert stream to file");
            e.printStackTrace();
            //Logging exception
        }
        Log.d(TAG,"no file blat") ;
        return null;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //Set this APK Full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set this APK no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //
        // Create my camera preview
        //
        MyCameraPreview = new ImageView(this);

        SurfaceView camView = new SurfaceView(this);
        SurfaceHolder camHolder = camView.getHolder();
        camPreview = new CameraPreview(PreviewSizeWidth, PreviewSizeHeight, MyCameraPreview);

        camHolder.addCallback(camPreview);
        camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        mainLayout = (FrameLayout) findViewById(R.id.frameLayout1);
        mainLayout.addView(camView, new LayoutParams(PreviewSizeWidth, PreviewSizeHeight));
        mainLayout.addView(MyCameraPreview, new LayoutParams(PreviewSizeWidth, PreviewSizeHeight));

        /// test out http send request

        /// remove this later ...



      //  Bitmap bmp = getBitmapFromAsset(this.getApplicationContext()  ,"flower3.bmp");
        Log.d(TAG,"upload file");





        Fuel.upload("http://10.0.2.2:5000/upload2").timeout(1000).source(new Function2<Request, URL, File>() {
            @Override
            public File invoke(Request request, URL url) {


                InputStream is = null;

                try {
                    is  = getAssets().open("flower3.bmp");

                } catch (IOException e) {
                    Log.d(TAG,"no file????");
                    e.printStackTrace();
                }


                Log.d(TAG,"invoked request");
                if (is == null)
                    Log.d(TAG,"bad stream :(");
                Log.d(TAG, request.toString());


                return createFileFromInputStream(is);




            }
        }).responseString(new Handler<String>() {



            @Override
            public void failure(@NotNull Request request, @NotNull Response response, @NotNull FuelError error) {
                //updateUI(error, null);
                Log.d(TAG,"upload failure");
                Log.d(TAG, request.toString());
                Log.d(TAG, response.toString());
                Log.d(TAG, error.toString());
            }

            @Override
            public void success(@NotNull Request request, @NotNull Response response, String data) {
                //updateUI(null, data);
                Log.d(TAG,"upload success");
            }
        });




    }
    protected void onPause()
    {
        if ( camPreview != null)
            camPreview.onPause();
        super.onPause();
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



}
