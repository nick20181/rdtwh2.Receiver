package cs255hw2.Sender;

import cs255hw2.*;
import java.util.zip.*;
import java.nio.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class PacketSender {

    private CRC32 finalCheckSum = new CRC32();
    private byte[] seqNum;
    private byte[] checksum;
    private byte[] srcPortNum;
    private byte[] desPortNum;
    private byte[] dataLength;
    private byte[] payload = new byte[1004];

    public PacketSender(int seqNum, int srcPortNum, int desPortNum, byte[] payload) {
        this.seqNum = fillPacket(seqNum);
        this.checksum = fillPacket(0);
        this.srcPortNum = fillPacket(srcPortNum);
        this.desPortNum = fillPacket(desPortNum);
        this.dataLength = fillPacket(payload.length);
        this.payload = payload;
        //creates the checksum of the completed packet
        this.finalCheckSum.update(makePacket());
        this.checksum = fillPacket((int) this.finalCheckSum.getValue());

    }

    public byte[] makePacket() {
        byte[] toReturn = new byte[1024];
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //loads all fields into one byte array
        buffer.put(this.seqNum);
        buffer.put(this.desPortNum);
        buffer.put(this.checksum);
        buffer.put(this.srcPortNum);
        buffer.put(this.dataLength);
        if(this.payload.length != 1004){
            buffer.put(paddPayload());
        } else {
            buffer.put(this.payload);
        }
        
        buffer.flip();
        //gets the packet out of the buffer
        buffer.get(toReturn);
        //returns the completed packet
        return toReturn;

    }
    
    public byte[] paddPayload(){
        ByteBuffer buffer = ByteBuffer.allocate(1004);
        int counter = 0;
        int toAdd = (1004 - this.payload.length);
        
        byte[] toReturn = new byte[1004];
        byte padding = 0;
        
        buffer.put(this.payload);
        for(int i = 0; i != (toAdd); i++){
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
