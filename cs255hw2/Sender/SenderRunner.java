package cs255hw2.Sender;

import java.io.*;
import java.nio.ByteBuffer;

/**
 *
 * @author admin.dakota
 */
public class SenderRunner {

    public SenderRunner() {
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
        byte[][] payloads = pish.packetPayloadAssembler(new File("C:\\Users\\nick201\\Desktop\\JavaApplication2.7z"));
        for (int i = 0; i != payloads.length; i++) {
            while (run) {
                System.out.println("Sending packet one");
                pcktOne = new Packet(0, 4467, sender.getPort(), payloads[i]);
                sender.sendCommand(pcktOne.makePacket());
                ackOne = new Packet(sender.getPckt());

                if (ackOne.getSeqNum() == pcktOne.getSeqNum() && ackOne.notCorrupt()) {
                    System.out.println("Ack recived for one");
                    run = false;
                    i++;
                }
            }

            if (i != payloads.length) {
                run = true;
            }

            while (run) {
                System.out.println("Sending packet Two");
                pcktTwo = new Packet(1, 4467, sender.getPort(), payloads[i]);
                sender.sendCommand(pcktTwo.makePacket());
                ackTwo = new Packet(sender.getPckt());

                if (ackTwo.getSeqNum() == pcktTwo.getSeqNum() && ackTwo.notCorrupt()) {
                    System.out.println("Packjet Two Ack Ok");
                    run = false;
                }

            }
            if (i != payloads.length) {
                run = true;
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
