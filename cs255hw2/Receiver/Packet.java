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
    private byte[] payload = new byte[1004];

    public Packet(byte[] pcktContents) {
        breakPacket(pcktContents, this.seqNum);
        breakPacket(pcktContents, this.desPortNum);
        breakPacket(pcktContents, this.checksum);
        breakPacket(pcktContents, this.srcPortNum);
        breakPacket(pcktContents, this.dataLength);
    }

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
        while(start!= (start+4)){
            buffer.put(pcktContents[start]);
            start++;
        }
        buffer.flip();
        target = buffer.getInt();
    }

}
