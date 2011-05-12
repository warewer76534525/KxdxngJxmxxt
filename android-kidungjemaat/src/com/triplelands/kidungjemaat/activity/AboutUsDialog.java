package com.triplelands.kidungjemaat.activity;

import com.triplelands.kidungjemaat.R;

import android.app.Dialog;
import android.content.Context;

public class AboutUsDialog extends Dialog {

	public AboutUsDialog(Context context) {
		super(context);
		setTitle("Tentang Pengembang");
		setCancelable(true);
		setContentView(R.layout.about);
	}

}
