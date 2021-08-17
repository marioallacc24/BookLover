package com.booklover.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.booklover.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {

    private EditText mEmail;
    private Button btnReset;

    private ProgressDialog mDialog;

    //Firebase...
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);

        resetPass();
    }

    private void resetPass() {

        mEmail = findViewById(R.id.email_restart);
        btnReset = findViewById(R.id.btn_restartuj_lozinku);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();

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

                mDialog.setMessage("Obrada...");
                mDialog.show();

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Proverite Vas email!",Toast.LENGTH_SHORT).show();
                        }else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Greska!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}