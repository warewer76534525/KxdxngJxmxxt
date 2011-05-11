package com.triplelands.kidungjemaat.utils;

import java.util.Vector;

public class StringHelpers {

	private static final int MODE_PREV = 0;
	private static final int MODE_NEXT = 1;
	private static String[] abPart = { "030", "031", "037", "050", "095", "146",
			"174", "222", "229", "240", "248", "250", "287", "303", "311", "312", 
			"369", "466", "473", };
	
	private static String[] abcPart = {"168", "365"};

	public static String urlSymbolEncode(String s) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < s.length(); i++)
			switch (s.charAt(i)) {
			case ' ':
				sb.append("%20");
				break;
			case '+':
				sb.append("%2b");
				break;
			case '\'':
				sb.append("%27");
				break;
			case '<':
				sb.append("%3c");
				break;
			case '>':
				sb.append("%3e");
				break;
			case '#':
				sb.append("%23");
				break;
			case '%':
				sb.append("%25");
				break;
			case '{':
				sb.append("%7b");
				break;
			case '}':
				sb.append("%7d");
				break;
			case '\\':
				sb.append("%5c");
				break;
			case '^':
				sb.append("%5e");
				break;
			case '~':
				sb.append("%73");
				break;
			case '[':
				sb.append("%5b");
				break;
			case ']':
				sb.append("%5d");
				break;
			default:
				sb.append(s.charAt(i));
				break;
			}
		return sb.toString();
	}

	public static String urlEncode(String sUrl) {
		StringBuffer urlOK = new StringBuffer();

		for (int i = 0; i < sUrl.length(); i++) {
			char ch = sUrl.charAt(i);

			switch (ch) {
			case '<':
				urlOK.append("%3C");
				break;
			case '>':
				urlOK.append("%3E");
				break;
			case '/':
				urlOK.append("%2F");
				break;
			case ' ':
				urlOK.append("%20");
				break;
			case ':':
				urlOK.append("%3A");
				break;
			case '-':
				urlOK.append("%2D");
				break;
			default:
				urlOK.append(ch);
				break;
			}
		}
		return urlOK.toString();
	}

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
		String part = current.substring(current.length() - 1);
		
		String number = current;
		if (part.equals("A") || part.equals("B") || part.equals("C")) {
			number = current.substring(0, current.length() - 1);
		}
		
		String prev = buildNumberFromRaw(Integer.parseInt(number) - 1, MODE_PREV);
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
		String part = current.substring(current.length() - 1);
		
		String number = current;
		if (part.equals("A") || part.equals("B") || part.equals("C")) {
			number = current.substring(0, current.length() - 1);
		}
		
		String next = buildNumberFromRaw(Integer.parseInt(number) + 1, MODE_NEXT);
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
	
	private static String buildNumberFromRaw(int raw, int mode){
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
