package com.example.hardwaremart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwaremart.databinding.HomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
   HomeBinding binding;
   String currentUserId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.homeToolBar);
        getSupportActionBar().setTitle("HardwareMart");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserProfile();
    }

    private void checkUserProfile(){
        StoreService.StoreApi storeApi = StoreService.getStoreApiInstance();
        Call<Store> call = storeApi.getShopKepperProfile(currentUserId);
        call.enqueue(new Callback<Store>() {
            @Override
            public void onResponse(Call<Store> call, Response<Store> response) {
                if(response.code() == 200){
                    Store store = response.body();
                    Toast.makeText(HomeActivity.this, "Profile already created", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(HomeActivity.this, "Please create profile first", Toast.LENGTH_SHORT).show();
                    sendUserToProfileActivity();
                }
                else
                    Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Store> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Internal server error, Try later", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void sendUserToProfileActivity(){
        Intent in = new Intent(HomeActivity.this,ProfileActivity.class);
        startActivity(in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Logout");
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String tilte = item.getTitle().toString();
        if(tilte.equals("Logout")){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            sendUserToLoginActivity();
        }
        return super.onOptionsItemSelected(item);
    }
    private void sendUserToLoginActivity(){
        Intent in = new Intent(this,LoginActivity.class);
        startActivity(in);
        finish();
    }
}
