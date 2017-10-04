package cs255hw2.Sender;
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
        SenderClient test = new SenderClient();
        ByteBuffer buffer = ByteBuffer.allocate(4);
        
        FileHandlerSender pish = new FileHandlerSender();
        byte[][] payloads = pish.packetPayloadAssembler(new File("C:\\Users\\nicholas.bohm\\Desktop\\cafe-javadoc.zip"));
        PacketSender pckt1 = new PacketSender(0,4466,4467,payloads[0]);
        byte[] pcktm = pckt1.makePacket();
        test.sendCommand(pcktm);
        
    }
    
}
