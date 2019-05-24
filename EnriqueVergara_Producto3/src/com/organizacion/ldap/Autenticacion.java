package com.organizacion.ldap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

public class Autenticacion {
	
    private static final Logger logger = LogManager.getLogger(com.organizacion.ldap.Autenticacion.class);
	
	// URL contra la que conectar
	private static String ldap_URL="ldap://localhost:10389";
	
	// Rama base donde buscar los usuarios para autenticar
	private static String ldap_DN_Base="uid=admin,ou=system";
	//private static String ldap_DN_Base="uid=admin,ou=system";
	
	// Cuenta de acceso para conectar al directorio y poder 
	// buscar el DN del usuario
	private static String ldap_user="admin";
	
	// Contraseña del usuario con acceso para la búsqueda
	private static String ldap_passwd="secret";
	
	// Filtro que aplicar en las búsquedas
	private static String ldap_filter=null;
	
    // Objeto DirContext para almacenar la conexión al directorio 
    private DirContext dirCtx=null;
	
    // Almacena el DN del usuario autenticado
    private String userDN_logged=null;
    
    /**
     * Obtiene la URL para la conexión al directorio LDAP
     * @return Cadena con la URL de conexión, ejemplo: ldap://ldap1.organizacion.net
     */
	public static String getConfigLdap_URL() {
		return ldap_URL;
	}
	
	/**
	 * Permite configurar la URL del servidor LDAP al que conectar  
	 * @param url LDAP-URL a la que conectar, ejemplo: ldap://ldap1.organizacion.net
	 */
	public static void setConfigLdap_URL(String url) {
		if (! isNull(url) ) {
			logger.info("Se configura como URL_LDAP=["+ url +"]");
			ldap_URL = url;
		} else {
			logger.warn("Se evitó configurar como URL_LDAP un valor nulo");
		}
	}

	/**
	 * Obtiene el DN (distinguised name) de la rama del directorio LDAP
	 *   a partir de la cual buscar los usuarios
	 * @return Distinguised Name de la rama donde buscar los usuarios 
	 */
	public static String getConfigLdap_BaseDN() {
		return ldap_DN_Base;
	}

	/**
	 * Configura el DN de la rama desde la que buscar los usuarios
	 * @param base DN de la rama, ejemplo: "ou=usuarios,o=directorio,c=es"
	 */
	public static void setConfigLdap_BaseDN(String base) {
		if (! isNull(base) ) {
			logger.info("Se configura como BASEDN_LDAP=["+ base +"]");
			ldap_DN_Base = base;
		} else {
			logger.warn("Se evitó configurar como BASEDN_LDAP un valor nulo");
		}		
	}

	/**
	 * Obtiene el DN del usuario que usar para las búsquedas 
	 * @return El Distinguised Name de la cuenta a usar para 
	 *   conectar al directorio, ejemplo: "uid=lector,o=organizacion,c=es"
	 */
	public static String getConfigLdap_User() {
		return ldap_user;
	}

	/**
	 * Configura el Distinguised Name del usuario que usar 
	 * para las búsquedas
	 * @param dn DN de la cuenta, ejemplo: "uid=lector,o=organizacion,c=es"
	 */
	public static void setConfigLdap_User(String dn) {
		if (! isNull(dn) ) {
			logger.info("Se configura como USUARIO_LDAP=["+ dn +"]");
			ldap_user = dn;
		}	
	}
	
	/**
	 * Devuelve la contraseña que se para poder conectar al directorio
	 *   y realizar búsquedas de usuarios
	 * @return Contraseña que se configuró
	 */
	public static String getConfigLdap_Passwd() {
		return ldap_passwd;
	}
    
	/**
	 * Establece la contraseña que debe usarse en la conexión y búsqueda
	 *   de usuarios.
	 * @param password Contraseña que usar
	 */
	public static void setConfigLdap_Passwd(String password) {
		if (! isNull(password) ) {
			ldap_passwd = password;
		}	
	}
	
	/**
	 * Obtiene el filtro de búsqueda LDAP para localizar cuentas de 
	 *   usuario
	 * @return Cadena con el filtro configurado, 
	 *   ejemplo: "(!(uid=%login%)(cn=%login%))"
	 */
	public static String getConfigLdap_Filter() {
		return ldap_filter;
	}
	
	/**
	 * Establece el filtro de búsqueda LDAP para localizar los usuarios 
	 * @param filter Cadena con el filtro que configurar,
	 *   ejemplo: "(!(uid=%login%)(cn=%login%))" 
	 *   
	 */
	public static void setConfigLdap_Filter(String filter) {
		if (! isNull(filter) ) {
			logger.info("Se configura como FILTRO_LDAP=["+ filter +"]");
			ldap_filter = filter;
		}	
	}
	
