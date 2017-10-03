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
        ByteBuffer buffer = ByteBuffer.allocate(4);
        
        FileHandlerSender pish = new FileHandlerSender();
        byte[][] payloads = pish.packetPayloadAssembler(new File("C:\\Users\\nicholas.bohm\\Desktop\\cafe-javadoc.zip"));
        PacketSender pckt1 = new PacketSender(0,4466,4467,payloads[0]);
        PacketSender pckt2 = new PacketSender(0,4466,4467,payloads[1]);
        byte[] pcktm = pckt1.makePacket();
        byte[] pckts = pckt1.makePacket();
        System.out.println("pcktm" + pcktm.length + ", pckts" + pckts.length );
        buffer.put(pcktm[4]);
        buffer.put(pcktm[5]);
        buffer.put(pcktm[6]);
        buffer.put(pcktm[7]);
        
        buffer.flip();
        
        System.out.println(buffer.getInt());
        
    }
    
}
