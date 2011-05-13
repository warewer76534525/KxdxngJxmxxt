package com.triplelands.kidungjemaat.activity;

import com.triplelands.kidungjemaat.R;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class AboutUsDialog extends Dialog {

	private Context ctx;
	
	public AboutUsDialog(Context context) {
		super(context);
		ctx = context;
		setTitle("Tentang Pengembang");
		setCancelable(true);
		setContentView(R.layout.about);
		((TextView)this.findViewById(R.id.txtAppName)).setText("KidungJemaat " + getVersionName());
	}
	
	private String getVersionName() {
		try {
			return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName;
		} catch (Exception e) {
			return "";
		}
	}

}
