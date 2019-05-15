<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.organizacion.partidas.Partidas" %>
<%@ page language="java" import="model.Partida" %>
<%
String msg="";
Partida[] partidas = Partidas.devolverPartidas();
for (int i = 0; i < partidas.length; i++) {
	msg+="<tr>";
	String id = partidas[i].getId();
	msg+="<td>" + id + "</td>";
	String usuario = partidas[i].getUsuarioBean().getNombre();
	msg+="<td>" + usuario + "</td>";
	String fecha = partidas[i].getFecha().toString();
	msg+="<td>" + fecha + "</td>";
	int puntos = partidas[i].getPuntos();
	msg+="<td>" + puntos + "</td>";
	String tiempo = partidas[i].getTiempo().toString();
	msg+="<td>" + tiempo + "</td>";
	msg+="</tr>";
}


%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
    <meta name="viewport" content="width=device-width initial-scale=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />    
    <title>Partidas</title>
</head>
<body>
	<table border="1">
		<caption>Titulo de la tabla</caption>
		<tr>
			<th>ID</th>
			<th>Usuario</th>
			<th>Fecha</th>
			<th>Puntos</th>
			<th>Tiempo</th>
		</tr>
		<%= msg %>
	</table>
	<button onclick="location.href='../EnriqueVergara'">Volver</button>
</body>
</html>