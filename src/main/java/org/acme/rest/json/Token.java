package org.acme.rest.json;

import java.util.UUID;

import org.json.JSONObject;

public class Token extends JSONObject{
	private UUID id;

	public String CNonce;
	public String SNonce;
	public String MAC;
	public String sessionKey;
	public String encryptedSessionKey;
	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getEncryptedSessionKey() {
		return encryptedSessionKey;
	}

	public void setEncryptedSessionKey(String encryptedSessionKey) {
		this.encryptedSessionKey = encryptedSessionKey;
	}





	public String getMAC() {
		return MAC;
	}

	public void setMAC(String mAC) {
		MAC = mAC;
	}

	public Token() {
    }

    public Token( String CNonce,  String SNonce) {
    	id = UUID.randomUUID();
        this.CNonce = CNonce;
        this.SNonce = SNonce;
    }
}