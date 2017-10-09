/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255hw2.Receiver;

import cs255hw2.shared.*;

/**
 *
 * @author nicholas.bohm
 */
public class ReceiverRewrite {

    public ReceiverRewrite() {
        String ip = "10.15.1.21";
        int srcPort = 4467;
        int desPort = 4466;
        String fileName = "test.txt";
        String directory = "..";
        int payLoadSize = 1004;
        PacketRewrite current;
        PacketRewrite prev;
        PacketRewrite ACK;
        int seqNum = 0;
        boolean run = true;
        ClientRewrite client = new ClientRewrite(ip, srcPort);

        DataHandlerRewrite dataHandler = new DataHandlerRewrite();
        dataHandler.setDirectory(directory);
        while (true) {
            current = new PacketRewrite(client.receivePckt());
            System.out.println("Packet: " + seqNum + " CheckSum: " + dataHandler.byteToInt(current.getCheckSum()) + " srcPort: " + dataHandler.byteToInt(current.getSrcPort())
                    + " desPort: " + dataHandler.byteToInt(current.getDesPort()) + " datalength: " + dataHandler.byteToInt(current.getDataLength()));
            if (current.notCorrupt()) {
                dataHandler.ByteToFile(current.getPayload());
                ACK = new PacketRewrite(seqNum, 0, srcPort, desPort, dataHandler.intToByte(1, 4));
                System.out.println("CheckSum for Ack " + ACK.getSeqNum() + " :" + ACK.getCheckSum());

                System.out.println("Ack: " + ACK.getPayload() + " : " + ACK);
                client.sendPacket(ACK.makePckt());
                ACK = null;
                if (seqNum == 1) {
                    seqNum = 0;
                } else {
                    seqNum++;
                }
                prev = current;
                current = null;
                //if courrupt sends a pckt back that is not a ack
            } else {
                ACK = new PacketRewrite(seqNum, 0, srcPort, desPort, dataHandler.intToByte(1, 0));
                System.out.println("Sent NAK for Packt: " + current.getSeqNum());
                client.sendPacket(ACK.makePckt());
                ACK = null;
            }

            ACK = new PacketRewrite(seqNum, 0, srcPort, desPort, dataHandler.intToByte(1, 4));

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new ReceiverRewrite();

    }
}
