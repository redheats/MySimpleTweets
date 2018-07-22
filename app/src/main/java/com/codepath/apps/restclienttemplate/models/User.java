package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    public String name;
    public long id;
    public String screen_name;
    public String profile_image_url;
    public String location;

    public static User fromJSON(JSONObject jsonObject){
        User user = new User();

        try {
            user.id = jsonObject.getLong("id");
            user.name = jsonObject.getString("name");
            user.screen_name = jsonObject.getString("screen_name");
            user.location = jsonObject.getString("location");
            user.profile_image_url = jsonObject.getString("profile_image_url");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }
}
