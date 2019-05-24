
package com.organizacion.jsopaletras_v02;


/**
 * @author Edmond Duke
 * email: edu1738@gmail.com
 */
public class InsertaPalabra{
    
    private Sopaletras2 sopa;
    private int posX,posY,forma;
    //private Color color;

    public InsertaPalabra(Sopaletras2 xsopa, int i, int j){
        super();
        sopa = xsopa;
        posX = i;
        posY = j;
        forma = 1;
    }

    private static boolean verifyString(String cadena, String caracteres){
        char c[],x;
        int i,j,k;
        int error = 0;
        String s = caracteres;
        c = s.toCharArray();
        for( i=0 ;  i < cadena.length() && error == 0;i++){
            x = cadena.charAt(i);
            k = 0;
            for(j = 0 ;  j < s.length() && k == 0;j++){
                if(x==c[j])
                    k++;
            }
            if( k == 0)
                error++;
        }
        if(error == 0)
            return true;
        else
            return false;
    }
    
    public boolean AgregarPalabra(String s){
    	boolean b = false;
        if(s.length()> 0){
            int num = sopa.Verify(s, posX, posY, forma);
            if(num == 0) {
                    if(verifyString(s,sopa.getCaracteresPermitidos())) {
                        sopa.setPalabra(s, posX, posY, forma);
                        b = true;
                    }
                    else
                    	System.out.printf("Palabra no agregada. La palabra contiene letras que no forman parte de la sopa de letras. También se distingue entre mayúsculas y minúsculas.", "jSopaLetras" , 1);
            }
            else if (num == 1)
                System.out.printf("Palabra muy extensa para ser colocada", "jSopaLetras" , 1);
            else if(num == 2)
            	System.out.printf("Palabra se cruza con otra ya agregada anteriormente", "jSopaLetras" , 1);
        }else
        	System.out.printf("Palabra no válida", "jSopaLetras" , 1);
        return b;
    }
    
    public boolean agregar(String s) {       
    	int n = (int)(Math.random() * 8);
    	forma = n;    
        return AgregarPalabra(s);      
    }
   
}
