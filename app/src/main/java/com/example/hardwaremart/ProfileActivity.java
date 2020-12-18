package com.example.hardwaremart;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.example.hardwaremart.databinding.ShopProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
   ShopProfileBinding binding;
   Uri imageUri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ShopProfileBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},111);
        }

        binding.ivStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent();
                in.setAction(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(in,111);
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(imageUri != null){
                   File file = FileUtils.getFile(ProfileActivity.this, imageUri);
                   RequestBody requestFile =
                           RequestBody.create(
                                   MediaType.parse(Objects.requireNonNull(getContentResolver().getType(imageUri))),
                                   file
                           );

                   MultipartBody.Part body =
                           MultipartBody.Part.createFormData("file", file.getName(), requestFile);

                   String ownerName = binding.etOwnerName.getText().toString();
                   String shopName = binding.etShopName.getText().toString();
                   String contactNumber = binding.etContactNumber.getText().toString();
                   String email = binding.etEmail.getText().toString();
                   String address = binding.etAddress.getText().toString();

                   RequestBody owner = RequestBody.create(okhttp3.MultipartBody.FORM, ownerName);
                   RequestBody sName = RequestBody.create(okhttp3.MultipartBody.FORM,shopName);
                   RequestBody storeNumber = RequestBody.create(okhttp3.MultipartBody.FORM, contactNumber);
                   RequestBody storeEmail = RequestBody.create(okhttp3.MultipartBody.FORM, email);
                   RequestBody storeAddress = RequestBody.create(okhttp3.MultipartBody.FORM, address);
                   RequestBody storeToken = RequestBody.create(okhttp3.MultipartBody.FORM, "45454545454555555");
                   RequestBody shopKeeperId = RequestBody.create(okhttp3.MultipartBody.FORM, FirebaseAuth.getInstance().getCurrentUser().getUid());

                   StoreService.StoreApi storeApi = StoreService.getStoreApiInstance();
                   Call<Store> call = storeApi.saveStoreProfile(body,owner,sName,storeNumber,storeAddress,storeEmail,storeToken,shopKeeperId);

                   call.enqueue(new Callback<Store>() {
                       @Override
                       public void onResponse(Call<Store> call, Response<Store> response) {
                           if(response.code() == 200){
                               Store store = response.body();
                               Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                               finish();
                           }
                           else
                               Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                       }

                       @Override
                       public void onFailure(Call<Store> call, Throwable t) {
                           Toast.makeText(ProfileActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                       }
                   });
               }
               else
                   Toast.makeText(ProfileActivity.this, "Image is mendatory", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111 && resultCode == RESULT_OK){
            imageUri  = data.getData();
            binding.ivStore.setImageURI(imageUri);
        }
    }
}
