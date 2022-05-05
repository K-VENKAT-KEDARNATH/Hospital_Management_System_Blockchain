import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
//import java.util.Random;

public class User {
	public PublicKey publickey;
	private PrivateKey privatekey;
	public String name;
	//int x;
	public User(String name) {
		generateKeys();
		this.name=name;
		// Random rnd = new Random();
		// x = rnd.nextInt();
		// if(x<0)
		// 	x = -x;
	}
	
	public void generateKeys() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random); //256 
	        KeyPair keyPair = keyGen.generateKeyPair();
	        // Set the public and private keys from the keyPair
	        privatekey = keyPair.getPrivate();
	        publickey = keyPair.getPublic();   
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Transaction sendData(PublicKey receiverKey,String senderName,String receiverName,String data) {
		Transaction newTransaction = new Transaction(publickey,receiverKey,senderName,receiverName,data);
		newTransaction.generateSignature(privatekey);
		return newTransaction;
	}
}
