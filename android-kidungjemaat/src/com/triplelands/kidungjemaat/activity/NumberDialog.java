package com.triplelands.kidungjemaat.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.triplelands.kidungjemaat.R;
import com.triplelands.kidungjemaat.utils.StringHelpers;

public class NumberDialog extends Dialog {
	
	private Button btnGo;
	private EditText txtNumber;
	private Handler handler;
	
	public NumberDialog(Context context, Handler handler) {
		super(context);
		this.handler = handler;
		setTitle("Go To");
		setContentView(R.layout.dialog);
		setCancelable(true);
		btnGo = (Button) this.findViewById(R.id.btnGo);
		txtNumber = (EditText) this.findViewById(R.id.txtNumber);
		setOnClickButton();
	}
	
	private void setOnClickButton(){
		btnGo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!txtNumber.getText().toString().equals("")){
					String number = StringHelpers.BuildNumberFromRaw(Integer.parseInt(txtNumber.getText().toString()), StringHelpers.MODE_NORMAL);
					Message msg = handler.obtainMessage();
					Bundle bundle = new Bundle();
					bundle.putString("number", number);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
				dismiss();
			}
		});
	}
}
