package com.triplelands.kidungjemaat.model;

import java.io.Serializable;
import java.util.List;

import android.os.Environment;

@SuppressWarnings("serial")
public class Lagu implements Serializable
{
	public static final int KJ = 0;
	public static final int PKJ = 1;
	public static final int NKB = 2;
	
	private String no;
	private int jumlahAyat;
	private String judul;
	private List<String> listAyat;
	
	public Lagu() {
	}

	public String getNo() {
		return no.toUpperCase();
	}

	public int getJumlahAyat() {
		return jumlahAyat;
	}

	public String getJudul() {
		return judul;
	}
	
	public String getFullJudul(int jenis){
		return (getCurrentJenisString(jenis) + ". " + no + " " + judul).toUpperCase();
	}
	
	public static String getFullNumber(String num){
		String full = "" + num;
		if(num.toUpperCase().contains("A") || num.toUpperCase().contains("B") || num.toUpperCase().contains("C")){
			full = num.substring(0, num.length() - 1);
			System.out.println("full cut: " + full);
		}
		if(full.length() == 1) full = "00" + num;
		else if(full.length() == 2) full = "0" + num;
		else full = "" + num;
		System.out.println("full number: " + full);
		return full.toUpperCase();
	}
	public String getFullNumber(){
		return getFullNumber(no);
	}
	
	
	public String getStringNumber(int jenis){
		return getCurrentJenisString(jenis) + " - No. " + getFullNumber(no);
	}

	public List<String> getListAyat() {
		return listAyat;
	}
	
	public static String getCurrentJenisString(int jenis){
		if(jenis == KJ) return "KJ";
		else if(jenis == PKJ) return "PKJ";
		else return "NKB";
	}
	
	public static String getSongDir(int jenis){
		if(jenis == KJ) return Environment.getExternalStorageDirectory() + "/.kidungjemaat/songfiles/";
		else if(jenis == PKJ) return Environment.getExternalStorageDirectory() + "/.kidungjemaat/songfiles/pkj/";
		else return Environment.getExternalStorageDirectory() + "/.kidungjemaat/songfiles/nkb/";
	}
}
