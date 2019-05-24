package com.organizacion.aplicacion;

import java.io.IOException;
import java.math.BigInteger;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.naming.CommunicationException;
import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.organizacion.ldap.Autenticacion.BadConfiguration;
import com.organizacion.ldap.Autenticacion.BadCredentials;
import com.organizacion.ldap.Autenticacion.UserNotExists;

import model.Usuario;


public class Autenticacion extends HttpServlet {

	private static final Logger logger = LogManager.getLogger(com.organizacion.aplicacion.Autenticacion.class);
	
	// Nos lo impone la serialización de HttpServlet
	private static final long serialVersionUID = -1319553576303272323L;

	// Nombre del atributo de sesión que usar para notificar de errores
	public static final String ERROR_LABEL="ErrorMsg";
	
	public static String correo="";
	
    /**
     * Función que nos permite saber si una cadena es nula o no
     * @param cadena String que se quiere comprobar
     * @return TRUE cuando la cadena sea null o vacía. FALSE en otro caso
     */
    public static boolean isNull(String cadena) {
    	if ( null != cadena ) {
    		if ( (! "".equalsIgnoreCase( cadena.trim() )) && (! "null".equalsIgnoreCase( cadena.trim() )) ) {
    			return false;
    		}
    	}
    	return true;
    }    
    
	/**
	 * Implementación del método POST del servlet
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Página a la que volver como respuesta
		String returnPage="/index.jsp";
		
		// Para almacenar el error que debe mostrársele al usuario en el JSP
		String errorMsg=null;
				
		// Crear una sesión (ya tendremos JSESSIONID)
		HttpSession session = request.getSession(true);
		try {
			session.removeAttribute( ERROR_LABEL );
		}catch(Exception e){
		}
		
		// Leer los parámetros que deben habernos pasado
		String usr = request.getParameter( "login" );
		logger.debug("Se lee del POST login='"+ usr +"'");
		String pwd = request.getParameter( "passwd" );
		logger.debug("Se lee del POST passwd='"+ pwd +"'");
		
		if ( (!isNull(usr)) && (!isNull(pwd)) ) {
			// instanciar mi objeto de librería
			com.organizacion.ldap.Autenticacion ldapAuth=new com.organizacion.ldap.Autenticacion();			
			com.organizacion.ldap.Autenticacion ldapUser=new com.organizacion.ldap.Autenticacion();
			
			try {				
				ldapUser=ldapAuth.connect(usr, pwd);
				if ( null != ldapUser ) {
					// Ok, está autenticado ... leer la dirección de correo
					try {
						String email=ldapUser.getEmailUser();
						correo=email;
						
						if (! isNull(email)) {
							// Ok, está todo bien guardamos el campo en la sesión
							logger.info("Se encontró email='"+ email +"' para el usuario. Se apila en la sesión");
							session.setAttribute( "email", email );
							
							//JPA insetar usuario si no existe
							if(!exists(email))
								insertar(ldapUser);
							
						} else {
							logger.info("No se encontró email para el usuario. Se apila en la sesión mensaje de error");
							session.setAttribute( "error", "Usted no tiene configurado email en LDAP" );
						}
						// Redirigir al jsp de respuesta						
						returnPage="/autenticado.jsp";
						
					} catch (NamingException e) {
						logger.error( "ERROR al leer la dirección de correo.", e);
						
						errorMsg="No se pudo leer su dirección de correo de LDAP. Avise al administrador";
					} finally {
						ldapUser.close();
					}
				}
				
				
			} catch (AuthenticationException a) {
				errorMsg="Nombre de usuario o contraseña inconrrecta";
				
			} catch (CommunicationException x) {
				logger.error( "ERROR en la comunicación con el servidor.", x);
				
				errorMsg="Uff, parece que tenemos problemas para conectar a LDAP. Avise al administrador";
			} catch (NamingException e) {
				logger.error( "ERROR en la autenticación del usuario.", e);
				
				errorMsg="Existe algún problema para poder conectar a LDAP. Avise al administrador";
				
			} catch (BadConfiguration e1) {
				logger.info( "ERROR de configuración", e1);
				
				errorMsg="Existe un problema de configuración. Avise al administrador";
			} catch (UserNotExists e2) {
				logger.info( "No se encontró el usuario", e2 );
								
				errorMsg="El usuario no existe en LDAP";
			} catch (BadCredentials e3) {
				logger.info( "Login o contraseña erróneas", e3);
				
				errorMsg="Login o contraseña erróneas";
			} finally {
				ldapAuth.close();
			}
		} else {
			errorMsg="Debes escribir tu login y contraseña";
		}
		
		if (! isNull( errorMsg)  ) {
			logger.info( "Se apila en la sesión ["+ ERROR_LABEL +"]='"+ errorMsg +"'");
			session.setAttribute( ERROR_LABEL , errorMsg );
		} 
		logger.info( "Se envía la respuesta a ["+ returnPage +"]");
    	request.getRequestDispatcher( response.encodeURL( returnPage ) ).forward( request, response );			
	}
	
	public boolean exists(final String correo) { 
	    EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "EnriqueVergara" );
        EntityManager entitymanager = emfactory.createEntityManager();
	    final TypedQuery<Usuario> tq = entitymanager.createQuery("SELECT * from usuarios where `correo` = " + correo, Usuario.class);
	    return tq.getSingleResult() != null;
	}
	
	public void insertar(com.organizacion.ldap.Autenticacion ldapUser) throws NamingException {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "Eclipselink_JPA" );
		EntityManager entitymanager = emfactory.createEntityManager( );
		entitymanager.getTransaction().begin();
		
		Usuario usuario = new Usuario(); 
		usuario.setCorreo(ldapUser.getEmailUser());
		usuario.setNombre(ldapUser.getNameUser());
		  
		entitymanager.persist(usuario);
		entitymanager.getTransaction( ).commit( );
		
		entitymanager.close( );
		emfactory.close( );
	}
	
	public static BigInteger getId_user() {
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "EnriqueVergara" );
        EntityManager entitymanager = emfactory.createEntityManager();
	    final TypedQuery<Usuario> tq = entitymanager.createQuery("SELECT * from usuarios where `correo` = " + correo, Usuario.class);
	    
	    if(tq.getSingleResult() != null)
	    	return tq.getSingleResult().getId();
	    else
	    	return BigInteger.valueOf(0);
	}
	
	
}
