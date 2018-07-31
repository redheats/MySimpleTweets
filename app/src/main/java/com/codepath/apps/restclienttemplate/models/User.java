package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    public String name;
    public long id;
    public String screen_name;
    public String profile_image_url;
    public String location;
    public int followers_count;
    public int favourites_count;
    public boolean profile_use_background_image;
    public String profile_background_color;
    public String profile_background_image_url;
    public int friends_count;

    public static User fromJSON(JSONObject jsonObject){
        User user = new User();

        try {
            user.id = jsonObject.getLong("id");
            user.name = jsonObject.getString("name");
            user.screen_name = jsonObject.getString("screen_name");
            user.location = jsonObject.getString("location");
            user.followers_count = jsonObject.getInt("followers_count");
            user.profile_image_url = jsonObject.getString("profile_image_url");
            user.followers_count = jsonObject.getInt("followers_count");
            user.favourites_count = jsonObject.getInt("favourites_count");
            user.profile_use_background_image = jsonObject.getBoolean("profile_use_background_image");
            user.profile_background_image_url = jsonObject.getString("profile_background_image_url");
            user.profile_background_color = jsonObject.getString("profile_background_color");
            user.friends_count = jsonObject.getInt("friends_count");

        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }

    public User(){

    }
}
