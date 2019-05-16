<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page language="java" import="com.organizacion.jsopaletras_v02.Juego" %>
<%@ page language="java" import="com.organizacion.jsopaletras_v02.CreaSopaLetras" %>
<%
String msg="", msg2="";
CreaSopaLetras sopa = Juego.generarSopa();
/*char[][] tabla = sopa.getTabla();
String[] palabras = sopa.getPalabras();
for (int i = 0; i < tabla.length; i++) {
	System.out.println();
	for (int j = 0; j < tabla.length; j++) {
		System.out.print(tabla[i][j] + " ");
		msg+="<td>tabla[i][j]</td>";
	}
}
System.out.println("\n\nPalabras:");
for (int i = 0; i < palabras.length; i++) {
	System.out.println("	" + (i+1) + ".-" + palabras[i]);
}*/
//msg="<span style=\"color:red;\">"+ err +"</span>";

char [][] tabla = {{'S','D','X','A','O','L','A','D','O','R','M','G'}, 
				   {'O','F','X','K','T','E','M','R','Q','Q','E','Q'}, 
				   {'V','D','T','D','D','C','I','Z','E','H','K','C'}, 
				   {'I','E','M','I','Q','N','L','Z','K','B','B','D'}, 
				   {'P','S','A','N','O','A','O','Z','K','F','O','N'}, 
				   {'A','E','N','E','J','C','H','P','A','Z','A','B'}, 
				   {'R','N','F','R','A','C','M','D','P','R','T','D'}, 
				   {'O','C','A','I','G','F','N','C','A','K','R','M'}, 
				   {'M','O','N','L','E','J','X','N','I','K','O','A'}, 
				   {'H','N','O','L','R','J','J','I','G','L','L','M'}, 
				   {'L','A','S','O','F','O','L','L','D','W','H','J'}, 
				   {'O','R','E','N','A','R','F','E','R','K','N','L'}};
