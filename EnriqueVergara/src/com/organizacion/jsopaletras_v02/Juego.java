
package com.organizacion.jsopaletras_v02;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.naming.CommunicationException;
import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Juego extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/juego.jsp").forward(request, response);
	}
	
	public static CreaSopaLetras generarSopa() throws SQLException {
        CreaSopaLetras sopa = new CreaSopaLetras();
        sopa.crearSopaLetras();      
		return sopa;
    }

}

