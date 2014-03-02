package com.lovrhatr.socialize;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Registration extends Activity {

	private TextView title;
	private EditText username;
	private EditText password;
	private EditText firstName;
	private EditText lastName;
	private EditText email;
	private Button signUp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
		setContentView(R.layout.activity_registration);
		title = (TextView)findViewById(R.id.register_title);
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");
		title.setTypeface(typeFace);
		
		Parse.initialize(this, "ZNUGpdAW35nGYe5hvleBl3IndIphZPbZjVfn8Vcn", "JGTTBGebCrZXx2J2nc6TVh3is6bwUBq5hROkFCSI");
		
		signUp = (Button)findViewById(R.id.register);
		username = (EditText)findViewById(R.id.newUser);
		password = (EditText)findViewById(R.id.passwordNew3);
		firstName = (EditText)findViewById(R.id.newFirst);
		lastName = (EditText)findViewById(R.id.newLast);
		email = (EditText)findViewById(R.id.editTextemail);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		return true;
	}
	
	public void createUser(View view){
		ParseUser newPerson = new ParseUser();
		String pass = password.getText().toString();
		System.out.println(pass);
		newPerson.setUsername(username.getText().toString());
		newPerson.put("first_name", firstName.getText().toString());
		newPerson.put("last_name", lastName.getText().toString());
		newPerson.setEmail(email.getText().toString());
		
		newPerson.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			      Intent intent = new Intent(Registration.this, MainActivity.class);
			      onPause();
			      onStop();
			      startActivity(intent);
			    } else {
			      // Sign up didn't succeed. Look at the ParseException
			      // to figure out what went wrong
			    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Registration.this);

					// set title
					alertDialogBuilder.setTitle("Sign up Error");

					// set dialog message
					alertDialogBuilder
					.setMessage(""+ e)
					.setCancelable(false)
					.setPositiveButton("Try again",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(intent);
						}
					})
					.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							dialog.cancel();
						}
					});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
			    }
			  }
			});
	}

}
