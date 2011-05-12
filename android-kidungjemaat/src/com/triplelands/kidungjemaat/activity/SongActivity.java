package com.triplelands.kidungjemaat.activity;

import java.io.File;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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

import com.triplelands.kidungjemaat.R;
import com.triplelands.kidungjemaat.model.Lagu;
import com.triplelands.kidungjemaat.tools.MusicPlayer;
import com.triplelands.kidungjemaat.tools.SongDownloaderTask;
import com.triplelands.kidungjemaat.utils.LyricLoader;
import com.triplelands.kidungjemaat.utils.StringHelpers;

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
    
	private String songNumber;
	private SharedPreferences appPreference;
	private MusicPlayer mp;
	private String musicPath = Environment.getExternalStorageDirectory() + "/.kidungjemaat/songfiles/";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mp = MusicPlayer.GetInstance();
        
        appPreference =  PreferenceManager.getDefaultSharedPreferences(this);
        songNumber = appPreference.getString("nomor_lagu", "001");
        
        loadSong();
        
        showLyric(songNumber);
        btnNomor.setText("KJ - No." + songNumber);
        
        btnPrev.setOnClickListener(new ButtonClickListener());
        btnNext.setOnClickListener(new ButtonClickListener());
        btnNomor.setOnClickListener(new ButtonClickListener());
        
        btnPlay.setOnClickListener(new ButtonClickListener());
        btnPause.setOnClickListener(new ButtonClickListener());
        btnStop.setOnClickListener(new ButtonClickListener());
    }
    
    private class ButtonClickListener implements OnClickListener {
		public void onClick(View v) {
			if(v == btnPrev){
				if(StringHelpers.PrevNumber(songNumber) != null){
					String prevNo = StringHelpers.PrevNumber(songNumber);
					mp.stop();
					playerLayout.setVisibility(View.INVISIBLE);
					showLyric(prevNo);
					updateNumber(prevNo);
					loadSong();
					btnNomor.setText("KJ - No." + prevNo);
				}
			} else if(v == btnNext){
				if(StringHelpers.NextNumber(songNumber) != null){
					String nextNo = StringHelpers.NextNumber(songNumber);
					mp.stop();
					playerLayout.setVisibility(View.INVISIBLE);
					showLyric(nextNo);
					updateNumber(nextNo);
					loadSong();
					btnNomor.setText("KJ - No." + nextNo);
				}
			} else if(v == btnNomor){
				initGoToDialog();
			} else if(v == btnPlay){
				if(mp.isStopped()){
					mp.prepareMediaPlayer(musicPath + getCompleteNumber() + ".mid");
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
    
    private void updateNumber(String nomor){
    	songNumber = nomor;
    	SharedPreferences.Editor editor = appPreference.edit();
        editor.putString("nomor_lagu", nomor);
        editor.commit();
    }
    
    private boolean showLyric(String num){
    	Lagu lagu = LyricLoader.GetLagu(this, num);
    	if(lagu == null){
    		Toast.makeText(this, "Nomor Kidung Jemaat tidak valid.", Toast.LENGTH_SHORT).show();
    		return false;
    	} else {
    		txtIsi.setText(Html.fromHtml("<B>" + lagu.getJudul() + "</B>"));
    		txtIsi.append("\n\n");
    		int no = 1;
    		for (String ayat : lagu.getListAyat()) {
    			txtIsi.append(no + ". " + ayat);
    			txtIsi.append("\n");
    			no++;
    		}
    		return true;
    	}
    }
    
    private void startDownloadSong(){
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				String error = msg.getData().getString("error");
				if(error != null && error.equals("error")){
					setStatus("Gagal mengunduh lagu.");
				} else {
					loadSong();
				}
			}
		};
    	new SongDownloaderTask(this, handler, "http://www.kj.triplelands.com/index.php/kj/get/" + getCompleteNumber()).execute();
    }
    
    public void loadSong(){
    	File songDir = new File(musicPath);
    	songDir.mkdirs();
    	File fileSong = new File(songDir, getCompleteNumber() + ".mid");
    	if(fileSong.isFile()){
    		hideStatus();
    		playerLayout.setVisibility(View.VISIBLE);
    	} else {
    		setStatus("Mengunduh lagu...");
    		startDownloadSong();
    	}
    }
    
    private String getCompleteNumber(){
    	String nomorLagu;
		if(String.valueOf(songNumber).length() == 1) nomorLagu = "00" + songNumber;
		else if(String.valueOf(songNumber).length() == 2) nomorLagu = "0" + songNumber;
		else nomorLagu = "" + songNumber;
		return nomorLagu;
    }
    
    private void initGoToDialog() {
		Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				String goToNum = msg.getData().getString("number");
				
				if(showLyric(goToNum)){
					mp.stop();
					playerLayout.setVisibility(View.INVISIBLE);
					updateNumber(goToNum);
					loadSong();
					btnNomor.setText("KJ - No." + goToNum);
				}
			}
		};
		
		new NumberDialog(this, handler).show();
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
			case R.id.aboutMenu:
				new AboutUsDialog(this).show();
				break;
		}
		return true;
    }
}