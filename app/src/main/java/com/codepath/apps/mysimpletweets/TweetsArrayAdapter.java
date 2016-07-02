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

    TwitterClient client;
    boolean pressed = false;

    public TweetsArrayAdapter(Context context, List<Tweet>tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
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

        if (!tweet.getRetweeted()) {
            Picasso.with(getContext()).load(R.mipmap.ic_retweet).into(retweeter);
        }
        else if (tweet.getRetweeted()) {
            Picasso.with(getContext()).load(R.mipmap.ic_redretweeted).into(retweeter);
        }

        if (!tweet.getRetweeted()) {

            retweeter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    tweet.setRetweeted(true);

                    client = TwitterApplication.getRestClient();
                    client.reTweet((Long) retweeter.getTag(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("DEBUG", json.toString());
                            Picasso.with(getContext()).load(R.mipmap.ic_redretweeted).into(retweeter);
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

            retweeter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    tweet.setRetweeted(false);

                    client = TwitterApplication.getRestClient();
                    client.unreTweet((Long) retweeter.getTag(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("DEBUG", json.toString());
                            Picasso.with(getContext()).load(R.mipmap.ic_retweet).into(retweeter);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                        }
                    });

                }
            });
        }

        final ImageView favoriter = (ImageView) convertView.findViewById(R.id.imageView);
        favoriter.setTag(tweet.getUid());

        if (!tweet.getFavorited()) {
            Picasso.with(getContext()).load(R.mipmap.ic_favorite).into(favoriter);
        }
        else if (tweet.getFavorited()) {
            Picasso.with(getContext()).load(R.mipmap.ic_redfavorited).into(favoriter);
        }

        if (!tweet.getRetweeted()) {

            favoriter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    tweet.setFavorited(true);

                    client = TwitterApplication.getRestClient();
                    client.favorite((Long) favoriter.getTag(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("DEBUG", json.toString());
                            Picasso.with(getContext()).load(R.mipmap.ic_redfavorited).into(favoriter);
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

            favoriter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    tweet.setFavorited(false);

                    client = TwitterApplication.getRestClient();
                    client.unfavorite((Long) favoriter.getTag(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                            Log.d("DEBUG", json.toString());
                            Picasso.with(getContext()).load(R.mipmap.ic_favorite).into(favoriter);
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

