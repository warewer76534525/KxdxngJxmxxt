package com.triplelands.kidungjemaat.utils;

import java.util.Vector;

public class StringHelpers {

	private static String[] abPart = { "030", "031", "037", "050", "095", "146",
			"168", "174", "222", "229", "240", "248", "250", "287", "303",
			"311", "312", "365", "369", "466", "473", };
	
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
	
	public static String GetPrevSongNumber(String current) {
		if(current.equals("001")) return null;
		
		String part = current.substring(current.length() - 1);
		String number = current;
		if (part.equals("A") || part.equals("B") || part.equals("C")) {
			number = current.substring(0, current.length() - 1);
		}
		
		String prev = String.valueOf(Integer.parseInt(number) - 1);
		for(int i = 0; i < abPart.length; i++){
			if(number.equals(abPart[i])){
				if(part.equals("B")){
					prev = number + "A";
					break;
				} else {
					prev = String.valueOf(Integer.parseInt(number) - 1);
					for(int j = i; j < abPart.length; j++){
						if(Integer.parseInt(prev) == Integer.parseInt(abPart[j])){
							prev = abPart[j] + "B";
							break;
						}
					}
					for(int k = 0; k < abcPart.length; k++){
						if(Integer.parseInt(prev) == Integer.parseInt(abcPart[k])){
							prev = abcPart[k] + "C";
							break;
						}
					}
				}
			}
		}
		for(int i = 0; i < abcPart.length; i++){
			if(number.equals(abcPart[i])){
				if(part.equals("C")){
					prev = number + "B";
					break;
				} else if (part.equals("B")){
					prev = number + "A";
					break;
				} else {
					prev = String.valueOf(Integer.parseInt(number) - 1);
					for(int j = i; j < abPart.length; j++){
						if(Integer.parseInt(prev) == Integer.parseInt(abPart[j])){
							prev = abPart[j] + "B";
							break;
						}
					}
					for(int k = 0; k < abcPart.length; k++){
						if(Integer.parseInt(prev) == Integer.parseInt(abcPart[k])){
							prev = abcPart[k] + "C";
							break;
						}
					}
				}
			}
		}
		
		if(prev.length() == 1) prev = "00" + prev;
		else if(prev.length() == 2) prev = "0" + prev;
		
		System.out.println("PREV: " + prev);
		return prev;
	}

	public static String GetNextSongNumber(String current) {
		System.out.println("Current: " + current);
		if(current.equals("478")) return null;
		
		String part = current.substring(current.length() - 1);
		String number = current;
		if (part.equals("A") || part.equals("B") || part.equals("C")) {
			number = current.substring(0, current.length() - 1);
		}

		String next = String.valueOf(Integer.parseInt(number) + 1);
		System.out.println("nomer next: " + next);
		for(int i = 0; i < abPart.length; i++){
			if(number.equals(abPart[i])){
				if(part.equals("A")){
					next = number + "B";
					break;
				} else {
					next = String.valueOf(Integer.parseInt(number) + 1);
					for(int j = i; j < abPart.length; j++){
						if(Integer.parseInt(next) == Integer.parseInt(abPart[j])){
							next = abPart[j] + "A";
							break;
						}
					}
					for(int k = 0; k < abcPart.length; k++){
						if(Integer.parseInt(next) == Integer.parseInt(abcPart[k])){
							next = abcPart[k] + "A";
							break;
						}
					}
				}
			}
		}
		for(int i = 0; i < abcPart.length; i++){
			if(number.equals(abcPart[i])){
				if(part.equals("A")){
					next = number + "B";
					break;
				} else if (part.equals("B")){
					next = number + "C";
					break;
				} else {
					next = String.valueOf(Integer.parseInt(number) + 1);
					for(int j = i; j < abPart.length; j++){
						if(Integer.parseInt(next) == Integer.parseInt(abPart[j])){
							next = abPart[j] + "A";
							break;
						}
					}
					for(int k = 0; k < abcPart.length; k++){
						if(Integer.parseInt(next) == Integer.parseInt(abcPart[k])){
							next = abcPart[k] + "A";
							break;
						}
					}
				}
			}
		}
		
		if(next.length() == 1) next = "00" + next;
		else if(next.length() == 2) next = "0" + next;
		
		System.out.println("NEXT: " + next);
		return next;
	}

}
