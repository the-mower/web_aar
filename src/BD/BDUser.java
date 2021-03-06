package BD;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import exceptions.EndOfSessionException;
import exceptions.KeyUndefinedException;

public class BDUser {

	/**
	 * Permet de vErifier qu'un utilisateur existe
	 * @param login
	 * @return
	 */
	public static boolean userExists(String login){
		DB db=null;

		try {
			db = DBStatic.getMongoDb();

			DBCollection collection = db.getCollection("users");	// selection de la collection voulue
			BasicDBObject searchQuery = new BasicDBObject("login", login);	// creation du champs recherchE
			DBCursor cursor = collection.find(searchQuery);		// recherche du champs au sein de la base

			if (cursor.count() != 0){
				db.getMongo().close();
				return true;
			}

		} catch (UnknownHostException e) {
			if (db!=null){
				db.getMongo().close();
			}
			e.printStackTrace();
		}
		finally{	// fermeture de la connection
			try{
				if (db!=null){
					db.getMongo().close();
				}
			}catch (Exception e){
				if (db!=null){
					db.getMongo().close();
				}
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * InsEre un utilisateur A la BD
	 * @param prenom
	 * @param nom
	 * @param login
	 * @param password
	 * @throws Exception
	 */
	public static void insertUser(String prenom,String nom,String login,String password) throws Exception{
		DB db=null;

		try {
			db = DBStatic.getMongoDb();

			DBCollection collection = db.getCollection("users");	// selection de la collection voulue
			BasicDBObject user = new BasicDBObject("nom", nom).append("prenom", prenom).append("login", login).append("password", password);	// creation de l'objet A insErer
			collection.insert(user);	// insertion du nouvel utilisateur

			db.getMongo().close();	// fermeture de la connection

		} catch (UnknownHostException e) {
			if (db!=null){
				db.getMongo().close();
			}
			throw new Exception("Probleme serveur. Veuillez refaire votre demande");
		}
		finally{	// fermeture de la connection
			try{
				if (db!=null){
					db.getMongo().close();
				}
			}catch (Exception e){
				throw new Exception("Probleme serveur. Veuillez refaire votre demande");
			}
		}
	}

	/**
	 * renvoie toutes les informations relatives a un utilisateur donnE, 
	 * si le prenom est null alors nom est le login de l'utilsateur
	 * 
	 * @param nom
	 * @param prenom
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String, String> getInfoUser(String nom, String prenom) throws Exception{
		DB db=null;
		HashMap<String, String> map= new HashMap<String, String>();

		try {
			db = DBStatic.getMongoDb();

			DBCollection collection = db.getCollection("users");	// selection de la collection voulue
			BasicDBObject searchQuery;

			if (prenom==null){
				searchQuery = new BasicDBObject("login", nom);
			}else{
				searchQuery = new BasicDBObject("nom", nom).append("prenom", prenom);	// creation du champs recherchE
			}

			DBCursor cursor = collection.find(searchQuery);		// recherche du champs au sein de la base

			if (cursor.count() == 0){
				db.getMongo().close();
				throw new Exception("Probleme serveur. Veuillez refaire votre demande");
			}

			// transformation des donnEes en map
			List<DBObject> tab = cursor.toArray();	
			DBObject dbObject = tab.get(0);
			Set<String> keys = dbObject.keySet();
			for (String key : keys) {
				map.put(key, dbObject.get(key).toString());
			}

		} catch (UnknownHostException e) {
			db.getMongo().close();
			throw new Exception("Probleme serveur. Veuillez refaire votre demande");
		}
		finally{	// fermeture de la connection
			try{
				if (db!=null){
					db.getMongo().close();
				}
			}catch (Exception e){
				throw new Exception("Probleme serveur. Veuillez refaire votre demande");
			}
		}

		return map;
	}

	/**
	 * renvoie la liste d'amis sous form d'une table HTML
	 * @param nom
	 * @param prenom
	 * @return
	 * @throws Exception
	 */
	public static String getFriendsUser(String nom, String prenom) throws Exception{
		DB db=null;
		String result="";

		try {
			db = DBStatic.getMongoDb();

			DBCollection collection = db.getCollection("friends");	// selection de la collection voulue
			BasicDBObject searchQuery = new BasicDBObject("nom", nom).append("prenom", prenom);	// creation du champs recherchE
			DBCursor cursor = collection.find(searchQuery);		// recherche du champs au sein de la base

			if (cursor.size()!=0){
				BasicDBList e = (BasicDBList) cursor.next().get("friends");	// rEcupe de la liste d'amis

				for (Object object : e) {
					BasicDBObject b = (BasicDBObject) object;

					result+="<tr><td><form action='GetProfil' method='post'>"
							+ "<input type='hidden' name='patronyme' value='"+b.get("prenom")+" "+b.get("nom")+"' /> "
							+ "<input type='submit' value='"+b.get("prenom")+" "+b.get("nom")+"'></form></tr></td>";
				}
			}

		} catch (UnknownHostException e) {
			db.getMongo().close();
			throw new Exception("Probleme serveur. Veuillez refaire votre demande");
		}
		finally{	// fermeture de la connection
			try{
				if (db!=null){
					db.getMongo().close();
				}
			}catch (Exception e){
				throw new Exception("Probleme serveur. Veuillez refaire votre demande");
			}
		}

		return result;
	}

	/**
	 * Renvoie vrai s'ils sont amis
	 * @param nom_1
	 * @param prenom_1
	 * @param nom_2
	 * @param prenom_2
	 * @return
	 * @throws Exception
	 */
	public static boolean areFriends(String nom_1, String prenom_1, String nom_2, String prenom_2) throws Exception{
		DB db=null;

		try {
			db = DBStatic.getMongoDb();
			DBCollection collection = db.getCollection("friends");	// selection de la collection voulue

			BasicDBObject searchQuery = new BasicDBObject("nom", nom_1).append("prenom", prenom_1);	// creation du champs recherchE
			DBCursor cursor = collection.find(searchQuery);		// recherche du champs au sein de la base

			if (cursor.size() !=0){
				BasicDBList e = (BasicDBList) cursor.next().get("friends");	// rEcupe de la liste d'amis

				for (Object object : e) {
					BasicDBObject b = (BasicDBObject) object;
					
					if (b.get("nom").toString().equals(nom_2) && b.get("prenom").toString().equals(prenom_2)){
						db.getMongo().close();
						return true;
					}
				}
			}

		} catch (UnknownHostException e) {
			db.getMongo().close();
			throw new Exception("Probleme serveur. Veuillez refaire votre demande");
		}
		finally{	// fermeture de la connection
			try{
				if (db!=null){
					db.getMongo().close();
				}
			}catch (Exception e){
				throw new Exception("Probleme serveur. Veuillez refaire votre demande");
			}
		}

		return false;
	}

	/**
	 * Permet de vErifier que le login et le mot de passe existent dans la BD
	 * @param login
	 * @param password
	 * @return
	 */
	public static boolean verifLoginPassword(String login,String password){
		DB db=null;
		boolean result = false;

		try {
			db = DBStatic.getMongoDb();

			DBCollection collection = db.getCollection("users");	// selection de la collection voulue
			BasicDBObject searchQuery = new BasicDBObject("login", login).append("password", password);	// creation du champs recherchE
			DBCursor cursor = collection.find(searchQuery);		// recherche du champs au sein de la base

			if (cursor.count() != 0){
				result= true;
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		finally{	// fermeture de la connection
			try{
				if (db!=null){
					db.getMongo().close();
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}

		System.out.println(result?"BDUser.verifLoginPassword : User found ":"BDUser.verifLoginPassword : User not found ");
		return result;
	}

	/**
	 * Ajoute un ami A la liste des amis d'un utilisateur
	 * @param nom_1
	 * @param prenom_1
	 * @param login_1
	 * @param nom_2
	 * @param prenom_2
	 * @param login_2
	 * @throws Exception
	 */
	public static void addFriend(String nom_1, String prenom_1, String login_1, String nom_2, String prenom_2, String login_2) throws Exception{
System.out.println(nom_1+" "+prenom_1+" "+login_1+", "+nom_2+" "+prenom_2+" "+login_2);
		DB db=null;

		try {
			db = DBStatic.getMongoDb();
			DBCollection collection = db.getCollection("friends");	// selection de la collection voulue

			BasicDBObject searchQuery = new BasicDBObject("nom", nom_1).append("prenom", prenom_1).append("login", login_1);	// creation du champs recherchE
			DBCursor cursor = collection.find(searchQuery);		// recherche du champs au sein de la base
			BasicDBObject friendToInsert = new BasicDBObject("nom", nom_2).append("prenom", prenom_2).append("login", login_2); // creation du champ A insErer

			if (cursor.count() != 0){	// l'utilisateur possEde deja des amis dans la BD

				BasicDBObject updateCommand = new BasicDBObject("$push", new BasicDBObject("friends", friendToInsert));	//	selection du tableau
				collection.update(searchQuery, updateCommand);

			}else{	// l'utilisateur ne possEde aucun ami dans la BD

				List<BasicDBObject> friendsList = new ArrayList<>();
				friendsList.add(friendToInsert);
				searchQuery.put("friends", friendsList);
				collection.insert(searchQuery);
			}

			db.getMongo().close();	//	fermeture de la connexion

		} catch (UnknownHostException e) {
			db.getMongo().close();
			throw new Exception("Probleme serveur. Veuillez refaire votre demande");
		}
		finally{	// fermeture de la connection
			try{
				if (db!=null){
					db.getMongo().close();
				}
			}catch (Exception e){
				throw new Exception("Probleme serveur. Veuillez refaire votre demande");
			}
		}
	}

	//ajout d'un commentaire à la base NOSQL
	public static void addcomment(String key, String text) {
	}

	/*
	//recherche de commentaires par mots clés !
	public static String search(String key, String query, String friends) {//friends = 1 si cochée, sinon 0
		String result="";
		int id_user;
		int ami = new Integer(friends).intValue();

		try {
			Mongo mongo = new Mongo("132.227.201.134",27017);
			DB db = mongo.getDB("li328");
			DBCollection collection = db.getCollection("binome10");
			BasicDBObject doc;

			if ((key=="")&&(query=="")&&(friends=="")){		//si search sans paramétre on affiche les commentaires de moins d'une heure
				GregorianCalendar calendar = new java.util.GregorianCalendar();
				calendar.add(Calendar.HOUR,-1);
				java.util.Date date = calendar.getTime();
				doc = new BasicDBObject();
				doc.put("date", new BasicDBObject("$gt",date));
				DBCursor curseur =collection.find(doc);
				while (curseur.hasNext())
					result+=curseur.next();
			}

			id_user = getUserByKey(key);
			if (sessionPerimee(key)||(id_user==-1)){		//on crée une erreur pour sortir directement
				throw new EndOfSessionException("Session terminée");
			}

			doc = new BasicDBObject();
			doc.put("auteur", id_user);		//tous les commentaires de id

			if((query=="")||(query==null)){		// recherche sans mots clés

				if (ami==0){			//recherche sans tri par ami et sans mots clés 
					DBCursor curseur = collection.find(doc);
					while (curseur.hasNext())
						result+=curseur.next();

				}else{				//recherche par tri d'amis et sans mots clés

					//connection à la base SQL et récupération de la liste des amis de l'utilisateur
					Class.forName("com.mysql.jdbc.driver");
					Connection connexion = DBStatic.getMysqlConnection();
					Statement statement = connexion.createStatement();	
					ResultSet listAmi = statement.executeQuery("SELECT * FROM Friends WHERE id_user_key="+id_user);
					if (!listAmi.next()){
						throw new EndOfSessionException("Liste d'amis introuvable");
					}
					else{
						statement.close();
						connexion.close();
					}	//liste d'amis obtenue 

					//recherche des comentaires avec la liste d'amis 
					doc = new BasicDBObject();
					ArrayList<Integer> list = new ArrayList<Integer>();		
					while (listAmi.next())
						list.add(new Integer(listAmi.toString()).intValue());
					doc.put("auteur", new BasicDBObject("$in",list));	//trie les commentaires par amis 

					DBCursor curseur = collection.find(doc);
					while (curseur.hasNext())
						result+=curseur.next();
				}


			}else{		// recherche avec mots clés


			}
		} catch (KeyUndefinedException e) {
			e.printStackTrace();
		} catch (EndOfSessionException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}		//FAIRE UN FINALLY QUI RENVOIT UN CHAMP D'ERREUR <==============================

		return result;		// la BD NOSQL retourne le commentaire au type JSON et pas STRING comment faire alors ?
	}
	 */}