package com.lovrhatr.socialize;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfilePage extends Activity {
	
	private ImageView bear;
	private TextView username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile_page);
		username = (TextView)findViewById(R.id.names);
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/Pacifico.ttf");
		username.setTypeface(typeFace);
		Bitmap imageBitmap = null;
		bear = (ImageView) findViewById(R.id.imageView1);
		getImageTask image = new getImageTask();
		try {
			imageBitmap = image.execute("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcTNR-DyGXt4-jcUp-aSLojNiR9UCT7pdgA5cr8g4QUfScphrvgOXg").get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bear.setImageBitmap(getRoundedShape(Bitmap.createScaledBitmap(imageBitmap, 450, 600, false)));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_page, menu);
		return true;
	}
	
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
	    int targetWidth = 450;
	    int targetHeight = 450;
	    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
	                        targetHeight,Bitmap.Config.ARGB_8888);

	    Canvas canvas = new Canvas(targetBitmap);
	    Path path = new Path();
	    path.addCircle(((float) targetWidth - 1) / 2,
	        ((float) targetHeight - 1) / 2,
	        (Math.min(((float) targetWidth), 
	        ((float) targetHeight)) / 2),
	        Path.Direction.CCW);

	    canvas.clipPath(path);
	    Bitmap sourceBitmap = scaleBitmapImage;
	    canvas.drawBitmap(sourceBitmap, 
	        new Rect(0, 0, sourceBitmap.getWidth(),
	        sourceBitmap.getHeight()), 
	        new Rect(0, 0, targetWidth, targetHeight), null);
	    return targetBitmap;
	}
	
	private class getImageTask extends AsyncTask<String, Void, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
			Bitmap bitmap = null;
			try {
				  bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
				} catch (MalformedURLException e) {
				  e.printStackTrace();
				} catch (IOException e) {
				  e.printStackTrace();
				}
			
			return bitmap;
		}
	}

}
