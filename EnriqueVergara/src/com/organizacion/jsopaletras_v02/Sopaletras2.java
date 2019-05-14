
package com.organizacion.jsopaletras_v02;

import java.util.Vector;
import javax.swing.JLabel;

public class Sopaletras2 {
    //private char[][] letras;
    private int filas, columnas;
    private char[][] jletra;
    private int[][] lockedPos;
    private String caracteres;
    private Vector<PalabrasIntro> palabras;

    public Sopaletras2(int xfilas, int xcolumnas, String xcaracteres){
        filas = xfilas;
        columnas = xcolumnas;
        jletra = new char[filas][columnas];
        lockedPos = new int[filas][columnas];
        palabras = new Vector();
        caracteres = xcaracteres;
        int xAscii,i,j;
        for(i = 0; i < filas; i++){
            for(j = 0; j < columnas; j++){
                xAscii = (int)(Math.random()* caracteres.length());
                jletra[i][j] = caracteres.substring(xAscii,xAscii+1).charAt(0);    
            }
        }
    }
    public void New(){
        int xAscii,i,j;
        lockedPos = new int[filas][columnas];
        palabras = new Vector();
        for(i = 0; i < filas; i++){
            for(j = 0; j < columnas; j++){
                xAscii = (int)(Math.random()* caracteres.length());
                jletra[i][j]=caracteres.substring(xAscii,xAscii+1).charAt(0);
            }
        }
    }
    public char[][] getJLabel(){
        return jletra;
    }
    public boolean lockedPosition(int x, int y){ // Verdadero si la posicion es ocupada por una
        if(lockedPos[x][y] == 1)                 // letra de una palabra agregada
            return true;
        else
            return false;
    }
    public int getTotalFilas(){
        return filas;
    }
    public int getTotalColumnas(){
        return columnas;
    }
    public String getCaracteresPermitidos(){
        return caracteres;
    }
    public char getChar(int xfila, int xcolumna){
        return jletra[xfila][xcolumna];
    }
    public int getTotalPalabras(){
        return palabras.size();
    }
    public void RemovePalabra(int pos){
        PalabrasIntro pa = palabras.get(pos);
        int i,xAscii,x,y;
        for(i = 0; i < pa.getSizePalabra(); i++){
            xAscii = (int)(Math.random() * caracteres.length());
            x = pa.getPosX(i);
            y = pa.getPosY(i);
            lockedPos[x][y]--;
            if(lockedPos[x][y] < 1)
                jletra[x][y]=caracteres.substring(xAscii, xAscii+1).charAt(0);
        }
        palabras.remove(pos);
    }
    
    public Vector getVectorPalabas(){
        return palabras;
    }
    
    public PalabrasIntro getPalabra(int pos){
        return palabras.get(pos);
    }
    
    public void setPalabra(String palabra, int xfila, int xcolumna, int forma){
        //forma = 1 ; izquierda, 2 derecha, 3 arriba, 4 abajo
        // 5 Arriba izquierda
        // 6  Arriba derecha
        // 7 Abajo izquierda
        // 8 Abajo derecha
        //char[] charPalabra = palabra.toCharArray();
        int tamano = palabra.length();
        int j;
        PalabrasIntro pa = new PalabrasIntro(palabra);
        switch(forma){
            case 1:
                for(j = 0; j < tamano; j++){
                    jletra[xfila][xcolumna]=palabra.substring(j,j+1).charAt(0);
                    pa.addPosicion(xfila,xcolumna);
                    lockedPos[xfila][xcolumna]++;
                    xcolumna--;
                }
                break;
            case 2:
                for(j = 0; j < tamano; j++){
                    jletra[xfila][xcolumna]=palabra.substring(j,j+1).charAt(0);
                    pa.addPosicion(xfila,xcolumna);
                    lockedPos[xfila][xcolumna]++;
                    xcolumna++;
                }
                break;
            case 3:
                for(j = 0; j < tamano; j++){
                        jletra[xfila][xcolumna]=palabra.substring(j,j+1).charAt(0);
                        pa.addPosicion(xfila,xcolumna);
                        lockedPos[xfila][xcolumna]++;
                        xfila--;
                    }
                break;
            case 4:
                for(j = 0; j < tamano; j++){
                        jletra[xfila][xcolumna]=palabra.substring(j,j+1).charAt(0);
                        pa.addPosicion(xfila,xcolumna);
                        lockedPos[xfila][xcolumna]++;
                        xfila++;
                    }
                break;
            case 5:
                for(j = 0; j < tamano; j++){
                    jletra[xfila][xcolumna]=palabra.substring(j,j+1).charAt(0);
                    pa.addPosicion(xfila,xcolumna);
                    lockedPos[xfila][xcolumna]++;
                    xfila--;
                    xcolumna--;
                }
                break;
            case 6:
                for(j = 0; j < tamano; j++){
                    jletra[xfila][xcolumna]=palabra.substring(j,j+1).charAt(0);
                    pa.addPosicion(xfila,xcolumna);
                    lockedPos[xfila][xcolumna]++;
                    xfila--;
                    xcolumna++;
                }
                break;
            case 7:
                for(j = 0; j < tamano; j++){
                    jletra[xfila][xcolumna]=palabra.substring(j,j+1).charAt(0);
                    pa.addPosicion(xfila,xcolumna);
                    lockedPos[xfila][xcolumna]++;
                    xfila++;
                    xcolumna--;
                }
                break;
            case 8:
                for(j = 0; j < tamano; j++){
                    jletra[xfila][xcolumna]=palabra.substring(j,j+1).charAt(0);
                    pa.addPosicion(xfila,xcolumna);
                    lockedPos[xfila][xcolumna]++;
                    xfila++;
                    xcolumna++;
                }
                break;
        }
        palabras.add(pa);
    }
    
