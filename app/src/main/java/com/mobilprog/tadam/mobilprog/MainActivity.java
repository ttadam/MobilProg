package com.mobilprog.tadam.mobilprog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView registrationTextView;
    private Button loginButton;
    private EditText inputEmailEditText;
    private EditText passwordEditText;


    // Firebase
    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mfirebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FirebaseApp.initializeApp(this);
        // Initialize FirebaseAuth
        mfirebaseAuth = FirebaseAuth.getInstance();

        // Get the component references
        registrationTextView = (TextView) findViewById(R.id.link_signup);
        loginButton = (Button) findViewById(R.id.btn_login);

        mfirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(MainActivity.this, PartnersActivity.class);
                    startActivity(intent);
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startSignIn();
            }
        });

        registrationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startSignIn() {
        inputEmailEditText = (EditText) findViewById(R.id.input_email);
        passwordEditText = (EditText) findViewById(R.id.input_password);
        String email = inputEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        Log.d(TAG, "Username: " + email);
        Log.d(TAG, "Password: " + password);

        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(this, "User or password fields are empty", Toast.LENGTH_LONG).show();
        } else {

            mfirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        Log.d("MainActivity", "Error: " + task.getException());
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mfirebaseAuth.addAuthStateListener(mfirebaseAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mfirebaseAuth.removeAuthStateListener(mfirebaseAuthStateListener);
    }
}
