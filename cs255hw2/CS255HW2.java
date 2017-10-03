package cs255hw2;
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
        byte[] test = pish.fileTobyte(new File("C:\\Users\\nick201\\Desktop\\AddonPedsVars.xml"));
        byte[][] payloads = pish.packetPayloadAssembler(test);
        PacketSender pckt1 = new PacketSender(0,4466,4467,payloads[0]);
        PacketSender pckt2 = new PacketSender(0,4466,4467,payloads[1]);
        byte[] pcktm = pckt1.makePacket();
        byte[] pckts = pckt1.makePacket();
        System.out.println("pcktm" + pcktm.length + ", pckts" + pckts.length );
//        for(int i = 0; i != pcktm.length; i++ ){
//            System.out.println("Packet " + i + ": " + pcktm.length);
//        }
        
        
    }
    
}
