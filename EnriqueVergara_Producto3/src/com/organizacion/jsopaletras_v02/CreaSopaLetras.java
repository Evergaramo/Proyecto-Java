
package com.organizacion.jsopaletras_v02;

import java.text.Normalizer;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.Palabra;

public class CreaSopaLetras{
	
	final String texto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	final int dimen = 12, nPalabras = 10;
	Sopaletras2 sopa = new Sopaletras2(dimen, dimen, texto);
	
	// Query que usarás para hacer lo que necesites
	private String sSQL = "SELECT palabra FROM Palabras WHERE LENGTH(palabra) >= 3 AND LENGTH(palabra) <= 10 ORDER BY RAND() LIMIT " + nPalabras + ";";
	
	char[][] tabla;
	String[] palabras;
	

    public void crearSopaLetras() {
    	palabras = cogerPalabras();
    	agregarPalabras(palabras);
    	tabla = sopa.getJLabel();
    }
    
    private String[] cogerPalabras() {
    	
        String[] strings = new String[nPalabras];

        EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "EnriqueVergara" );
        EntityManager entitymanager = emfactory.createEntityManager();
        
        TypedQuery<Palabra> query = entitymanager.createQuery(sSQL, Palabra.class);
        List<Palabra> results = query.getResultList();
        int i = 0;
        for (Palabra result : results) {
        	strings[i]=normalizeString(result.getPalabra().toUpperCase());
        	i++;
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
