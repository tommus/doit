package com.todev.doit;

import android.app.Application;
import com.firebase.client.Firebase;

public class DoIt extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    Firebase.setAndroidContext(this);
  }
}
