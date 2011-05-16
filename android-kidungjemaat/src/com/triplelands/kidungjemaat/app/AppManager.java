package com.triplelands.kidungjemaat.app;

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
	
	private List<IndexLagu> listIndexKJ, listIndexPKJ, listIndexNKB;
	private SharedPreferences appPreference;
	private int currentIndexKJ, currentIndexPKJ, currentIndexNKB;
	private int currentJenis, latestJenis;
	
	@Inject
	private Context context;
	
	public AppManager() {
	}

	public void initSongIndex(Context context) {
		if(appPreference == null){
			appPreference =  PreferenceManager.getDefaultSharedPreferences(context);
			currentIndexKJ = appPreference.getInt("KJ_index", 0);
			currentIndexPKJ = appPreference.getInt("PKJ_index", 0);
			currentIndexNKB = appPreference.getInt("NKB_index", 0);
			currentJenis = appPreference.getInt("current_jenis", Lagu.KJ);
		}
		if(listIndexKJ == null){
			Type listType = new TypeToken<List<IndexLagu>>(){}.getType();
			listIndexKJ = JsonUtils.toListObject2(FileLoader.readFile(context, "KJINDEX"), listType);
		}
		if(listIndexPKJ == null){
			Type listType = new TypeToken<List<IndexLagu>>(){}.getType();
			listIndexPKJ = JsonUtils.toListObject2(FileLoader.readFile(context, "PKJINDEX"), listType);
		}
		if(listIndexNKB == null){
			Type listType = new TypeToken<List<IndexLagu>>(){}.getType();
			listIndexNKB = JsonUtils.toListObject2(FileLoader.readFile(context, "NKBINDEX"), listType);
		}
	}
	
	public List<IndexLagu> getListIndexKJ(){
		return listIndexKJ;
	}
	
	public List<IndexLagu> getListIndexPKJ(){
		return listIndexPKJ;
	}
	
	public List<IndexLagu> getListIndexNKB(){
		return listIndexNKB;
	}
	
	public Lagu getCurrentSong(){
		System.out.println("current number: " + currentIndexKJ);
		IndexLagu index = getCurrentIndexLagu();
		return FileLoader.GetLagu(context, currentJenis, Lagu.getFullNumber(index.getNo()));
	}
	
	public Lagu next(){
		if(!isLastSong()){
			IndexLagu nextIndex = getCurrentIndexList().get(getCurrentIndex() + 1);
			String nextNumber = nextIndex.getNo();
			System.out.println("next Number: " + nextNumber);
			setCurrentIndex(getCurrentIndex() + 1);
			return FileLoader.GetLagu(context, currentJenis, Lagu.getFullNumber(nextNumber));
		}
		return null;
	}
	
	public Lagu prev(){
		if(!isFirstSong()){
			IndexLagu prevIndex = getCurrentIndexList().get(getCurrentIndex() - 1);
			String prevNumber = prevIndex.getNo();
			setCurrentIndex(getCurrentIndex() - 1);
			return FileLoader.GetLagu(context, currentJenis, Lagu.getFullNumber(prevNumber));
		}
		return null;
	}
	
	private IndexLagu getCurrentIndexLagu(){
		System.out.println("INDEX CURRENT: " + getCurrentIndex());
		return getCurrentIndexList().get(getCurrentIndex());
	}

	public boolean isFirstSong() {
		return getCurrentIndex() == 0;
	}

	public boolean isLastSong() {
		return getCurrentIndex() == getCurrentIndexList().size() - 1;
	}

	public Lagu goTo(String number) {
		String goToNumber = number;
		List<IndexLagu> listGoTo = getCurrentIndexList();
		
		int indexToGo = 0;
		for (IndexLagu indexLagu : listGoTo) {
			if(indexLagu.getNo().startsWith(number)){
				goToNumber = indexLagu.getNo();
				indexToGo = getCurrentIndexList().indexOf(indexLagu);
				break;
			}
		}
		System.out.println("INDEX TO GO:" + indexToGo);
		setCurrentIndex(indexToGo);
		return FileLoader.GetLagu(context, currentJenis, Lagu.getFullNumber(goToNumber));
	}
	
	private void setCurrentIndexKJ(int index){
		currentIndexKJ = index;
    	SharedPreferences.Editor editor = appPreference.edit();
        editor.putInt("KJ_index", index);
        editor.commit();
	}
	
	private void setCurrentIndexPKJ(int index){
		System.out.println("current PKJ index: " + index);
		currentIndexPKJ = index;
    	SharedPreferences.Editor editor = appPreference.edit();
        editor.putInt("PKJ_index", index);
        editor.commit();
	}
	
	private void setCurrentIndexNKB(int index){
		currentIndexNKB = index;
    	SharedPreferences.Editor editor = appPreference.edit();
        editor.putInt("NKB_index", index);
        editor.commit();
	}
	
	public void setCurrentJenis(int index){
		latestJenis = currentJenis;
		currentJenis = index;
    	SharedPreferences.Editor editor = appPreference.edit();
        editor.putInt("current_jenis", index);
        editor.commit();
	}
	
	public int getCurrentJenis(){
		return currentJenis;
	}
	
	public void rollBackJenis(){
		currentJenis = latestJenis;
    	SharedPreferences.Editor editor = appPreference.edit();
        editor.putInt("current_jenis", latestJenis);
        editor.commit();
	}
	
	private List<IndexLagu> getCurrentIndexList(){
		List<IndexLagu> listCurrent;
		System.out.println("CURRENT JENIS: " + currentJenis);
		if(currentJenis == Lagu.KJ) listCurrent = listIndexKJ;
		else if(currentJenis == Lagu.PKJ) listCurrent = listIndexPKJ;
		else listCurrent = listIndexNKB;
		return listCurrent;	
	}
	
	private void setCurrentIndex(int index){
		System.out.println("SET CURRENT INDEX: " + index);
		if(currentJenis == Lagu.KJ) setCurrentIndexKJ(index);
		else if(currentJenis == Lagu.PKJ) setCurrentIndexPKJ(index);
		else setCurrentIndexNKB(index);
	}
	
	private int getCurrentIndex(){
		if(currentJenis == Lagu.KJ) return currentIndexKJ;
		else if(currentJenis == Lagu.PKJ) return currentIndexPKJ;
		else return currentIndexNKB;
	}
}
