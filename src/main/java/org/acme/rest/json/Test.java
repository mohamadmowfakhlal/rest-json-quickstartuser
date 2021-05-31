package org.acme.rest.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.SecretKey;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
        byte[] Snonce = new byte[16];
        new SecureRandom().nextBytes(Snonce);
        
		String x = Snonce.toString();
		System.out.print(x);
		byte[] y = x.getBytes();
    	//define a class that does a decryption
    	String key = "2222222222222222";

    	String encoded = Base64.getEncoder().encodeToString(Snonce);
    	byte[] decoded = Base64.getDecoder().decode(encoded);
    	String s="";
    	byte[]  ddd;
		s = new String(Snonce, java.nio.charset.StandardCharsets.ISO_8859_1);
        ddd = s.getBytes( java.nio.charset.StandardCharsets.ISO_8859_1);
    	AES aes = new AES();
    	///String res = AES.encrypt("222222222222",key);
    	
        byte[] nonce = new byte[12];
        new SecureRandom().nextBytes(nonce);

        System.out.println(convertBytesToHex(nonce));
        KeyStore keyStore = null;
        try {
			keyStore = KeyStore.getInstance("JKS");
			try {
				keyStore.load(null,null);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        char[] myPass = null;
        SecretKey secretKey = null;

        /*
         * Store a SecretKey in the KeyStore, with "mypass"
         * as the password.
         */

        InputStream keyStream = new ByteArrayInputStream("afa".getBytes());
        try {
			keyStore.load(keyStream, null);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    // util to print bytes in hex
    private static String convertBytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte temp : bytes) {
            result.append(String.format("%02x", temp));
        }
        return result.toString();
    }
}


