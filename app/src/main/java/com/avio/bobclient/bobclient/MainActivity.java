
package com.avio.bobclient.bobclient;



import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

@SuppressWarnings( "deprecation" )
public class MainActivity extends AppCompatActivity  implements Camera.PreviewCallback  {

    String TAG = "my_app" ;
    private Camera mCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCamera = camera;

    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

        Log.d(TAG,"frame");

    }
}
