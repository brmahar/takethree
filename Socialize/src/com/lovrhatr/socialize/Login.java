package com.lovrhatr.socialize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

@SuppressLint("DefaultLocale")
public class Login extends Activity {
	
	private TextView title;
	private EditText username;
	private EditText password;
	private Button login;
	private Button signUp;
	private String getUser;
	private String getPass;
	int authCheck = 0;
	private JSONObject request;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
		setContentView(R.layout.activity_layout);
		title = (TextView)findViewById(R.id.login_title);
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");
		title.setTypeface(typeFace);
		
		Parse.initialize(this, "ZNUGpdAW35nGYe5hvleBl3IndIphZPbZjVfn8Vcn", "JGTTBGebCrZXx2J2nc6TVh3is6bwUBq5hROkFCSI");
		

		login = (Button)findViewById(R.id.login);
		signUp = (Button)findViewById(R.id.signup);
		username = (EditText)findViewById(R.id.email);
		password = (EditText)findViewById(R.id.password);

		login.setOnClickListener(loginClick());

		signUp.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this, Registration.class);
				onPause();
				onStop();
				startActivity(intent);
			}
		});


		loadSavedPreferences();
	}

	private void loadSavedPreferences(){
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
		String sUser = shared.getString("User", "");
		String sPass = shared.getString("Password", "");
		boolean check = shared.getBoolean("stored", false);
		if(check != false){
			ParseUser.logInInBackground(sUser, sPass, new LogInCallback() {

				@Override
				public void done(ParseUser arg0, ParseException arg1) {
					Intent intent = new Intent(Login.this, MainActivity.class);
					onPause();
					onStop();
					startActivity(intent);
				}
				
			});
			
		}

	}

	private void savePreferences(String key, String value){
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);

		Editor edit = shared.edit();
		edit.putString(key, value);
		edit.commit();
	}

	private void savePreferences(String key, boolean value){
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);

		Editor edit = shared.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	private OnClickListener loginClick() {
		return new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				getUser = username.getText().toString();
				getPass = password.getText().toString();
				savePreferences("stored", true);
				savePreferences("User", getUser);
				savePreferences("Password", getPass);
				ParseUser.logInInBackground(getUser, getPass, new LogInCallback() {

					@Override
					public void done(ParseUser arg0, ParseException arg1) {
						Intent intent = new Intent(Login.this, MainActivity.class);
						onPause();
						onStop();
						startActivity(intent);
					}
					
				});
			}



		};
	}


	public void store(final String firstName, final String lastName, String targetId){
		final int actualId = Integer.parseInt(targetId);
		final ParseUser userNew = new ParseUser();

		ParseUser.logInInBackground(getUser, getPass,  new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if(user != null){
					//Well good thing thats over
				}else{
					userNew.setUsername(getUser);
					userNew.setPassword(getPass);
					userNew.put("first_name", firstName);
					userNew.put("last_name", lastName);

					userNew.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							if (e == null) {
								// Hooray! Let them use the app now.
							} else {
								// Sign up didn't succeed. Look at the ParseException
								// to figure out what went wrong
								Toast.makeText(getApplicationContext(), 
										e.toString(), Toast.LENGTH_LONG).show();
							}
						}
					});
				}

			}
		});


	}
}
