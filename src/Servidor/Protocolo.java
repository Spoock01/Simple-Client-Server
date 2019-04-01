
package Servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Protocolo implements Runnable{

    private DataInputStream in;
    private DataOutputStream out;
    private Socket s;
    private boolean end = false;
    private String path = "src\\Servidor\\";
    
    public Protocolo(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        
        defineInputOutput();
        
        while(!end){
            end = waitForRequest();
        }
        System.out.println("CONNECTION CLOSED.");
    }
    
    public void defineInputOutput(){
        try {
            in = new DataInputStream (s.getInputStream());
            out = new DataOutputStream (s.getOutputStream());
            
        } catch (IOException ex) {
            System.out.println("Error creating input/output stream. - SERVIDOR");
        }
    }
    
    private boolean waitForRequest(){
        int requestType = -1;
        try {
            
            requestType = Integer.parseInt(in.readUTF());
            System.out.println("Request: {" + requestType + "}");
            
            switch(requestType){
                case Constants.END_OF_REQUEST:
                    System.out.println("Request: End of connection.");
                    return true;
                case Constants.READ_FILE:
                    System.out.println("Request read file");
                    readFile();
                    return false;
                case Constants.WRITE_FILE:
                    System.out.println("Request write file");
                    writeFile();
                    return false;
                case Constants.CREATE_FILE:
                    System.out.println("Request to create a new file");
                    createFile();
                    return false;
                default:
                    System.out.println("Invalid option!");
                    return true;     
            }      
            
        } catch (SocketException se){
                System.out.println("Connection lost.");
        } catch (IOException ex) {
            System.out.println("RequestType must be an integer. " + ex.getMessage() + "\n" + ex.getLocalizedMessage());
        }
        return true;

    }
    
    private void readFile() throws IOException{
    
        String filename = path + in.readUTF();
        System.out.println(filename);
        
        File f = new File(filename);
        BufferedReader file = new BufferedReader(new FileReader(filename)); 
        
        if(f.exists()){
            out.writeUTF("OK STATUS");
            String str;
            while(true){
                str = file.readLine();
                System.out.println(str);
                if (str == null){
                    out.writeUTF("***");
                    break;
                }else{
                    out.writeUTF(str);
                }
                
                
            }
        }else{
            out.writeUTF("File not found");
        }
        
    
    }

    private void writeFile() {
        BufferedWriter bw = null;
        try {
            String filename = in.readUTF();
            System.out.println("Filename received: " + filename);
            
            File f = new File(path + filename);            
            FileWriter fw = new FileWriter(path + filename);
            bw = new BufferedWriter(fw);
            
            if(f.exists()){
                
                out.writeUTF("The file was found.");
                String content = in.readUTF();
                bw.write(content);
                bw.flush();
            }else{
                out.writeUTF("File not found.");
            }
        } catch (IOException ex) {        
            Logger.getLogger(Protocolo.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(Protocolo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void createFile() throws IOException {
        out.writeUTF("Create File Function. Insert file name: ");
        
        String filename = in.readUTF(); 
        System.out.println("File: " + filename);
        File f = new File(path + filename);
        
        if(f.createNewFile())
            out.writeUTF("OK STATUS");
        else
            out.writeUTF("ERROR STATUS - File already exists.");
    }   
}
