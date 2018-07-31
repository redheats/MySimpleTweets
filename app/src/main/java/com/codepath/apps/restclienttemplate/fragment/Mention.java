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

public class Mention extends Fragment {
    TwitterClient client;
    public static TweetAdapter tweetAdapter;
    public static ArrayList<Tweet> tweets;
    public static RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    public Mention(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mention, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        client = TwitterApp.getTwitterClient(getContext());
        linearLayoutManager = new LinearLayoutManager(getContext());

        swipeRefreshLayout = view.findViewById(R.id.mention_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateMention();
            }
        });
        recyclerView = view.findViewById(R.id.mention_recyclerView);
        tweets = new ArrayList<>();

        tweetAdapter = new TweetAdapter(getContext(), tweets);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tweetAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (tweets.size() > 0) {
                        loadMoreMention(tweets.get(tweets.size() - 1).id);
                    }
                    else {
                        populateMention();
                    }
                }
                //else if(!recyclerView.canScrollVertically(-1)){
                //loadRecent(tweets.get(0).id);
                //}
            }
        });

        populateMention();

    }

    private void loadMoreMention(long id) {
        client.loadMoreMention(new JsonHttpResponseHandler(){
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

    private void populateMention() {
        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
        client.getMentionTimeline(new JsonHttpResponseHandler(){
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
