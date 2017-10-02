package cs255hw2;

import java.util.zip.*;
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
    private byte[] payload = new byte[1004];
    
    public Packet(int seqNum, int srcPortNum, int desPortNum, int dataLength, byte[] payload){
        CRC32 checkSum = new CRC32();
        checkSum.update(payload);
        
        fillPacket(seqNum, this.seqNum);
        fillPacket( 0, this.checksum);
        fillPacket(srcPortNum, this.srcPortNum);
        fillPacket(desPortNum, this.desPortNum);
        fillPacket(dataLength, this.dataLength);
        this.payload = payload;
        
    }
    
    public void fillPacket(int toFill, byte[] target){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        //converts the int and puts it into the buffer
        buffer.putInt(toFill);
        buffer.flip();
        //puts the buffer and puts it into the target array
        buffer.get(target);
        buffer.flip();
    }
}