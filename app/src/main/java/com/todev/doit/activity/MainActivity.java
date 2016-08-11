package com.todev.doit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.todev.doit.R;

public class MainActivity extends AppCompatActivity {

  private FirebaseAuth auth;
  private FirebaseAuth.AuthStateListener authListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    auth = FirebaseAuth.getInstance();
    authListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
          Intent intent = new Intent(MainActivity.this, LoginActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);
        }
      }
    };
  }

  @Override
  protected void onStart() {
    super.onStart();

    auth.addAuthStateListener(authListener);
  }

  @Override
  protected void onStop() {
    super.onStop();

    if (authListener != null) {
      auth.removeAuthStateListener(authListener);
    }
  }
}
