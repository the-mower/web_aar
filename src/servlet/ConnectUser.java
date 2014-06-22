package servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import api.Users;

//import api.Users;

/**
 * Servlet implementation class ConnectUser
 */
public class ConnectUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String VUE          = "/connexion.jsp";
	public static final String CHAMP_USERNAME = "username";
	public static final String CHAMP_PASSWORD = "password";
	public static final String ATT_RESULTAT = "resultat";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ConnectUser() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resultat="";
		HashMap<String, String> infos= new HashMap<String, String>();

		/* Récupération des champs du formulaire. */
		String login = request.getParameter( CHAMP_USERNAME );
		String motDePasse = request.getParameter( CHAMP_PASSWORD );

		try {
			infos = Users.connectUser(login, motDePasse);
		} catch (Exception e) {
			resultat= e.getMessage();
		}

		/* Stockage du résultat et des messages d'erreur dans l'objet request */
		request.setAttribute( ATT_RESULTAT, resultat );

		if(resultat.length()==0){		//	enregistrement effectuE sans erreurs
			System.out.println("ConnectUser.doPost : utilisateur connu");
			String user = infos.get("prenom")+" "+infos.get("nom");
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30*60);	//	setting session to expiry in 30 mins
            
            Cookie userName = new Cookie("user", user);
            response.addCookie(userName);
            //Get the encoded URL string
            String encodedURL = response.encodeRedirectURL("index.jsp");
            response.sendRedirect(encodedURL);
        }else{
        	System.out.println("AddUser.doPost : "+resultat);
        	
        	/* Transmission de la paire d'objets request/response à notre JSP */
    		this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }
	}

}
