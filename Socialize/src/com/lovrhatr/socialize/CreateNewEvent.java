package com.lovrhatr.socialize;

import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.View;
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
		create = (Button)findViewById(R.id.button1);
		eventName = (EditText)findViewById(R.id.name);
		eventDescription = (EditText)findViewById(R.id.description);
		date = (EditText)findViewById(R.id.editText2);
		time = (EditText)findViewById(R.id.editText3);
		locationDesc = (EditText)findViewById(R.id.editText4);
		numberPeople = 0;
		
		
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
	
	public void createEvent(View view) {
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
					ParseObject store = new ParseObject("Event");
					store.put("name", eventName);
					store.put("description", eventDescription);
					
				}
			  })
			.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// if this button is clicked, just close
					// the dialog box and do nothing
					dialog.cancel();
				}
			});
	    // Do something in response to button
		
	}

}
