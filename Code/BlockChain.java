import java.io.*;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.Security;
import java.util.ArrayList;
import com.google.gson.*;

public class BlockChain implements Serializable{
	//private static final long serialVersionUID = 1L;
	int difficulty = 4;
	public ArrayList<Block> Chain = new ArrayList<Block>();
	public ArrayList<Transaction> unaddedTransactions = new ArrayList<Transaction>();
	public BlockChain() {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		generateGenesisBlock();
	}
	
	public void generateGenesisBlock() {
		User genesis = new User("genesis");
		Transaction genesisTransaction = new Transaction(genesis.publickey,genesis.publickey,genesis.name,genesis.name,"This is genesis block");
		unaddedTransactions.add(genesisTransaction);
		Block genesisBlock = new Block("0",unaddedTransactions);
		genesisBlock.mineBlock(difficulty);
		genesisBlock.printBlock();
		unaddedTransactions.remove(0);
		Chain.add(genesisBlock);
	}
	
    public boolean verifyTransactions(ArrayList<Transaction> list) {
    	for(Transaction trans : list) {
    		if(!trans.verifySignature()) 
    			return false;
    	}
    	return true;
    }
    
    public boolean createBlock() {
    	if(verifyTransactions(unaddedTransactions)) {
    		String previousHash = Chain.get(Chain.size()-1).hash;
    		Block newBlock = new Block(previousHash,unaddedTransactions);
    		newBlock.mineBlock(difficulty);
    		Chain.add(newBlock);
			System.out.println("----------------CREATING A BLOCK WITH BELOW TRANSACTIONS----------------");
    		newBlock.printBlock();
			System.out.println("----------------BLOCK CREATION COMPLETED----------------");
    		unaddedTransactions.clear();
			System.out.println("Data added!");
    		return true;
    	}
		System.out.println("VERIFICATION FAILED. Data not added");
    	return false;
    }
    
    public String viewUser(PublicKey publickey,String name) {
    	String str="";
    	str += "User name: "+name+"\n"+"Public key:\n"+publickey+"\n";
    	
		for(Block block : Chain) {
			for(Transaction trans : block.transactionList) {
				if(trans.receiverKey == publickey) 
					str += "Received data: " + trans.data + "\nfrom " + trans.senderName + "\nwith " + trans.senderKey + "\nat timestamp " + Long.toString(trans.timestamp) + "\n";
				if(trans.senderKey == publickey) 
					str += "Sent data: " + trans.data + "\nto " + trans.receiverName + "\nwith " + trans.receiverKey + "\nat timestamp " + Long.toString(trans.timestamp) + "\n";
			}
		}
    	return str;
    }

	public static void writeJSON(BlockChain obj, String Filename)
	{
		try(Writer writer = new FileWriter(Filename)){
			Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
				@Override
				public boolean shouldSkipField(FieldAttributes arg0) {
					return false;
				}
				
				@Override
				public boolean shouldSkipClass(Class<?> arg0)
				{
					return arg0 == PublicKey.class || arg0 == PrivateKey.class || arg0 == byte[].class;
				}
			}).setPrettyPrinting().create();
			gson.toJson(obj,writer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}