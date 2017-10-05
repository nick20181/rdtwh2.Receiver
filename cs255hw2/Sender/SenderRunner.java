package cs255hw2.Sender;
import java.io.*;
import java.nio.ByteBuffer;
/**
 *
 * @author admin.dakota
 */
public class SenderRunner {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SenderClient test = new SenderClient();
        ByteBuffer buffer = ByteBuffer.allocate(4);
        
        FileHandlerSender pish = new FileHandlerSender();
        
        byte[][] payloads = pish.packetPayloadAssembler(new File("C:\\Users\\admin.dakota\\Desktop\\testing.txt"));
        Packet pckt = new Packet(0,4466,4467, payloads[0]); 
        
        
        test.sendCommand(pckt.makePacket());
        
    }
    
}
