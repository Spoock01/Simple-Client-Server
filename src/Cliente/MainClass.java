
package Cliente;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joao
 */
public class MainClass {
    public static void main(String[] args) {
        
        
        ExecutorService EXECUTOR = Executors.newCachedThreadPool();
        
        Cliente c[] = new Cliente[40];
        
        for (int i = 0; i < 40; i++){
            c[i] = new Cliente(i);
        }

        
        for (int i = 0; i < 40; i++){
            System.out.println("Cliente: "+ i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
            }
            EXECUTOR.execute(c[i]);
            
        }
    }
}
