import java.security.PublicKey;
import java.util.*;

public class CLI {
    private int count=0;
	BlockChain bc = new BlockChain();
	ArrayList <User> Users=new ArrayList<User>();
    // ArrayList<String> users=new ArrayList<String>();
    public boolean CreateNewUser(String user_name){
        for(User user:this.Users){
            if(user.name.equals(user_name)){
                System.out.print("Entered user name already exists\nCreate new User name : ");
                return false;
            }
        }
        User w=new User(user_name);
        this.Users.add(w);
        System.out.println("User successfully created");
        return true;
    }
    public static void main(String args[]){
        Scanner sc=new Scanner(System.in);
        // ArrayList<String> users=new ArrayList<String>();
        CLI c = new CLI();
        User w = new User("Default_User");
        c.Users.add(w);

        System.out.println("Welcome to the blockchain system");
        System.out.println("Current User: 'Default_User'");

        while(true){
            System.out.println("Options (1: Enter data) (2: Create New User) (3: View User's data)");
            System.out.print("Choose your option : ");
            int opt=sc.nextInt();
            if(opt==3){
                System.out.print("Enter User name : ");
                String user_name=sc.next();
                for(User user:c.Users){
                    if(user.name.equals(user_name)){
                        String pr=c.bc.viewUser(user.publickey, user.name);
                        System.out.println("--------PRINTING THE RECEIVED DATA--------");
                        System.out.println(pr);
                    }
                }
            }
            else if(opt==2){
                System.out.print("Enter User name : ");
                String s=sc.next();
                boolean done=c.CreateNewUser(s);
                while(!done){
                    s=sc.next();
                    done=c.CreateNewUser(s);
                }
            }
            else if(opt==1){
                String sender,receiver;
                System.out.print("Enter sender's user name : ");
                sender=sc.next();
                boolean sender_user_name_ok=false;
                for(User user:c.Users){
                    if(user.name.equals(sender)){
                        sender_user_name_ok=true;
                        break;
                    }
                }
                while(sender_user_name_ok==false){
                    System.out.println("Entered user_name is not available. Re-enter user_name");
                    System.out.print("Enter sender's user name : ");
                    sender=sc.next();
                    for(User user:c.Users){
                        if(user.name.equals(sender)){
                            sender_user_name_ok=true;
                            break;
                        }
                    }
                }
                System.out.print("Enter receiver's user name : ");
                receiver=sc.next();
                boolean receiver_user_name_ok=false;
                for(User user:c.Users){
                    if(user.name.equals(receiver)){
                        receiver_user_name_ok=true;
                        break;
                    }
                }
                while(receiver_user_name_ok==false){
                    System.out.println("Entered user_name is not available. Re-enter user_name");
                    System.out.print("Enter receiver's user name : ");
                    receiver=sc.next();
                    for(User user:c.Users){
                        if(user.name.equals(receiver)){
                            receiver_user_name_ok=true;
                            break;
                        }
                    }
                }
                String data;
                System.out.print("Enter data : ");
                sc.nextLine();
                data=sc.nextLine();

                System.out.println("DEFAULT LOGIC: A block is created when there are 4 transactions to be added");
                System.out.println("Options: (1: Add) (2: Add by creating block)");

                int add;
                System.out.print("Enter option : ");
                add=sc.nextInt();
                
                User sender_user = c.Users.get(0);
                PublicKey receiver_pk = c.Users.get(0).publickey;
                for(User user:c.Users){
                        if(user.name.equals(sender)){
                            sender_user = user;
                        }
                        if(user.name.equals(receiver)){
                            receiver_pk = user.publickey;
                        }
                    }
                Transaction t = sender_user.sendData(receiver_pk, sender, receiver, data);
                c.count++;
                c.bc.unaddedTransactions.add(t);

                if(add==1){
                    // Transaction t
                    
                    if(c.count%4==0){
                        c.bc.createBlock();
                        BlockChain.writeJSON(c.bc, "hello.json");
                    }
                }
                else{
                    c.bc.createBlock();
                    BlockChain.writeJSON(c.bc, "hello.json");
                    c.count=0;
                }
            }   
        }   
    }
}
