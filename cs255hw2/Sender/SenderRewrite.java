/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255hw2.Sender;

import cs255hw2.shared.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class SenderRewrite {

    public SenderRewrite() {
        String ip = "10.15.1.21";
        int srcPort = 4466;
        int desPort = 4467;
        String fileName = "test.txt";
        String directory = "C:\\Users\\TEMP\\Desktop\\tosend";
        int payLoadSize = 1004;
        PacketRewrite current;
        PacketRewrite prev;
        PacketRewrite ACK;
        int seqNum = 0;
        boolean run = true;
        ClientRewrite client = new ClientRewrite(ip, srcPort);

        DataHandlerRewrite dataHandler = new DataHandlerRewrite(directory, fileName);
        int fileSize = (payLoadSize / (int) dataHandler.getFileToSend(fileName).length()) + 1;
        for (int i = 0; i <= 13; i++) {
            byte[] currentPayload = dataHandler.FileToByte(fileName, payLoadSize);
            current = new PacketRewrite(seqNum, 0, srcPort, desPort, currentPayload);
            System.out.println("Packet: " + seqNum + " CheckSum: " + dataHandler.byteToInt(current.getCheckSum()) + " srcPort: " + dataHandler.byteToInt(current.getSrcPort())
                    + " desPort: " + dataHandler.byteToInt(current.getDesPort()) + " datalength: " + dataHandler.byteToInt(current.getDataLength()));
            client.sendPacket(current.makePckt());
            while (run) {
                ACK = new PacketRewrite(client.receivePckt());
                System.out.println("Ack Checksum: " + ACK.getCheckSum() + "SystemCheckSum: " + ACK.notCorrupt());
                if (ACK.notCorrupt()) {
                    System.out.println("recived Ack: " + ACK.getPayload() + " Saved Ack: " + ACK);
                    //iftimeout
                    System.out.println("Ack recived!.");
                    if (seqNum == 1) {
                        seqNum = 0;
                    } else {
                        seqNum++;
                    }

                    run = false;
                    prev = current;
                    current = null;
                } else {
                    System.out.println("Nak Recived!");
                    client.sendPacket(current.makePckt());
                }
            }
            run = true;
            currentPayload = null;
            
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SenderRewrite();

    }
}
