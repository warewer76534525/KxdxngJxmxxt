package com.triplelands.kidungjemaat.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

import com.triplelands.kidungjemaat.model.Lagu;

public class LyricLoader {

	public static Lagu GetLagu(Context ctx, String noLagu) {
		String nomorLagu = "KJ" + noLagu;
			
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(ctx.getAssets().open(nomorLagu)));

			String line;
			StringBuilder sb = new StringBuilder();

			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			Lagu lagu = JsonUtils.toObject(cleanJson(sb.toString()), Lagu.class);
			return lagu;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String cleanJson(String result) {
		if (result == null || result.trim().equals(""))
			return null;
		return result.split("\n")[0];
	}
}
