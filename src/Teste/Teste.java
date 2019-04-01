package Teste;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Teste {
    public static void main(String[] args) {
        
        String FILENAME = "src\\Servidor\\CLIENTE-teste.txt";
        File f = new File(FILENAME);
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BufferedWriter bw = null;
        try {
            FileWriter fw = new FileWriter(FILENAME);
            bw = new BufferedWriter(fw);
        } catch (IOException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            bw.write(FILENAME);
        } catch (IOException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Teste.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
