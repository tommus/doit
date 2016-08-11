package com.todev.doit.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.todev.doit.R;

public class RegisterActivity extends AppCompatActivity {

  private FirebaseAuth auth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    final EditText emailEditText = (EditText) findViewById(R.id.email);
    final EditText passwordEditText = (EditText) findViewById(R.id.password);
    Button registerButton = (Button) findViewById(R.id.register);
    Button loginButton = (Button) findViewById(R.id.login);

    auth = FirebaseAuth.getInstance();

    loginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
      }
    });

    registerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
          AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
          builder.setMessage("Invalid email and/or password.")
              .setTitle("Invalid credentials")
              .setPositiveButton(android.R.string.ok, null);
          AlertDialog dialog = builder.create();
          dialog.show();
        } else {
          auth.createUserWithEmailAndPassword(email, password)
              .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Account created. You can now login.")
                        .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                          }
                        });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                  } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage(task.getException().getLocalizedMessage())
                        .setTitle("Error")
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
