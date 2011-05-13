package com.triplelands.kidungjemaat.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.myjson.reflect.TypeToken;
import com.triplelands.kidungjemaat.model.IndexLagu;
import com.triplelands.kidungjemaat.model.Lagu;
import com.triplelands.kidungjemaat.utils.FileLoader;
import com.triplelands.kidungjemaat.utils.JsonUtils;

@Singleton
public class AppManager {
	
	private List<IndexLagu> listIndexLagu;
	private SharedPreferences appPreference;
	private int currentIndex;
	
	@Inject
	private Context context;
	
	public AppManager() {
	}

	public void initSongIndex(Context context) {
		if(appPreference == null){
			appPreference =  PreferenceManager.getDefaultSharedPreferences(context);
			currentIndex = appPreference.getInt("index_song", 0);
		}
		if(listIndexLagu == null){
			Type listType = new TypeToken<List<IndexLagu>>(){}.getType();
			listIndexLagu = JsonUtils.toListObject2(readFile(context, "KJINDEX"), listType);
		}
	}
	
	public List<IndexLagu> getListIndexLagu(){
		return listIndexLagu;
	}
	
	private String readFile(Context ctx, String fileName){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(ctx.getAssets().open(fileName)));

			String line;
			StringBuilder sb = new StringBuilder();

			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Lagu getCurrentSong(){
		System.out.println("current number: " + currentIndex);
		IndexLagu index = getCurrentIndexLagu();
		return FileLoader.GetLagu(context, Lagu.getFullNumber(index.getNo()));
	}
	
	public Lagu next(){
		if(!isLastSong()){
			IndexLagu nextIndex = listIndexLagu.get(currentIndex + 1);
			String nextNumber = nextIndex.getNo();
			System.out.println("next Number: " + nextNumber);
			setCurrentIndex(currentIndex + 1);
			return FileLoader.GetLagu(context, Lagu.getFullNumber(nextNumber));
		}
		return null;
	}
	
	public Lagu prev(){
		if(!isFirstSong()){
			IndexLagu prevIndex = listIndexLagu.get(currentIndex - 1);
			String prevNumber = prevIndex.getNo();
			setCurrentIndex(currentIndex - 1);
			return FileLoader.GetLagu(context, Lagu.getFullNumber(prevNumber));
		}
		return null;
	}
	
	private IndexLagu getCurrentIndexLagu(){
		return listIndexLagu.get(currentIndex);
	}

	public boolean isFirstSong() {
		return currentIndex == 0;
	}

	public boolean isLastSong() {
		return currentIndex == listIndexLagu.size() - 1;
	}

	public Lagu goTo(String number) {
		String goToNumber = number;
		int index = currentIndex;
		for (IndexLagu indexLagu : listIndexLagu) {
			if(indexLagu.getNo().startsWith(number)){
				goToNumber = indexLagu.getNo();
				index = listIndexLagu.indexOf(indexLagu);
				break;
			}
		}

		setCurrentIndex(index);
		return FileLoader.GetLagu(context, Lagu.getFullNumber(goToNumber));
	}
	
	private void setCurrentIndex(int index){
		currentIndex = index;
    	SharedPreferences.Editor editor = appPreference.edit();
        editor.putInt("index_song", index);
        editor.commit();
	}
}
