package com.example.checkwebsite;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CheckService extends Service {

	final String LOG_TAG = "myLogs";
	ExecutorService es;
	boolean start_stop = false;
	DBHelper dbHelper;
	
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "MyService onCreate");
		es = Executors.newFixedThreadPool(1);
		dbHelper = new DBHelper(this);
		
	}
  
	public void onDestroy() {
	 	 start_stop = false;
	  	super.onDestroy();
	  	Log.d(LOG_TAG, "MyService onDestroy");
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "MyService onStartCommand");
		
		if(isOnline()){
			start_stop = true;
			Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
		}
		else{
			start_stop = false;
			Toast.makeText(this, "No access", Toast.LENGTH_SHORT).show();
		}
		
		MyRun mr = new MyRun(startId);
		es.execute(mr);
		return super.onStartCommand(intent, flags, startId);
	}
	
	public boolean isOnline() {
		String cs = Context.CONNECTIVITY_SERVICE;
		ConnectivityManager cm = (ConnectivityManager)getSystemService(cs);
		cm.getActiveNetworkInfo();
		if (cm.getActiveNetworkInfo() == null) {
			return false;
		}
		return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	public IBinder onBind(Intent arg0) {
		return null;
	}
  
	class MyRun implements Runnable {
		int startId;
		ContentValues cv;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
	    	public MyRun(int startId) {
	    		this.startId = startId;
	    		cv = new ContentValues();
	    		Log.d(LOG_TAG, "MyRun# create");
	    	}
	    
		public void run() {
			while(start_stop){
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet("http://"+Prime.getAddress());
				Log.d(LOG_TAG, Prime.getAddress() + " ");
				HttpResponse response = null;
				try {
					response = client.execute(request);
				} catch (Exception e) {
					Log.d(LOG_TAG, "no execute");
				} 
				   
				int statusCode = response.getStatusLine().getStatusCode();
				
				cv.put("date", new java.util.Date().getTime());
			   	cv.put("answer", statusCode);
			    
			    	long rowID = db.insert("response", null, cv);
			      	Log.d(LOG_TAG, "row inserted, ID = " + rowID);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Log.d(LOG_TAG, "Error thread");
				}
			}
			stop();
				
	    	}
	    
		public void stop() {
			Log.d(LOG_TAG, "MyRun#" + startId + " end, stopSelf(" + startId + ")");
			Log.d(LOG_TAG, "--- Rows in mytable: ---");

		      	Cursor c = db.query("response", null, null, null, null, null, null);

		      	if (c.moveToFirst()) {

			        int idColIndex = c.getColumnIndex("id");
			        int nameColIndex = c.getColumnIndex("date");
			        int emailColIndex = c.getColumnIndex("answer");
	
			        do {
	
			          Log.d(LOG_TAG,
			              "ID = " + c.getInt(idColIndex) + 
			              ", date = " + c.getLong(nameColIndex) + 
			              ", answer = " + c.getInt(emailColIndex));
	
			        } while (c.moveToNext());
		      	} else
		        	Log.d(LOG_TAG, "0 rows");
		        	
		      	c.close();
			stopSelf(startId);
		}
	}
}
