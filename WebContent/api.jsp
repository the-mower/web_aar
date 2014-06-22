<html>
<head>
<title>Books API Example</title>
</head>
<body>
	<div id="content">
		<table id="tableau" cellspacing="0" cellpadding="3px" rules="rows"
			style="border: solid 1px #777777; border-collapse: collapse; font-family: verdana; font-size: 11px;" width="80%" align="center"></table>
	</div>
	<script>
		function handleResponse(response) {
			for (var i = 0; i < response.items.length; i++) {
				var item = response.items[i];
				var auteurs = "";

				for (var j = 0; j < item.volumeInfo.authors.length; j++) {
					auteurs += item.volumeInfo.authors[j] + " & ";
				}
				auteurs = auteurs.substring(0, auteurs.length - 2);

				document.getElementById("tableau").innerHTML += "<tr><td><a href='index.html'><img src="+ item.volumeInfo.smallThumbnail+" alt='Logo'/></a></td>"
						+ "<td><h3>"
						+ item.volumeInfo.title
						+ "</h3> de "+ auteurs + " ("+item.volumeInfo.publishedDate+")<br>"+item.volumeInfo.publisher+"</td></tr>";

			}
			document.getElementById("tableau").innerHTML += "</table>";
		}
	</script>
	<script
		src="https://www.googleapis.com/books/v1/volumes?q=harry+potter&callback=handleResponse"></script>
</body>
</html>