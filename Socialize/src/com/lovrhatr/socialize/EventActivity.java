package com.lovrhatr.socialize;

import com.parse.Parse;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "ZNUGpdAW35nGYe5hvleBl3IndIphZPbZjVfn8Vcn", "JGTTBGebCrZXx2J2nc6TVh3is6bwUBq5hROkFCSI");
		setContentView(R.layout.activity_event);
		
		title = (TextView)findViewById(R.id.editText1);
		creator = (TextView)findViewById(R.id.editText2);
		date = (TextView)findViewById(R.id.editText3);
		time = (TextView)findViewById(R.id.editText4);
		atendees = (TextView)findViewById(R.id.TextView01);
		description = (TextView)findViewById(R.id.editText5);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}

}
