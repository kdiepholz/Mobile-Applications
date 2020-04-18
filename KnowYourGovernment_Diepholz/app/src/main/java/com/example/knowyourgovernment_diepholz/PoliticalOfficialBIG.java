package com.example.knowyourgovernment_diepholz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class PoliticalOfficialBIG extends AppCompatActivity {

    private TextView location;

    private TextView position;
    private TextView name;
    private ImageView picture;
    private PoliticalOfficial politicalOfficial;
    private View view;
    private String string;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.politicalofficialbig);

        location = findViewById(R.id.location2);

        name = findViewById(R.id.name);
        position = findViewById(R.id.position);
        picture = findViewById(R.id.Big_Picture);
        view = this.getWindow().getDecorView();


        //SETTING UP COLORS FOR DIFFERENT PARTIES

        Intent intent = getIntent();
        politicalOfficial = (PoliticalOfficial) intent.getSerializableExtra("official");
        string = intent.getStringExtra("location");

        location.setText(string);

        position.setText(politicalOfficial.getPosition());

        name.setText(politicalOfficial.getName());

        //black if no party specified
        //view.setBackgroundColor(Color.parseColor("#000000"));

        //red if republican
        if(politicalOfficial.getParty().contains("Rep")){
            getWindow().getDecorView().setBackgroundColor(Color.RED);
        }
        else if (politicalOfficial.getParty().contains("Dem")){
            getWindow().getDecorView().setBackgroundColor(Color.BLUE);
        }

        putImage(politicalOfficial.getPhoto());

    }

    //network check
    private boolean networkCheck(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null){
            return true;
        }
        else if(networkInfo.isConnected()){
            return true;
        }
        else {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("NO NETWORK CONNECTION FOUND!");
            alert.setMessage("Know Your Government cannot be run without an Internet Connection!");
            alert.show();
            return false;
        }
}

//load image from internet into the big view
    //used picasso
public void putImage(final String url){
        if(networkCheck()){
            if(url != null){
                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        final String failURL = url.replace("http:", "https:");

                        picasso.load(failURL).error(R.drawable.brokenimage);
                        picasso.load(failURL).placeholder(R.drawable.placeholder);
                        picasso.load(failURL).into(picture);
                        }
                    }
                ).build();
                picasso.load(url).error(R.drawable.brokenimage);
                picasso.load(url).placeholder(R.drawable.placeholder);
                picasso.load(url).into(picture);

            }else {
                Picasso.with(this).load(url).error(R.drawable.brokenimage);
                Picasso.with(this).load(url).placeholder(R.drawable.placeholder);
                Picasso.with(this).load(url).into(picture);
            }
        }
        else{
            picture.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
        }
}

}
