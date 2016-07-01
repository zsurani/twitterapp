package com.codepath.apps.mysimpletweets;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class UserProfileActivity extends AppCompatActivity {

    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));
        client = TwitterApplication.getRestClient();
        client.showUserInfo (user.getScreenName(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                populateProfileHeader(user);
            }
        });

        if (savedInstanceState == null) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(user.getScreenName());
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void populateProfileHeader(User user) {
        TextView tvName = (TextView) findViewById(R.id.Name);
        TextView tvTagLine = (TextView) findViewById(R.id.tvLine);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollow);
        TextView tvFollowing = (TextView) findViewById(R.id.tFollowing);
        //TextView timeStamp = (TextView) findViewById(R.id.timeStamp); //timestamp
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivImage);

        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFriendsCount() + " Following");
        //timeStamp.setText(user.getRelativeTimeAgo(user.getCreatedAt()));
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }


}
