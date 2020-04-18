package com.example.knowyourgovernment_diepholz;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {


    private TextView location;
    private TextView position;
    private TextView name;
    private TextView party;
    private TextView website;
    private TextView email;
    private TextView address;
    private TextView phoneNumber;
    private View view;
    private ImageView picture;
    private ImageView fb;
    private ImageView google;
    private ImageView twitter;
    private ImageView youtube;
    PoliticalOfficial politicalOfficial;
    String string;
    Boolean aBoolean;
    Boolean bBoolean = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.politicalofficial);

        location = findViewById(R.id.location);
        position = findViewById(R.id.Position);
        name = findViewById(R.id.name);
        party = findViewById(R.id.party);
        website = findViewById(R.id.website_info);
        email = findViewById(R.id.email_info);
        address = findViewById(R.id.Address_Info);
        phoneNumber = findViewById(R.id.phone_info);
        picture = findViewById(R.id.imageView);
        fb = findViewById(R.id.Facebook);
        google = findViewById(R.id.Google);
        twitter = findViewById(R.id.Twitter);
        youtube = findViewById(R.id.Youtube);

        view = this.getWindow().getDecorView();

        //MAKE BACKGROUND COLOR

        Intent intent = getIntent();
        politicalOfficial = (PoliticalOfficial) intent.getSerializableExtra("official");
        location.setText(intent.getStringExtra("location"));

        view.setBackgroundColor(Color.parseColor("#000000"));
        position.setText(politicalOfficial.getPosition());
        name.setText(politicalOfficial.getName());
        party.setText(politicalOfficial.getParty());

        if (politicalOfficial != null) {
            position.setText(politicalOfficial.getPosition());

            name.setText(politicalOfficial.getName());

            //black if no party specified
            //view.setBackgroundColor(Color.parseColor("#000000"));

            //red if republican
            if (politicalOfficial.getParty().contains("Dem")) {
                view.setBackgroundColor(Color.parseColor("#0000ff"));
            } else if (politicalOfficial.getParty().contains("Rep")) {
                view.setBackgroundColor(Color.parseColor("#ff0000"));
            }
            else{
                view.setBackgroundColor(Color.parseColor("white"));
            }
            putImage(politicalOfficial.getPhoto());


            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(OfficialActivity.this, PoliticalOfficialBIG.class);
                    intent1.putExtra("official", politicalOfficial);
                    intent1.putExtra("location", string);
                    startActivity(intent1);
                }
            });
            //phone number finder...if no number make an error

            if (!politicalOfficial.getPhone().equals("")) {
                phoneNumber.setText(politicalOfficial.getPhone());
                phoneNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String realPhone = politicalOfficial.getPhone().replace("(", ""); //replacing parentheses with nothing
                        realPhone = realPhone.replace(")", "-"); //replacing last parentheses with a dash
                        Intent intent1 = new Intent(Intent.ACTION_DIAL); //cannot use toString, have to decode the phone number myself as above^
                        intent1.setData(Uri.parse("Telephone: " + realPhone));

                        //start action

                        if (intent1.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent1);
                        }
                    }
                });
            }

            if (!politicalOfficial.getAddress().equals("")) {
                address.setText(politicalOfficial.getAddress());


                address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String realAddress = politicalOfficial.getAddress().replace("\n", " "); //have to replace new line to make it all one line (for google maps)
                        Uri uri = Uri.parse(Uri.encode(realAddress));
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                        intent1.setPackage("com.google.android.apps.maps");


                        //start action

                        if (intent1.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent1);
                        }
                    }
                });
            }
            if (!politicalOfficial.getEmail().equals("")) {
                email.setText(politicalOfficial.getEmail());

                email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] realEmails = new String[]{
                                (politicalOfficial.getEmail())
                        };
                        Intent intent1 = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));

                        intent1.putExtra(Intent.EXTRA_EMAIL, realEmails);

                        //start action

                        if (intent1.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent1);
                        }
                    }
                });
            }

            if (!politicalOfficial.getWebsite().equals("") && networkCheck()) {
                website.setText(politicalOfficial.getWebsite());

                website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String string = politicalOfficial.getWebsite();
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(string));
                        startActivity(intent1);
                    }
                });
            }
            if (!politicalOfficial.getYoutube().equals("")) {
                youtube.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = politicalOfficial.getYoutube();
                        Intent intent1;
                        try {
                            intent1 = new Intent(Intent.ACTION_VIEW);

                            intent1.setPackage("com.google.android.youtube");
                            intent1.setData(Uri.parse("https://www.youtube.com/" + url));
                            startActivity(intent1);
                        } catch (ActivityNotFoundException e) { //if app is not found use browser?
                            startActivity(new Intent(Intent.ACTION_VIEW));
                            Uri.parse("https://www.youtube.com/" + url);
                        }
                    }
                });
            } else { //if youtube is not there, make it invisible
                youtube.setVisibility(View.INVISIBLE);
            }

            if (!politicalOfficial.getFacebook().equals("")) {
                fb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://facebook.com/" + politicalOfficial.getFacebook();

                        Intent intent1;

                        try {
                            intent1 = new Intent(Intent.ACTION_VIEW);

                            intent1.setPackage("com.google.android.facebook");

                            intent1.setData(Uri.parse(url));

                            startActivity(intent1);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW));
                            Uri.parse(url);
                        }
                    }
                });
            } else {
                fb.setVisibility(View.INVISIBLE);
            }

            if (!politicalOfficial.getTwitter().equals("")) {
                twitter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1;
                        String url = politicalOfficial.getTwitter();

                        try {
                            getPackageManager().getPackageInfo("com.twitter.android", 0);
                            intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + url)); //twitter URL full

                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //adding new flags to check for errors
                        } catch (Exception e) {
                            intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + url)); //if app not found, use Browser
                        }
                        startActivity(intent1);
                    }
                });
            } else {
                twitter.setVisibility(View.INVISIBLE);
            }

            if (!politicalOfficial.getGoogle().equals("")) {
                google.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = politicalOfficial.getGoogle();
                        Intent intent1;

                        try {
                            intent1 = new Intent(Intent.ACTION_VIEW);
                            intent1.setPackage("com.google.android.apps.plus");

                            intent1.putExtra("customAppUri", url);

                            startActivity(intent1);
                        } catch (ActivityNotFoundException e) { //if google plus app not found, use browser
                            startActivity(new Intent(Intent.ACTION_VIEW));
                            Uri.parse("https://plus.google.com/" + url);
                        }
                    }
                });
            } else {
                google.setVisibility(View.INVISIBLE);
            }
        }
    }




    //put image from other method
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
}
