function main(){
	environnement = new Object(); //site final
	environnement.users = new Array(); //site final
	var u1 = new User(4, "Yohan"); //simulation
	environnement.users[1] = u1;//simulation
	environnement.actif = environnement.users[1];//simulation
	environnement.actif.key = 15487845488456;
	gererDivConnexion();//simulation
	//$("#disconnect").click(disconnect); //site final
	//$("#box.friends").click(func_filtre); //site final
	var json_text = envoiCommentaires("Coucou");//simulation
	RechercheCommentaire.traiterReponseJSON(json_text);//simulation
	
	nb_erreur = 0; //site final (pour ne pas avoir une boucle infine)
}

function User(id, login, contact){
	this.id = id; //Comme on appelle un new User(), le this s\'applique sur l\'objet User.
	this.login = login;
	this.contact = contact;
	if (contact == undefined){
		this.contact = false; //Par défaut contact est à false
	}
	if (environnement==undefined){
		environnement ==[];
	}
	
	if (environnement.users==undefined){
		environnement.users==[];
	}
	environnement.users[this.id]=this;
}

User.prototype.modifStatus=function(){
	if (this.contact){
		this.contact = false;
	}
	else{
		this.contact = true;
	}
};

function Commentaire(id, auteur, texte, date){
	this.id=id; 
	this.auteur=auteur; 
	this.texte=  texte;
	this.date=date;
}

Commentaire.prototype.getHtml = function(){
	var text="<div class='commentaire'>"+this.texte+"<br/><span class='infos_comment'>Post&eacute; par "+this.auteur.login+" le "+this.date+"</span>";
	if (this.auteur.id != environnement.actif.id){
		if(this.auteur.contact==true){
		text+=" <span class='moins"+this.auteur.id+"'><img src='images/supprimer.gif' alt='Supprimer cet ami' onClick='javascript:ajoutsup_contact(\"-\","+this.auteur.id+");' /></span>";
		}
		else{
		text+=" <span class='plus"+this.auteur.id+"'><img src='images/ajouter.gif' alt='Ajouter comme ami' onClick='javascript:ajoutsup_contact(\"+\","+this.auteur.id+");' /></span>";
		}
	}
	return (text);
};

function ajoutsup_contact(valeurimage, id_friend){
	environnement.users[id_friend].modifStatus();	//status de l'utilisateur concerné modifié !
	if (valeurimage == "+"){
		$.ajax({
			type: "GET", //méthode d'envoi au serveur
			url: "http://132.227.201.134:8080/binome10/servlet/AddFriend", //addresse du serveur et "AddFriend" est le nom de la servlet
			data: "key="+environnement.actif.id+"&id_friend="+id_friend, //comment ça apparait après l'url
			dataType: "json",//le p sert à éviter les cas de cross domain car la servlet n'est pas à la meme adresse
			success: $(".plus"+id_friend).replaceWith("<span class='moins"+id_friend+"'><img src='images/supprimer.gif' alt='Supprimer cet ami' id='-' onClick='javascript:ajoutsup_contact(\"-\","+id_friend+")' /></span>"), 
							//quand on aura une réponse du serveur, on modifie le button
			error: function (jhi, status, exception){ //2eme = code d'erreur, 3eme = exception
				alert("Probleme de communication "+status+" "+exception);
				deconnexion();			//deconnexion de l'utilisateur si erreur 
			}
		});
	}else{
		$.ajax({
			type: "GET", //méthode d'envoi au serveur
			url: "http://132.227.201.134:8080/binome10/servlet/RemoveFriend", //addresse du serveur et "RemoveFriend" est le nom de la servlet
			data: "key="+environnement.actif.id+"&id_friend="+id_friend, //comment ça apparait après l'url
			dataType: "json",//le p sert à éviter les cas de cross domain car la servlet n'est pas à la meme adresse
			success: $(".moins"+id_friend).replaceWith("<span class='plus"+id_friend+"'><img src='images/ajouter.gif' alt='Ajouter cet ami' id='+' onClick='javascript:ajoutsup_contact(\"+\","+id_friend+")' /></span>"),
									//quand on aura une réponse du serveur, on modifie le button
			error: function (jhi, status, exception){ //2eme = code d'erreur, 3eme = exception
				alert("Probleme de communication "+status+" "+exception);
				deconnexion();			//deconnexion de l'utilisateur si erreur 
				}
		});
	}
}

function RechercheCommentaire(resultats, recherche, contacts_only, auteur, date){
	this.resultats=resultats;
	this.contacts_only = contacts_only;
	this.auteur = auteur;
	this.date = date;
	this.recherche = recherche;
	if (recherche.length == 0 || recherche == undefined){
		this.recherche = "";
	}
	if (contacts_only == undefined){
		this.contacts_only = false;
	}
	if (date == undefined){
		this.date = new Date();
	}
}

