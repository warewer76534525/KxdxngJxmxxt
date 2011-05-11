package com.triplelands.kidungjemaat.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.triplelands.kidungjemaat.utils.StringHelpers;

public class SongDownloaderTask extends AsyncTask<Void, String, Void> implements InternetConnectionListener{

	private InternetHttpConnection internetConnection;
	private String url;
	private Context context;
	private Handler handler;
	
	public SongDownloaderTask(Context ctx, Handler handler, String url) {
		context = ctx;
		this.url = url;
		this.handler = handler;
		internetConnection = new InternetHttpConnection(this);
	}
	
	protected Void doInBackground(Void... params) {
		internetConnection.setAndAccessURL(url);
		return null;
	}

	@Override
	public void onReceivedResponse(InputStream is, int length) {
		String exploded[] = StringHelpers.explode('/', url);
		String name = exploded[exploded.length - 1];
		
		File songDir = new File(Environment.getExternalStorageDirectory() + "/kidungjemaat/files/");
		songDir.mkdirs();
		
		
		try {
			/*Save song*/
			File fileSong = new File(songDir, name + ".mid");
			FileOutputStream out = new FileOutputStream(fileSong);
			
			byte buf[] = new byte[1024];
	        int downloaded = 0;
	        
	        do {
	            int numread = is.read(buf);
	            if (numread <= 0)
	                break;
	            downloaded += numread;
	            out.write(buf, 0, numread);
	        } while (true);
	        
	        if(downloaded < length ){
	        	deleteFile(songDir, name);
	        } else {
	        	/* download selesai */
	        	if(fileSong.isFile()){
	        		System.out.println("selesai!");
	        		Message msg = handler.obtainMessage();
	        		handler.sendMessage(msg);
	        	}
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deleteFile(File dir, String filename){
		try{
			File file = new File(dir, filename);
	    	file.delete();
		}catch (Exception e) {
			Log.e("ERROR", "error deleting file");
		}
	}

	@Override
	public void onConnectionError(Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public void onConnectionResponseNotOk() {
		
	}

	@Override
	public void onConnectionTimeout() {
		
	}

	@Override
	public void onCancelledConnection() {
		
	}

}
