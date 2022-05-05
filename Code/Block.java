import java.security.MessageDigest;
import java.io.Serializable;
import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class Block implements Serializable  {
	private static final long serialVersionUID = 1L;
	Long timestamp;
    String hash;
    String previousHash;
    long nonce = (long) 0;
    ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
    String merkelroot;
    
    public Block(String previousHash, ArrayList<Transaction> list) {
    	this.timestamp = System.currentTimeMillis();
    	this.previousHash = previousHash;
    	this.transactionList = new ArrayList<Transaction>(list);
    	this.merkelroot = totalTransaction();
    	this.hash = calcHash();
    }
    
    public String totalTransaction() {      //like a merkel root
    	String data = "";
    	for(Transaction trans: transactionList) {
    		data = data + trans.toString();
    	}
    	return data;
    }

	public boolean mineBlock(int difficulty) {
    	String target = new String(new char[difficulty]).replace('\0', '0');
    	nonce = (long) 0;
    	while(!target.equals(hash.substring(0, difficulty))) {
    		nonce++ ;
    		hash = calcHash();
    	}
    	return true;
    }

	public String calcHash(){
		String input = previousHash + Long.toString(timestamp) + Long.toString(nonce) + merkelroot;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
	        
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
    
    public String toString() {
		return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
    
    public void printBlock() {
    	System.out.println("timestamp: "+timestamp);
    	System.out.println("hash: "+ hash);
    	System.out.println("prevHash: "+ previousHash);
    	System.out.println("nonce: "+ nonce);
    	
    	for(Transaction trans: transactionList) {
    		System.out.println("\n");
			System.out.println("Timestamp: " + Long.toString(trans.timestamp));
    		System.out.println("Sender name: " + trans.senderName);
    		//System.out.println("Sender publickey" + trans.senderKey);
    		System.out.println("Receiver name: " + trans.receiverName);
    		//System.out.println("Receiver publickey" + trans.receiverKey);
    		System.out.println(trans.data);
    		System.out.println("\n");
    		System.out.println("----------------End of Transaction----------------");
    	}
    }
    
}
