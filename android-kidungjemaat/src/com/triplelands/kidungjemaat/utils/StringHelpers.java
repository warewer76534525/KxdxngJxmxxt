package com.triplelands.kidungjemaat.utils;

import java.util.Vector;

public class StringHelpers {

	public static final int MODE_PREV = 0;
	public static final int MODE_NEXT = 1;
	public static final int MODE_NORMAL = 2;
	private static String[] abPart = { "030", "031", "037", "050", "095", "146",
			"174", "222", "229", "240", "248", "250", "287", "303", "311", "312", 
			"369", "466", "473", };
	
	private static String[] abcPart = {"168", "365"};

	public static boolean isValidPhoneNumber(String num) {
		if (num.length() >= 8) {
			if (!num.substring(0, 1).equals("0")
					&& !num.substring(0, 1).equals("+")) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public static String[] explode(char separator, String str) {
		Vector<String> exploded = new Vector<String>(0, 1);
		String tmpstr = null;
		int beginIndex = 0, endIndex = 0;
		while (endIndex < str.length()) {
			if (str.charAt(endIndex) == separator) {
				if (endIndex > beginIndex) {
					tmpstr = str.substring(beginIndex, endIndex);
					exploded.addElement(tmpstr);
					endIndex++;
					beginIndex = endIndex;
					tmpstr = null;
				} else {
					exploded.addElement("");
					endIndex++;
					beginIndex = endIndex;
				}
			} else {
				endIndex++;
			}
		}
		if (endIndex > beginIndex) {
			tmpstr = str.substring(beginIndex, endIndex);
			exploded.addElement(tmpstr);
		}
		String[] res = new String[exploded.size()];
		exploded.copyInto(res);
		return res;
	}
	
	public static String PrevNumber(String current){
		if(current.equals("001")) return null;
		
		String part = current.substring(current.length() - 1);
		
		String number = current;
		if (part.equals("A") || part.equals("B") || part.equals("C")) {
			number = current.substring(0, current.length() - 1);
		}
		
		String prev = BuildNumberFromRaw(Integer.parseInt(number) - 1, MODE_PREV);
		if(isABPart(current)){
			if(part.equals("B")){
				prev = number + "A";
			}
		} else if(isABCPart(current)){
			if(part.equals("C")){
				prev = number + "B";
			} else if(part.equals("B")) {
				prev = number + "A";
			}
		}
		System.out.println("PREV: " + prev);
		return prev;
	}
	
	public static String NextNumber(String current){
		if(current.equals("478")) return null;
		
		String part = current.substring(current.length() - 1);
		
		String number = current;
		if (part.equals("A") || part.equals("B") || part.equals("C")) {
			number = current.substring(0, current.length() - 1);
		}
		
		String next = BuildNumberFromRaw(Integer.parseInt(number) + 1, MODE_NEXT);
		if(isABPart(current)){
			if(part.equals("A")){
				next = number + "B";
			}
		} else if(isABCPart(current)){
			if(part.equals("A")){
				next = number + "B";
			} else if(part.equals("B")) {
				next = number + "C";
			}
		}
		System.out.println("NEXT: " + next);
		return next;
	}
	
	private static boolean isABPart(String number){
		for(int i=0; i < abPart.length; i++){
			if(number.contains(abPart[i])) return true;
		}
		return false;
	}
	
	private static boolean isABCPart(String number){
		for(int i=0; i < abcPart.length; i++){
			if(number.contains(abcPart[i])) return true;
		}
		return false;
	}
	
	public static String BuildNumberFromRaw(int raw, int mode){
		String partAB = (mode == MODE_PREV) ? "B" : "A";
		String partAC = (mode == MODE_PREV) ? "C" : "A";
		for(int i=0; i < abPart.length; i++){
			if(Integer.parseInt(abPart[i]) == raw) return abPart[i] + partAB;
		}
		for(int i=0; i < abcPart.length; i++){
			if(Integer.parseInt(abcPart[i]) == raw) return abcPart[i] + partAC;
		}
		
		if(("" + raw).length() == 1) return "00" + raw;
		else if (("" + raw).length() == 2) return "0" + raw;
		else return "" + raw;
	}

}
