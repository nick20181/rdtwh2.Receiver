package cs255hw2.Receiver;

import cs255hw2.shared.*;

/**
 *
 * @author Nicholas Bohm
 * @author Dakota Vanwormer
 */
public class Receiver {

    public Receiver() {
        String ip = "10.15.1.21";
        int srcPort = 4467;
        int desPort = 4466;
        String directory = "..";
        System.out.println("Receiver Start on Ip: " + ip + " On Port: " +
                srcPort + " downloads the data to the project directory.");
        Packet ACK;
        int seqNum = 0;
        boolean run = true;
        Client client = new Client(ip, srcPort);
        DataHandler dataHandler = new DataHandler(directory);
        Packet prev = new Packet(2, 0, srcPort, desPort, 
                dataHandler.intToByte(0, 4));
        Packet current = new Packet(0, 0, srcPort, desPort, 
                dataHandler.intToByte(0, 4));
        byte[] recived;
        while (true) {
            recived = null;
            recived = client.receivePckt();
            //checks to see if the recived packet is courrpt.
            if (current.notCorrupt(recived)) {
                current = null;
                current = new Packet(recived);
                //checks to see if the seq numbers are the correct numbers
                if (seqNum == dataHandler.byteToInt(current.getSeqNum())) {
                    dataHandler.ByteToFile(current.getPayload());
                    ACK = new Packet(seqNum, 0, srcPort, desPort,
                            dataHandler.intToByte(1, 4));
                    client.sendPacket(ACK.makePckt());
                    ACK = null;
                    if (seqNum == 1) {
                        seqNum = 0;
                    } else {
                        seqNum++;
                    }
                    prev = current;
                } else {
                    ACK = new Packet(dataHandler.byteToInt(
                            prev.getSeqNum()), 0, srcPort, desPort, 
                            dataHandler.intToByte(1, 4));
                    client.sendPacket(ACK.makePckt());
                }
            } else {
                ACK = new Packet(seqNum, 0, srcPort, desPort, 
                        dataHandler.intToByte(0, 4));
                client.sendPacket(ACK.makePckt());
                ACK = null;
            }
            ACK = new Packet(seqNum, 0, srcPort, desPort, 
                    dataHandler.intToByte(1, 4));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Receiver();

    }
}
