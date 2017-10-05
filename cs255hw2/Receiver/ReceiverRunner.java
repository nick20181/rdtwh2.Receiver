package cs255hw2.Receiver;
import cs255hw2.Sender.*;
import java.io.*;
import java.nio.ByteBuffer;
/**
 *
 * @author admin.dakota
 */
public class ReceiverRunner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(4);
        
        FileHandlerReceiver pish = new FileHandlerReceiver();
        
        ReceiverClient client = new ReceiverClient();
        
        PacketReceiver pckts = new PacketReceiver(client.getPckt());
        byte[][] a = new byte[1][];
        a[0] = pckts.getPayload();
        pish.construct(a);
        
    }
    
}
