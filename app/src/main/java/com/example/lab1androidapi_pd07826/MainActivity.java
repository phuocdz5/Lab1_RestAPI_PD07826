package com.example.lab1androidapi_pd07826;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin,btnSingUp;
    private EditText email,password;
    private TextView txtForgot,txtSMS;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);
        btnSingUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.LoginUsername);
        password = findViewById(R.id.LoginPassword);
        txtForgot = findViewById(R.id.textForgot);
        txtSMS = findViewById(R.id.textSMS);
        auth = FirebaseAuth.getInstance();
        txtSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SMSPhone.class));
            }
        });
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUp.class));

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Useremail = email.getText().toString();
                String Userpassword = password.getText().toString();
                if(!Userpassword.isEmpty() &&!Useremail.isEmpty()){
                    auth.signInWithEmailAndPassword(Useremail,Userpassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = auth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this,Home.class));
                            }else {
                                Log.w("phuoc", "fail", task.getException());
                                Toast.makeText(MainActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForgot();
            }
        });
    }
    private void showDialogForgot() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.forgot_password, null);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();
        EditText edtSendEmail = view.findViewById(R.id.forgotEmail);
        Button btnSendEmail = view.findViewById(R.id.btnSendEmail);
        Button btnBackToLogin = view.findViewById(R.id.btnBackToLogin);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtSendEmail.getText().toString();
                if(!email.isEmpty()){
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Vui lòng kiểm tra hộp thư", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }else {
                                Log.w("phuoc", "fail", task.getException());
                                Toast.makeText(MainActivity.this, "Gửi thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                alertDialog.dismiss();
            }
        });
    }
}