RechercheCommentaire.prototype.getHtml = function(){
	var text="";
	if(this.contacts_only){
		 for each (com in this.resultats){
			if (com.auteur.contact == true){
				text+=com.getHtml()+"<br/><br/>";
			}
		}
	}
	else{
		for each (com in this.resultats){
			text+=com.getHtml()+"<br/><br/>";
		}
	}
	return text;
};

RechercheCommentaire.revive = function(key, value){
	if (key.length == 0){
		var r;
		if ((value.erreur == undefined) || (value.erreur == 0)){ //erreur sera utilisé par le serveur. 
		//Si une opération s'est bien passée, le champ erreur est undefined ou égal à 0, sinon il est > 0. Suivant sa veleur, on pourra renvoyer différents types d'erreur.
			r = new RechercheCommentaire(value.resultats, value.recherche, value.contacts_only, value.auteur, value.date);
		}
		else{
			r = new Object;
			r.erreur = value.erreur;
		}
		return (r);
	}
	else 
		if (key=='auteur'){ //si la cle est un auteur
			var u;
			if ((environnement!=undefined) && (environnement.users != undefined) && (environnement.users[value.id] != undefined)){
				u = environnement.users[value.id];
			}
			else{
				u = new User(value.id, value.login, value.contact);
			}
			return (u); //on renvoie l'utilisateur cherché, s'il n'existe pas on envoie un nouvel utilisateur créé juste avant
		}
		else 
			if((isNumber(key)) && (value.auteur instanceof User)){//si la clé est un numéro, c'est qu'il s'agit d'un commentaire. 
			//On fait un test inutile en plus (value.auteur instanceOf user) et on retourne un nouveau commentaire
				var c = new Commentaire(value.id, value.auteur, value.texte, value.date, value.score);
				return (c);
			}
			else 
				if(key=='date'){//si la clé est une date on retourne une nouvelle date avec la valeur tapée
					var d=new Date(value);
					return (d);
				}
				else{	//donc c'est un comentaire 
					return (value);
				}
};

RechercheCommentaire.traiterReponseJSON = function(json_text) {
	var old_users = environnement.users; //on enregistre l'ancien tableau de users de l'environnement
	environnement.users = [];
	var obj = JSON.parse(json_text,RechercheCommentaire.revive);
	if (obj.erreur == undefined){
		$("#comment").html("<h3>Commentaires:</h3>"+obj.getHtml()); //remplace le texte de la div par le getHtml de cette recherche de comentaires
	}
	else{
		environnement.users = old_users;
		if (obj.erreur ==1){
			alert("Probleme serveur");
		}
		else{
			alert("Probleme base de donnees");
		}
	}
};

function envoiCommentaires(rech){
	var u1 = new User(1, "Lela", true);
	var u2 = new User(2, "Krystal", false);
	var u3 = new User(3, "Naomi");
	var com1 = new Commentaire(23, u2, "Coucou !", new Date(), 45);
	var com2 = new Commentaire(5, u1, "Salut !", new Date(), 27);
	var com3 = new Commentaire(7, u3, "Kikou lol !", new Date(), 12);
	var tab= [];
	tab[0] = com1;
	tab[1] = com2;
	tab[2] = com3;
	var d = new Date();
	var rec= new RechercheCommentaire(tab, rech, false, u3, d);
	return (JSON.stringify(rec));
}


function isNumber(x) { 
	return ! isNaN (x-0);
}

function gererDivConnexion(){
	if (environnement.actif == undefined){
		$("#liens").replaceWith("<div id='liens'><a href='connexion.html'>Connexion</a><br/><a href='enregistrement.html'> Enregistrement <a/></div>");
	}
	else{
		$("#liens").replaceWith("<div id='liens'>Bienvenu(e) "+environnement.actif.login+"<img src='images/deconnexion.gif' alt='Deconnexion' onClick='javascript:deconnexion();'/></div>");
	}
}

