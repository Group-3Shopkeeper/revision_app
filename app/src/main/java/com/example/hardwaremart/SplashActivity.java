package com.example.hardwaremart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwaremart.databinding.SplashBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    SplashBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SplashBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser == null)
                    sendUserToLoginActivity();
                else
                    sendUserToHomeActivity();
            }
        },5000);
    }
    private void sendUserToLoginActivity(){
        Intent in =new Intent(SplashActivity.this,LoginActivity.class);
        startActivity(in);
        finish();
    }
    private void sendUserToHomeActivity(){
       Intent in = new Intent(this,HomeActivity.class);
       startActivity(in);
       finish();
    }
}
