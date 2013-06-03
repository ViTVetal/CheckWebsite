package com.example.checkwebsite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class All extends Activity {

	int[] flags = new int[]{
		        R.drawable.green,
		        R.drawable.red,
	};
	
	  ArrayList<String> answer = new ArrayList<String>();
	  ArrayList<String> date= new ArrayList<String>();
	
	final String LOG_TAG = "myLogs";
	DBHelper dbHelper;
	
  	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all);
        
        dbHelper = new DBHelper(this);
        String str = "";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "1");
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM response", null);
        c.moveToFirst();
        str = c.getString(0);
        int count = Integer.parseInt(str);
        int j = 0;
        Log.d(LOG_TAG, "2");
        c = db.rawQuery("SELECT * FROM response", null);
        //-------------------------
        Log.d(LOG_TAG, "3");
        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");
            int dateColIndex = c.getColumnIndex("date");
            int answerColIndex = c.getColumnIndex("answer");
            Log.d(LOG_TAG, "4");

            do {
            	String answ = c.getString(answerColIndex);
            	Log.d(LOG_TAG, answ);
            	answer.add(answ);
            	Log.d(LOG_TAG, "5");
            	str = c.getString(dateColIndex);
            	Log.d(LOG_TAG, "6");
            	Date dt = new Date(Long.parseLong(str));

                str = dt.toString();
 
                SimpleDateFormat Format_date = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
        		Format_date.format(dt);
        		
                date.add( Format_date.format(dt).toString());
              Log.d(LOG_TAG,
                  "ID = " + c.getInt(idColIndex) + 
                  ", name = " + c.getString(answerColIndex) + 
                  ", email = " + c.getString(dateColIndex));
              j++;
              
            } while (c.moveToNext());
          } else
            Log.d(LOG_TAG, "0 rows");
          c.close();
        //-----------------------
          List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

          for(int i=0;i<count;i++){
              HashMap<String, String> hm = new HashMap<String,String>();
              hm.put("txt", "Answer : " + answer.get(i));
              hm.put("cur","Date : " + date.get(i));
              if(answer.get(i).equals("200"))
            	  hm.put("flag", Integer.toString(flags[0]) );
              else
            	  hm.put("flag", Integer.toString(flags[1]) );
              aList.add(hm);
          }

          // Keys used in Hashmap
          String[] from = { "flag","txt","cur" };

          // Ids of views in listview_layout
          int[] to = { R.id.flag,R.id.txt,R.id.cur};

          // Instantiating an adapter to store each items
          // R.layout.listview_layout defines the layout of each item
          SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);

          // Getting a reference to listview of main.xml layout file
          ListView listView = ( ListView ) findViewById(R.id.listview);

          // Setting the adapter to the listView
          listView.setAdapter(adapter);
    }
}