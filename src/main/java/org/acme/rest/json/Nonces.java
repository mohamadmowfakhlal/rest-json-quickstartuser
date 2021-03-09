package org.acme.rest.json;

public class Nonces {
public String CNonce;
public String SNonce;
public String MAC;
public String getCNonce() {
	return CNonce;
}
public void setCNonce(String cNonce) {
	CNonce = cNonce;
}
public String getSNonce() {
	return SNonce;
}
public void setSNonce(String sNonce) {
	SNonce = sNonce;
}
public String getMAC() {
	return MAC;
}
public void setMAC(String mAC) {
	MAC = mAC;
}
}