function choixSearch(x){
	if (x==1){	// recherche sur YouTube
		$("#recherche").replaceWith("<div id='recherche'><form action='http://www.youtube.com/results' method='get' target='_blank'><TABLE width=100%>"+
		"<A HREF='http://www.youtube.com'><img id='logo' src='images/yt.png' alt='Accueil YouTube' align='absmiddle'></A>"+
		"<input type='text' maxlength='128' /><select name='search_type'><option value=''>Videos</option>"+
		"<option value='search_users'>Channels</option></select><input type='submit' value='Search' />"+
		"<input id='retour' type='submit' value='Rechoisir' Onclick='javascript:choixSearch(4);'/></form></div>");
	}else{
		if (x==2){	//recherce sur notre site
			$("#recherche").replaceWith("<div id='recherche'><form name='searchform' id='search.form' action='javascript:search(this)'>"+
			"<input type='text' placeholder='Recherche' name='Recherche' id='text_search'/><br/>"+
			"<input type='checkbox' value='friends' id='box_friends'/> Filtrer par contacts <input type='submit' value='Rechercher' id='submit_search'/>"+
			"<input id='retour' type='submit' value='Rechoisir' Onclick='javascript:choixSearch(4);'/></form></div>");
		}
		else{	//recherche sur Google
			if (x==3){
				$("#recherche").replaceWith("<div id='recherche'><FORM method=GET action='http://www.google.fr/search'><TABLE width=100%><tr>"+
				"<td><A HREF='http://www.google.fr'><img SRC='http://www.google.com/logos/Logo_40wht.gif' border='0' ALT='Google' align='absmiddle'>"+
				"</A><INPUT TYPE=text name=q size=50 maxlength=255 value=''><INPUT type=submit name=btnG VALUE='Recherche'></td></tr>"+
				"<TFOOT ALIGN='right'><tr><td><input id='retour' type='submit' value='Rechoisir' Onclick='javascript:choixSearch(4);'/>"+
				"</td></tr></TFOOT></TABLE></FORM></div>");
			}else{	//remise des champs de recherche
				$("#recherche").replaceWith("<div id='recherche'><form name='searchform' id='search.form'><I>Faire une rechercher sur : </I></br>"+
				"<table width=100%><td align='center'>"+
				"<img src='images/ic_youtube.png' class='photoGauche' alt='youtube-logo' onClick='javascript:choixSearch(1);' /></td>"+
				"<td align='center'><img src='images/Logo.png' class='photo' alt='Logo' onClick='javascript:choixSearch(2);'/></td>"+
				"<td align='center'><img src='images/logoGoogle.gif' class='photoDroite' alt='google-logo' onClick='javascript:choixSearch(3);'/></td></table></form></div>");
			}
	}
}
}

function deconnexion(){ 
	$.ajax({
		type: "GET", //méthode d'envoi au serveur
		url: "http://132.227.201.134:8080/binome10/servlet/Logout", //addresse du serveur et "Logout" est le nom de la servlet
		data: "key="+environnement.actif.key,
		dataType: "json",//le p sert à éviter les cas de cross domain car la servlet n'est pas à la meme adresse
		success: ($("#liens").replaceWith("<div id='liens'><a href='connexion.html'>Connexion</a><br/><a href='enregistrement.html'> Enregistrement <a/></div>"), 
						//quand on aura une réponse du serveur, on modifie la zone de connexion
				$("#comment").replaceWith("<div id='comment'><h3>Commentaires:</h3><div class='infos_comment'></div></div>")),
		error: function (jhi, status, exception){ //2eme = code d'erreur, 3eme = exception
			alert("Probl�me communication "+status+" "+exception);
			if(nb_erreur==0){
				deconnexion();			//deconnexion de l'utilisateur si erreur 
			}
			nb_erreur ++;
		}
	});
	environnement.actif = undefined;
	environnement.key= undefined;
}

function search(form){
	if(box_friends.checked){l=1;} else{l=0;};
	$.ajax({
		type: "GET", //méthode d'envoi au serveur
		url: "http://132.227.201.134:8080/binome10/servlet/Search", //addresse du serveur et "Search" est le nom de la servlet
		data: "key="+environnement.actif.id+"&query="+text_search.value+"&friends="+l,
		dataType: "json",//le p sert à éviter les cas de cross domain car la servlet n'est pas à la meme adresse
		success: RechercheCommentaire.traiterReponseJSON, 
					//quand on aura une réponse du serveur, on modifie la zone de connexion
		error: function (jhi, status, exception){ //2eme = code d'erreur, 3eme = exception
			alert("Problème communication "+status+" "+exception);
			deconnexion();			//deconnexion de l'utilisateur si erreur 
			}
	});
	alert("recherche de : "+ l + " : "+ text_search.value + "\n\n\n   doit etre supprimer ");
}	

function func_new_comment(form){ 
	$.ajax({
		type: "GET", //méthode d'envoi au serveur
		url: "http://132.227.201.134:8080/binome10/servlet/AddComment", //addresse du serveur et "AddComment" est le nom de la servlet
		data: "key="+environnement.actif.id+"&text="+commentaire.value, //comment ça apparait après l'url
		dataType: "json",       //le p sert � �viter les cas de cross domain car la servlet n'est pas à la meme adresse
		success: search,	//quand on aura une r�ponse du serveur, on rappelle search()
		error: function (jhi, status, exception){ //2eme = code d'erreur, 3eme = exception
			alert("Probleme de communication "+status+" "+exception);
			deconnexion();			//deconnexion de l'utilisateur si erreur 
		}
	});
}