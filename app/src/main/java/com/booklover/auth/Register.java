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

import com.booklover.R;
import com.booklover.dao.DAOUser;
import com.booklover.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    //Definisanje komponenti na frontu
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegister;
    private TextView mLogin;

    private ProgressDialog mDialog;

    //Firebase objekat
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        //Dobijanje instance Firabase objketa
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        //Metoda ya registraciju
        registration();
    }

    private void registration() {

        DAOUser dao = new DAOUser();

        //Povezivanje komponneti sa frontom
        mName = findViewById(R.id.ime_registration);
        mEmail = findViewById(R.id.email_registration);
        mPassword = findViewById(R.id.lozinka_registration);
        mRegister = findViewById(R.id.btn_registruj_se);
        mLogin = findViewById(R.id.uloguj_se);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Izvlacenje podataka iz komponenti
                String name = mName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String pass = mPassword.getText().toString().trim();

                //Provera da li su podaci pravilni
                if(TextUtils.isEmpty(name)){
                    mName.setError("Obavezno polje...");
                    mName.requestFocus();
                    return;
                }
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
                    mPassword.setError("Obavezno polje...");
                    mPassword.requestFocus();
                    return;
                }

                //Stavljamo progresdialog da se vrti
                mDialog.setMessage("Obrada...");
                mDialog.show();

                //Programiramo kreiranje korisnika
                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Proveramo da li je korinsik kreiran
                        if(task.isSuccessful()){

                            //Pravimo objekat korisnika
                            User user = new User(name,"clan",0,email,"null");

                            dao.add(user).addOnFailureListener(err -> {
                                Toast.makeText(getApplicationContext(),""+err.getMessage(),Toast.LENGTH_LONG).show();
                            });



                            //Gasimo progresbar i ispisujemo toast poruku
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Uspesna registracija",Toast.LENGTH_SHORT).show();

                            //Prebacujemo korisnika na login prozor
                            startActivity(new Intent(getApplicationContext(),Login.class));
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Neuspesna registracija",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        //Pokretanje prozora za login
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });



    }
}