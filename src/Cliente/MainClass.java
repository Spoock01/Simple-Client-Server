
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
    public static void main(String[] args) throws InterruptedException {
        
        
        ExecutorService EXECUTOR = Executors.newCachedThreadPool();
        int i = 0;

        while(true){
            EXECUTOR.execute(new Cliente(i++));
            
            Thread.sleep(200);
            
            if(i == 100){
                break;
            }
        }
        
        EXECUTOR.shutdown();
        System.out.println("Ending main class.");
    }
}