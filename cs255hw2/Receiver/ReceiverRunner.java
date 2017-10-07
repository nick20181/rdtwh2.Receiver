package cs255hw2.Receiver;

/**
 * @author Nicholas Bohm
 * @author Dakota Vanwormer
 */
public class ReceiverRunner {

    public ReceiverRunner() {
        int port = 4467;
        boolean run = true;
        byte[] ACK = new byte[4];
        for (int i = 0; i != 4; i++) {
            ACK[i] = 1;
        }
        byte[] NAK = new byte[4];
        for (int i = 0; i != 4; i++) {
            NAK[i] = 0;
        }
        FileHandler dataHandler = new FileHandler();
        ReceiverClient client = new ReceiverClient();
        System.out.println("The Data Downloaded to: " + dataHandler.getDirectoryPath());
        Packet prevPckt = null;
        Packet currentPckt;
        Packet pcktACK;
        //does not have nak info but name is used becuase lazy naming
        Packet pcktNAK;

        int currentSeq = 0;
        while (true) {

            currentPckt = new Packet(client.getPckt());
            //checks to see if the packet is the current seqNum the runner is on.
//            if (currentPckt.getSeqNum() == currentSeq) {
//                //checks to see if the packt is corrupted.
                if (currentPckt.notCorrupt()) {
                    dataHandler.construct(currentPckt.getPayload());
                    pcktACK = new Packet(currentPckt.getSeqNum(), port, currentPckt.getSrcPortNum(), ACK);
                    System.out.println("CheckSum for Ack " + pcktACK.getSeqNum() + " :" + pcktACK.getCheckSum());

                    System.out.println("Ack: " + pcktACK.getPayload() + " : " + ACK);
                    client.sendCommand(pcktACK.makePacket());
                    pcktACK = null;
                    if (currentSeq == 1) {
                        currentSeq = 0;
                    } else {
                        currentSeq++;
                    }
                    prevPckt = currentPckt;
                    currentPckt = null;
                    //if courrupt sends a pckt back that is not a ack
                } else {
                    pcktNAK = new Packet(currentPckt.getSeqNum(), port, currentPckt.getSrcPortNum(), NAK);
                    System.out.println("Sent NAK for Packt: " + currentPckt.getSeqNum());
                    client.sendCommand(pcktNAK.makePacket());
                    pcktNAK = null;
                }
                //sends the prevs pckts ack if recvied differnt seq nums
//            } else {
//                pcktACK = new Packet(prevPckt.getSeqNum(), port, currentPckt.getSrcPortNum(), ACK);
//
//                System.out.println("CheckSum for Ack " + pcktACK.getSeqNum() + " :" + pcktACK.getCheckSum());
//                client.sendCommand(pcktACK.makePacket());
//                pcktACK = null;
//            }
            if (currentSeq == 1) {
                currentSeq = 0;
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
