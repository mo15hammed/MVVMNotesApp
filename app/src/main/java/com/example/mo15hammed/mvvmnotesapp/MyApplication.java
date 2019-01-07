package com.example.mo15hammed.mvvmnotesapp;

import android.app.Application;
import android.widget.Toast;

import com.squareup.leakcanary.LeakCanary;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "You Can Swipe To Delete !", Toast.LENGTH_SHORT).show();
        
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

}
