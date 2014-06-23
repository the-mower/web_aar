package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import BD.BDUser;

/**
 * Servlet implementation class AddFriend
 */
public class AddFriend extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ATT_RESULTAT = "resultat";
	public static final String FWD_PROFIL = "/profil.jsp";
	public static final String FWD_ACCUEIL = "/index.jsp";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddFriend() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String resultat="";

		/* Récupération des champs du formulaire. */
		String login_connect = (String) session.getAttribute("login");
		String user_connect = (String) session.getAttribute("user");
		String login_profil = request.getParameter("login_profil");
		String patronyme = request.getParameter( "patronyme" );		// ce champs est composé du prenom et nom du profil qu'il faut afficher
		String[] patronyme_profil = patronyme.split(" "),
				patronyme_connecte = user_connect.split(" ");

		try {
			BDUser.addFriend(patronyme_connecte[1], patronyme_connecte[0], login_connect, patronyme_profil[1], patronyme_profil[0], login_profil);
		} catch (Exception e) {
			resultat= e.getMessage();
		}

		/* Stockage du résultat et des messages d'erreur dans l'objet request */
		request.setAttribute( ATT_RESULTAT, resultat );

		if(resultat.length()==0){		//	pas d'erreurs
			System.out.println("AddFriend.doPost : chargement du profil de "+patronyme);

			/* Transmission de la paire d'objets request/response à notre JSP */
			this.getServletContext().getRequestDispatcher( FWD_ACCUEIL ).forward( request, response );
		}else{
			System.out.println("AddFriend.doPost : "+resultat);

			/* Transmission de la paire d'objets request/response à notre JSP */
			this.getServletContext().getRequestDispatcher( FWD_ACCUEIL ).forward( request, response );
		}
	}

}
