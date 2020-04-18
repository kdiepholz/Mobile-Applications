package com.example.knowyourgovernment_diepholz;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class PoliticalAsync extends AsyncTask<String, Integer, String> {

    private MainActivity mainActivity;

    private static final String TAG = "PoliticalAsync";


    private final String url = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyBIFs4lBC77M_GZlQqaSai5pk8PcdQLQ4A&address=";

    public PoliticalAsync(MainActivity main){
        this.mainActivity = main;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected void onPostExecute(String s){
        //super.onPostExecute(s);
        ArrayList<PoliticalOfficial> politicalOfficials = parseJSON(s);
        mainActivity.updateData(politicalOfficials);
    }



    @Override
    protected String doInBackground(String...params) {
        Log.d(TAG, "PoliticalAsync... doInBackground" + params[0]);

            StringBuilder sb = new StringBuilder();


            try {
                URL url1 = new URL(url + params[0]);

                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                connection.setDoInput(true);
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                int status = connection.getResponseCode();
                /*if(status != HttpURLConnection.HTTP_OK){
                    inputStream = connection.getErrorStream();
                }
                else{
                    inputStream = connection.getInputStream();
                }
                */
                BufferedReader bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));

                String yeet;

                while ((yeet = bufferedReader.readLine()) != null) sb.append(yeet).append('\n');

                return sb.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }


    private ArrayList<PoliticalOfficial> parseJSON(String s){
        ArrayList<PoliticalOfficial> politicalList = new ArrayList<>();
        String [] position = new String [1000];
        System.out.println(s);
        if(s != "") {
            try {

                JSONObject jsonObject = new JSONObject(s);


                JSONObject normalizedInput = jsonObject.getJSONObject("normalizedInput");

                //if(normalizedInput !=null){

                //}


                //String location = normalizedInput.getString("city")+" " + normalizedInput.getString("state") + " " + normalizedInput.getString("zip");
                //mainActivity.getData(location);


                JSONArray offices = jsonObject.getJSONArray("offices");
                JSONArray officials = jsonObject.getJSONArray("officials");


                politicalList.add(new PoliticalOfficial(normalizedInput.getString("city") + " " + normalizedInput.getString("state") + " " + normalizedInput.getString("zip")));


                //need a for loop to store everything...
                for (int i = 0; i < offices.length(); i++) {
                    JSONObject offices1 = (JSONObject) offices.get(i);

                    JSONArray jsonArray = offices1.getJSONArray("officialIndices"); //get official indices from array and store them
                    for (int j = 0; j < jsonArray.length(); j++) {

                        int i1 = (int) jsonArray.get(j);
                        if (position[i1] == null) {
                            position[i1] = offices1.getString("name");

                        } else {
                            String s1 = position[i1];
                            position[i1] = s1 + offices1.getString("name");
                        }
                    }
                }
                for (int k = 0; k < officials.length(); k++) {

                    JSONObject jsonObject1 = (JSONObject) officials.get(k);


                    String partyName = "";

                    if (jsonObject1.has("party")) {
                        //setting Republican and democrat, otherwise nothing

                        if (jsonObject1.getString("party").contains("Rep")) {
                            partyName = "Republican";
                        } else if (jsonObject1.getString("party").contains("Dem")) {
                            partyName = "Democrat";
                        }//also need "Democratic"
                        else if (jsonObject1.getString("party").contains("Dem")) {
                            partyName = "Democrat";
                        } else {
                            partyName = "";
                        }
                    } else {
                        partyName = ""; //nothing if independant, no name required
                    }

                    PoliticalOfficial politicalOfficial = new PoliticalOfficial(position[k], jsonObject1.getString("name"), partyName);
                    //time to fill everything in
                    if (jsonObject1.has("address")) {

                        JSONArray jsonArray = jsonObject1.getJSONArray("address");

                        String fullAddress = "";

                        if (!fullAddress.equals(""))
                            //if its not empty, have to make it all one line...

                            fullAddress = fullAddress + "\n"; //line 1

                        JSONObject jsonObject2 = (JSONObject) jsonArray.get(0);
                        if (jsonObject2.has("line1"))
                            fullAddress = fullAddress + jsonObject2.getString("line1") + "\n";

                        if (jsonObject2.has("line2"))
                            fullAddress = fullAddress + jsonObject2.getString("line2") + "\n";

                        if (jsonObject2.has("line3"))
                            fullAddress = fullAddress + jsonObject2.getString("line3") + "\n";

                        fullAddress = fullAddress + jsonObject2.getString("city") + " " + jsonObject2.getString("state") + " " + jsonObject2.getString("zip");

                        politicalOfficial.setAddress(fullAddress);
                    }

                    //now for URL
                    if (jsonObject1.has("urls")) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("urls");
                        politicalOfficial.setWebsite((String) jsonArray.get(0));
                    }
                    //now for Phones

                    if (jsonObject1.has("phones")) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("phones");
                        politicalOfficial.setPhone((String) jsonArray.get(0));
                    }

                    //emails...

                    if (jsonObject1.has("emails")) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("emails");
                        politicalOfficial.setEmail((String) jsonArray.get(0));
                    }

                    //youtube,fb,twitter,google plus

                    if (jsonObject1.has("photoUrl"))
                        politicalOfficial.setPhoto(jsonObject1.getString("photoUrl"));

                    if (jsonObject1.has("channels")) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("channels");

                        //looks like they are in the order google plus, facebook, twitter, need for loop

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            if (jsonObject2.getString("type").equals("Facebook"))
                                politicalOfficial.setFacebook(jsonObject2.getString("id"));

                            if (jsonObject2.getString("type").equals("Twitter"))
                                politicalOfficial.setTwitter(jsonObject2.getString("id"));

                            if (jsonObject2.getString("type").equals("GooglePlus"))
                                politicalOfficial.setGoogle(jsonObject2.getString("id"));

                            if (jsonObject2.getString("type").equals("Youtube"))
                                politicalOfficial.setYoutube(jsonObject2.getString("id"));
                        }
                    }

                    politicalList.add(politicalOfficial);

                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }



        return politicalList;
    }




}
