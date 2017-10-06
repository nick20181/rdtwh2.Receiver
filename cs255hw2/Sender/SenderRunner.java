package cs255hw2.Sender;

import java.io.*;
import java.nio.ByteBuffer;

/**
 *
 * @author admin.dakota
 */
public class SenderRunner {

    public SenderRunner() {
        byte[] ACK = new byte[4];
        for (int i = 0; i != 4; i++) {
            ACK[i] = 1;
        }
        System.out.println("Ack being used: " + ACK);
        int desPort = 4467;
        boolean run = true;
        int currentSeq = 0;

        Packet prevPckt = null;
        Packet currentPckt;
        Packet currentAck;
        SenderClient sender = new SenderClient();
        FileHandlerSender pish = new FileHandlerSender();
        byte[][] payloads = pish.packetPayloadAssembler(new File("C:\\Users\\nick201\\Desktop\\JavaApplication2.7z"));
        for (int i = 0; i != payloads.length; i++) {

            currentPckt = new Packet(currentSeq, sender.getPort(), desPort, payloads[i]);
            System.out.println("Sending Packet " + currentSeq + " CheckSum: " + currentPckt.getCheckSum());
            sender.sendCommand(currentPckt.makePacket());

            while (run) {
                currentAck = new Packet(sender.getPckt());
                System.out.println("Ack Checksum: " + currentAck.getCheckSum() + "SystemCheckSum: " + currentAck.notCorrupt());
                if (currentAck.notCorrupt()) {
                    System.out.println("recived Ack: " + currentAck.getPayload() + " Saved Ack: " + ACK);
                    if (this.getInt(currentAck.getPayload()) == this.getInt(ACK)) {
                        System.out.println("Ack recived!.");
                        currentSeq++;
                        run = false;
                        prevPckt = currentPckt;
                        currentPckt = null;
                    } else {
                        System.out.println("Nak Recived!");
                        sender.sendCommand(currentPckt.makePacket());
                    }

                } else {
                    System.out.println("ACKPckt is corrupt resending currentPckt");
                    sender.sendCommand(currentPckt.makePacket());
                }
            }
            run = true;

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SenderRunner();

    }
    
    public int getInt(byte[] data){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(data);
        buffer.flip();
        return buffer.getInt();
    }
}
