package com.triplelands.kidungjemaat.activity;

import roboguice.activity.RoboTabActivity;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.triplelands.kidungjemaat.R;

public class SongListTabActivity extends RoboTabActivity {
	@InjectView(android.R.id.tabhost)
	private TabHost mTabHost;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_lagu_tab);
		mTabHost.getTabWidget().setDividerDrawable(android.R.drawable.divider_horizontal_bright);
		Intent intent;
		intent = new Intent().setClass(this, KJListActivity.class);
		setupTab(intent, "KJ");
		intent = new Intent().setClass(this, PKJListActivity.class);
		setupTab(intent, "PKJ");
		intent = new Intent().setClass(this, NKBListActivity.class);
		setupTab(intent, "NKB");
	}

	private void setupTab(final Intent intent, final String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(intent);
		mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
	
	@Override
	public void finishFromChild(Activity child) {
		String no;
		if(child instanceof KJListActivity) no = ((KJListActivity)child).getResult();
		else if(child instanceof PKJListActivity) no = ((PKJListActivity)child).getResult();
		else no = ((NKBListActivity)child).getResult();
			
		Intent data = new Intent();
		data.putExtra("nomor", no);
		setResult(RESULT_OK, data);
		super.finishFromChild(child);
	}
}
