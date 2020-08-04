package com.isaacbfbu.studygroup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.isaacbfbu.studygroup.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.facebook.ParseFacebookUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Activity that = this;

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(that, null, new LogInCallback() {
                    @Override
                    public void done(final ParseUser user, ParseException err) {
                        if (err != null) {
                            Log.e("MyApp","WHAT", err);
                        }
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            GraphRequest request = GraphRequest.newMeRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(
                                                JSONObject object,
                                                GraphResponse response) {
                                            String name = "";
                                            String id = "";
                                            try {
                                                name = object.getString("name");
                                                id = object.getString("id");
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            user.put("displayName", name);
                                            user.put("facebookProfileURL", "https://graph.facebook.com/" + id + "/picture?type=large");
                                            user.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    goMainActivity();
                                                    finish();
                                                }
                                            });
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name");
                            request.setParameters(parameters);
                            request.executeAsync();

                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            goMainActivity();
                            finish();
                        }
                    }
                });
            }
        });
    }

    public void onClickLoginButton(View v) {
        Log.i(TAG, "onClick login button");
        String username = binding.etUsername.getText().toString();
        String password = binding.etPassword.getText().toString();

        Log.i(TAG, "Attempting to login user " + username);
        binding.pbLoading.setVisibility(View.VISIBLE);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                binding.pbLoading.setVisibility(View.INVISIBLE);
                if (e != null) {
                    Log.e(TAG, "Issue with login", e);
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                // navigate to the main activity of the user has signed in properly
                goMainActivity();
                finish();
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickSignupButton(View v) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}