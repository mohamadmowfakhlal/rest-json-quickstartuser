package org.acme.rest.json;

import java.util.UUID;

import org.json.JSONObject;

public class Nonc extends JSONObject{
	private UUID id;

	public String CNonce;
	public String SNonce;
	public String MAC;



	public String getMAC() {
		return MAC;
	}

	public void setMAC(String mAC) {
		MAC = mAC;
	}

	public Nonc() {
    }

    public Nonc( String CNonce,  String SNonce) {
    	id = UUID.randomUUID();
        this.CNonce = CNonce;
        this.SNonce = SNonce;
    }
}