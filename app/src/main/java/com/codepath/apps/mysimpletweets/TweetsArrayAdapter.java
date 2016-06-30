package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

/**
 * Created by zsurani on 6/27/16.
 */
public class TweetsArrayAdapter  extends ArrayAdapter<Tweet> {

    Tweet tweet;

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

        View.OnClickListener clickListener;

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
        return convertView;

    }

}

