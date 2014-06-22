package api;

import org.json.JSONException;
import org.json.JSONObject;

import BD.BDUser;

public class Comments {

	public static JSONObject addComment(String key,String text){
		if (key == null || text == null){
			return Tools.erreur("Parametre manquant");
		}
		else{
			BDUser.addcomment(key, text);
			JSONObject json = new JSONObject();
			try {
				json.put("output", "OK");
			} catch (JSONException e) {
				return Tools.erreur(e.getMessage());
			}
			return json;
		}
	}


	public static JSONObject search(String key, String query, String friends) {
		if (key == null || query == null || friends == null)
		{
			return Tools.erreur("Parametre manquant");	//retourne un objet JSON avec un champ erreur ayant pour valeur "parametre manquant"
		}
		else{
			//la BD doit faire quoi là ? normalement, en cas de reponse success, on appelle dans environnement 
			//la fonction traiterReponseJson qui prend en parametre un JSon et edite la page avec le texte du
			//JSon, seul pb ici je vois pas sur quel json qu'on doit renvoyer, mais certainement pas un avec
			//juste OutPut ok
			String commentaires = "cucu";//BDUser.search(key,query,friends);
			JSONObject json = new JSONObject();
			try {
				json.put ("text", commentaires);
			} catch ( JSONException e ) {
				return Tools.erreur(e.getMessage());
			}
			return json;
		}
	}
}