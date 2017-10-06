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
        Packet pcktOne = null;
        Packet pcktTwo = new Packet(1, 0, 0, NAK);;
        Packet pcktACK;
        //does not have nak info but name is used becuase lazy naming
        Packet pcktNAK;
        while (true) {
            pcktOne = new Packet(client.getPckt());
            if (pcktOne.getSeqNum() != pcktTwo.getSeqNum()) {
                if (pcktOne.notCorrupt()) {
                    pcktACK = new Packet(pcktOne.getSeqNum(), pcktOne.getSrcPortNum(), client.getPort(), ACK);
                    client.sendCommand(pcktACK.makePacket());
                    dataHandler.construct(pcktOne.getPayload());
                } else {
                    pcktNAK = new Packet(pcktOne.getSeqNum(), pcktOne.getSrcPortNum(), client.getPort(), NAK);
                    client.sendCommand(pcktNAK.makePacket());
                }
            } else {
                pcktACK = new Packet(pcktTwo.getSeqNum(), pcktTwo.getSrcPortNum(), client.getPort(), ACK);
                client.sendCommand(pcktACK.makePacket());
            }
            pcktTwo = new Packet(client.getPckt());
            if (pcktOne.getSeqNum() != pcktTwo.getSeqNum()) {
                if (pcktTwo.notCorrupt()) {
                    pcktACK = new Packet(pcktOne.getSeqNum(), pcktOne.getSrcPortNum(), client.getPort(), ACK);
                    client.sendCommand(pcktACK.makePacket());
                    dataHandler.construct(pcktTwo.getPayload());
                } else {
                    pcktNAK = new Packet(pcktTwo.getSeqNum(), pcktTwo.getSrcPortNum(), client.getPort(), NAK);
                    client.sendCommand(pcktNAK.makePacket());
                }
            } else {
                pcktACK = new Packet(pcktOne.getSeqNum(), pcktOne.getSrcPortNum(), client.getPort(), ACK);
                client.sendCommand(pcktACK.makePacket());
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ReceiverRunner();

    }

}
