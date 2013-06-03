package com.example.checkwebsite;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TabHost tabHost = getTabHost();

		TabHost.TabSpec tabSpec;
		tabSpec = tabHost.newTabSpec("tag1");
		tabSpec.setIndicator("Prime");
		tabSpec.setContent(new Intent(this, Prime.class));
		
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("tag2");
		tabSpec.setIndicator("All");
		tabSpec.setContent(new Intent(this, All.class));
		
		tabHost.addTab(tabSpec);
		tabSpec = tabHost.newTabSpec("tag3");
		tabSpec.setIndicator("Failures");
		tabSpec.setContent(new Intent(this, Failures.class));
		tabHost.addTab(tabSpec);
	}
}