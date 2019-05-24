package com.organizacion.partidas;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import model.Palabra;
import model.Partida;

/**
 * Servlet implementation class Partidas
 */
@WebServlet("/Partidas")
public class Partidas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Partidas() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		getServletContext().getRequestDispatcher("/partidas.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public static Partida[] devolverPartidas() {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "EnriqueVergara" );
        EntityManager entitymanager = emfactory.createEntityManager();
		
		TypedQuery<Partida> query = entitymanager.createQuery("SELECT * from partidas", Partida.class);
		  List<Partida> results = query.getResultList();
		  int i = 1;
		  Partida[] partidas = new Partida[i];
		  for (Partida result : results) {
			  Partida[] nueva_partidas = new Partida[++i];
			  for (int j = 0; j < partidas.length; j++) {
		        	nueva_partidas[j]=partidas[j];
			  }
			  nueva_partidas[i-1]=result;
			  partidas=nueva_partidas;
		  }
		
        return partidas;
	}

}
