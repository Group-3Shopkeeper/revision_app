package com.example.hardwaremart;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class StoreService {
   public static String baseUrl = "http://192.168.29.40:8080";
   public static StoreApi storeApi = null;

   public static StoreApi getStoreApiInstance(){
       Retrofit retrofit = new Retrofit.Builder()
               .baseUrl(baseUrl)
               .addConverterFactory(GsonConverterFactory.create())
               .build();
       storeApi = retrofit.create(StoreApi.class);
       return  storeApi;
   }

   public interface StoreApi{
     @Multipart
     @POST("/shopkeeper/")
     public Call<Store> saveStoreProfile(@Part MultipartBody.Part file,
                                         @Part("name")RequestBody name,
                                         @Part("shopName") RequestBody shopName,
                                         @Part("contactNumber") RequestBody contactNumber,
                                         @Part("address") RequestBody address,
                                         @Part("email") RequestBody email,
                                         @Part("token") RequestBody token,
                                         @Part("shopKeeperId") RequestBody shopKeeperId);

    @GET("/shopkeeper/view/{id}")
    public Call<Store> getShopKepperProfile(@Path("id") String shopKeeperId);
   }
}
