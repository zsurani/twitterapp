package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
    TextView lblCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etPost = (EditText) findViewById(R.id.etPost);
        lblCount = (TextView)findViewById(R.id.lblCount);

        // Attached Listener to Edit Text Widget
        etPost.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {

                // Display Remaining Character with respective color
                int count = 140 - s.length();
                lblCount.setText(Integer.toString(count));
                lblCount.setTextColor(Color.GREEN);
                if(count < 10)
                {
                    lblCount.setTextColor(Color.YELLOW);
                }
                if(count < 0)
                {
                    lblCount.setTextColor(Color.RED);
                }

            }
        });


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