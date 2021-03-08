package org.acme.rest.json;

public class Test {

	public static void main(String[] args) {
    	//define a class that does a decryption
    	String key = "2222222222";

    	AES aes = new AES();
    	String res = AES.encrypt("222222222222",key);

	}

}
