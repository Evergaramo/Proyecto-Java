package com.organizacion.aplicacion;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AppLoader  implements ServletContextListener {


	/**
	 * Método que invocará el Tomcat cuando detenga nuestra aplicación. 
	 */
    public void contextDestroyed(ServletContextEvent arg) {  
    	System.err.println("Se detiene la aplicación: "+ arg.getServletContext().getServletContextName() );
    }
    
 
	
	/**
	 * Método que invocará el Tomcat cuando arranque nuestra aplicación. Lo aprovechamos
	 * para leer la configuración que se estableció en Context.xml 
	 */
    public void contextInitialized(ServletContextEvent arg) {
		// Obtener la configuración de la aplicación
	    ServletContext servletContext = arg.getServletContext();
		
		// Obtener el fichero desde el que cargar la configuración de la aplicación
		/*String log4jConfFile=servletContext.getInitParameter("config_log4j");
		if (! Autenticacion.isNull(log4jConfFile) ) {
			// Ok, hay parámetro... ¿pero existe ese fichero?
			File fileConf=new File(log4jConfFile);
			if ( fileConf.exists() ) {
				// Averiguar la extensión del fichero:
				LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);

				context.setConfigLocation(fileConf.toURI());
				
			} else {
				System.err.println("ERROR: No se puede leer el fichero para log4j que se configuró ["+ log4jConfFile +"]");				
			}
		} else {
			System.err.println("ERROR: No se configuró correctamente el parámetro config_log4j");
		}	*/
		
		Logger logger = LogManager.getLogger(com.organizacion.aplicacion.AppLoader.class);
		
		String cfgLdap="/WEB-INF/configuracion-ldap.properties";	
		
	//	if (! Autenticacion.isNull( cfgLdap )) {
			try {
				if ( com.organizacion.ldap.Autenticacion.loadConfigurationFromFile(servletContext.getResourceAsStream(cfgLdap) )) {
					logger.info( "Se cargó correctamente la configuración para LDAP desde el fichero:"+ servletContext.getInitParameter("config_ldap") );
				} else {
					logger.info( "No se pudo leer la configuración para LDAP desde el fichero:"+ servletContext.getInitParameter("config_ldap") );				
				}
			} catch(Exception x) {
				throw new RuntimeException("No se puede iniciar la aplicación");
			}
		//} else {
		//	logger.info( "No se definió 'config_ldap' en el context.xml de la aplicación");
		//	throw new RuntimeException("No se puede iniciar la aplicación");
		//   }
		System.err.println("Se inicia la aplicación: "+ arg.getServletContext().getServletContextName() );
		
    }
}
