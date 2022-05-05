import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.Key;
import java.util.Base64;
//import java.util.Random;

public class Transaction implements Serializable{
	private static final long serialVersionUID = 1L;
	Long timestamp;
	public PublicKey senderKey;
	public PublicKey receiverKey; 	
	public String senderName;
	public String receiverName;
	public String data;
	public byte[] signature;
	//int p,g,y,h,s0,s1;
	
	public Transaction(PublicKey senderKey,PublicKey receiverKey,String senderName,String receiverName,String data){
		this.timestamp = System.currentTimeMillis();
		this.senderKey = senderKey;
		this.receiverKey = receiverKey;
		this.senderName=senderName;
		this.receiverName=receiverName;
		this.data = data;
	}

	public void generateSignature(PrivateKey privateKey) {
		//long x = Long.valueOf(keyToString(privateKey));

		// Random rnd = new Random();
		// p = Prime.getPrime();
		// //g = 1 + rnd.nextInt(9);
		// g = Generator.gen(p);
		// y = Prime.pow_mod(g, x, p);
		// int r = rnd.nextInt((int)p-1); // 0 to p-2
		// h = Prime.pow_mod(g, r, p);
		// s0 = r%(p-1);
		// s1 = (r+x)%(p-1);

		String data = keyToString(senderKey) + keyToString(receiverKey) + this.data;
		Signature dsa;
		try {
			dsa = Signature.getInstance("ECDSA", "BC");
			//dsa = Signature.getInstance("RSA");
			dsa.initSign(privateKey);
			dsa.update(data.getBytes());
			signature = dsa.sign();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean verifySignature() {
		// Random rnd = new Random();
		// int b = rnd.nextInt(2);
		// int s;
		// if(b==0)
		// 	s = s0;
		// else
		// 	s = s1;

		// if(Prime.pow_mod(g,s,p) != h*Prime.pow_mod(y,b,p))
		// 	return false;

		String data = keyToString(senderKey) + keyToString(receiverKey) + this.data;
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(senderKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String toString() {
		String data = keyToString(senderKey) + keyToString(receiverKey) + this.data;
		return data;
	}

	public String keyToString(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}	

}