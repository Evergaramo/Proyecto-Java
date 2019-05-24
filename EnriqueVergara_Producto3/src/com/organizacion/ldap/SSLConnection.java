package com.organizacion.ldap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.security.KeyStore;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SSLConnection extends SSLSocketFactory {
	
	private static final Logger logger = LogManager.getLogger(com.organizacion.ldap.SSLConnection.class);
	
	private static String trustore=null;
	private static String jks_passwd=null;
	
    private SSLSocketFactory socketFactory;

        
    public SSLConnection() {
        try {
        	 TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        	 KeyStore ks = KeyStore.getInstance("JKS");
        	 File trustFile = new File( getTruststore() );
        	 
        	 // Obtener la clave del Truststore
        	 char[] pwd = null;
        	 if (null != getTruststorePassword() ) {
        		 pwd= getTruststorePassword().toCharArray();
        	 }
        	 
        	 ks.load(new FileInputStream(trustFile), pwd );
        	 tmf.init(ks);
        	 
        	 SSLContext ctx = SSLContext.getInstance("TLS");
        	 ctx.init(null, tmf.getTrustManagers(),null);  
        	 socketFactory = ctx.getSocketFactory();
        	 
        } catch (Exception ex) {
        	logger.error("Al crear el socket", ex );
        }
    }
    
    public Socket createSocket() throws IOException {
        return socketFactory.createSocket();
    }

    public static SocketFactory getDefault() {
        return new SSLConnection();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return socketFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return socketFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String string, int i, boolean bln) throws IOException {
        return socketFactory.createSocket(socket, string, i, bln);
    }

    @Override
    public Socket createSocket(String string, int i) throws IOException, UnknownHostException {
        return socketFactory.createSocket(string, i);
    }

    @Override
    public Socket createSocket(String string, int i, InetAddress ia, int i1) throws IOException, UnknownHostException {
        return socketFactory.createSocket(string, i, ia, i1);
    }

    @Override
    public Socket createSocket(InetAddress ia, int i) throws IOException {
        return socketFactory.createSocket(ia, i);
    }

    @Override
    public Socket createSocket(InetAddress ia, int i, InetAddress ia1, int i1) throws IOException {
        return socketFactory.createSocket(ia, i, ia1, i1);
    }

    
    
	public static String getTruststore() {
		return trustore;
	}

	public static void setTruststore(String trustore) {
		SSLConnection.trustore = trustore;
		logger.info("Se configura como TrustStore='"+ trustore +"'");
	}

	public static String getTruststorePassword() {
		return jks_passwd;
	}

	public static void setTruststorePassword(String jks_passwd) {
		SSLConnection.jks_passwd = jks_passwd;
		logger.debug("Se configura como clave para el TrustStore='"+ jks_passwd +"'");
	}

}
