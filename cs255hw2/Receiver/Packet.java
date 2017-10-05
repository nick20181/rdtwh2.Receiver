package cs255hw2.Receiver;

import java.util.zip.*;
import java.nio.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class Packet {

    private CRC32 finalCheckSum = new CRC32();
    private int seqNum;
    private int checksum;
    private int srcPortNum;
    private int desPortNum;
    private int dataLength;
    private byte[] payload;
    
    /**
     * Makes a packet from the byte array given.
     * @param pcktContents 
     */
    public Packet(byte[] pcktContents) {
        breakPacket(pcktContents, this.seqNum);
        breakPacket(pcktContents, this.desPortNum);
        breakPacket(pcktContents, this.checksum);
        breakPacket(pcktContents, this.srcPortNum);
        breakPacket(pcktContents, this.dataLength);
        extractPayload(pcktContents);
    }
    
    public byte[] getPayload(){
        return this.payload;
    }
    
    public int getSeqNum(){
        return this.seqNum;
    }
    
    public int getCheckSum(){
        return this.checksum;
    }
    
    public int getDesPortNum(){
        return this.desPortNum;
    }
    
    public int getDataLength(){
        return this.dataLength;
    }
    
    public int getSrcPortNum(){
        return this.srcPortNum;
    }
    /**
     * extracts packets payload
     * @param pcktContents
     * @return 
     */
    public void extractPayload(byte[] pcktContents){
        ByteBuffer buffer = ByteBuffer.allocate(this.dataLength);
        int start = 21;
        for(int i = 0; i != dataLength;i++){
            buffer.put(pcktContents[start]);
            start++;
        }
        buffer.flip();
        buffer.get(this.payload);
    }
    /**
     * breaks a pakets header apart.
     * @param pcktContents
     * @param target 
     */
    public void breakPacket(byte[] pcktContents, int target) {
        
        int start;
        if (target == this.seqNum) {
            start = 0;
        } else if (target == this.desPortNum) {
            start = 4;
        } else if (target == this.checksum) {
            start = 8;
        } else if (target == this.srcPortNum) {
            start = 12;
        } else {
            start = 16;
        } 

        
        ByteBuffer buffer = ByteBuffer.allocate(4);
        //might break here
        int a = start;
        while(start != (a+4)){
            buffer.put(pcktContents[start]);
            start++;
        }
        buffer.flip();

        if (target == this.seqNum) {
            this.seqNum = buffer.getInt();
        } else if (target == this.desPortNum) {
            this.desPortNum = buffer.getInt();
        } else if (target == this.checksum) {
            this.checksum = buffer.getInt();
        } else if (target == this.srcPortNum) {
            this.srcPortNum = buffer.getInt();
        } else {
            this.dataLength = buffer.getInt();
        } 
    }

}
