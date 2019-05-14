
package com.organizacion.jsopaletras_v02;

import java.sql.SQLException;
import java.text.Normalizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Palabra;

public class CreaSopaLetras{
	
	final String texto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	final int dimen = 12, nPalabras = 10;
	Sopaletras2 sopa = new Sopaletras2(dimen, dimen, texto);
	
	// Instancias la clase que hemos creado anteriormente
	//private ConexionMySQL SQL = new ConexionMySQL();
	// Llamas al método que tiene la clase y te devuelve una conexión
	//private Connection conn = ConexionMySQL.conectarMySQL();
	// Query que usarás para hacer lo que necesites
	//private String sSQL = "SELECT palabra FROM Palabras WHERE LENGTH(palabra) >= 3 AND LENGTH(palabra) <= 10 ORDER BY RAND() LIMIT " + nPalabras + ";";
	
	char[][] tabla;
	String[] palabras;
	

    public void crearSopaLetras() throws SQLException{
    	palabras = cogerPalabras();
    	agregarPalabras(palabras);
    	tabla = sopa.getJLabel();
    }
    
    private String[] cogerPalabras() throws SQLException{
    	
    	EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "EnriqueVergara" );
        EntityManager entitymanager = emfactory.createEntityManager();

        String[] strings = new String[nPalabras];

        for(int i = 0; i < nPalabras; i++) {
        	int valorDado = (int) Math.floor(Math.random()*40000+1);//hasta 40000 palabras
        	Palabra palabra= entitymanager.find( Palabra.class, valorDado );
        	strings[i]=normalizeString(palabra.getPalabra().toUpperCase());
        }
   
		return strings;
	}
    
    public static String normalizeString(String str){
    	 str=Normalizer.normalize(str,Normalizer.Form.NFKD);
    	 return str.replaceAll("[^a-z,^A-Z,^0-9]", "");
    	}

	private void agregarPalabras(String[] strings) {
    	for (int i = 0; i < strings.length; i++) {
    		boolean correcto = false;
			do {
				correcto = agregarPalabra(strings[i]);
			} while(!correcto);
		}
    }

	private boolean agregarPalabra(String string) {		
		 int x = (int)(Math.random() * dimen);
		 int y = (int)(Math.random() * dimen);
		 return new InsertaPalabra(sopa, x, y).agregar(string);	
	}
	
	public char[][] getTabla(){
		return tabla;
	}
	
	public String[] getPalabras(){
		return palabras;
	}
    
}
