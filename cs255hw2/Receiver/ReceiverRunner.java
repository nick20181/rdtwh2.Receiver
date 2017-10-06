package cs255hw2.Receiver;

import cs255hw2.Sender.*;
import java.io.*;
import java.nio.ByteBuffer;

/**
 *
 * @author admin.dakota
 */
public class ReceiverRunner {

    public ReceiverRunner() {
        byte[] ACK = new byte[4];
        byte[] NAK = new byte[4];
        for (int i = 0; i != 4; i++) {
            ACK[i] = 1;
        }
        for (int i = 0; i != 4; i++) {
            NAK[i] = 0;
        }
        FileHandlerReceiver dataHandler = new FileHandlerReceiver();
        ReceiverClient client = new ReceiverClient();
        System.out.println("The Data Downloaded to: " + dataHandler.getDirectoryPath());
        Packet pcktOne = null;
        Packet pcktTwo = new Packet(1, 0, 0, NAK);;
        Packet pcktACK;
        boolean run = true;
        //does not have nak info but name is used becuase lazy naming
        Packet pcktNAK;
        while (true) {
            while (run) {
                pcktOne = new Packet(client.getPckt());
                System.out.println("recieved1");
                if (pcktOne.getSeqNum() != pcktTwo.getSeqNum()) {
                    if (pcktOne.notCorrupt()) {
                        pcktACK = new Packet(pcktOne.getSeqNum(), pcktOne.getSrcPortNum(), client.getPort(), ACK);
                        System.out.println("Send ACK1");
                        client.sendCommand(pcktACK.makePacket());
                        dataHandler.construct(pcktOne.getPayload());
                        run = false;
                    } else {
                        pcktNAK = new Packet(pcktOne.getSeqNum(), pcktOne.getSrcPortNum(), client.getPort(), NAK);
                        System.out.println("NAK1");
                        client.sendCommand(pcktNAK.makePacket());
                    }
                } else {
                    pcktACK = new Packet(pcktTwo.getSeqNum(), pcktTwo.getSrcPortNum(), client.getPort(), ACK);
                    System.out.println("Sent ACK2");
                    client.sendCommand(pcktACK.makePacket());
                }
            }
            run = true;
            while (run) {
                pcktTwo = new Packet(client.getPckt());
                System.out.println("Received2");
                    System.out.println("Seq nums :" + pcktOne.getSeqNum() + ", " + pcktTwo.getSeqNum());
                if (pcktOne.getSeqNum() != pcktTwo.getSeqNum()) {
                    if (pcktTwo.notCorrupt()) {
                        pcktACK = new Packet(pcktOne.getSeqNum(), pcktOne.getSrcPortNum(), client.getPort(), ACK);
                        System.out.println("Sent ACK2");
                        client.sendCommand(pcktACK.makePacket());
                        dataHandler.construct(pcktTwo.getPayload());
                        run = false;
                    } else {
                        pcktNAK = new Packet(pcktTwo.getSeqNum(), pcktTwo.getSrcPortNum(), client.getPort(), NAK);
                        System.out.println("Sent NAK2");
                        client.sendCommand(pcktNAK.makePacket());
                    }
                } else {
                    pcktACK = new Packet(pcktOne.getSeqNum(), pcktOne.getSrcPortNum(), client.getPort(), ACK);
                    System.out.println("Sent ACK1");
                    client.sendCommand(pcktACK.makePacket());
                }
            }
            run = true;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ReceiverRunner();

    }

}
