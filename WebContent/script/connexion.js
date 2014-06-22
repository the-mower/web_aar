function connecte(formulaire){
	var login=formulaire.username.value;
	var password=formulaire.password.value;
	var ok=verif_formulaire_connexion(login,password);
//	if (ok){
//		connexion(login,password);
//	}
//	return false;
	return ok;
}

// affichage d'un message d'erreur
function message_erreur(msg){
	if (msg == null){
		return true;
	}
	var er=$("#erreur");
	if (er.length==0){
		$("#formulaire").prepend("<div id='erreur' color='green'>"+msg+"</div>");
	}
	else{
		$("#erreur").replaceWith("<div id='erreur' color='green'>"+msg+"</div>");
	}
	return false;
}

// verification que les champs ont été saisi
function verif_formulaire_connexion(login,password){
	if (login.length == 0){
		message_erreur("Login obligatoire");
		return (false);
	}
	if (password.length == 0){
		message_erreur("Mot de passe obligatoire");
		return (false);
	}

	if (login.length >20){
		alert("Login trop long (max 20 charactères");
		return (false);
	}

	if (password.length > 20){
		alert("Mot de passe trop long (max 20 charactères)");
		return (false);
	}
	return true;
}

//function connexion(login, password){
//	$("#erreur").replaceWith();
//	$.ajax({
//	type: "GET", //méthode d'envoi au serveur
//	url: "http://127.0.0.1:8080/AAR/servlet/ConnectUser", //addresse du serveur et "Login" est le nom de la servlet
//	data: "login="+login+"&password="+password+"&callback=?jsonp", //comment ça apparait après l'url
//	dataType: "jsonp",//le p sert à éviter les cas de cross domain car la servlet n'est pas à la meme adresse
//	success: traiteReponseConnexion, //quand on aura une réponse du serveur, on appellera cette fonction avec en 
//	//paramètre la réponse du serveur
//	error: function (jhi, status, exception){ //2eme = code d'erreur, 3eme = exception
//	alert("Probleme de communication "+status+" "+exception);
//	deconnexion();				//deconnexion de l'utilisateur si erreur 
//	}
//	});
//
//	return false;
//}

//$("#erreur").replaceWith();
//$.ajax({
//type: "GET", //méthode d'envoi au serveur
//url: "http://127.0.0.1:8080/AAR/servlet/ConnectUser", //addresse du serveur et "Login" est le nom de la servlet
//data: "login="+login+"&password="+password, //comment ça apparait après l'url
//dataType: "jsonp",//le p sert à éviter les cas de cross domain car la servlet n'est pas à la meme adresse
//jsonpCallback: 'iden',
//success: function(data){
//	$("#erreur").replaceWith(data);
//},//traiteReponseConnexion, //quand on aura une réponse du serveur, on appellera cette fonction avec en 
////paramètre la réponse du serveur
//error: function (jhi, status, exception){ //2eme = code d'erreur, 3eme = exception
//alert("Probleme de communication "+status+" "+exception);
//deconnexion();				//deconnexion de l'utilisateur si erreur 
//}
//});
function traiteReponseConnexion(json){
	if(json.erreur!=undefined){
		message_erreur(json.erreur);
	}
	else{
		window.location.href="index.html?id="+json.id+"&login="+json.login+"&key="+json.key;
	}
	return false;
}
