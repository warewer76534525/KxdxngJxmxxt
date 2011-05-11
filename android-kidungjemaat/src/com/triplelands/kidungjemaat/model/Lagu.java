package com.triplelands.kidungjemaat.model;

import java.util.List;

public class Lagu {
	private String no;
	private int jumlahAyat;
	private String judul;
	private List<String> listAyat;
	
	public Lagu() {
	}

	public String getNo() {
		return no;
	}

	public int getJumlahAyat() {
		return jumlahAyat;
	}

	public String getJudul() {
		return judul;
	}

	public List<String> getListAyat() {
		return listAyat;
	}
	
	
}
