package com.lovrhatr.socialize;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateNewEvent extends Activity {

	private EditText eventName;
	private EditText eventDescription;
	private EditText numPeople;
	private EditText date;
	private EditText time;
	private EditText locationDesc;
	private Button plus;
	private Button minus;
	private Button create;
	private int numberPeople;
	private LocationManager locationManager;
	private String provider;
	private SharedPreferences shared;
	private String creator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_new_event);
		SpannableString s = new SpannableString("Socialize");
		s.setSpan(new TypefaceSpan(this, "Pacifico.ttf"), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		// Update the action bar title with the TypefaceSpan instance
		ActionBar actionBar = getActionBar();
		actionBar.setTitle(s);
		Parse.initialize(this, "ZNUGpdAW35nGYe5hvleBl3IndIphZPbZjVfn8Vcn", "JGTTBGebCrZXx2J2nc6TVh3is6bwUBq5hROkFCSI");
		plus = (Button)findViewById(R.id.increase);
		minus = (Button)findViewById(R.id.decrease);
		numPeople = (EditText)findViewById(R.id.editText1);
		create = (Button)findViewById(R.id.create);
		eventName = (EditText)findViewById(R.id.name);
		eventDescription = (EditText)findViewById(R.id.description);
		date = (EditText)findViewById(R.id.editText2);
		time = (EditText)findViewById(R.id.editText3);
		locationDesc = (EditText)findViewById(R.id.editText4);
		numberPeople = 0;
		shared = PreferenceManager.getDefaultSharedPreferences(this);
		create.setOnClickListener(createEvent());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_new_event, menu);
		return true;
	}
	public void increaseNum(View view) {
		// Do something in response to button
		numberPeople++;
		String help = ""+numberPeople;
		numPeople.setText(help);
	}
	public void decreaseNum(View view) {
		// Do something in response to button
		if(numberPeople == -1){
			return;
		}
		String help = ""+numberPeople;
		numberPeople--;
		numPeople.setText(help);
	}

	public OnClickListener createEvent() {
		return new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateNewEvent.this);

				// set title
				alertDialogBuilder.setTitle("Location");

				// set dialog message
				alertDialogBuilder
				.setMessage("The location for this event will be set to your current location. Is this where you want the event to be held?")
				.setCancelable(false)
				.setPositiveButton("Create",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						String name = eventName.getText().toString();
						String eDesc = eventDescription.getText().toString();
						int num = numberPeople;
						String newDate = date.getText().toString();
						String newTime = time.getText().toString();
						String locDesc = locationDesc.getText().toString();
						String creator = null;
						String first;
						String last;
						ParseUser currentUser = ParseUser.getCurrentUser();
						if (currentUser != null) {
						  first = (String) currentUser.get("first_name");
						  last = (String) currentUser.get("last_name");
						  creator = first+" "+last;
						} else {
						  // show the signup or login screen
						}
						ParseObject store = new ParseObject("Event");
						store.put("name", name);
						store.put("description", eDesc);
						store.put("people_attending", num);
						store.put("date", newDate);
						store.put("time", newTime);
						store.put("location_string", locDesc);
						store.put("creator", creator);
						LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
						boolean enabled = service
								.isProviderEnabled(LocationManager.GPS_PROVIDER);
						if (!enabled) {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateNewEvent.this);

							// set title
							alertDialogBuilder.setTitle("Location Services");

							// set dialog message
							alertDialogBuilder
							.setMessage("Please turn on your GPS settings")
							.setCancelable(false)
							.setPositiveButton("Settings",new DialogInterface.OnClickListener() {
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
						locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
						// Define the criteria how to select the locatioin provider -> use
						// default
						Criteria criteria = new Criteria();
						provider = locationManager.getBestProvider(criteria, false);
						Location location = locationManager.getLastKnownLocation(provider);
						int lat = (int) (location.getLatitude());
						int lng = (int) (location.getLongitude());
						ParseGeoPoint point = new ParseGeoPoint(lat, lng);
						store.put("location", point);
						store.saveInBackground(new SaveCallback(){

							@Override
							public void done(ParseException e) {

								Intent intent = new Intent(CreateNewEvent.this, MainActivity.class);
								onPause();
								onStop();
								startActivity(intent);

							}

						});

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
				// Do something in response to button
			}
		};
	}
}
