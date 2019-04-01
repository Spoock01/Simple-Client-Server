
package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    
    public static void main(String[] args) {
        
        ServerSocket servidor;
        Socket socket;
        ExecutorService EXECUTOR = Executors.newCachedThreadPool();
        
        
        try {
            servidor = new ServerSocket(Constants.SOCKET_PORT);
            
            while(true){
                socket = servidor.accept();
                System.out.println("Connected: " + socket);
                EXECUTOR.execute(new Protocolo(socket));
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
