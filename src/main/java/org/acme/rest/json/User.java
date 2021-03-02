package org.acme.rest.json;

import java.util.UUID;

public class User {
	private UUID id;

    public String username;
    public String password;

    public User() {
    }

    public User(String username, String password) {
		id = UUID.randomUUID();
        this.username = username;
        this.password = password;
    }
}