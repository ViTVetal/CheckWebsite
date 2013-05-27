package com.example.checkwebsite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	final String LOG_TAG = "myLogs";
	public DBHelper(Context context) {
	      super(context, "myDB", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		 Log.d(LOG_TAG, "--- onCreate database ---");

	      db.execSQL("create table response ("
	          + "id integer primary key autoincrement," 
	          + "date long,"
	          + "answer int" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
