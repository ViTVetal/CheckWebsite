package com.example.checkwebsite;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class Prime extends Activity {
  	TextView text;
  	TextView date;
  	ImageView image;
  	EditText site_address;
  	static String goal_address="sfdsdf";
  	final String LOG_TAG = "myLogs";
  	DBHelper dbHelper;
    	public void onCreate(Bundle savedInstanceState) {
        	super.onCreate(savedInstanceState);
        	setContentView(R.layout.prime);
 
  	        date = (TextView) findViewById(R.id.textError);
        	image = (ImageView) findViewById(R.id.imageView1);
        	site_address = (EditText) findViewById(R.id.site_address);
        
    		if(status().equals("200"))
    			image.setImageResource(R.drawable.green1);
    		else
    			image.setImageResource(R.drawable.red1);
    	}
    
    	public void onClickStart(View v) {
    		goal_address = site_address.getText().toString();
    		Log.d(LOG_TAG, goal_address);
    		startService(new Intent(this, CheckService.class));
   	 }
    
   	 public void onClickStop(View v) {
    	 	stopService(new Intent(this, CheckService.class));
    	 }
    
    	public void onClickPie(View v) {
    		 Intent intent = new Intent(this, Pie.class);
         	startActivity(intent);
    	}
    
    	public void onClickRefresh(View v) {
    		if(status().equals("200"))
    			image.setImageResource(R.drawable.green1);
    		else
    			image.setImageResource(R.drawable.red1);
    	}
    
    	public void onClickCrash(View v) {
    		date.setText(crash());
      	}
    
    	public String status(){
	        dbHelper = new DBHelper(this);
	        String str = "";
	        SQLiteDatabase db = dbHelper.getWritableDatabase();
	        Cursor c = db.rawQuery("SELECT MAX(id) FROM response", null);
	        c.moveToFirst();
	        str = c.getString(0);
	        c = db.rawQuery("SELECT * FROM response WHERE id = " + str, null);
	        
	        c.moveToFirst();
	        String date_str = c.getString(1);
	
	        Date dt = new Date(Long.parseLong(date_str));
	        date_str = dt.toString();
	 
	        SimpleDateFormat Format_date = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
			Format_date.format(dt);
	
			date_str = Format_date.format(dt).toString();
			date.setText(date_str);
			
	        c.moveToFirst();
	        return str = c.getString(2);
    	}
    
   	 public String crash(){
	        dbHelper = new DBHelper(this);
	        String str = "";
	        SQLiteDatabase db = dbHelper.getWritableDatabase();
	        Cursor c = db.rawQuery("SELECT MAX(id) FROM response WHERE answer != 200", null);
	        c.moveToFirst();
	        str = c.getString(0);
	        c = db.rawQuery("SELECT * FROM response WHERE id = " + str, null);
	        c.moveToFirst();
	        str = c.getString(1);
	        Log.d(LOG_TAG, "1");
	
	        Date dt = new Date(Long.parseLong(str));
	        Log.d(LOG_TAG, "2");
	        str = dt.toString();
	 
	        SimpleDateFormat Format_date = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
			Format_date.format(dt);
			image.setImageResource(R.drawable.red1);
	        return str = Format_date.format(dt).toString();
    	}
    
    	public static String getAddress(){
    		return goal_address;
    	}
}
