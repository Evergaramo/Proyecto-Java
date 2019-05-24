<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.organizacion.aplicacion.Autenticacion" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width initial-scale=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />

    <title>Resultado</title>
    <style type="text/css">
.box {
  width: 450px; 
  margin-top: 20%; 
  margin-left: 30%;
}
table {
  margin: auto;
}
.colc {
  text-align: center;
  padding: 20px;
}
    </style>
  </head>
  <body>
    <div align="center" class="box">
      <table>
        <tr>
          <td colspan="2" class="colc">
            <%= ( Autenticacion.isNull((String) session.getAttribute("email")) ? "" : session.getAttribute("email")  ) %>
          </td>
        </tr>
        <tr>
          <td colspan="2" class="colc">
            <span style="color:red;">
              <%= ( Autenticacion.isNull((String) session.getAttribute("error")) ? "" : session.getAttribute("error")  ) %>
            </span>
          </td>
        </tr>
      </table>
    </div>
  </body>
</html>
