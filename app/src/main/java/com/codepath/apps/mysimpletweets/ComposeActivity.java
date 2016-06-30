package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    String post;
    Tweet tweet;
    User u;
    EditText etPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);


//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//
//                tweet = Tweet.fromJSON(response);
////                tweet.setUser(u);
////                tweet.setCreatedAt(u.getCreatedAt());
////                tweet.setName(u.getName());
////                tweet.setUid(u.getUid());
////                tweet.setBody(etPost.getText().toString());
//            }
    }

    public void onReturn(View view) {

        etPost = (EditText) findViewById(R.id.etPost);

        client = TwitterApplication.getRestClient();
        client.postUserTimeline(etPost.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweet = Tweet.fromJSON(response);
                Intent data = new Intent();
                data.putExtra("tweet", Parcels.wrap(tweet));
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }


}