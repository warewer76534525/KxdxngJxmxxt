package com.triplelands.kidungjemaat.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.inject.Inject;
import com.triplelands.kidungjemaat.R;
import com.triplelands.kidungjemaat.app.AppManager;
import com.triplelands.kidungjemaat.model.Lagu;

public class NumberDialog extends Dialog {
	
	private Button btnGo;
	private EditText txtNumber;
	private Spinner spJenis;
	private Handler handler;
	
	@Inject
	private AppManager manager;
	private String[] jenis = {"KJ", "PKJ", "NKB"};
	private Context context;
	
	public NumberDialog(Context context, Handler handler, AppManager manager) {
		super(context);
		this.handler = handler;
		this.manager = manager;
		this.context = context;

		setTitle("Go To");
		setContentView(R.layout.dialog);
		setCancelable(true);
		btnGo = (Button) this.findViewById(R.id.btnGo);
		txtNumber = (EditText) this.findViewById(R.id.txtNumber);
		spJenis = (Spinner) this.findViewById(R.id.spJenis);
		populateDropdownList();
		setOnClickButton();
	}
	
	private void populateDropdownList(){
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, jenis);
		spJenis.setAdapter(adapter);
		spJenis.setSelection(manager.getCurrentJenis());
	}
	
	private void setOnClickButton(){
		btnGo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				boolean changed;
				changed = (manager.getCurrentJenis() != (int)spJenis.getSelectedItemId());
				if(changed){
					System.out.println("update jenis: " + spJenis.getSelectedItemId());
					manager.setCurrentJenis((int)spJenis.getSelectedItemId());
				}
				
				if(!txtNumber.getText().toString().equals("")){
					System.out.println("input user: " + txtNumber.getText().toString());
					Lagu lagu = manager.goTo(txtNumber.getText().toString());
					Message msg = handler.obtainMessage();
					Bundle bundle = new Bundle();
					bundle.putSerializable("lagu", lagu);
					msg.setData(bundle);
					handler.sendMessage(msg);
				} else {
					if(changed){
						Lagu lagu = manager.goTo("001");
						Message msg = handler.obtainMessage();
						Bundle bundle = new Bundle();
						bundle.putSerializable("lagu", lagu);
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
				}
				dismiss();
			}
		});
	}
}
