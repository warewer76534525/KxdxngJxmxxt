package com.triplelands.kidungjemaat.view;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.triplelands.kidungjemaat.R;
import com.triplelands.kidungjemaat.model.IndexLagu;

public class SongAdapterView extends LinearLayout {

	public SongAdapterView(Context context, IndexLagu lagu) {
		super(context);
		this.setOrientation(VERTICAL);
		
		View v = inflate(context, R.layout.song_row_item, null);
		
		setTag(lagu);
		
		((TextView)v.findViewById(R.id.txtNoLagu)).setText(lagu.getNo().toUpperCase());
		((TextView)v.findViewById(R.id.txtJudulLagu)).setText(lagu.getJudul());
		
		addView(v);
	}

}
