package com.example.eventhandlingwithdatabinding;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.internal.ContextUtils;

public class MainViewModel {
    private String name;

    public MainViewModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void showToast1(View view) {
        Toast.makeText(view.getContext(), "Method Reference using View", Toast.LENGTH_SHORT).show();
    }

    public void showLog1(View view) {
        Log.e("MethodReference", "Method Reference No Use View");
    }

    public void showLog2() {
        Log.e("ListenerBinding", "Listener Binding No Param");
    }

    public void showLog3(String message) {
        Log.e("ListenerBinding", "Listener Binding With Param: " + message);
    }

    public void showLog4(View view, String message) {
        Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showLog5(UserViewModel userViewModel) {
        Log.e("ListenerBinding", userViewModel.getName() + ", " + userViewModel.getAddress());
    }
}
