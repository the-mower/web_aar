<html>
<head>

<title>Page d'enregistrement</title>

<link rel="stylesheet" title="kit" media="screen" type="text/css"
	href="style/enregistrement.css" />

<style>
body {
	background: url(images/bg.png) center;
	margin: 0 auto;
	width: 960px;
	padding-top: 50px
}

.footer {
	margin-top: 50px;
	text-align: center;
	color: #666;
	font: bold 14px Arial
}

.footer a {
	color: #999;
	text-decoration: none
}

.main {
	margin: 50px auto;
}
</style>
<meta name="robots" content="noindex,follow" />
<script type="text/javascript" src="script/enregistrement.js"></script>
<script type="text/javascript" src="script/jquery.js"></script>

<%
	//redirection vers la page principale si une session est activEe
	if (session.getAttribute("user") != null) {
		response.sendRedirect("index.jsp");
	}
%>
</head>

<body>

	<div class="main">

		<h1>Enregistrement</h1>

		<form action="AddUser" method="post">

			<div id="formulaire">
				<div id="erreur" color="green">${resultat}</div>
				<div class="nom">
					<input type="text" name="prenom" placeholder="First Name" /> <input
						type="text" name="nom" placeholder="Name" />
				</div>

				<div class="ids">
					<input type="text" name="login" placeholder="Login" />
				</div>

				<div class="ids">
					<input type="password" name="mdp" placeholder="Password" />
				</div>

				<div class="ids">
					<input type="password" name="mdp2" placeholder="Password" />
				</div>

				<div class="buttons">
					<input type="submit" value="Enregistrer"
						onclick="javascript:enregistre(this)" /> <input type="button"
						value="Annuler" onClick="window.location.href='index.jsp'" />
				</div>
			</div>

		</form>
	</div>
</body>
</html>

