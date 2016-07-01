package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by zsurani on 6/27/16.
 */
public class TweetsArrayAdapter  extends ArrayAdapter<Tweet> {

    Tweet tweet;
    TwitterClient client;
    boolean pressed = false;

    public TweetsArrayAdapter(Context context, List<Tweet>tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        final ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        final TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView timeStamp = (TextView) convertView.findViewById(R.id.timeStamp);



        tvName.setText(tweet.getUser().getName());
        //timeStamp.setText(tweet.getRelativeTimeAgo());
        tvUserName.setText(tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        timeStamp.setText(tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
        ivProfileImage.setImageResource(android.R.color.transparent);

        ivProfileImage.setTag(tweet.getUser());

        ivProfileImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intent = new Intent(getContext(), UserProfileActivity.class);

                intent.putExtra("user", Parcels.wrap(ivProfileImage.getTag()));
                //Toast.makeText(getContext(), tweet.getUser().getName(), Toast.LENGTH_SHORT).show();
                getContext().startActivity(intent);
            }
        });

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);


        final ImageView retweeter = (ImageView) convertView.findViewById(R.id.button);
        retweeter.setTag(tweet.getUid());

        retweeter.setOnClickListener(new View.OnClickListener(){
            public void onClick (View v) {

                client = TwitterApplication.getRestClient();
                client.reTweet ((Long) retweeter.getTag(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                        Log.d("DEBUG", json.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                    }
                });

            }
        });

        final ImageView favoriter = (ImageView) convertView.findViewById(R.id.imageView);
        favoriter.setTag(tweet.getUid());


        if (!pressed) {
            pressed = true;
            favoriter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    client = TwitterApplication.getRestClient();
                    client.favorite((Long) favoriter.getTag(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("DEBUG", json.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                        }
                    });

                }
            });
        }
        else {
            pressed = false;
            favoriter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    client = TwitterApplication.getRestClient();
                    client.unfavorite((Long) favoriter.getTag(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("DEBUG", json.toString());
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                        }
                    });

                }
            });
        }


        return convertView;

    }

}

