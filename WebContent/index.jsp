<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" title="kit" media="screen" type="text/css"
	href="style/index.css" />
<script type="text/javascript" src="script/environnement.js"></script>
<script type="text/javascript" src="script/jquery.js"></script>
<title>Page principale</title>
<meta name="robots" content="noindex,follow" />
<style>
body {
	background: url(images/bg.png);
	margin: 0 auto;
}
</style>

</head>

<body onLoad="javascript:main()">
	<!--onload sert a declencher une fonction quand toute la page est chargee pour ne pas manipuler des donnÃ©es inexistantes-->
	<%
		//allow access only if session exists
		String user = null;
		if (session.getAttribute("user") == null) {
			response.sendRedirect("connexion.jsp");
		} else
			user = (String) session.getAttribute("user"); // user <=> prenom nom
		String userName = null;
		String sessionID = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("user"))
					userName = cookie.getValue();
				if (cookie.getName().equals("JSESSIONID"))
					sessionID = cookie.getValue();
			}
		} else {
			sessionID = session.getId();
		}
	%>
	<div id="header">

		<div id="logo">
			<a href="index.jsp"><img src="images/logo.png" alt="Logo" /></a>
		</div>
		<!-- zone de recherche avec les choix de recherche sur les diffÃ©rents sites !! -->
		<div id="recherche"></div>

		<%
			if (user == null) {
		%>
		<div id="liens">
			<span> <a href='connexion.jsp'>Connexion</a> / 
			<a href='enregistrement.jsp'> Enregistrement </a>
			</span>
		</div>
		<%
			} else {
		%>
		<div id="liens">
			<span>
				<form action="<%=response.encodeURL("GetProfil")%>" method="post">
				<input type="hidden" name="patronyme" value="<%=user%>"/> 
					<input type="submit" value="<%=user%>">
				</form>
				<form action="<%=response.encodeURL("Logout")%>" method="post">
					<input type="submit" value="Logout">
				</form>
			</span>
		</div>
		<%
			}
		%>
	</div>
	<div id="page">
		<div id="stat">
			<span> <I> Zone de recherche</I>
				<p></p>
				<form name="zone_recherche" id="zone_recherche">
					<table>
						<tr>
							<td><font color="white">Produit :</font></td>
							<td><label><SELECT name="produit" id="produit"
									size="1">
										<OPTION value="films">Films</OPTION>
										<OPTION value="livres">Livres</OPTION>
										<OPTION value="jeux">Jeux vidéo</OPTION>
										<OPTION value="albums">Albums de musique</OPTION>
								</SELECT></label></td>
						</tr>
						<tr>
							<td><font color="white">Titre :</font></td>
							<td><input type="text" name="titre" id="titre"
								placeholder="Titre du produit"></td>
						</tr>
						<tr>
							<td><font color="white">Auteur :</font></td>
							<td><input type="text" name="auteur" id="auteur"
								placeholder="Auteur du produit"></td>
						</tr>
						<tr>
							<td><font color="white">Edité(e) entre le :</font></td>
							<td><input type="date" max="2020-12-25" min="1600-01-01"
								name="date_deb_parution" id="date_deb_parution"></td>
						</tr>
						<tr>
							<td><font color="white">Et le :</font></td>
							<td><input type="date" max="2020-12-25" min="1600-01-01"
								name="date_fin_parution" id="date_fin_parution"></td>
						</tr>
						<tr>
							<td COLSPAN=2 align="right"><input type="submit"
								value="Rechercher"></td>
						</tr>
					</table>
				</form>

			</span>
		</div>

		<div id="content">
			<div id="ajouter_comment">
				<I>Ajouter un commentaire:</I>
				<form name="new_comment" id="new_comment"
					action="javascript:func_new_comment(this)">

					<textarea id="commentaire" placeholder="Ecrivez votre commentaire"></textarea>
					<br /> <input type="submit" value="Poster" id="new_button" />
				</form>
			</div>

			<div id="comment">
				<h3>Commentaires:</h3>
				<div class="infos_comment"></div>
			</div>
		</div>
	</div>
</body>

</html>
