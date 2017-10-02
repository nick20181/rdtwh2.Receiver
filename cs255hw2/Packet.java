/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255hw2;

import java.util.zip.Adler32;
import java.nio.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class Packet {
    private byte[] seqNum;
    private byte[] checksum;
    private byte[] srcPortNum;
    private byte[] desPortNum;
    private byte[] dataLength;
    private byte[] payload;
    
    public Packet(byte[] seqNum, byte[] checksum, byte[] srcPortNum, byte[] desPortNum, byte[] dataLength, byte[] payload){
        Adler32 checkSum = new Adler32();
           checkSum.update(payload);
           
        this.seqNum = seqNum;
        //this.checksum = ;
        this.desPortNum = desPortNum;
        this.payload = payload;
        this.dataLength = dataLength;
        this.srcPortNum = srcPortNum;
    }
}
