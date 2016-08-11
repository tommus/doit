package com.todev.doit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.todev.doit.Constants;
import com.todev.doit.R;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

  private Firebase ref;
  private FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    final EditText emailEditText = (EditText) findViewById(R.id.email);
    final EditText passwordEditText = (EditText) findViewById(R.id.password);
    Button loginButton = (Button) findViewById(R.id.login);
    Button registerButton = (Button) findViewById(R.id.register);

    ref = new Firebase(Constants.FIREBASE_URL);
    auth = FirebaseAuth.getInstance();

    registerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
      }
    });

    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
          AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
          builder.setMessage("Invalid email and/or password.")
              .setTitle("Invalid credentials")
              .setPositiveButton(android.R.string.ok, null);
          AlertDialog dialog = builder.create();
          dialog.show();
        } else {
          auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();

                Map<String, Object> map = new HashMap<>();
                map.put("email", user.getEmail());
                ref.child("users").child(user.getUid()).setValue(map);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
              } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(task.getException().getLocalizedMessage())
                    .setTitle("Cannot login")
                    .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
              }
            }
          });
        }
      }
    });
  }
}