    /**
     * Función que nos permite saber si una cadena es nula o no
     * @param cadena String que se quiere comprobar
     * @return TRUE cuando la cadena sea null o vacía. FALSE en otro caso
     */
    private static boolean isNull(String cadena) {
    	if ( null != cadena ) {
    		if ( (! "".equalsIgnoreCase( cadena.trim() )) && (! "null".equalsIgnoreCase( cadena.trim() )) ) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Permite saber si el objeto está conectado a LDAP
     * @return TRUE cuando esté conectado, false en otro caso
     */
    public boolean isConnected() {
    	return ( (null!=this.dirCtx) && (! isNull(this.userDN_logged) ));
    }
    
    /**
     * Método privado para conectar al directorio LDAP
     * @param dnUser Distinguised Name de la cuenta a usar
     * @param passwd Contraseña que usar en la conexión
     * @return TRUE cuando consiga conectar con éxito, FALSE en otro caso
     * @throws NamingException Cuando el servidor LDAP esté caído o no se pueda conectar
     */
    private boolean connectLdap(String dnUser, String passwd) throws NamingException {
    	// Factoría con la configuración necesaria para conectar  
    	Hashtable<String, Object> env = new Hashtable<String, Object>();	   	

    	env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    	env.put(Context.SECURITY_AUTHENTICATION, "simple");
    	env.put(Context.REFERRAL, "follow");
    	env.put("com.sun.jndi.ldap.connect.timeout", "5000");
    	env.put("com.sun.jndi.ldap.read.timeout", "10000");
    	
    	// Configurar la URL del servidor de conexión
    	env.put(Context.PROVIDER_URL, getConfigLdap_URL() );
    	
    	// Soporte para LDAPS
    	if ( getConfigLdap_URL().toLowerCase().startsWith("ldaps") ) {
    		env.put("java.naming.ldap.factory.socket", "com.organizacion.ldap.SSLConnection");
    	}
    	
    	// Construir el Distinguised Name del usuario
    	env.put(Context.SECURITY_PRINCIPAL, dnUser );
    	
    	// Configurar la clave a usar en la conexión
    	if (! isNull(passwd) ) {
    		env.put(Context.SECURITY_CREDENTIALS, passwd );
    	}
    	
    	// Realizar la conexión... (simple bind al directorio) 
    	logger.debug("Se intenta la conexión usuario=["+ dnUser +"] y contraseña ["+ passwd +"]");
    	this.dirCtx = new InitialLdapContext(env,null);

    	if (null!= this.dirCtx) {
    		logger.info("Usuario=["+ dnUser +"] autenticado con éxito ["+ passwd +"]");
    		this.userDN_logged = dnUser ;
    		return true;
    	}
    	logger.info("No se pudo autenticar al usuario=["+ dnUser +"] ");
    	return false;    	
    }
    
    /**
     * Realiza una conexión al directorio LDAP con la cuenta configurada
     * @return TRUE cuando consiga conectar con éxito, FALSE en otro caso
     * @throws NamingException Cuando el servidor LDAP esté caído o no se pueda conectar
     */
    public boolean connect() throws NamingException {
    	
    	if ( (isNull(getConfigLdap_URL()))  || (isNull(getConfigLdap_User()))) {
    		logger.error("ERROR: No se configuró correctamente el servidor LDAP... imposible seguir");
    		return false;
    	}
    	try {
    		return this.connectLdap( getConfigLdap_User(), getConfigLdap_Passwd() );
    	} catch(AuthenticationException x) {
    		logger.error("Fallo la autenticación para el usuario '"+ getConfigLdap_User() +"' preconfigurado", x);
    	}
    	return false;
    }
    
    /**
     * Permite validar la conexión de un usuario al directorio LDAP. Para ello
     *   primero se intenta obtener el DN del usuario, y luego se prueba a 
     *   conectar
     * @param user Login del usuario, ejemplo: "pedro", "paco.martinez", "123423"
     *   algo que usar en el filtro de búsqueda 
     * @param passwd Contraseña del usuario
     * @return TRUE cuando consiga conectar con éxito, FALSE en otro caso
     * @throws NamingException Cuando el servidor LDAP esté caído o no se pueda conectar
     */
    public Autenticacion connect(String user,String passwd) throws NamingException, BadConfiguration, UserNotExists, BadCredentials {
    	    	
    	// Comprobar que el servidor esté configurado
    	if ( (isNull(getConfigLdap_BaseDN())) || (isNull(getConfigLdap_URL())) || (isNull(getConfigLdap_Filter())) || (isNull(getConfigLdap_User()))) {
    		logger.error("Falta configuración para la conexión LDAP");
    		throw new BadConfiguration("No se configuró correctamente el servidor LDAP");
    	}
    	
    	// Para devolver objeto
    	Autenticacion retVal=new Autenticacion();
    	
    	if ( this.connect() ) {
    		// Ok estamos conectados
    		
    		// Cambiar el filtro, para sustituir en el que nos pasaron %LOGIN% por el argumento user
    		String filtro=getConfigLdap_Filter().replaceAll("%LOGIN%", user);
    		
    		logger.info("Se intenta la búsqueda en LDAP para la rama ["+  getConfigLdap_BaseDN() +"] y filtro ["+ filtro + "]");
    		Vector<String> dns=this.searchIntoLDAP( getConfigLdap_BaseDN(), filtro, "dn");
    		if ( (null!= dns) && ( 0 < dns.size()) ) {
    			logger.debug("Se encontrarón "+ dns.size() +" DNs que coinciden con el filtro");
	    		for(int i=0; (i<dns.size()) ; i++ ) {
	    			logger.debug("Se intenta conectar intenta la conexión con el DN ["+ dns.get(i) +"]");
	    			if (retVal.connectLdap( dns.get(i), passwd)) {
	    				logger.info("El usuario ["+ user +"] con DN=["+ dns.get(i) +"] está autenticado autenticación");
	    				return retVal;
	    			}
	    		}	
	    		logger.debug("No funcionó ninguno de los DNS encontrados, generamos excepción: Igual la contraseña está mal");
	    		throw new BadCredentials();
    		} else {
    			logger.debug("No encontró ningún DN para el filtro, generamos excepción: igual el usuario no existe");
    			throw new UserNotExists("No se encontró ningún usuario que cumpliera el filtro ["+ filtro +"] en ["+ getConfigLdap_BaseDN() +"]");
    		}
    	}
    	logger.info("No se pudo conectar con las credenciales de sólo lectura, generamos excepción");
    	throw new BadConfiguration("No se pudo conectar a LDAP con las credenciales configuradas");

    }
    
    /**
     * Cierra la conexión con el directorio LDAP si estuviera abierta
     */
    public void close() {
    	if ( null!= this.dirCtx ) {
    		try {
    			this.dirCtx.close();
    		}catch(Exception x) {
    			logger.debug("Esto no debería pasar, habrá que depurarlo", x);
    		}
    	}
    }
    
    /**
     * Lee un atributo de LDAP de la cuenta del usuario que se ha autenticado
     * @param attr Nombre del atributo a leer, ejemplo: "mail"
     * @return El valor del atributo, o nulo en caso de que no exista/tenga valor
     * @throws NamingException Cuando haya algún problema con el servidor
     */
    private String readAttributeFromLDAP(String attr) throws NamingException {
    	String retVal=null;
    	if ( this.isConnected() ) {
    		// Atributo a leer 
    		String tmpArray[] = { attr };
    		// Construir el DN desde el que leerlo (el DN del usuario)
    		logger.debug("Se lanza la búsqueda del atributo ["+ attr +"] para el DN=["+ this.userDN_logged +"]");
    		Attributes atts  = this.dirCtx.getAttributes( this.userDN_logged , tmpArray );
    		Attribute  valor = atts.get(attr);
    		
    		if (null != valor ) {
        		logger.info("Se lee de LDAP  ["+ attr +"]=["+ valor.get().toString() +"] para el DN=["+ this.userDN_logged +"]");
    			retVal= valor.get().toString();
    		} else {
    			logger.info("No pudo obtener ningún valor del atributo ["+ attr +"] para el DN=["+ this.userDN_logged +"]");
    		}
    	} else {
    		logger.debug("No se puede recuperar el atributo ["+ attr +"] para el DN=["+ this.userDN_logged +"] porque no estamos conectados a LDAP");
    	}
    	return retVal;    	
    }
    
    /**
     * Obtiene el valor del campo MAIL de LDAP para el usuario autenticado
     * @return El valor del atributo siempre que esté definido
     * @throws NamingException Cuando exista algún problema en el servidor.
     */
    public String getEmailUser() throws NamingException {
    	return this.readAttributeFromLDAP("mail");
    }
    
    public String getNameUser() throws NamingException {
    	return this.readAttributeFromLDAP("nombre");
    }
    
    /**
     * Permite realizar un búsqueda por filtro en LDAP bajo una rama, y 
     * leer para cada ocurrencia del filtro un atributo
     * @param rama Rama del directorio desde la que lanzar la búsqueda
     * @param filter Filtro que se debe aplicar
     * @param campo Nombre del atributo del directorio a leer
     * @return Un vector con todos los valores encontrados
     */
    @SuppressWarnings("rawtypes")
    public  Vector<String> searchIntoLDAP( String rama, String filter, String campo) {    	
	   	Vector<String> ret=new Vector<String>();
	   	
	   	if ( null != this.dirCtx )  {
			NamingEnumeration answer = null;
	    	try {
		        // Preparar el filtro de busqueda
	    		// Limitar los atributos a buscar 
	    		String[] attrIDs = {campo};
		  
	    		// Preparar el objeto de búsqueda
	    		SearchControls ctls = new SearchControls();
	    		ctls.setReturningAttributes(attrIDs);
	    		// Este SCOPE hace que no buceemos por todo el árbol: Solo los hijos de la rama seleccionada.
	    		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	
	    		// Lanzar la consulta al LDAP
	    		answer = this.dirCtx.search(rama, filter, ctls);
	    		// Leer elementos, mientras haya para leer.
	    		try {
		    		while (answer.hasMore()) {
		    			SearchResult sr = (SearchResult)answer.next();
		    			String cmp=sr.getNameInNamespace();			    			
		    			if (! isNull(cmp) ){
		    				ret.add( cmp );
		    			}
		    		}	 
	    		}catch (javax.naming.PartialResultException k) {
	    			// Esto suele dar mucho en consultas al directorio activo, creo que cuando no hay más resultados..
	    		}
	    	} catch (Exception e) {
	    		System.err.println("Se produjo un error en 'search' con rama=["+ rama +"], filtro=["+ filter +"], leer-atributo=["+ campo+ "]");
	    		e.printStackTrace( System.err );
		    	
	    	} finally {
	    		if ( null != answer ) {
	    			try {
	    				answer.close();
	    			} catch (Exception e) { /* Ignorar */ }
	    		}
	    	}  
		}
    	return ret;
    }
    
    /**
     * Permite leer toda la configuración desde el fichero .properties 
     *   que se le pasa como argumento
     * @param pathFile Ruta absoluta del fichero de configuración
     * @return TRUE cuando se pudo leer correctamente la configuración
     *   desde el fichero y no se encontraron errores. FALSE en otro caso 
     * @throws IOException 
     */
    public static boolean loadConfigurationFromFile(String pathFile) throws IOException {
    	boolean retVal=false;
    	
    	InputStream inputStream=null;
		try {
			Properties prop = new Properties();
 
			inputStream = new FileInputStream(pathFile); 
			prop.load(inputStream);
			
 
			setConfigLdap_URL( prop.getProperty("url")  );
			setConfigLdap_BaseDN( prop.getProperty("base"));
			setConfigLdap_Filter( prop.getProperty("filter") );
			setConfigLdap_User( prop.getProperty("user") );
			setConfigLdap_Passwd( prop.getProperty("password") );
							
			if ( (Autenticacion.isNull( getConfigLdap_URL()) ) ||
			     (Autenticacion.isNull( getConfigLdap_BaseDN()) ) ||
			     (Autenticacion.isNull( getConfigLdap_User()) ) ||
			     (Autenticacion.isNull( getConfigLdap_Filter()) ) ) {
				logger.error("ERROR y GRAVE: No has configurado correctamente el servidor LDAP ");
			} else {
				retVal=true;
			}
			
			if (! Autenticacion.isNull( prop.getProperty("ssl_truststore_file") ) ) {				
				SSLConnection.setTruststore( prop.getProperty("ssl_truststore_file") );
				
				// Añadir la clave del TrustStore si nos la indicaron
				if (! Autenticacion.isNull( prop.getProperty("ssl_truststore_passwd") ) ) {
					SSLConnection.setTruststorePassword( prop.getProperty("ssl_truststore_passwd") );
				}
			} else {
				// No nos fijaron truststore para ssl...
				if ( getConfigLdap_URL().toLowerCase().startsWith("ldaps") ) {
		    		// Y sin embargo la URL es LDAPS... 
					logger.error("ERROR y GRAVE: No has configurado el truststore para comunicaciones SSL");
					retVal=false;
		    	}
			}
			
			
		} catch (Exception e) {
			logger.error("Se produjo un error al leer la configuración", e);			
		} finally {
			inputStream.close();
		}
		return retVal;
    }
    
    /**
     * Excepción para informar que hay un problema de configuración
     */
    public class BadConfiguration extends Exception {
		private static final long serialVersionUID = -2412280086466427530L;  
		
		public BadConfiguration(String msg) {
			super(msg);
		}
    }
    
    /**
     * Excepción para informar de que el usuario no existe 
     */
    public class UserNotExists extends Exception {
		private static final long serialVersionUID = -2412280086466427531L;  
		
		public UserNotExists(String msg) {
			super(msg);
		}
    }
    
    /**
     * Excepción para informar de que nada de lo que ser probó funcionó 
     */
    public class BadCredentials extends Exception {
		private static final long serialVersionUID = -2412280086466427532L;  
    }
}
