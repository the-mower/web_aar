<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<link rel="stylesheet" title="kit" media="screen" type="text/css"
	href="style/index.css" />
<%
	//allow access only if session exists
	String user = null;
	String user_profil = null;
	String are_friends = "0";
	String nom_profil = null;
	String prenom_profil = null;
	String login_profil = null;

	if (session.getAttribute("user") == null) {
		response.sendRedirect("connexion.jsp");
	} else {
		user = (String) session.getAttribute("user"); // user <=> prenom nom
		user_profil = (String) request.getAttribute("user_profil");
		are_friends = (String) request.getAttribute("is_my_friend");
		nom_profil = (String) request.getAttribute("nom");
		prenom_profil = (String) request.getAttribute("prenom");
		login_profil = (String) request.getAttribute("login");
	}
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

<title><%=user_profil%></title>
<meta name="robots" content="noindex,follow" />
<style>
body {
	background: url(images/bg.png);
	margin: 0 auto;
}
</style>
</head>

<body>

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
			<span> <a href='connexion.jsp'>Connexion</a> / <a
				href='enregistrement.jsp'> Enregistrement </a>
			</span>
		</div>
		<%
			} else {
		%>
		<div id="liens">
			<span>
				<form action="<%=response.encodeURL("GetProfil")%>" method="post">
					<input type="hidden" name="patronyme" value="<%=user%>" /> <input
						type="submit" value="<%=user%>">
				</form>
				<form action="<%=response.encodeURL("Logout")%>" method="post">
					<input type="submit" value="Logout" />
				</form>
			</span>
		</div>
		<%
			}
		%>
	</div>

	<div id="page">
		<div id="stat">
			<span> <I> Informations personnelles : </I> <br>
				<form action="<%=response.encodeURL("AddFriend")%>" method="post" name="zone_recherche" id="zone_recherche">
					<input type="hidden" name="patronyme" value="<%=user_profil%>" /> 
					<input type="hidden" name="nom_profil" value="<%=nom_profil%>" />Nom : ${nom} <br>
					<input type="hidden" name="prenom_profil" value="<%=prenom_profil%>" />Prenom : ${prenom} <br>
					<input type="hidden" name="login_profil" value="<%=login_profil%>" />Login : ${login}<br>
					<%
						if (!are_friends.equals("1")) {
					%>
					<span style="text-align: right;">
							<input type="submit" value="Ajouter comme ami(e)" />
					</span>
					<%
						} else {
					%>
					<br>
					<%
						}
					%>
				</form> <%-- <table name="zone_recherche" id="zone_recherche">
					<tr>
						<td><font color="white">Nom : ${nom}</font></td>
					</tr>
					<tr>
						<td><font color="white">Prénom : ${prenom}</font></td>
					</tr>
					<tr>
						<td><font color="white">Login : ${login}</font></td>
					</tr>
				</table> --%>
			</span>

			<!-- Ajouter comme ami s'il le faut -->


			<!-- listes d'amis -->
			<span> <I> Liste d'amis : </I>
				<table name="zone_recherche" id="zone_recherche">
					${liste_amis}
				</table>
			</span>
		</div>

		<div id="content">derniers commentaires postés</div>
	</div>

</body>
</html>