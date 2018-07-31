package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {

    public String text;
    public long id;
    public String created_at;
    public User user;
    public Media media;

    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();

        try {
            tweet.text = jsonObject.getString("text");
            tweet.id = jsonObject.getLong("id");
            tweet.created_at = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            if (jsonObject.has("extended_entities")){
                tweet.media = Media.fromJSON(jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0));
            }
            else {
                tweet.media = null;
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return tweet;

    }

    public  Tweet(){

    }
}
