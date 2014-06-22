package api;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

public class Tools {
	public static JSONObject erreur(String s){
		JSONObject json = new JSONObject();
		try {
			json.put("error", s);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public static String genererCle(){
		Random generateur = new Random();
		String[] tab = {"0", "1","2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
		String s = "";
		for (int i = 0; i<16; i++){
			s+=tab[generateur.nextInt(16)];
		}
		return s;
	}
}