    public int Verify(String palabra, int xfila, int xcolumna, int forma){
        int i = 1;
        int j,err;
        err = 0;
        /* si i = 0; ningun error
         * si i = 1; palabra no entra
         * si i = 2; palabra se cruza con otra */
        int tamano = palabra.length();
        char[] charPalabra = palabra.toCharArray();
        switch(forma){
            case 1: //Izquierda
                if(tamano <= xcolumna+1)
                    i = 0;
                if(i == 0){ //Comprueba sino sobrepone otra palabra
                    for(j = 0; j < tamano && err == 0; j++){
                        if(lockedPos[xfila][xcolumna] > 0){
                            if(getChar(xfila, xcolumna) != charPalabra[j])
                                err++;
                        }
                        xcolumna--;
                    }
                }
                break;
            case 2: //Derecha
                if(tamano <= columnas - xcolumna)
                    i = 0;
                if(i == 0){
                    for(j = 0; j < tamano && err == 0; j++){
                        if(lockedPos[xfila][xcolumna] > 0){
                            if(getChar(xfila, xcolumna) != charPalabra[j])
                                err++;
                        }
                        xcolumna++;
                    }
                }
                break;
            case 3: //Arriba
                if(tamano <= xfila+1)
                    i = 0;
                if(i == 0){
                    for(j = 0; j < tamano && err == 0; j++){
                        if(lockedPos[xfila][xcolumna] > 0){
                            if(getChar(xfila, xcolumna) != charPalabra[j])
                                err++;
                        }
                        xfila--;
                    }
                }
                break;
            case 4: //Abajo
                if(tamano <= filas - xfila)
                    i = 0;
                if(i == 0){
                    for(j = 0; j < tamano && err == 0; j++){
                        if(lockedPos[xfila][xcolumna] > 0){
                            if(getChar(xfila, xcolumna) != charPalabra[j])
                                err++;
                        }
                        xfila++;
                    }
                }
                break;
            case 5: //Arriba Izquierda
                if(tamano <= xcolumna + 1  &&  tamano <= xfila +1)
                    i = 0;
                if(i == 0){
                    for(j = 0; j < tamano && err == 0; j++){
                        if(lockedPos[xfila][xcolumna] > 0){
                            if(getChar(xfila, xcolumna) != charPalabra[j])
                                err++;
                        }
                        xfila--;
                        xcolumna--;
                    }
                }
                break;
            case 6: //Arriba Derecha
                if(tamano <= xfila+1 && tamano <= columnas - xcolumna)
                    i = 0;
                if(i == 0){
                    for(j = 0; j < tamano && err == 0; j++){
                        if(lockedPos[xfila][xcolumna] > 0){
                            if(getChar(xfila, xcolumna) != charPalabra[j])
                                err++;
                        }
                        xfila--;
                        xcolumna++;
                    }
                }
                break;
            case 7: //Abajo izquierda
                if(tamano <= filas - xfila && tamano <= xcolumna+1)
                    i = 0;
                if(i == 0){
                    for(j = 0; j < tamano && err == 0; j++){
                        if(lockedPos[xfila][xcolumna] > 0){
                            if(getChar(xfila, xcolumna) != charPalabra[j])
                                err++;
                        }
                        xfila++;
                        xcolumna--;
                    }
                }
                break;
            case 8: //Abajo derecha
                if(tamano <= filas - xfila && tamano <= columnas - xcolumna)
                    i = 0;
                if(i == 0){
                    for(j = 0; j < tamano && err == 0; j++){
                        if(lockedPos[xfila][xcolumna] > 0){
                            if(getChar(xfila, xcolumna) != charPalabra[j])
                                err++;
                        }
                        xfila++;
                        xcolumna++;
                    }
                }
                break;
        }
        if(err != 0)
            i = 2;
        return i;
    }
}