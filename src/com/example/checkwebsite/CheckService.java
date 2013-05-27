package com.example.checkwebsite;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

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
		start_stop = true;
		MyRun mr = new MyRun(startId);
		es.execute(mr);
		return super.onStartCommand(intent, flags, startId);
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
			int response_random;
			int[] server_response = {500, 200};
			Random rand = new Random();
			
			while(start_stop){
				response_random = rand.nextInt(5);
				
				if(response_random != 0)
					response_random = 1;
				
				Log.d(LOG_TAG, "response = " + server_response[response_random] + " " +new  java.util.Date());
				
				cv.put("date", new java.util.Date().getTime());
			    cv.put("answer", server_response[response_random]);


			      long rowID = db.insert("response", null, cv);
			      Log.d(LOG_TAG, "row inserted, ID = " + rowID);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Log.d(LOG_TAG, "Œ¯Ë·Í‡");
				}
			}
			stop();
	    }
	    
		void stop() {
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