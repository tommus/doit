package com.todev.doit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.todev.doit.Constants;
import com.todev.doit.R;

public class MainActivity extends AppCompatActivity {

  private FirebaseAuth auth;
  private FirebaseAuth.AuthStateListener authListener;

  private String userId;
  private String itemsUrl;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    auth = FirebaseAuth.getInstance();
    authListener = new FirebaseAuth.AuthStateListener() {
      @Override
      public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() == null) {
          startLoginActivity();
        }
      }
    };

    try {
      userId = auth.getCurrentUser().getUid();
    } catch (NullPointerException e) {
      startLoginActivity();
    }

    itemsUrl = Constants.FIREBASE_URL + "/users/" + userId + "/items";

    final ListView listView = (ListView) findViewById(R.id.items);
    final ArrayAdapter<String> adapter =
        new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1);
    listView.setAdapter(adapter);

    final EditText text = (EditText) findViewById(R.id.todo);
    final Button add = (Button) findViewById(R.id.add);
    add.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        new Firebase(itemsUrl).push().child("title").setValue(text.getText().toString().trim());
      }
    });

    new Firebase(itemsUrl).addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        adapter.add((String) dataSnapshot.child("title").getValue());
      }

      @Override
      public void onChildChanged(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onChildRemoved(DataSnapshot dataSnapshot) {
        adapter.remove((String) dataSnapshot.child("title").getValue());
      }

      @Override
      public void onChildMoved(DataSnapshot dataSnapshot, String s) {

      }

      @Override
      public void onCancelled(FirebaseError firebaseError) {

      }
    });
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

  private void startLoginActivity() {
    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
  }
}
