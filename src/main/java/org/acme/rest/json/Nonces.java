package org.acme.rest.json;
import java.util.UUID;

import org.json.*;

public class Nonces {
	private UUID id;

public byte[] CNonce;
public byte[] SNonce;
public String MAC;


public  Nonces(byte[] x,byte []y) {
	id = UUID.randomUUID();

CNonce = x;
SNonce = y;
}
public byte[] getCNonce() {
	return CNonce;
}
public void setCNonce(byte[] CNonce) {
	this.CNonce = CNonce;
}
public byte[] getSNonce() {
	return SNonce;
}
public void setSNonce(byte[] SNonce) {
	this.SNonce = SNonce;
}
public String getMAC() {
	return MAC;
}
public void setMAC(String mAC) {
	MAC = mAC;
}

}
