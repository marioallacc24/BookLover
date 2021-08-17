package com.booklover.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.booklover.MainActivity;
import com.booklover.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPass;
    private Button btnLogin;
    private TextView mForgetPass;
    private TextView mSignUp;

    private ProgressDialog mDialog;

    //Firebase...
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        loginDetails();
    }

    private void loginDetails() {

        mEmail = findViewById(R.id.email_login);
        mPass = findViewById(R.id.lozinka_login);
        btnLogin = findViewById(R.id.btn_uloguj_se);
        mForgetPass = findViewById(R.id.zaboravljenja_lozinka);
        mSignUp = findViewById(R.id.registruj_se);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Obavezno polje...");
                    mEmail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Unesite pravilan email!");
                    mEmail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    mPass.setError("Obavezno polje...");
                    mPass.requestFocus();
                    return;
                }

                mDialog.setMessage("Obrada...");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user.isEmailVerified()){
                                mDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Uspesno logovanje",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                mDialog.dismiss();
                                user.sendEmailVerification();
                                Toast.makeText(getApplicationContext(),"Proverite Vas email!",Toast.LENGTH_LONG).show();
                            }


                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Neuspesno logovanje",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        //Pokretanje prozora za registraciju
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        //Pokretanje prozora za reset lozinke
        mForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ForgotPass.class));
            }
        });

    }
}