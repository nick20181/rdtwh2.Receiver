package cs255hw2.Sender;
import java.io.*;
import java.nio.ByteBuffer;
/**
 *
 * @author admin.dakota
 */
public class SenderRunner {
    
    public SenderRunner(){
        boolean run = true;
        byte[] ACK = new byte[4];
        for (int i = 0; i != 4; i++) {
            ACK[i] = 1;
        }
        Packet pcktOne;
        Packet pcktTwo;
        Packet ackOne;
        Packet ackTwo;
        SenderClient sender = new SenderClient();
        FileHandlerSender pish = new FileHandlerSender();
        byte[][] payloads = pish.packetPayloadAssembler(new File("C:\\Users\\nicholas.bohm\\Desktop\\cafe-javadoc.zip"));
        for(int i = 0; i != payloads.length; i++){
            while(run){
            pcktOne = new Packet(0, 4467, sender.getPort(), payloads[i]);
            sender.sendCommand(pcktOne.makePacket());
            byte[] test = sender.getPckt();
                System.out.println(test[0]);
                System.out.println(test[1]);
                System.out.println(test[2]);
                System.out.println(test[3]);
            ackOne = new Packet(test);
            
            if(ackOne.getSeqNum() == pcktOne.getSeqNum() && ackOne.notCorrupt()){
                run = false;
                i++;
            }
            }
            
            if(i != payloads.length){
                run = true;
            }
            
            while(run){
            pcktTwo = new Packet(1, 4467, sender.getPort(), payloads[i]);
            sender.sendCommand(pcktTwo.makePacket());
            ackTwo = new Packet(sender.getPckt());
            
            if(ackTwo.getSeqNum() == pcktTwo.getSeqNum() && ackTwo.notCorrupt()){
                run = false;
            }
            }
            
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SenderRunner();
        
    }
    
    
}
