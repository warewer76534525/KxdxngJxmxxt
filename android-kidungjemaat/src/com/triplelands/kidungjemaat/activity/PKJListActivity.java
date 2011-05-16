package com.triplelands.kidungjemaat.activity;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.google.inject.Inject;
import com.triplelands.kidungjemaat.R;
import com.triplelands.kidungjemaat.app.AppManager;
import com.triplelands.kidungjemaat.model.IndexLagu;
import com.triplelands.kidungjemaat.model.Lagu;
import com.triplelands.kidungjemaat.view.SongAdapter;

public class PKJListActivity extends RoboActivity {
	private SongAdapter adapter;
	private List<IndexLagu> listIndex;
	@InjectView(R.id.listViewLagu)
	private ListView lvLagu;
	@InjectView(R.id.txtSearch)
	private EditText txtSearch;
	@Inject
	private AppManager manager;
	private String result;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songlist);

		listIndex = manager.getListIndexPKJ();
		adapter = new SongAdapter(this);
		adapter.setList(listIndex);

		lvLagu.setAdapter(adapter);

		lvLagu.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id) {
				manager.setCurrentJenis(Lagu.PKJ);
				IndexLagu lagu = (IndexLagu) view.getTag();
				setResultToParent(lagu.getNo());
				finish();
			}

		});
		txtSearch.addTextChangedListener(filterTextWatcher);
	}
	
	private void setResultToParent(String res){
		this.result = res;
	}
	
	public String getResult(){
		return result;
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable arg0) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			populateList(filterIndex(listIndex, s.toString()));
		}
	};

	private void populateList(List<IndexLagu> filteredList) {
		adapter.setList(filteredList);
		lvLagu.setAdapter(adapter);
	}

	private List<IndexLagu> filterIndex(List<IndexLagu> source, String filter) {
		List<IndexLagu> filtered = new ArrayList<IndexLagu>();
		if (source != null) {
			for (int i = 0; i < source.size(); i++) {
				if ((source.get(i).getNo().toLowerCase() + " " + source.get(i)
						.getJudul().toLowerCase()).contains(filter
						.toLowerCase())) {
					filtered.add(source.get(i));
				}
			}
		}
		return filtered;
	}
}
