package cs255hw2.Receiver;
import cs255hw2.Sender.*;
import java.io.*;
import java.nio.ByteBuffer;
/**
 *
 * @author admin.dakota
 */
public class CS255HW2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(4);
        
        FileHandlerReceiver pish = new FileHandlerReceiver();
        
        RecieverClient client = new RecieverClient();
        byte[][] a = new byte[1][];
        //Packet b = new Packet(client.getToReturn());
        //a[0] = b.getPayload();
        //pish.construct(a);
        a[0] = client.getToReturn();
        buffer.put(a[0][4]);
        buffer.put(a[0][5]);
        buffer.put(a[0][6]);
        buffer.put(a[0][7]);
        buffer.flip();
        
        System.out.println(buffer.getInt());
    }
    
}
