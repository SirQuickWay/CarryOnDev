package com.project.carryon.carryon;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//GEOCODE TO GET COORDINATES
public class  GetCoordinates extends AsyncTask<String,Void,String> {

    private boolean addressFound;
    private String lat, lng;
    private String Address1 = "",
            Address2 = "",
            City = "",
            State = "",
            Country = "",
            County = "",
            PIN = "",
            Area ="";
    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }
    @Override
    protected String doInBackground(String... strings) {
        String response;
        try{
            String address = strings[0];
            HttpDataHandler http = new HttpDataHandler();
            String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyAZos6hzsvLWSS5_aCg1d7JIPsCud7d4QQ", address);
            response = http.getHttpData(url);

            JSONObject jsonObj =  new JSONObject(response);
            lat = ((JSONArray)jsonObj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
            lng = ((JSONArray)jsonObj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
            String Status = jsonObj.getString("status");

            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (!TextUtils.isEmpty(long_name) || !long_name.equals(null) || long_name.length() > 0 || !long_name.equals("")) {
                        if (Type.equalsIgnoreCase("street_number")) {
                            Address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            Address1 = Address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            Address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            City = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            County = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            State = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            Country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        } else if (Type.equalsIgnoreCase("neighborhood")) {
                            Area = long_name;
                        }
                    }
                }
            }
            addressFound = true;
            return response;
        }catch (Exception ex){
            addressFound = false;
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
       /* try{
            JSONObject jsonObj =  new JSONObject(s);
            //Country = ((JSONArray)jsonObj.get("results")).getJSONObject(0).getJSONObject("address_component").getJSONObject("location").get("lat").toString();
            lat = ((JSONArray)jsonObj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
            lng = ((JSONArray)jsonObj.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();
            String Status = jsonObj.getString("status");

            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (! TextUtils.isEmpty(long_name) || !long_name.equals(null) || long_name.length() > 0 || !long_name.equals("")) {
                        if (Type.equalsIgnoreCase("street_number")) {
                            Address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            Address1 = Address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            Address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            City = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            County = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            State = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            Country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        }else if( Type.equalsIgnoreCase("neighborhood")){
                            Area = long_name;
                        }
                    }
                }
            }
            Log.i("COu",PIN);


        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress1() {
        return Address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getCountry() {
        return Country;
    }

    public String getCounty() {
        return County;
    }

    public String getPIN() {
        return PIN;
    }

    public String getArea() {
        return Area;
    }

    public boolean isAddressFound() {
        return addressFound;
    }
}
