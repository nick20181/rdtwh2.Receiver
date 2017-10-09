package cs255hw2.Receiver;

import cs255hw2.shared.*;
/**
 * @author Nicholas Bohm
 * @author Dakota Vanwormer
 */
public class ReceiverRunner {
    public ReceiverRunner() {
        //Varibles
        int port = 4467;
        boolean run = true;
        byte[] ACK = new byte[4];
        Packet prevPckt = null;
        Packet currentPckt;
        Packet pcktACK;
        //does not have nak info but name is used becuase lazy naming
        Packet pcktNAK;
        //Initlization of the byte array of the Ack.
        for (int i = 0; i != 4; i++) {
            ACK[i] = 1;
        }
//        byte[] NAK = new byte[4];
//        for (int i = 0; i != 4; i++) {
//            NAK[i] = 0;
//        }
        // Creation of needed Objects
        DataHandler dataHandler = new DataHandler("C:\\Users\\nicholas.bohm\\Desktop\\New folder");
        Client client = new Client();
        System.out.println("The Data Downloaded to: " + dataHandler.getDirectoryPath());
        
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
                    pcktNAK = new Packet(currentPckt.getSeqNum(), port, currentPckt.getSrcPortNum());
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
    
    public ReceiverRunner(boolean t) {
        String ip = "10.15.1.21";
        int srcPort = 4467;
        String fileName = "testing.txt";
        int payLoadSize = 1004;
        
        ClientRewrite client = new ClientRewrite(ip, srcPort);
        
        DataHandlerRewrite dataHandler = new DataHandlerRewrite();
        dataHandler.setDirectory("C:\\Users\\nicholas.bohm\\Desktop\\retrived");
        
        PacketRewrite packet = new PacketRewrite(client.receivePckt());
        System.out.println("seqNum: " + dataHandler.byteToInt(packet.getSeqNum()));
        System.out.println("srcPort: " + dataHandler.byteToInt(packet.getSrcPort()));
        System.out.println("desPort: " + dataHandler.byteToInt(packet.getDesPort()));
        System.out.println("checkSum: " + dataHandler.byteToInt(packet.getCheckSum()));
        System.out.println("dataLength: " + dataHandler.byteToInt(packet.getDataLength()));
        
        dataHandler.ByteToFile(packet.getPayload());
        

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ReceiverRunner(true);

    }

}
