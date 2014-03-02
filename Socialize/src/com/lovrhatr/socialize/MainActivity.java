package com.lovrhatr.socialize;

import java.util.List;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;



public class MainActivity extends Activity {

	private LocationManager locationManager;
	private String provider;
	private TextView title;
	private TextView date;
	private TextView time;
	private TextView creator;
	private TextView going;
	private CardLayout layout;
	private String name;
	//private String ObjectID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "ZNUGpdAW35nGYe5hvleBl3IndIphZPbZjVfn8Vcn", "JGTTBGebCrZXx2J2nc6TVh3is6bwUBq5hROkFCSI");
		setContentView(R.layout.main_lists);

		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		SpannableString s = new SpannableString("Socialize");
	    s.setSpan(new TypefaceSpan(this, "Pacifico.ttf"), 0, s.length(),
	            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	 
	    // Update the action bar title with the TypefaceSpan instance
	    ActionBar actionBar = getActionBar();
	    actionBar.setTitle(s);
		
		// check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if (!enabled) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

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

		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the location provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		if (location != null) {
			System.out.println("Provider " + provider + " has been selected.");
			onLocationChanged(location);
		} else {

		}

	}

	public void onLocationChanged(Location location) {
		double lat = (double) (location.getLatitude());
		double lng = (double) (location.getLongitude());
		ParseGeoPoint point = new ParseGeoPoint(lat, lng);

		

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
		query.whereNear("location", point);
		query.setLimit(10);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				for (int i = 0; i < arg0.size(); i++){
					layout = (CardLayout) findViewById(R.id.theLayout);
					LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);    
					final View cardView = inflater.inflate(R.layout.main_list_card, null);
					title = (TextView)cardView.findViewById(R.id.list_title);
					creator = (TextView)cardView.findViewById(R.id.checkBox1);
					date = (TextView)cardView.findViewById(R.id.checkBox2);
					time = (TextView)cardView.findViewById(R.id.checkBox3);
					going = (TextView)cardView.findViewById(R.id.more_items);
					final String ObjectID = arg0.get(i).getObjectId();
					System.out.println(ObjectID);
					name = arg0.get(i).getString("name");
					title.setText(arg0.get(i).getString("name"));
					creator.setText("  Creator: " + arg0.get(i).getString("creator"));
					date.setText("  Date: " + arg0.get(i).getString("date"));
					time.setText("  Time: " + arg0.get(i).getString("time"));
					int size = arg0.get(i).getList("people_attending").size();
					going.setText(String.valueOf(size) + " people attending");
					going.setPadding(0, 0, 0, 10);
					layout.addView(cardView);
					
					cardView.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(MainActivity.this, EventActivity.class);
							intent.putExtra("name", name);
							intent.putExtra("id", ObjectID);
							onPause();
							//onStop();
							startActivity(intent);

						}

					});
				}
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_activity_actions, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.menu_new:
			Intent intentNew = new Intent(MainActivity.this, CreateNewEvent.class);
			onPause();
			onStop();
			startActivity(intentNew);
			return true;
		case R.id.profile:
			Intent intentProf = new Intent(MainActivity.this, ProfilePage.class);
			onPause();
			onStop();
			startActivity(intentProf);
			return true;
		case R.id.action_logout:
			SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
			Editor edit = shared.edit();
			edit.putString("User", "");
			edit.putString("Password", "");
			edit.putBoolean("stored", false);
			edit.commit();
			ParseUser.logOut();
			Intent intent = new Intent(MainActivity.this, Login.class);
			onPause();
			onStop();
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
