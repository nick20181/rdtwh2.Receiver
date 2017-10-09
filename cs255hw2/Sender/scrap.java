/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255hw2.Sender;

import cs255hw2.shared.ClientRewrite;
import cs255hw2.shared.DataHandlerRewrite;
import cs255hw2.shared.PacketRewrite;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author nicholas.bohm
 */
public class scrap {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255hw2.Sender;

import cs255hw2.shared.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class SenderRewrite {

    private ActionListener taskPerformer;
    private PacketRewrite current;
    private ClientRewrite client;
    private Timer timeout;
    private PacketRewrite ACK;
    private String ip;
    private int srcPort;
    private int desPort;
    private DataHandlerRewrite dataHandler;
    private int seqNum;
    private PacketRewrite prev;
    private byte[] currentPayload;

    public SenderRewrite() {
        this.taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                timeout.stop();
                System.out.println("Timeout");
                client.sendPacket(current.makePckt());
                timeout.start();
                waitForAck();

            }
        };
        this.timeout = new Timer(30, this.taskPerformer);
        this.ip = "10.15.1.21";
        this.srcPort = 4466;
        this.desPort = 4467;
        String fileName = "test.txt";
        String directory = "C:\\Users\\TEMP\\Desktop\\tosend";
        int payLoadSize = 1004;
        PacketRewrite prev;
        PacketRewrite ACK;
        this.seqNum = 0;

        this.client = new ClientRewrite(ip, srcPort);

        this.dataHandler = new DataHandlerRewrite(directory, fileName);
        //fix filesize
        int fileSize = (payLoadSize / (int) this.dataHandler.getFileToSend(fileName).length()) + 1;
        for (int i = 0; i <= 13; i++) {
            this.currentPayload = this.dataHandler.FileToByte(fileName, payLoadSize);
            this.current = new PacketRewrite(this.seqNum, 0, this.srcPort, this.desPort, this.currentPayload);
            System.out.println("Packet: " + seqNum + " CheckSum: " + dataHandler.byteToInt(current.getCheckSum()) + " srcPort: " + dataHandler.byteToInt(current.getSrcPort())
                    + " desPort: " + dataHandler.byteToInt(current.getDesPort()) + " datalength: " + dataHandler.byteToInt(current.getDataLength()));
            this.client.sendPacket(this.current.makePckt());
            System.out.println("");
            System.out.println("Waiting for Ack");
            System.out.println("");
            this.timeout.start();
            waitForAck();
        }
    }

    public void waitForAck() {
        boolean run = true;
        while (run) {
            //timeout
            this.ACK = new PacketRewrite(this.client.receivePckt());
            System.out.println("Ack Checksum: " + this.ACK.getCheckSum() + "SystemCheckSum: " + this.ACK.notCorrupt());
            if (this.ACK.notCorrupt() && this.dataHandler.byteToInt(this.ACK.getPayload()) == 1) {
                System.out.println("recived Ack: " + this.ACK.getPayload() + " Saved Ack: " + this.ACK);
                //iftimeout
                System.out.println("Ack recived!.");
                if (this.seqNum == 1) {
                    this.seqNum = 0;
                } else {
                    this.seqNum++;
                }

                run = false;
                this.prev = this.current;
                this.current = null;

            } else {
                System.out.println("Nak Recived!");
                this.client.sendPacket(current.makePckt());
            }
        }
        run = true;
        this.currentPayload = null;

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new SenderRewrite();
    }
}

}
