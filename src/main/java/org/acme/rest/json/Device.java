package org.acme.rest.json;

import java.util.UUID;

import org.json.JSONObject;

public class Device  extends JSONObject{
	private UUID id;

    public String deviceID;
    public String oldDeviceID;
	public String MAC;
    public String key;
	public String username;

	
    public String getOldDeviceID() {
		return oldDeviceID;
	}

	public void setOldDeviceID(String oldDeviceID) {
		this.oldDeviceID = oldDeviceID;
	}



    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key1) {
		key = key1;
	}

	public Device() {
    }

    public Device(String deviceID, String key) {
    	id = UUID.randomUUID();
        this.deviceID = deviceID;
        this.key = key;
    }
}