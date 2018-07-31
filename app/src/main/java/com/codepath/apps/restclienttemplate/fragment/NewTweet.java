package com.codepath.apps.restclienttemplate.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.activities.TwitterTimeline;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NewTweet extends DialogFragment  {



    private Button newtweet_button;
    private EditText newtweet_text;
    private TextView newtweet_count;
    private ImageButton newtweet_close;
    private ImageView newtweet_user_profile;
    private TextView newtweet_user_name;
    private TextView newtweet_user_screen_name;
    private TwitterClient twitterClient;
    private User user ;



    public static NewTweet newInstance (){
        return new NewTweet();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.newtweet,null);

        twitterClient = TwitterApp.getTwitterClient(getContext());

      getCurrentUser();
        newtweet_button=view.findViewById(R.id.newtweet_button);
        newtweet_count=view.findViewById(R.id.newtweet_count);
        newtweet_text=view.findViewById(R.id.newtweet_text);
        newtweet_close=view.findViewById(R.id.newtweet_close);
        newtweet_user_profile=view.findViewById(R.id.newtweet_user_profile);
        newtweet_user_name=view.findViewById(R.id.newtweet_user_name);
        newtweet_user_screen_name=view.findViewById(R.id.newtweet_user_screen_name);

        newtweet_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              sendTweet(newtweet_text.getText().toString().trim());
              dismiss();
            }
        });

        newtweet_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
        newtweet_text.addTextChangedListener(new TextCounter(newtweet_text));

        builder.setView(view);
        Dialog dialog   = builder.create();
        return dialog ;

    }

    private void sendTweet(String status) {
        twitterClient.postTweet(new JsonHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Tweet tweet =Tweet.fromJSON(response);
                                        Timeline.tweets.add(tweet);
                                        Timeline.tweetAdapter.notifyItemInserted(0);

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                        //super.onFailure(statusCode, headers, throwable, errorResponse);
                                    }
                                }
                , status);

    }

    private void getCurrentUser() {
        twitterClient.getCurrentUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user=User.fromJSON(response);
                newtweet_user_screen_name.setText("@"+user.screen_name);
                newtweet_user_name.setText(user.name);
                Glide.with(getContext())
                    .load(Uri.parse(user.profile_image_url))
                        .into(newtweet_user_profile);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
               // super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public int charLeft(String string){
        return 140 - string.length();
    }

    private class TextCounter implements TextWatcher {

        View view ;
        public TextCounter(View view ){
            this.view=view;

        }
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()){
                case  R.id.newtweet_text:
                    newtweet_count.setText(String.valueOf(charLeft(newtweet_text.getText().toString())));
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (newtweet_text.getText().toString().length() > 140){
                newtweet_button.setEnabled(false);
            }
            else {
                newtweet_button.setEnabled(true);
            }

        }
    }
}
