
package com.organizacion.jsopaletras_v02;

/**
 * Contendrá la palabra agrega en la Sopa de letras
 * @author Edmond Duke
 */
public class PalabrasIntro {
    private String palabra;
    private int[][] pos;
    private int xx,yy;

    public PalabrasIntro(){
        palabra = "";
        pos = new int[1][2];
        xx = 0;
        yy = 0;
    }

    public PalabrasIntro(String str){
        palabra = str;
        int len = palabra.length();
        if(len == 1)
            pos = new int[1][2];
        else
            pos = new int[len][len];
        xx = 0;
        yy = 0;
    }

    public void setPalabra(String str){
        palabra = str;
        pos = new int[palabra.length()][palabra.length()];
        xx = 0;
        yy = 0;
    }

    public void addPosicion(int x, int y){
        if(xx>palabra.length())
            xx = 0;
        pos[xx][yy] = x;
        yy++;
        pos[xx][yy] = y;
        xx++;
        yy = 0;
    }
    public int getPosX(int posx){
        return pos[posx][0];
    }

    public int getPosY(int posx){
        return pos[posx][1];
    }
    public int getSizePalabra(){
        return palabra.length();
    }
    @Override
    public String toString(){
        return palabra;
    }
}
