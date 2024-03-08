package com.example.lab1androidapi_pd07826;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SMSPhone extends AppCompatActivity {
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private  String verification;
    private EditText txtOtp,txtPhone;
    private Button btnGetOTP,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsphone);
        txtOtp = findViewById(R.id.textOTP);
        txtPhone = findViewById(R.id.textPhone);
        btnGetOTP = findViewById(R.id.btnGetOtp);
        btnLogin = findViewById(R.id.btnLogin);
        auth = FirebaseAuth.getInstance();
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                txtOtp.setText(phoneAuthCredential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification = s;
            }
        };
        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = txtPhone.getText().toString();
                if(!phone.isEmpty()){
                    getOtp(phone);
                }else {
                    Toast.makeText(SMSPhone.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = txtOtp.getText().toString();
                if(!otp.isEmpty()){
                    verifyOTP(otp);
                }else {
                    Toast.makeText(SMSPhone.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getOtp(String phone){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+84"+phone)
                        .setTimeout(20L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(callbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyOTP(String otp){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification,otp);
        loginWithSMS(credential);
    }
    private void loginWithSMS(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SMSPhone.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(SMSPhone.this,Home.class));
                        }else {
                            Log.w("phuoc", "fail", task.getException());
                            if(task.getException() instanceof FirebaseAuthInvalidUserException){
                            }
                        }
                    }
                });
    }
}