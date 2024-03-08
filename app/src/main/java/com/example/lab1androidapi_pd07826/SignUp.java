package com.example.lab1androidapi_pd07826;

import static com.google.firebase.auth.FirebaseAuth.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText email,password;
    private Button btnBack, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email = findViewById(R.id.SignUsername);
        password = findViewById(R.id.SignPassword);
        auth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();

                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    auth.createUserWithEmailAndPassword(userEmail, userPassword)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = auth.getCurrentUser();
                                        Toast.makeText(SignUp.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Log.w("phuoc", "fail", task.getException());
                                        Toast.makeText(SignUp.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(SignUp.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}