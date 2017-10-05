package cs255hw2.Receiver;

import java.util.zip.*;
import java.nio.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class PacketReceiver {

    private CRC32 finalCheckSum = new CRC32();
    private int seqNum;
    private int checksum;
    private int srcPortNum;
    private int desPortNum;
    private int dataLength;
    private byte[] payload;

    /**
     * Makes a packet from the byte array given from sender.
     *
     * @param pcktContents
     */
    public PacketReceiver(byte[] pcktContents) {
        breakPacket(pcktContents);
        this.payload = new byte[this.dataLength];
        extractPayload(pcktContents);
    }

    /**
     * breaks a pakets header apart from raw pckt.
     *
     * @param pcktContents
     * @param target
     */
    public void breakPacket(byte[] pcktContents) {
        int currentByte = 0;
        ByteBuffer buffer = ByteBuffer.allocate(4);
        if (currentByte >= 0) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                
                currentByte++;
            }
            buffer.flip();
            this.seqNum = buffer.getInt(this.seqNum);
            System.out.println("Current seqNum" + this.seqNum);
            buffer.clear();
            System.out.println(this.seqNum);
        }
        if (currentByte <= 4) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
            this.desPortNum = buffer.getInt(this.desPortNum);
            System.out.println("Current desport" + this.desPortNum);
            buffer.clear();
            System.out.println(this.desPortNum);
        }
        if (currentByte<= 8) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
           this.checksum = buffer.getInt(this.checksum);
            System.out.println("Current check" + this.checksum);
            buffer.clear();
            System.out.println(this.checksum);
        }
        if (currentByte <= 12) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
            this.srcPortNum = buffer.getInt(this.srcPortNum);
            System.out.println("Current srcport" + this.srcPortNum);
            buffer.clear();
            System.out.println(this.srcPortNum);
        }
        if (currentByte <= 16){
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
            this.dataLength = buffer.getInt(this.dataLength);
            System.out.println("Current datal" + this.dataLength);
            buffer.clear();
            System.out.println(this.dataLength);
        }
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public int getSeqNum() {
        return this.seqNum;
    }

    public int getCheckSum() {
        return this.checksum;
    }

    public int getDesPortNum() {
        return this.desPortNum;
    }

    public int getDataLength() {
        return this.dataLength;
    }

    public int getSrcPortNum() {
        return this.srcPortNum;
    }

    /**
     * extracts packets payload from raw recived packet
     *
     * @param pcktContents
     * @return
     */
    public void extractPayload(byte[] pcktContents) {
        ByteBuffer buffer = ByteBuffer.allocate(this.dataLength);
        System.out.println(buffer.capacity());
        int start = 20;
        for (int i = 0; i != dataLength; i++) {
          
            buffer.put(pcktContents[start]);
            start++;
        }
        buffer.flip();
        buffer.get(this.payload);
    }

}
