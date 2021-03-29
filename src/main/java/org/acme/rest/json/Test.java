package org.acme.rest.json;

import java.security.SecureRandom;

public class Test {

	public static void main(String[] args) {
    	//define a class that does a decryption
    	String key = "2222222222222222";

    	AES aes = new AES();
    	///String res = AES.encrypt("222222222222",key);
    	
        byte[] nonce = new byte[12];
        new SecureRandom().nextBytes(nonce);

        System.out.println(convertBytesToHex(nonce));

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


