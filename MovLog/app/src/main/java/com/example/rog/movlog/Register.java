package com.example.rog.movlog;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Register extends AppCompatActivity {
    private MaterialEditText edtemail, edtname, edtpassword;
    private Button btnCreateAccount, btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() !=null){

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        btnLogin = (Button)findViewById(R.id.btnlogin);
        edtemail = (MaterialEditText) findViewById(R.id.edtemail);
        edtpassword = (MaterialEditText) findViewById(R.id.edtpassword);
        edtname = (MaterialEditText) findViewById(R.id.edtname);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);


         auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(Register.this, Login.class);
                startActivity(login);
            }
        });
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registeruser();
            }
        });
    }

    private void registeruser() {

        final String Name = edtname.getText().toString();
        final String Email = edtemail.getText().toString();
        final String Password = edtpassword.getText().toString();



        if(TextUtils.isEmpty(Name)){
            Toast.makeText(this,"Please enter full name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(Password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        else {

            auth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                User user = new User(
                                       Email,
                                        Name,
                                        Password
                                );
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Intent log = new Intent(Register.this, Login.class);
                                            startActivity(log);
                                            Toast.makeText(Register.this, "Register Succesfully !", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Register.this, "Register Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
