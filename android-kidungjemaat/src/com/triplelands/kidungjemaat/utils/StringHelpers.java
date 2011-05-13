package com.triplelands.kidungjemaat.utils;

import java.util.Vector;

public class StringHelpers {

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

	public static boolean isIntNumber(String num) {
		try {
			Integer.parseInt(num);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

}
