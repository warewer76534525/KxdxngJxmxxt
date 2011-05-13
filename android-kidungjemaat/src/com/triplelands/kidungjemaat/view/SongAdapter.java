package com.triplelands.kidungjemaat.view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.triplelands.kidungjemaat.model.IndexLagu;

public class SongAdapter extends BaseAdapter {

	private List<IndexLagu> listLagu;
	private Context context;

	public SongAdapter(Context ctx) {
		this.context = ctx;
	}

	public int getCount() {
		return listLagu.size();
	}
	
	public void setList(List<IndexLagu> listLagu){
		this.listLagu = listLagu;
	}

	public IndexLagu getItem(int position) {
		return listLagu.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup viewGroup) {
		IndexLagu lagu = listLagu.get(position);
		return new SongAdapterView(context, lagu);
	}

}
