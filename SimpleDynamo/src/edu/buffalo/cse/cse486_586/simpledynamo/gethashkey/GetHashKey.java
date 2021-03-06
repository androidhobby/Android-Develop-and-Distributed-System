package edu.buffalo.cse.cse486_586.simpledynamo.gethashkey;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * @author Fang Wu
 * April 20, 2012
 *
 */

public class GetHashKey {
	
	@SuppressWarnings("unused")
	private static final String TAG = "GetHashKey";
	
	public GetHashKey() { }
	
	public String genHash(String input) throws NoSuchAlgorithmException {
		MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
		byte[] sha1Hash = sha1.digest(input.getBytes());
		Formatter formatter = new Formatter();
		for (byte b : sha1Hash) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}
}
