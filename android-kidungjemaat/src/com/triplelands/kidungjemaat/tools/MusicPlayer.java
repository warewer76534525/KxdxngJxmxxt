package com.triplelands.kidungjemaat.tools;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

public class MusicPlayer implements OnErrorListener, OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener {

	private static MusicPlayer instance;
	private String path;
	private MediaPlayer mp;
	private boolean stopped;
	
	private MusicPlayer() {
		mp = new MediaPlayer();
		mp.setOnErrorListener(this);
		mp.setOnBufferingUpdateListener(this);
		mp.setOnCompletionListener(this);
		mp.setOnPreparedListener(this);
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp.setLooping(true);
		stopped = true;
	}
	
	public static MusicPlayer GetInstance(){
		if (instance == null){
			instance = new MusicPlayer();
		}
		return instance;
	}
	
	public void prepareMediaPlayer(String path){
		this.path = path;
		try {
			mp.reset();
			mp.setDataSource(path);
			mp.prepare();
		} catch (Exception e) {
			if (mp != null) {
				mp.stop();
				mp.release();
			}
		}
	}
	
	public boolean isPlaying(){
		return mp.isPlaying();
	}
	
	public void play(){
		if(stopped){
			try {
				mp.reset();
				mp.setDataSource(path);
				mp.prepare();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mp.start();
		stopped = false;
	}
	
	public void pause(){
		if(mp.isPlaying()){
			mp.pause();
		}
	}
	
	public void stop(){
		mp.stop();
		stopped = true;
	}
	
	public boolean isStopped(){
		return stopped;
	}
	
	public void release(){
		mp.stop();
		mp.release();
	}
	
	@Override
	public void onPrepared(MediaPlayer arg0) {
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		stop();
		play();
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}
}
