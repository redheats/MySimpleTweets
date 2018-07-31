package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Media {

    public String type;
    public  String media_url;
    public String url;
    public  long id;


    public Media(){

    }
    public static Media fromJSON(JSONObject jsonObject){
        Media media =new Media ();

        try{
            media.id=jsonObject.getLong("id");
            media.type=jsonObject.getString("type");
            media.media_url=jsonObject.getString("media_url");
            if (jsonObject.has("video_info")){
                media.url=jsonObject.getJSONObject("video_info").getJSONArray("variants").getJSONObject(0).getString("url");

            }

        }

        catch (JSONException e){
            e.printStackTrace();
        }
        return media ;
    }


}
