package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.MutableByte;
import android.view.View;
import android.widget.Toast;

import com.example.testapp.api.ServiceBuilder;
import com.example.testapp.model.Post;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MyRecycleAdapter.ItemClicked {

    private RecyclerView recyclerView;
    private final int REQUST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        getPost();
    }

    private void getPost() {

        new ServiceBuilder().retrofitService.getPost().enqueue(new Callback<ArrayList<Post>>() {

            @Override
            public void onResponse(Call<ArrayList<Post>> call, Response<ArrayList<Post>> response) {

                if (response.isSuccessful()){
                    ArrayList arrayList = new ArrayList();

                    for (int i = 0; i < 10; i++) {
                        arrayList.add(response.body().get(i).getTitle());
                    }

                    addTitlesToRecyclerView(arrayList);
                }
                else {
                    Toast.makeText(MainActivity.this, response.code() + " " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Post>> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addTitlesToRecyclerView(ArrayList arrayList) {

        MyRecycleAdapter recycleAdapter = new MyRecycleAdapter(this, arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recycleAdapter);
    }


    @Override
    public void onItemClicked(String title) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
    }

    public void getLocation(View view) {

        CancellationToken token = new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @Override
            public CancellationToken onCanceledRequested(OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUST_CODE);
            return;
        }

        new FusedLocationProviderClient(this)
                .getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, token)
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            Toast.makeText(MainActivity.this, "" + location.getLatitude() + ", "
                                    + location.getLongitude(), Toast.LENGTH_SHORT).show();
                        }

                        else Toast.makeText(MainActivity.this, "Could not find location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void showAlertDialog(){
//        new AlertDialog.Builder(this)
//                .setTitle("Enable location access")
//                .setMessage("Do you want to enable location access?")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //Prompt the user once explanation has been shown
//                        ActivityCompat.requestPermissions(MainActivity.this,
//                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                                        Manifest.permission.ACCESS_COARSE_LOCATION},
//                                REQUST_CODE);
//                    }
//                })
//                .create()
//                .show();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == REQUST_CODE){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLocation(findViewById(R.id.button));
            }
        }
    }
}