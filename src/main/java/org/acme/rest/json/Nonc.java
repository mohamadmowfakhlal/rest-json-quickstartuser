package org.acme.rest.json;

import java.util.UUID;

public class Nonc {
	private UUID id;

	public byte[] CNonce;
	public byte[] SNonce;
	public String MAC;



	public Nonc() {
    }

    public Nonc( byte[] CNonce,  byte[] SNonce) {
    	id = UUID.randomUUID();
        this.CNonce = CNonce;
        this.SNonce = SNonce;
    }
}