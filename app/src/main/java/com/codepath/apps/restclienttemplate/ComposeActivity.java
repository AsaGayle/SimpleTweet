package com.codepath.apps.restclienttemplate;

import android.animation.TimeAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_TWEET_LENGTH = 140;
    public static final String TAG = "ComposeActivity";

    TwitterClient client;

    EditText etCompose;
    Button btnTweet;
    //TextInputLayout mtrCompose;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        btnTweet = findViewById(R.id.btnTweet);

        client = TwitterApp.getRestClient(this);

        //Set click listener
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tweetContent = etCompose.getText().toString();
                if( tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Tweet is empty. Please enter text", Toast.LENGTH_LONG).show();
                    return;
                }
                if(tweetContent.length() > MAX_TWEET_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Tweet is too long! Please shorten tweet!", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG).show();
                // Make an API Call to Twitter
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish Tweet");
                        try {
                            Tweet tweet = Tweet.fromJSON(json.jsonObject);
                            Log.i(TAG, "Published tweet says " + tweetContent);
                            Intent data = new Intent();
                            data.putExtra("tweet", Parcels.wrap(tweet));
                            setResult(RESULT_OK, data);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish Tweet");
                    }
                });
            }
        });
    }
}
