package com.codepath.apps.restclienttemplate.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.adapters.ViewPagerAdapter;
import com.codepath.apps.restclienttemplate.fragment.Mention;
import com.codepath.apps.restclienttemplate.fragment.NewTweet;
import com.codepath.apps.restclienttemplate.fragment.Timeline;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TwitterTimeline extends AppCompatActivity {



    Toolbar toolbar;
    User user;
    TwitterClient twitterClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_timeline);

        twitterClient = TwitterApp.getTwitterClient(this);
        getCurrentUser();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.twitter);

        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setupWithViewPager(viewPager);



    }

    private void getCurrentUser() {
        twitterClient.getCurrentUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline_menu, menu);
        MenuItem composeAction=menu.findItem(R.id.compose_tweet);
        MenuItem profileAction=menu.findItem(R.id.view_my_profile);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.compose_tweet:
                NewTweet newTweet =NewTweet.newInstance();
                newTweet.show(getSupportFragmentManager(),"compose");
                return true;
            case R.id.view_my_profile:
                Intent intent = new Intent(this, ViewUser.class);
                intent.putExtra("self", Parcels.wrap(user));
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter =new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Timeline(), "Timeline");
        viewPagerAdapter.addFragment(new Mention(),"Mention");
        viewPager.setAdapter(viewPagerAdapter);
    }


}
