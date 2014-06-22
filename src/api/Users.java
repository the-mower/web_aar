package api;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import BD.BDUser;

public class Users {

	public static void addUser(String prenom, String nom, String login, String password) throws Exception{
		if (prenom == null || prenom =="")
			throw new Exception("Prenom manquant");

		if (nom==null || nom =="")
			throw new Exception("Nom manquant");

		if (login == null || login=="")
			throw new Exception("Login manquant");

		if (password ==null || password=="")
			throw new Exception("Mot de passe manquant");

		if (BDUser.userExists(login))
			throw new Exception("Login deja existant");
		
		BDUser.insertUser(prenom, nom, login, password);
	}

	public static HashMap<String, String> connectUser(String login, String password) throws Exception{
		if (login == null || login==""){
			throw new Exception("Login manquant");
		}
		if (password == null || password==""){
			throw new Exception("Mot de passe manquant");
		}
		if (!BDUser.verifLoginPassword(login, password)){
			throw new Exception("Login ou mot de pass incorrect");
		}
		
		return BDUser.getInfoUser(login, null);	// renvoie les informations relatives A l'utilisateur
	}

	public static JSONObject addFriend(String key, String id_friend) {
		if (key == null || id_friend == null)
		{
			return Tools.erreur("Parametre manquant");//retourne un objet JSON avec un champ erreur ayant pour valeur "parametre manquant"
		}
		else{
			BDUser.addFriend(key, id_friend);
			JSONObject json = new JSONObject();
			try {
				json.put ("output" ,"OK" );
			} catch ( JSONException e ) {
				return Tools.erreur(e.getMessage());
			}
			return json;
		}
	}

	public static JSONObject removeFriend(String key, String id_friend) {
		if (key == null || id_friend == null)
		{
			return Tools.erreur("Parametre manquant");//retourne un objet JSON avec un champ erreur ayant pour valeur "parametre manquant"
		}
		else{
			BDUser.removeFriend(key, id_friend);
			JSONObject json = new JSONObject();
			try {
				json.put ("output" ,"OK" );
			} catch ( JSONException e ) {
				return Tools.erreur(e.getMessage());
			}
			return json;
		}
	}

	public static JSONObject Logout() {
		JSONObject json = new JSONObject();
		try {
			json.put ("output" ,"OK" );
		} catch ( JSONException e ) {
			return Tools.erreur(e.getMessage());
		}
		return json;
	}

}
