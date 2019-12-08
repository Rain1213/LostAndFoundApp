package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {
    private EditText mUserEmail;
    private EditText mUserPassword;
    private Button mUserLoginButton;
    private TextView mUserLoginErrorText;
    private Button mCreateActButton;



    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    OnlyEmail obj = new OnlyEmail();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserEmail=(EditText)findViewById(R.id.UserEmail);
        mUserPassword=(EditText)findViewById(R.id.UserPassword);
        mUserLoginButton=(Button)findViewById(R.id.UserLoginButton);
        mUserLoginErrorText=(TextView)findViewById(R.id.UserLoginError);
        mCreateActButton=(Button)findViewById(R.id.CreateActButton);

        FirebaseAuth.getInstance().signOut();
        mAuth=FirebaseAuth.getInstance();

        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=mAuth.getCurrentUser();
                if(user!=null){
                    Toast.makeText(MainActivity.this, "You are Logged In",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,TwoButtonsActivity.class));
                }
            }
        };

        mUserLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String userEmail=mUserEmail.getText().toString();
                String userPwd=mUserPassword.getText().toString();

                if(userEmail.isEmpty()){
                    mUserEmail.setError("Please Enter Email");
                    mUserEmail.requestFocus();
                }

                if(userPwd.isEmpty()){
                    mUserPassword.setError("Please Enter Password");
                    mUserPassword.requestFocus();
                }

                if(userEmail.isEmpty() && userPwd.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fields Are Empty",
                            Toast.LENGTH_SHORT).show();
                }

                if(!(userEmail.isEmpty() && userPwd.isEmpty())){
                    mAuth.signInWithEmailAndPassword(userEmail,userPwd)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser currentUser=mAuth.getCurrentUser();
                                        String email = currentUser.getEmail();
                                        obj.setEmail(email);


                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Success", "signInWithEmail:success");
                                        startActivity(new Intent(MainActivity.this,TwoButtonsActivity.class));
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Failed", "signInWithEmail:failure", task.getException());
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        mUserLoginErrorText.setVisibility(View.VISIBLE);
                                    }
                                    // ...
                                }
                            });
                }
            }
        });

        mCreateActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
            }
        });
    }







    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}