String[] palabras = {"FREGAJO", "DINERILLO", "NARANJO", "CANCEL", "MANFANOS", "OVIPARO", "DESENCONAR", "TROL", "BOBERA", "REFRANERO"};
for (int i = 0; i < 12; i++) {
	msg+="<tr>";
	for (int j = 0; j < 12; j++) {
		msg+="<td style='text-align:center' id="+(i+1)+"_"+(j+1)+">"+tabla[i][j]+"</td>";
	}
	msg+="</tr>";
}
for (int i = 0; i < 10; i++) {
	msg2+="<tr><td>"+i+"</td><td id="+palabras[i]+">"+palabras[i]+"</td></tr>";
}
%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width initial-scale=1" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />    
    <title>Juego</title>
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
    <div align="center">
    	<script type="text/javascript" src="jquery-3.4.1.js"></script>
    	<script>
    	
    		//$("table tr td").click(function() {
    		  //var total = $(this).find("td:last-child").text();
    		  //alert(total);
    		//});
    		
    		var color="yellow";
    		var array_palabras=[];
    	
    		window.onload = function(){
        		  var table = document.getElementById('juego');
        		  table.addEventListener('click',event,false);
        		  var crono = document.getElementById('crono');
        		  empezarDetener(crono);
        		}
    		
    		var ultimaRow;
    		var ultimaCell;
    		var ultimoelement;
    		var ultimoColor;

    		function event(e){
				var element = e.srcElement || e.target;
				if(element.nodeName == 'TABLE') return;
				
				if(ultimoelement != null){
					ultimoelement.style.backgroundColor = ultimoColor;
				}
				ultimoelement=element;
				ultimoColor=element.style.backgroundColor;
				
				element.style.backgroundColor = "red";
				
				//alert(e.target.innerText);
				
				var selected_cell=element.cellIndex;
				
				while(element.nodeName != 'TR') element = element.parentNode;
				
				var selected_row=element.rowIndex;
    		  
    		    var palabra = "";
    		    var posInicio, posFinal;
    		    
    		    
	    		if(ultimaCell==null || ultimaRow==null){
	    			ultimaCell=selected_cell;
	    			ultimaRow=selected_row;
	    		}
	    		else{
		    		if(ultimaCell==selected_cell){//vertical
		    			if(selected_row>ultimaRow){
		    				posInicio=ultimaRow;
		    				posFinal=selected_row;
		    			}
		    			else{
		    				posInicio=selected_row;
		    				posFinal=ultimaRow;
		    			}
		    			for(var i = posInicio; i <= posFinal; i++){
		                    trDelResultado=$("#juego").find("tr:eq("+i+")");
		    				var td = trDelResultado.find("td:eq("+selected_cell+")").html();
		    				palabra+=td;
		    			}
		    		}else if(ultimaRow==selected_row){//horizontal
		    			if(selected_cell>ultimaCell){
		    				posInicio=ultimaCell;
		    				posFinal=selected_cell;
		    			}
		    			else{
		    				posInicio=selected_cell;
		    				posFinal=ultimaCell;
		    			}
		    			for(var i = posInicio; i <= posFinal; i++){
		    				var elementosTD=e.srcElement.parentElement.getElementsByTagName("td")[i].innerHTML;
		    				palabra+=elementosTD;
		    			}
		    		}
		    		else if(Math.abs(ultimaCell-selected_cell) == Math.abs(ultimaRow-selected_row)){
		    			var desplazamientoCell = ultimaCell-selected_cell;
		    			if(desplazamientoCell > 0)
		    				desplazamientoCell=1;
		    			else
		    				desplazamientoCell=-1;
		    			var desplazamientoRow = ultimaRow-selected_row;
		    			if(desplazamientoRow > 0)
		    				desplazamientoRow=1;
		    			else
		    				desplazamientoRow=-1;
		    			var lon = Math.abs(ultimaCell-selected_cell);
		    			for(var i = 0; i <= lon; i++){
		    				trDelResultado=$("#juego").find("tr:eq("+(selected_row+i*desplazamientoRow)+")");
		    				var td = trDelResultado.find("td:eq("+(selected_cell+i*desplazamientoCell)+")").html();
		    				palabra+=td;
		    			}
		    		}
		    		comprobar(palabra);
		    		ultimaCell=null;
	    			ultimaRow=null;
	    		}
	    		
	    		function comprobar(p){
	    			if(jsBuscar(p)){
	    				document.getElementById(p).style.backgroundColor = color;
	    				pintar();
	    				var bool = array_palabras.includes(p);
	    				if(!bool){
	    					array_palabras.push(p);
	    					sumarPuntos();
	    				}
	    			}else if(jsBuscar(reverseString(p))){
	    				document.getElementById(reverseString(p)).style.backgroundColor = color;
	    				pintar();
	    				var bool = array_palabras.includes(reverseString(p));
	    				if(!bool){
	    					array_palabras.push(reverseString(p));
	    					sumarPuntos();
	    				}
	    			}
	    			if(array_palabras.length>=10){
						alert("Has ganado la partida\Tiempo: " + timeout + "\nPuntos: " + puntos);
						empezarDetener(crono);
						location.reload();
					}
	    		}
	    		
	    		//función que realiza la busqueda
	    		function jsBuscar(p){
	    		 
	    		        //obtenemos el valor insertado a buscar
	    		        buscar=p;
	    		 
	    		        //utilizamos esta variable solo de ayuda y mostrar que se encontro
	    		        encontradoResultado=false;
	    		 
	    		        //realizamos el recorrido solo por las celdas que contienen el código, que es la primera
	    		        $("#palabras tr").find('td:eq(1)').each(function () {	    		        	  
	    		 
							//obtenemos el codigo de la celda
							codigo = $(this).html(); 
							
							//comparamos para ver si el código es igual a la busqueda
							if(codigo==buscar){
								encontradoResultado=true;
							}
	    		 
	    		        })
	    		 
	    		        return encontradoResultado;
	    		}
	    		
	    		function reverseString(str) {
					return str.split("").reverse().join("");
    			}

	    		
	    		//pintar la tabla
	    		function pintar(){
	    			if(ultimaCell==selected_cell){//vertical
		    			if(selected_row>ultimaRow){
		    				posInicio=ultimaRow;
		    				posFinal=selected_row;
		    			}
		    			else{
		    				posInicio=selected_row;
		    				posFinal=ultimaRow;
		    			}
		    			for(var i = posInicio; i <= posFinal; i++){
		    				document.getElementById((i+1)+"_"+(selected_cell+1)).style.backgroundColor = color;
		    			}
		    		}else if(ultimaRow==selected_row){//horizontal
		    			if(selected_cell>ultimaCell){
		    				posInicio=ultimaCell;
		    				posFinal=selected_cell;
		    			}
		    			else{
		    				posInicio=selected_cell;
		    				posFinal=ultimaCell;
		    			}
		    			for(var i = posInicio; i <= posFinal; i++){		   
		    				document.getElementById((selected_row+1)+"_"+(i+1)).style.backgroundColor = color;
		    			}
		    		}else if(Math.abs(ultimaCell-selected_cell) == Math.abs(ultimaRow-selected_row)){
		    			var desplazamientoCell = ultimaCell-selected_cell;
		    			if(desplazamientoCell > 0)
		    				desplazamientoCell=1;
		    			else
		    				desplazamientoCell=-1;
		    			var desplazamientoRow = ultimaRow-selected_row;
		    			if(desplazamientoRow > 0)
		    				desplazamientoRow=1;
		    			else
		    				desplazamientoRow=-1;
		    			var lon = Math.abs(ultimaCell-selected_cell);
		    			for(var i = 0; i <= lon; i++){
		    				document.getElementById(((selected_row+1)+i*desplazamientoRow)+"_"+((selected_cell+1)+i*desplazamientoCell)).style.backgroundColor = color;
		    			}
		    		}
	    		}

    		}
    		
    		//puntuacion
    		var puntos=0;
    		function sumarPuntos(){
    			if(timeout<60)
    				puntos+=10;
    			else if(timeout<120)
    				puntos+=5;
    			else
    				puntos+=2;
    			document.getElementById('puntuacion').innerHTML = puntos;
    		}
    		
    		
    		//crono
    		
    		var inicio=0;
			var timeout=0;
		 
			function empezarDetener(elemento)
			{
				if(timeout==0)
				{
					// empezar el cronometro
		 
					elemento.value="Detener";
		 
					// Obtenemos el valor actual
					inicio=vuelta=new Date().getTime();
		 
					// iniciamos el proceso
					funcionando();
				}else{
					// detemer el cronometro
		 
					elemento.value="Empezar";
					clearTimeout(timeout);
					timeout=0;
				}
			}
		 
			function funcionando()
			{
				// obteneos la fecha actual
				var actual = new Date().getTime();
		 
				// obtenemos la diferencia entre la fecha actual y la de inicio
				var diff=new Date(actual-inicio);
		 
				// mostramos la diferencia entre la fecha actual y la inicial
				var result=LeadingZero(diff.getUTCHours())+":"+LeadingZero(diff.getUTCMinutes())+":"+LeadingZero(diff.getUTCSeconds());
				document.getElementById('crono').innerHTML = result;
		 
				// Indicamos que se ejecute esta función nuevamente dentro de 1 segundo
				timeout=setTimeout("funcionando()",1000);
			}
		 
			/* Funcion que pone un 0 delante de un valor si es necesario */
			function LeadingZero(Time) {
				return (Time < 10) ? "0" + Time : + Time;
			}
    		
    	</script>
    	<table style="float: left; width: 30%;" id="juego" border="1"><%= msg %></table>
    	<table style="float: right; width: 15%;" id="palabras" border="1"><%= msg2 %></table>
    	
		<style>
			.crono_wrapper {text-align:center;width:200px;}
		</style>
		<div class="crono_wrapper">
			<h2 id='crono'>00:00:00</h2>
		</div>
		<button onclick="location.href='juego'">Nueva partida</button>
		<button onclick="location.href='../EnriqueVergara'">Volver</button>
		<a><br><br><1min = 10ptos<br>>1min & <2min = 5ptos<br>>2min = 2ptos<br><br>Puntos: </a>
		<a id="puntuacion">0</a>
    </div>
  </body>
</html>
    