package cs255hw2;
import java.io.*;
/**
 *
 * @author admin.dakota
 */
public class CS255HW2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FileHandler pish = new FileHandler();
        byte[] test = pish.fileTobyte(new File("C:\\Users\\TEMP\\Desktop\\HW2RDT3.0.zip"));
        System.out.println(test.length);
        byte[][] test2 = pish.packetPayloadAssembler(test);
        System.out.println(test2.length);
    }
    
}
