package com.codepath.apps.restclienttemplate.activities;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.adapters.TweetAdapter;
import com.codepath.apps.restclienttemplate.adapters.ViewPagerAdapter;
import com.codepath.apps.restclienttemplate.fragment.Favorites;
import com.codepath.apps.restclienttemplate.fragment.Mention;
import com.codepath.apps.restclienttemplate.fragment.Photos;
import com.codepath.apps.restclienttemplate.fragment.Timeline;
import com.codepath.apps.restclienttemplate.fragment.Tweets;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class ViewUser extends AppCompatActivity {

    Toolbar toolbar ;
public static User user, self ;
public ImageView view_user_profile;
public TextView view_user_name;
public  TextView view_user_followers_count ;
public TextView  view_user_friends_count;



    ImageButton view_user_followed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        user= (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        self = (User) Parcels.unwrap(getIntent().getParcelableExtra("self"));

        view_user_profile=findViewById(R.id.view_user_profile);
        Glide.with(this).load(Uri.parse(user.profile_image_url)).into(view_user_profile);

        view_user_name=findViewById(R.id.view_user_name) ;
        view_user_followers_count = findViewById(R.id.view_user_followers_count);
        view_user_followers_count.setText(String.valueOf(user.followers_count));
      // view_user_friends_count = findViewById(R.id.view_user_friends_count);
       //view_user_friends_count.setText(String.valueOf(view_user_friends_count));

        view_user_name.setText(user.name);


        view_user_followed = findViewById(R.id.view_user_followed);



        if (self != null){
            view_user_followed.setVisibility(View.GONE);




        }






        toolbar = findViewById(R.id.view_user_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.twitter);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_left));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        ViewPager viewPager = findViewById(R.id.view_user_view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.view_user_tab_layout);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter =new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Tweets(), "Tweets");
        viewPagerAdapter.addFragment(new Photos(),"Photos");
        viewPagerAdapter.addFragment(new Favorites(),"Favorites");
        viewPager.setAdapter(viewPagerAdapter);
    }
}


