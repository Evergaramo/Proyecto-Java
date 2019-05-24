<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.organizacion.aplicacion.Autenticacion" %>
<%
 
 String err=(String) session.getAttribute( Autenticacion.ERROR_LABEL);
 String msg="", msg2="";
 if (!Autenticacion.isNull(err)){
    msg="<span style=\"color:red;\">"+ err +"</span>";
 }else if(!Autenticacion.isNull(Autenticacion.correo)){
	 msg2="<button onclick=\"location.href='juego'\">Jugar</button><button onclick=\"location.href='partidas.jsp'\">Ver partidas</button>";
 }

%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width initial-scale=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>Página de login</title>
    <style type="text/css">
.box {
  width: 450px; 
  margin-top: 20%; 
  margin-left: 30%;
}
table {
  margin: auto;
}
.col1 {
  text-align: right;
}
.colc {
  text-align: center;
  padding: 20px;
}
    </style>
  </head>
  <body>
    <div align="center" class="box">
      <h1>Bienvenido</h1>
      <form action="comprobar-ldap" method="post">
        <table>
          <tr>
            <td class="col1">Nombre de usuario: </td>
            <td><input type="text" name="login" /></td>
          </tr>
          <tr>
            <td class="col1">Contraseña: </td>
            <td><input type="password" name="passwd" /></td>
          </tr>
          <tr>
            <td colspan="2" class="colc">
              <input type="submit" value="Autenticar" />
            </td>
          </tr>
          <tr>
            <td colspan="2" class="colc">
              <%= msg %>
            </td>
          </tr>
        </table>
      </form>
      <tr>
		<%= msg2 %>
	  </tr>
    </div>
  </body>
</html>
<%
 try {
   session.removeAttribute( Autenticacion.ERROR_LABEL );
 }catch(Exception e){
 }
%>
