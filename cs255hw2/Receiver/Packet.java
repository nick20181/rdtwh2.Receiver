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

    public byte[] paddPayload() {
        ByteBuffer buffer = ByteBuffer.allocate(1004);
        int counter = 0;
        int toAdd = (1004 - this.payload.length);

        byte[] toReturn = new byte[1004];
        byte padding = 0;

        buffer.put(this.payload);
        for (int i = 0; i != (toAdd); i++) {
            buffer.put(padding);
            counter++;
        }
        buffer.flip();
        buffer.get(toReturn);
        return toReturn;
    }

    public byte[] fillPacket(int toFill) {
        byte[] toReturn = new byte[4];
        ByteBuffer buffer = ByteBuffer.allocate(4);
        //converts the int and puts it into the buffer
        buffer.putInt(toFill);
        buffer.flip();
        //puts the buffer and puts it into the target array
        buffer.get(toReturn);
        buffer.flip();

        return toReturn;
    }
}
