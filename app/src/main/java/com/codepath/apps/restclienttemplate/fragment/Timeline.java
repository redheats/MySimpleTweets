package com.codepath.apps.restclienttemplate.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Timeline extends Fragment {
    public static TweetAdapter tweetAdapter;
    public static ArrayList<Tweet> tweets;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TwitterClient client;
    SwipeRefreshLayout swipeRefreshLayout;

    public Timeline(){
        // requires an empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout = view.findViewById(R.id.timeline_refresh);

        tweets = new ArrayList<>();
        client = TwitterApp.getTwitterClient(getContext());

        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView = view.findViewById(R.id.timeline_recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);

        tweetAdapter = new TweetAdapter(getContext(), tweets);
        recyclerView.setAdapter(tweetAdapter);


        populateTimeline();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline();
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){
                    loadMore(tweets.get(tweets.size()-1).id);
                }
            }
        });


    }
    private void loadMore(long id) {
        client.loadMore(new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                for (int i=0;i<response.length();i++){
                                    try {
                                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                                        tweets.add(tweet);
                                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.e("FAILURE", errorResponse.toString());
                            }
                        },
                id);
    }

    private void populateTimeline() {
        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for (int i=0;i<response.length();i++){
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(0);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("FAILURE", errorResponse.toString());
                swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("FAILURE", errorResponse.toString());
                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }
}
