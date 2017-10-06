package cs255hw2.Sender;

import cs255hw2.Sender.*;
import java.util.zip.*;
import java.nio.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class Packet {

    private CRC32 finalCheckSum = new CRC32();
    private byte[] seqNum = new byte[4];
    private byte[] checksum = new byte[4];
    private byte[] srcPortNum = new byte[4];
    private byte[] desPortNum = new byte[4];
    private byte[] dataLength = new byte[4];
    private byte[] payload = new byte[1004];

    public Packet(int seqNum, int srcPortNum, int desPortNum, byte[] payload) {
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

    /**
     * Makes a packet from the byte array given from sender.
     *
     * @param pcktContents
     */
    public Packet(byte[] pcktContents) {
        breakPacket(pcktContents);
        this.payload = new byte[getDataLength()];
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
            buffer.get(this.seqNum);
            buffer.clear();
        }
        if (currentByte <= 4) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
            buffer.get(this.desPortNum);
            buffer.clear();
        }
        if (currentByte <= 8) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
            buffer.get(this.checksum);
            buffer.clear();
        }
        if (currentByte <= 12) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
            buffer.get(this.srcPortNum);
            buffer.clear();
        }
        if (currentByte <= 16) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
            buffer.get(this.dataLength);
            buffer.clear();
        }
    }
    
    public boolean notCorrupt() {
        byte[] storeChecksum = this.checksum;
        int receivedChecksum = this.getCheckSum();
        this.checksum = fillPacket(0);
        
        this.finalCheckSum.update(makePacket());
        if (receivedChecksum==this.finalCheckSum.getValue()){
            this.checksum = storeChecksum;
            return true;
        } else {
            this.checksum = storeChecksum;
            return false;
        }
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public int getSeqNum() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.seqNum);
        buffer.flip();
        return buffer.getInt();
    }

    public int getCheckSum() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.checksum);
        buffer.flip();
        return buffer.getInt();
    }

    public int getDesPortNum() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.desPortNum);
        buffer.flip();
        return buffer.getInt();
    }

    public int getDataLength() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.dataLength);
        buffer.flip();
        return buffer.getInt();
    }

    public int getSrcPortNum() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.srcPortNum);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * extracts packets payload from raw recived packet
     *
     * @param pcktContents
     * @return
     */
    public void extractPayload(byte[] pcktContents) {
        ByteBuffer buffer = ByteBuffer.allocate(this.getDataLength());
        System.out.println(buffer.capacity());
        int start = 20;
        for (int i = 0; i != this.getDataLength(); i++) {

            buffer.put(pcktContents[start]);
            start++;
        }
        buffer.flip();
        buffer.get(this.payload);
    }

    /**
     * makes the packet from the info given
     *
     * @return
     */
    public byte[] makePacket() {
        byte[] toReturn = new byte[1024];
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //loads all fields into one byte array
        buffer.put(this.seqNum);
        buffer.put(this.desPortNum);
        buffer.put(this.checksum);
        buffer.put(this.srcPortNum);
        buffer.put(this.dataLength);
        if (this.payload.length != 1004) {
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

    /**
     * pads the payload of the packet if it does not have 1004 bytes
     *
     * @return
     */
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

    /**
     * fills the packet with the reqired info
     *
     * @param toFill
     * @return
     */
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
