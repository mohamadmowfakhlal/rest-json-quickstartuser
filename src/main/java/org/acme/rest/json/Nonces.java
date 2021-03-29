package org.acme.rest.json;

public class Nonces {
public byte[] CNonce;
public byte[] SNonce;
public String MAC;
public byte[] getCNonce() {
	return CNonce;
}
public void setCNonce(byte[] cNonce) {
	CNonce = cNonce;
}
public byte[] getSNonce() {
	return SNonce;
}
public void setSNonce(byte[] sNonce) {
	SNonce = sNonce;
}
public String getMAC() {
	return MAC;
}
public void setMAC(String mAC) {
	MAC = mAC;
}
}
