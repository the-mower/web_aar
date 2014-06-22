function enregistre(formulaire){
	var nom=formulaire.nom.value;
	var prenom=formulaire.prenom.value;
	var login=formulaire.login.value;
	var password=formulaire.mdp.value;
	var password2=formulaire.mdp2.value;
	var ok=verif_formulaire_enregistrement(prenom, nom, login, password, password2);
	if (ok){
		connexion(prenom,nom,login,password);
	}
	return false;
}

function verif_formulaire_enregistrement(prenom, nom, login,password, password2){
	if (prenom.length ==0){
		message_erreur("Pr&eacute;nom obligatoire " );
		return (false);
	}
	if (nom.length == 0){
		message_erreur(" Nom obligatoire ");
		return (false);
	}
	if (login.length == 0){
		message_erreur("Login obligatoire");
		return (false);
	}
	if (password.length == 0){
		message_erreur("Mot de passe obligatoire");
		return (false);
	}
	
	if (login.length >20){
		message_erreur("Login trop long (20 charactères max)");
		return (false);
	}
	
	if (password.length > 20){
		message_erreur("Mot de passe trop long (20 charactères max)");
		return (false);
	}
	
	if (password.length <8){
		message_erreur("Mot de passe trop court (min 8 characteres)");
		return (false);
	}
	
	if (password != password2){
		message_erreur("Les deux mots de passe sont diff&eacute;rents");
		return (false);
	}
	return true;
}

function connexion(prenom,nom,login, password){	 
	$("#erreur").replaceWith();
	$.ajax({
		type: "GET", //méthode d'envoi au serveur
		url: "http://127.0.0.1:8080/AAR/servlet/AddUser", //addresse du serveur et "AddUser" est le nom de la servlet
		data: "prenom="+prenom+"&nom="+nom+"&login="+login+"&password="+password, //comment ça apparait après l'url
		dataType: "json",//le p sert à éviter les cas de cross domain car la servlet n'est pas à la meme adresse
		success: window.location.href="index.html", //charge de la page principale en mode non connecté !
		error: function (jhi, status, exception){ //2eme = code d'erreur, 3eme = exception
			alert("Problème communication "+status+" "+exception);
			deconnexion();		//deconnexion de l'utilisateur si erreur 
			}
	});
	return false;
}

function message_erreur(msg){
	var er=$("#erreur");
	if (er.length==0){
		$("#formulaire").prepend("<div id='erreur'>"+msg+"</div>");
	}
	else{
		$("#erreur").replaceWith("<div id='erreur'>"+msg+"</div>");
	}
	return false;
}
