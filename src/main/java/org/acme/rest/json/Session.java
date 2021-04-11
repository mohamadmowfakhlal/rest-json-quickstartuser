package org.acme.rest.json;

import java.util.UUID;

import org.json.JSONObject;

public class Session extends JSONObject{
	public UUID session;
	public void setUUID(UUID session) {
		this.session = session;
		
	}

}
