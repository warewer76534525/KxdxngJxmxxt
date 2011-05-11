package com.triplelands.kidungjemaat.activity;

import java.io.File;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.triplelands.kidungjemaat.R;
import com.triplelands.kidungjemaat.model.Lagu;
import com.triplelands.kidungjemaat.tools.MusicPlayer;
import com.triplelands.kidungjemaat.tools.SongDownloaderTask;
import com.triplelands.kidungjemaat.utils.LyricLoader;
import com.triplelands.kidungjemaat.utils.StringHelpers;

public class SongActivity extends RoboActivity {
	@InjectView(R.id.txtIsi) private TextView txtIsi;
	@InjectView(R.id.btnPrev) private Button btnPrev;
	@InjectView(R.id.btnNext) private Button btnNext;
	@InjectView(R.id.btnNomor) private Button btnNomor;
    @InjectView(R.id.linearLayout1) private LinearLayout playerLayout;
    @InjectView(R.id.btnPlay) private Button btnPlay;
	@InjectView(R.id.btnPause) private Button btnPause;
	@InjectView(R.id.btnStop) private Button btnStop;
    
	private String songNumber;
	private SharedPreferences appPreference;
	private MusicPlayer mp;
	private String musicPath = Environment.getExternalStorageDirectory() + "/kidungjemaat/files/";
	
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
				if(StringHelpers.GetPrevSongNumber(songNumber) != null){
					String prevNo = StringHelpers.GetPrevSongNumber(songNumber);
					mp.stop();
					playerLayout.setVisibility(View.INVISIBLE);
					showLyric(prevNo);
					updateNumber(prevNo);
					loadSong();
					btnNomor.setText("KJ - No." + prevNo);
				}
			} else if(v == btnNext){
				if(StringHelpers.GetNextSongNumber(songNumber) != null){
					String nextNo = StringHelpers.GetNextSongNumber(songNumber);
					mp.stop();
					playerLayout.setVisibility(View.INVISIBLE);
					showLyric(nextNo);
					updateNumber(nextNo);
					loadSong();
					btnNomor.setText("KJ - No." + nextNo);
				}
			} else if(v == btnNomor){
				
			} else if(v == btnPlay){
				if(mp.isStopped()){
					mp.prepareMediaPlayer(musicPath + getCompleteNumber() + ".mid");
				}
				mp.play();
			} else if(v == btnPause){
				mp.pause();
			} else if(v == btnStop){
				mp.stop();
			}
		}
    }
    
    private void updateNumber(String nomor){
    	System.out.println("parameter update: " + nomor);
    	SharedPreferences.Editor editor = appPreference.edit();
        editor.putString("nomor_lagu", nomor);
        editor.commit();
        System.out.println("setelah update: " + songNumber);
    }
    
    private void showLyric(String num){
    	Lagu lagu = LyricLoader.GetLagu(this, num);
		txtIsi.setText(Html.fromHtml("<B>" + lagu.getJudul() + "</B>"));
		txtIsi.append("\n\n");
		int no = 1;
		for (String ayat : lagu.getListAyat()) {
			txtIsi.append(no + ". " + ayat);
			txtIsi.append("\n");
			no++;
		}
    }
    
    private void startDownloadSong(){
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				loadSong();
			}
		};
    	new SongDownloaderTask(this, handler, "http://202.51.96.30/index.php/kj/get/" + getCompleteNumber()).execute();
    }
    
    private void loadSong(){
    	File songDir = new File(musicPath);
    	songDir.mkdirs();
    	File fileSong = new File(songDir, getCompleteNumber() + ".mid");
    	if(fileSong.isFile()){
    		playerLayout.setVisibility(View.VISIBLE);
//    		mp.prepareMediaPlayer(fileSong.getAbsolutePath());
    	} else {
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
}