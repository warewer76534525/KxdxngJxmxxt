package com.triplelands.kidungjemaat.activity;

import java.io.File;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.triplelands.kidungjemaat.R;
import com.triplelands.kidungjemaat.app.AppManager;
import com.triplelands.kidungjemaat.model.Lagu;
import com.triplelands.kidungjemaat.tools.MusicPlayer;
import com.triplelands.kidungjemaat.tools.SongDownloaderTask;

public class SongActivity extends RoboActivity {
	@InjectView(R.id.txtIsi) private TextView txtIsi;
	@InjectView(R.id.btnPrev) private ImageButton btnPrev;
	@InjectView(R.id.btnNext) private ImageButton btnNext;
	@InjectView(R.id.btnNomor) private Button btnNomor;
    @InjectView(R.id.linearLayout1) private LinearLayout playerLayout;
    @InjectView(R.id.btnPlay) private ImageButton btnPlay;
	@InjectView(R.id.btnPause) private ImageButton btnPause;
	@InjectView(R.id.btnStop) private ImageButton btnStop;
	@InjectView(R.id.txtStatus) private TextView txtStatus;
    
	private MusicPlayer mp;
	private String musicPath = Environment.getExternalStorageDirectory() + "/.kidungjemaat/songfiles/";
	
	@Inject
	private AppManager manager;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        manager.initSongIndex(this);
        mp = MusicPlayer.GetInstance();
        
        populateView(manager.getCurrentSong());
        
        btnPrev.setOnClickListener(new ButtonClickListener());
        btnNext.setOnClickListener(new ButtonClickListener());
        btnNomor.setOnClickListener(new ButtonClickListener());
        
        btnPlay.setOnClickListener(new ButtonClickListener());
        btnPause.setOnClickListener(new ButtonClickListener());
        btnStop.setOnClickListener(new ButtonClickListener());
        
        if(mp.isPlaying() && !mp.isPaused()){
			btnPlay.setBackgroundResource(R.drawable.button_yellow);
        }
        if(mp.isPaused()){
        	btnPause.setBackgroundResource(R.drawable.button_yellow);
        }
    }
    
    private class ButtonClickListener implements OnClickListener {
		public void onClick(View v) {
			if(v == btnPrev){
				Lagu lagu = manager.prev();
				if(lagu != null) {
					populateView(lagu);
					mp.stop();
					btnPlay.setBackgroundResource(R.drawable.button_teal);
					btnPause.setBackgroundResource(R.drawable.button_teal);
				}
			} else if(v == btnNext){
				Lagu lagu = manager.next();
				if(lagu != null){
					populateView(lagu);
					mp.stop();
					btnPlay.setBackgroundResource(R.drawable.button_teal);
					btnPause.setBackgroundResource(R.drawable.button_teal);
				}
			} else if(v == btnNomor){
				initGoToDialog();
			} else if(v == btnPlay){
				if(mp.isStopped()){
					mp.prepareMediaPlayer(musicPath + manager.getCurrentSong().getFullNumber() + ".mid");
				}
				mp.play();
				btnPlay.setBackgroundResource(R.drawable.button_yellow);
				btnPause.setBackgroundResource(R.drawable.button_teal);
			} else if(v == btnPause){
				mp.pause();
				btnPlay.setBackgroundResource(R.drawable.button_teal);
				if(!mp.isStopped())
					btnPause.setBackgroundResource(R.drawable.button_yellow);
			} else if(v == btnStop){
				mp.stop();
				btnPlay.setBackgroundResource(R.drawable.button_teal);
				btnPause.setBackgroundResource(R.drawable.button_teal);
			}
		}
    }
    
    private void populateView(Lagu lagu) {
		populateLyric(lagu);
		populateSong(lagu);
	}
    
    private void populateSong(Lagu lagu) {
    	playerLayout.setVisibility(View.INVISIBLE);
    	File songDir = new File(musicPath);
    	songDir.mkdirs();
    	File fileSong = new File(songDir, lagu.getFullNumber() + ".mid");
    	
    	if(fileSong.isFile()){
    		System.out.println("length: " + fileSong.length());
    		if(fileSong.length() > 378){
    			hideStatus();
        		playerLayout.setVisibility(View.VISIBLE);
    		}
    		
    	} else {
    		setStatus("Mengunduh lagu...");
    		startDownloadSong(lagu);
    	}
	}

	private void populateLyric(Lagu lagu) {
		txtIsi.setText(Html.fromHtml("<B>" + lagu.getFullJudul() + "</B>"));
		txtIsi.append("\n\n");
		int no = 1;
		for (String ayat : lagu.getListAyat()) {
			txtIsi.append(no + ". " + ayat);
			txtIsi.append("\n");
			no++;
		}
		btnNomor.setText(lagu.getStringNumber());
	}
    
    private void startDownloadSong(final Lagu lagu){
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				String error = msg.getData().getString("error");
				if(error != null && error.equals("error")){
					setStatus("Gagal mengunduh lagu.");
				} else {
					populateSong(lagu);
				}
			}
		};
    	new SongDownloaderTask(this, handler, "http://www.kj.triplelands.com/index.php/kj/get/" + lagu.getFullNumber()).execute();
    }
    
    private void initGoToDialog() {
		Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				Lagu lagu = (Lagu)msg.getData().getSerializable("lagu");
				if(lagu == null){
					Toast.makeText(SongActivity.this, "Nomor Kidung Jemaat tidak valid.", Toast.LENGTH_SHORT).show();
				} else {
					mp.stop();
					btnPlay.setBackgroundResource(R.drawable.button_teal);
					btnPause.setBackgroundResource(R.drawable.button_teal);
					populateView(lagu);
				}
			}
		};
		
		new NumberDialog(this, handler, manager).show();
	}
    
    private void setStatus(String message){
    	txtStatus.setVisibility(View.VISIBLE);
    	txtStatus.setText(message);
    }
    
    private void hideStatus(){
    	txtStatus.setVisibility(View.GONE);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.listMenu:
				startActivityForResult(new Intent(this, SongListActivity.class), 0);
				break;
			case R.id.aboutMenu:
				new AboutUsDialog(this).show();
				break;
		}
		return true;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(resultCode == RESULT_OK){
    		Lagu lagu = manager.goTo(data.getExtras().getString("nomor"));
    		mp.stop();
			btnPlay.setBackgroundResource(R.drawable.button_teal);
			btnPause.setBackgroundResource(R.drawable.button_teal);
			populateView(lagu);
    	}
    }
}