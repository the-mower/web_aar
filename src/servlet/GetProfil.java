package servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BD.BDUser;
import api.Users;

/**
 * Servlet implementation class GetProfil
 */
public class GetProfil extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String CHAMP_LOGIN = "login";
	public static final String CHAMP_NOM = "nom";
	public static final String CHAMP_PRENOM = "prenom";
	public static final String CHAMP_USER_PROFIL = "user_profil";
	public static final String CHAMP_ARE_FRIENDS = "is_my_friend";
	public static final String ATT_RESULTAT = "resultat";
	public static final String CHAMP_AMIS = "liste_amis";
	public static final String FWD_PROFIL = "/profil.jsp";
	public static final String FWD_ACCUEIL = "/index.jsp";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetProfil() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> infos= new HashMap<String, String>();
		HttpSession session = request.getSession();
		String resultat="";
		String liste_amis="";
		String is_my_friend="0";
		boolean are_friends = false;

		/* Récupération des champs du formulaire. */
		String user_connect = (String) session.getAttribute("user");
		String patronyme = request.getParameter( "patronyme" );		// ce champs est composé du prenom et nom du profil qu'il faut afficher
		String[] patronyme_profil = patronyme.split(" "),
				patronyme_connecte = user_connect.split(" ");

		try {
			infos = BDUser.getInfoUser(patronyme_profil[1], patronyme_profil[0]);
			liste_amis = BDUser.getFriendsUser(patronyme_profil[1], patronyme_profil[0]);
			are_friends = BDUser.areFriends(patronyme_connecte[1], patronyme_connecte[0], patronyme_profil[1], patronyme_profil[0]);
		} catch (Exception e) {
			resultat= e.getMessage();
		}

		if (user_connect.equals(patronyme) || are_friends){
			is_my_friend="1";
		}
		
		/* Stockage du résultat et des messages d'erreur dans l'objet request */
		request.setAttribute( ATT_RESULTAT, resultat );
		request.setAttribute( CHAMP_LOGIN, infos.get("login"));
		request.setAttribute(CHAMP_NOM, infos.get("nom"));
		request.setAttribute(CHAMP_PRENOM, infos.get("prenom"));
		request.setAttribute(CHAMP_USER_PROFIL, patronyme);
		request.setAttribute(CHAMP_AMIS, liste_amis);
		request.setAttribute(CHAMP_ARE_FRIENDS, is_my_friend);

		if(resultat.length()==0){		//	pas d'erreurs
			System.out.println("GetProfil.doPost : chargement du profil de "+patronyme);
			
			/* Transmission de la paire d'objets request/response à notre JSP */
			this.getServletContext().getRequestDispatcher( FWD_PROFIL ).forward( request, response );
		}else{
			System.out.println("GetProfil.doPost : "+resultat);

			/* Transmission de la paire d'objets request/response à notre JSP */
			this.getServletContext().getRequestDispatcher( FWD_ACCUEIL ).forward( request, response );
		}

	}

}
