package com.mobilprog.tadam.mobilprog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mobilprog.tadam.mobilprog.Firebase.MyFirebaseDataBase;
import com.mobilprog.tadam.mobilprog.Model.User;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    private Button registButton;
    private EditText inputUsernameEditText;
    private EditText inputEmailEditText;
    private EditText passwordEditText;

    // Declare Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Init firebase auth and database references
        mFirebaseAuth = FirebaseAuth.getInstance();
        // Databasereference to the child users of the root
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(MyFirebaseDataBase.USER_DB);

        progressDialog = new ProgressDialog(this);



        registButton = (Button) findViewById(R.id.btn_regist);
        inputUsernameEditText = (EditText) findViewById(R.id.username_input);
        inputEmailEditText = (EditText) findViewById(R.id.input_email);
        passwordEditText = (EditText) findViewById(R.id.input_password);

        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start registration
                startRegistration();
            }
        });
    }

    /**
     * Registration method with user creation to FireBaseDatabase
     */
    private void startRegistration() {
        final String username = inputUsernameEditText.getText().toString().trim();
        final String email = inputEmailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

            progressDialog.setMessage("Signing up...");
            progressDialog.show();

            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String userId = mFirebaseAuth.getCurrentUser().getUid();
                        DatabaseReference currentUser = mDatabaseReference.child(userId);
                        User user = new User(username, email);
                        currentUser.setValue(user);

                        progressDialog.dismiss();

                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
