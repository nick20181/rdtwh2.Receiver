package cs255hw2.Sender;

import cs255hw2.shared.DataHandler;
import cs255hw2.shared.*;
import java.io.*;
import java.nio.ByteBuffer;

/**
 *
 * @author Dakota Vanwormer
 */
public class SenderRunner {
    
    public SenderRunner() {

        //creates the ack byte array to be used to check the ack that is recieved
        byte[] ACK = new byte[4];
        for (int i = 0; i != 4; i++) {
            ACK[i] = 1;
        }
        //Varibles
        int desPort = 4467;
        boolean run = true;
        int currentSeq = 0;
        Packet prevPckt = null;
        Packet currentPckt;
        Packet currentAck;
        //Creating needed Objects
        Client client = new Client();
        DataHandler pish = new DataHandler();
        //the file that is broken up into payload segments.
        byte[][] payloads = pish.packetPayloadAssembler(new File("C:\\Users\\nicholas.bohm\\Desktop\\send\\testing.txt"));
        for (int i = 0; i != payloads.length; i++) {
            
            currentPckt = new Packet(currentSeq, client.getPort(), desPort, payloads[i]);
            System.out.println("Sending Packet " + currentSeq + " CheckSum: " + currentPckt.getCheckSum());
            client.sendCommand(currentPckt.makePacket());
            
            while (run) {
                currentAck = new Packet(client.getPckt());
                System.out.println("Ack Checksum: " + currentAck.getCheckSum() + "SystemCheckSum: " + currentAck.notCorrupt());
                if (currentAck.notCorrupt()) {
                    System.out.println("recived Ack: " + currentAck.getPayload() + " Saved Ack: " + ACK);
                    //if (this.getInt(currentAck.getPayload()) == this.getInt(ACK)) {
                    System.out.println("Ack recived!.");
                    if (currentSeq == 1) {
                        currentSeq = 0;
                    } else {
                        currentSeq++;
                    }
                    
                    run = false;
                    prevPckt = currentPckt;
                    currentPckt = null;
                } else {
                    System.out.println("Nak Recived!");
                    client.sendCommand(currentPckt.makePacket());
                }

                // } else {
                System.out.println("ACKPckt is corrupt resending currentPckt");
                client.sendCommand(currentPckt.makePacket());
                //}
            }
            run = true;
            
        }
        client.closeSocket();
    }
    
    public SenderRunner(boolean t) {
        String ip = "10.15.1.21";
        int srcPort = 4466;
        String fileName = "testing.txt";
        int payLoadSize = 1004;
        
        ClientRewrite client = new ClientRewrite(ip, srcPort);
        
        DataHandlerRewrite dataHandler = new DataHandlerRewrite();
        dataHandler.setDirectory("C:\\Users\\nicholas.bohm\\Desktop\\send");
        byte[] currentPayload = dataHandler.FileToByte(fileName, payLoadSize);
        
        PacketRewrite packet = new PacketRewrite(0, srcPort, 4467, 0, currentPayload);
        System.out.println("seqNum: " + dataHandler.byteToInt(packet.getSeqNum()));
        System.out.println("srcPort: " + dataHandler.byteToInt(packet.getSrcPort()));
        System.out.println("desPort: " + dataHandler.byteToInt(packet.getDesPort()));
        System.out.println("checkSum: " + dataHandler.byteToInt(packet.getCheckSum()));
        System.out.println("dataLength: " + dataHandler.byteToInt(packet.getDataLength()));
        System.out.println("PayLoadLength: " + currentPayload.length);
        byte[] toSend = packet.makePckt();
        client.sendPacket(toSend);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SenderRunner(true);
        
    }
    
}
