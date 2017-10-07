package cs255hw2.Sender;

import java.io.*;
import java.nio.ByteBuffer;

/**
 *
 * @author admin.dakota
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
        FileHandler pish = new FileHandler();
        //the file that is broken up into payload segments.
        byte[][] payloads = pish.packetPayloadAssembler(new File("C:\\Users\\nick201\\Desktop\\checksumtest.txt"));
        for (int i = 0; i != payloads.length; i++) {     
            
            currentPckt = new Packet(currentSeq, client.getPort(), desPort, payloads[i]);
            System.out.println("Sending Packet " + currentSeq + " CheckSum: " + currentPckt.getCheckSum());
            client.sendCommand(currentPckt.makePacket());

            while (run) {
                currentAck = new Packet(client.getPckt());
                System.out.println("Ack Checksum: " + currentAck.getCheckSum() + "SystemCheckSum: " + currentAck.notCorrupt());
                if (currentAck.notCorrupt()) {
                    System.out.println("recived Ack: " + currentAck.getPayload() + " Saved Ack: " + ACK);
                    if (this.getInt(currentAck.getPayload()) == this.getInt(ACK)) {
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

                } else {
                    System.out.println("ACKPckt is corrupt resending currentPckt");
                    client.sendCommand(currentPckt.makePacket());
                }
            }
            run = true;

        }
        client.closeSocket();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SenderRunner();

    }
    /**
     * turns a byte array into a int. Used for checking the acks of the receiver.
     * @param data
     * @return 
     */
    public int getInt(byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(data);
        buffer.flip();
        return buffer.getInt();
    }
}
