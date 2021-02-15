package com.example.loadin_app.data.services;


import com.example.loadin_app.data.RetroBoxService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import odu.edu.loadin.common.BoxSize;
import odu.edu.loadin.common.User;
import odu.edu.loadin.common.UserLoginRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserServiceImpl {
    RetroUserService retroService;




    public UserServiceImpl(){
        this("http://localhost:9000/");

    }
    public UserServiceImpl(String baseUrl){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retroService = retrofit.create(RetroUserService.class);
    }

    public User getUser(int id) throws ExecutionException, InterruptedException {
        CompletableFuture<DataWrapper<User>> promise = retroService.getUser(id);
        return promise.get().Data;
    }

    public UserLoginStatus login(String email, String password,  User user) throws IOException {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        DataWrapper<UserLoginRequest> wrapper = new DataWrapper<UserLoginRequest>();
        wrapper.Data = request;
        Call<DataWrapper<User>> call = retroService.login(wrapper);

        Response<DataWrapper<User>> response =  call.execute();

        if(!response.isSuccessful()){
            //TODO: how do we get the response back to a json object
            user = null;
            switch (response.code()){
                case 404:  //not found
                    return UserLoginStatus.NotFound;

                case 403: //invalid password ; forbidden
                    return UserLoginStatus.InvalidPassword;

            }
        }


        user = response.body().Data;
        return UserLoginStatus.Ok;

    }
    public enum UserLoginStatus{
        NotFound,
        InvalidPassword,
        Ok
    }
}
