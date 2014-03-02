package com.lovrhatr.socialize;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class EventActivity extends Activity {
	
	private TextView title;
	private TextView creator;
	private TextView date;
	private TextView time;
	private TextView atendees;
	private TextView description;
	private String ObjectID;
	private String eventName;

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
		
		title = (TextView)findViewById(R.id.editText1);
		creator = (TextView)findViewById(R.id.editText2);
		date = (TextView)findViewById(R.id.editText3);
		time = (TextView)findViewById(R.id.editText4);
		atendees = (TextView)findViewById(R.id.TextView01);
		description = (TextView)findViewById(R.id.editText5);
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
		query.whereEqualTo("objectId", ObjectID);
		query.findInBackground(new FindCallback<ParseObject>() {

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
