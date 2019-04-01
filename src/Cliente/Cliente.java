package Cliente;
import Servidor.Constants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Cliente implements Runnable{
    
    private static Socket server = null;
    private static DataInputStream in = null;
    private static DataOutputStream out = null;
    private static boolean endOfRequests = false;
    private int ID_CLIENTE;
    private static final int SLEEP_TIME = 10000;

    public Cliente(int id) {
        this.ID_CLIENTE = id;
    }
    
    
    
    private void defineInputOutput(){
        try {
            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Error creating input/output stream. - CLIENTE");
        }
    }
     
    public void clientReadRequest(){
        try{
            //sending request to read a file
            out.writeUTF("" + Constants.READ_FILE);
            //sending file name
            out.writeUTF(Constants.CLIENTE + this.ID_CLIENTE + ".txt");
            //waiting for server response - File exists
            String response = in.readUTF();
            
            if(response.equalsIgnoreCase("File not found.")){
                out.writeUTF("" + Constants.END_OF_REQUEST);
            }else{
                while(true){
                    System.out.println("====Reading file====\n\n");
                    String line = in.readUTF();
                    if(line.equals("***")){
                        break;
                    }else
                        System.out.println(line);
                }
            }
        }catch(IOException ie){
            
        }
    }
    
    public void clientWriteRequest(){
        try {
            //sending request to create a new file
            out.writeUTF("" + Constants.WRITE_FILE);
            //sending file name
            out.writeUTF(Constants.CLIENTE + this.ID_CLIENTE + ".txt");
            //waiting for server response - File exists
            String response = in.readUTF();
            
            if(response.equalsIgnoreCase("File not found.")){
                out.writeUTF("" + Constants.END_OF_REQUEST);
            }else{
                Random g = new Random();
                int numberOfChars = 20 + g.nextInt(50);
                String output = "";
                
                for (int i = 0; i < numberOfChars; i++){
                    output += Character.toString((char)(g.nextInt(26) + 'a'));
                }
                out.writeUTF(output);
            }

     
        } catch (SocketException se){
            System.out.println("Connection lost. {clientCreateFile} " + se.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException {clientCreateFile}. " + ex.getMessage());
        }
    }
    
    public void clientCreateFile(){
        
        try {
            //sending request to create a new file
            out.writeUTF("" + Constants.CREATE_FILE);
            //waiting for server response - create file
            System.out.println(in.readUTF());
            //sending file name
            out.writeUTF(Constants.CLIENTE + this.ID_CLIENTE + ".txt");
            //waiting for server response - ok-error status
            System.out.println(in.readUTF());
     
        } catch (SocketException se){
            System.out.println("Connection lost. {clientCreateFile} " + se.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException {clientCreateFile}. " + ex.getMessage());
        }
 
    }
    
    public void ggizi(){
        
    }
    
    public void sendRequest(int requestType){      
        
        switch(requestType){
            case Constants.READ_FILE:
                clientReadRequest();
                break;
            case Constants.END_OF_REQUEST:
        
                try {
                    out.writeUTF("" + Constants.END_OF_REQUEST);
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                } finally{
                    endOfRequests = true;
                }       
                
                return;
            case Constants.WRITE_FILE:
                clientWriteRequest();
                endOfRequests = true;
                break;
            case Constants.CREATE_FILE:
                clientCreateFile();
                break;
        }
    
    }
    
    public void run() {
       
        try {
            server = new Socket(Constants.IP, Constants.getSocket());
//            Constants.updateSocket();
            defineInputOutput();
        } catch (IOException ex) {
            System.out.println("Could not connect to server.");
        }
        
        Random g = new Random();
        
        while(!endOfRequests){
//            int number = 1 + g.nextInt(3);;
//            
//            System.out.println("Cliente ID #" + this.ID_CLIENTE + ": " + number);
            sendRequest(Constants.CREATE_FILE);
            
            if(!endOfRequests)
                sendRequest(Constants.END_OF_REQUEST);
        }
        
        try {
            server.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
