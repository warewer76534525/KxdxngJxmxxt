package com.triplelands.kidungjemaat.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Lagu implements Serializable
{
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
	
	public String getFullJudul(){
		return ("KJ. " + no + " " + judul).toUpperCase();
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
	
	
	public String getStringNumber(){
		return "KJ - No. " + getFullNumber(no);
	}

	public List<String> getListAyat() {
		return listAyat;
	}
	
	
}
