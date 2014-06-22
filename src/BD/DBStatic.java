package BD;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;

public class DBStatic {

	public static String mongo_host="192.168.56.101";
	public static String mongo_db="mydb";
	
	public static DB getMongoDb() throws UnknownHostException{
		MongoClient clientMongo = new MongoClient(mongo_host);
		DB db = clientMongo.getDB(mongo_db);
		
		return db;
	}
//	public static boolean mysql_pooling=false; //true pour tomcats
//	public static String mysql_host = "localhost"; //132.227.201.34
//	public static String mysql_db = "bd_aar"; //328_b10
//	public static String mysql_username = ""; //328_b10
//	public static String mysql_password = ""; //328_b10
//	public static DataBase database = null;
//
//	public static Connection getMysqlConnection() throws SQLException{
//		if (DBStatic.mysql_pooling == false){
//			return (DriverManager.getConnection("jdbc:mysql://"+DBStatic.mysql_host+"/"+DBStatic.mysql_db, DBStatic.mysql_username, DBStatic.mysql_password));
//		}
//		else{
//			if (database == null){
//				database = new DataBase("jdbc/db");
//			}
//			return (database.getConnection());
//		}
//	}
}
