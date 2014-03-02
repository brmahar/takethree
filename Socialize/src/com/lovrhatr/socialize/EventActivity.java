package com.lovrhatr.socialize;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EventActivity extends Activity {
	
	private TextView title;
	private TextView creator;
	private TextView date;
	private TextView time;
	private TextView atendees;
	private TextView description;
	private Button join;
	private Button leave;
	private String ObjectID;
	private String eventName;
	private String user;
	Activity thisThing = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "ZNUGpdAW35nGYe5hvleBl3IndIphZPbZjVfn8Vcn", "JGTTBGebCrZXx2J2nc6TVh3is6bwUBq5hROkFCSI");
		setContentView(R.layout.activity_event);
		
		Bundle extras = getIntent().getExtras();
		if(extras !=null) {
			eventName = extras.getString("name");
			ObjectID = extras.getString("id");
		}
		
		SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(this);
		user = shared.getString("User", "");
		
		title = (TextView)findViewById(R.id.editText1);
		creator = (TextView)findViewById(R.id.editText2);
		date = (TextView)findViewById(R.id.editText3);
		time = (TextView)findViewById(R.id.editText4);
		atendees = (TextView)findViewById(R.id.TextView01);
		description = (TextView)findViewById(R.id.editText5);
		join = (Button)findViewById(R.id.button1);
		leave = (Button)findViewById(R.id.Button01);
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("User_Event");
		query.whereEqualTo("username", user);
		query.whereEqualTo("eventID", ObjectID);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				if (arg0.size() == 0){
					join.setVisibility(View.VISIBLE);
					join.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							ParseObject user_event = new ParseObject("User_Event");
							user_event.put("username", user);
							user_event.put("eventID", ObjectID);
							user_event.saveInBackground();
							
							AlertDialog.Builder popupBuilder = new AlertDialog.Builder(thisThing);
							TextView myMsg = new TextView(thisThing);
							myMsg.setText("Event Joined");
							popupBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int which) {
			                    	Intent intent = new Intent(EventActivity.this, MainActivity.class);
			            			onPause();
			            			onStop();
			            			startActivity(intent);
			                    }
			                    });
							myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
							myMsg.setPadding(0, 50, 0, 0);
							popupBuilder.setView(myMsg);
							popupBuilder.show();
							
						}
					});
				}
				else{
					leave.setVisibility(View.VISIBLE);
					leave.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							ParseQuery<ParseObject> query1 = ParseQuery.getQuery("User_Event");
							query1.whereEqualTo("username", user);
							query1.whereEqualTo("eventID", ObjectID);
							query1.findInBackground(new FindCallback<ParseObject>() {

								@Override
								public void done(List<ParseObject> arg0, ParseException arg1) {
									arg0.get(0).deleteInBackground();
								}
							});
							
							AlertDialog.Builder popupBuilder = new AlertDialog.Builder(thisThing);
							TextView myMsg = new TextView(thisThing);
							myMsg.setText("Event Left");
							popupBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			                    public void onClick(DialogInterface dialog, int which) {
			                    	Intent intent = new Intent(EventActivity.this, MainActivity.class);
			            			onPause();
			            			onStop();
			            			startActivity(intent);
			                    }
			                    });
							myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
							myMsg.setPadding(0, 50, 0, 0);
							popupBuilder.setView(myMsg);
							popupBuilder.show();
							
						}
					});
				}
			}
		});
		
		
		ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Event");
		query1.whereEqualTo("objectId", ObjectID);
		query1.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				title.setText(arg0.get(0).getString("name"));
				creator.setText("  Creator: " + arg0.get(0).getString("creator"));
				date.setText("  Date: " + arg0.get(0).getString("date"));
				time.setText("  Time: " + arg0.get(0).getString("time"));
				int size = arg0.get(0).getList("people_attending").size();
				atendees.setText("  Attendees: " + String.valueOf(size));
				description.setText("  Description: " + arg0.get(0).getString("description"));
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

}
