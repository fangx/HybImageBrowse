package cn.nn.imagebrowsesample;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * **************************
 * Class:       MyApplication
 * Author:      fangx
 * Date:        16/9/18
 * Description:
 * ***************************
 */
public class MyApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

    }
}
