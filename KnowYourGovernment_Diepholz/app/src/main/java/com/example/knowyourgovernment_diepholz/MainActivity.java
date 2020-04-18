package com.example.knowyourgovernment_diepholz;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private List<PoliticalOfficial> politicalList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PoliticalAdapter mAdapt;
    private MainActivity main = this;
    private TextView current;
    com.example.knowyourgovernment_diepholz.Locator location;
    String enter = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        setContentView(R.layout.activity_main);
        current = findViewById(R.id.Location);
        current.setText("No Location Found, No Data");
        recyclerView = findViewById(R.id.recycler);
        mAdapt = new PoliticalAdapter(politicalList, this);
        location = new Locator(this);
        recyclerView.setAdapter(mAdapt);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void getData(double latitude, double longitude){
        Log.d(TAG, "getData: Latitude"+ latitude + "Longitude:"+longitude);
        enter = findAddress(latitude,longitude);

        if (enter.equals("")){
            if(networkCheck()){
                new PoliticalAsync(main).execute(enter);
            }
        }
    }

    public String findAddress (double latitude, double longitude){
        Log.d(TAG,"findAddress: Latitude "+ latitude + ", Longitude: "+ longitude);

        List<Address> addresses = null;

        for(int i = 0; i <3; i ++){
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            try{
                Log.d(TAG, "Finding Addresses...");
                addresses = geocoder.getFromLocation(latitude,longitude,1);
                Log.d(TAG,"Number of Addresses" +addresses.size());

                StringBuilder sb = new StringBuilder();

                for (Address a : addresses){
                    if (a.getPostalCode()!=null){
                        return a.getPostalCode();
                    }
                }

                return sb.toString();
            }catch (IOException e){
                Log.d(TAG,"Error in Find Addresses" + e.getMessage());
            }

        }
        return "";
    }

    public void noLocationAvailable(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Location is off...");

        alert.setMessage("Turn on Location Settings in order to Access Location").setCancelable(true);

        alert.show();
    }

    @Override
    protected void onDestroy(){
        location.power();
        super.onDestroy();
    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.search:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final EditText editText = new EditText(this);
                editText.setGravity(Gravity.CENTER_HORIZONTAL);

                builder.setView(editText);
                builder.setIcon(R.mipmap.ic_launcher_round);
                builder.setPositiveButton("GO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enter = editText.getText().toString();

                        if(networkCheck()){
                            new PoliticalAsync(main).execute(enter);
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setTitle("Select Your Location");
                builder.setMessage("Enter a State, City, or Zip Code:");

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;

            case R.id.info:
                Intent intent1 = new Intent(MainActivity.this,ActivityInfo.class);

                startActivity(intent1);
                return true;
                default:
                    return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onClick(View view){
        int position = recyclerView.getChildAdapterPosition(view);

        //PoliticalOfficial official = politicalList.get(position);
        //PoliticalOfficial politicalOfficial = politicalList.get(position);
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        if(politicalList.size() !=0){
            intent.putExtra("official", politicalList.get(position));

            intent.putExtra("location", current.getText());
            startActivityForResult(intent,1);
        }
    }

    public void onClick(View view, int position){

        //PoliticalOfficial official = politicalList.get(position);

        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);

        intent.putExtra("official", politicalList.get(position));

        intent.putExtra("location", current.getText());

        startActivityForResult(intent,1);
    }

    @Override
    public boolean onLongClick(View v){
        int position = recyclerView.getChildLayoutPosition(v);
        onClick(v,position);
        return true; //default true, cannot switch

    }

    private boolean networkCheck(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null){
            return true;
        }
        else if(networkInfo.isConnected()){
            return true;
        }

        current = findViewById(R.id.Location);
        current.setText("Location Cannot be Found!");


        politicalList.clear();

        mAdapt.notifyDataSetChanged();

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("NO NETWORK CONNECTION FOUND!");

        alert.setMessage("Know Your Government cannot Load Data without an Internet Connection!");

        final AlertDialog alertDialog = alert.create();

        alertDialog.show();
        return false; //if runs fully through, return false and show alert

    }

   public void updateData(List<PoliticalOfficial> officials){

        if(officials.isEmpty()){
            current = findViewById(R.id.Location);

            //current.setText(politicalOfficial.getPosition());

            return;
        }

        PoliticalOfficial politicalOfficial = officials.get(0);
        officials.remove(0);

        current = findViewById(R.id.Location);

        current.setText(politicalOfficial.getPosition());
        //current.setText(location.toString());
        politicalList.clear();
        politicalList.addAll(officials);
        //new PoliticalAsync(main).execute();
        mAdapt.notifyDataSetChanged();
    }

}


