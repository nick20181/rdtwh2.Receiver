package cs255hw2.shared;

import java.util.zip.*;
import java.nio.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 *
 */
public class Packet {

    private CRC32 finalCheckSum = new CRC32();
    private byte[] seqNum = new byte[4];
    private byte[] checksum = new byte[4];
    private byte[] srcPortNum = new byte[4];
    private byte[] desPortNum = new byte[4];
    private byte[] dataLength = new byte[4];
    private byte[] payload = new byte[1004];

    /**
     * Constructs a packet out of manually inputted varaibles
     *
     * @param seqNum
     * @param srcPortNum
     * @param desPortNum
     * @param payload
     */
    public Packet(int seqNum, int srcPortNum, int desPortNum, byte[] payload) {
        this.seqNum = fillPacket(seqNum);
        this.checksum = fillPacket(0);
        this.srcPortNum = fillPacket(srcPortNum);
        this.desPortNum = fillPacket(desPortNum);
        this.dataLength = fillPacket(payload.length);
        this.payload = payload;
        //creates the checksum of the completed packet
        this.finalCheckSum.reset();
        this.finalCheckSum.update(this.makePacket());
        this.checksum = fillPacket((int) this.finalCheckSum.getValue());
        this.finalCheckSum.reset();
    }

    /**
     * Constructs a packet that has no payload
     *
     * @param seqNum
     * @param srcPortNum
     * @param desPortNum
     * @param payload
     */
    public Packet(int seqNum, int srcPortNum, int desPortNum) {
        this.seqNum = fillPacket(seqNum);
        this.checksum = fillPacket(0);
        this.srcPortNum = fillPacket(srcPortNum);
        this.desPortNum = fillPacket(desPortNum);
        this.dataLength = fillPacket(0);
        this.payload[0] = (byte) 0;
        //creates the checksum of the completed packet
        this.finalCheckSum.reset();
        this.finalCheckSum.update(this.makePacket());
        this.checksum = fillPacket((int) this.finalCheckSum.getValue());
        this.finalCheckSum.reset();
    }

    /**
     * Returns true if the file is not corrupt
     *
     * @return
     */
    public boolean notCorrupt() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.checksum);
        buffer.flip();
        int testCheck = buffer.getInt();
        System.out.println("Checksum Sender: " + testCheck + " ReceiverChecksum :" + this.makeCheckRecieved());
        if (testCheck == this.makeCheckRecieved()) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * makes a check sum from a packet with a checksum of zero in the packet.
     *
     * @return
     */
    public int makeCheckRecieved() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //loads all fields into one byte array
        buffer.put(this.seqNum);
        buffer.put(this.desPortNum);
        buffer.putInt(0);
        buffer.put(this.srcPortNum);
        buffer.put(this.dataLength);
        if (this.payload.length != 1004) {
            buffer.put(paddPayload());
        } else {
            buffer.put(this.payload);
        }

        buffer.flip();
        //gets the packet out of the buffer
        this.finalCheckSum.reset();
        this.finalCheckSum.update(buffer);
        return (int) this.finalCheckSum.getValue();
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
     * breaks a packets header apart from raw packet.
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

    /**
     * extracts packets payload from raw recived packet
     *
     * @param pcktContents
     * @return
     */
    public void extractPayload(byte[] pcktContents) {
        ByteBuffer buffer = ByteBuffer.allocate(this.getDataLength());
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

    /**
     * gets the payload of the packet
     *
     * @return
     */
    public byte[] getPayload() {
        return this.payload;
    }

    /**
     * gets the sequence number of the packet
     *
     * @return
     */
    public int getSeqNum() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.seqNum);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * gets the checksum that is saved in the packet
     *
     * @return
     */
    public int getCheckSum() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.checksum);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * gets the destenation port number of the packet
     *
     * @return
     */
    public int getDesPortNum() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.desPortNum);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * gets the data length of the payload
     *
     * @return
     */
    public int getDataLength() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.dataLength);
        buffer.flip();
        return buffer.getInt();
    }

    /**
     * gets the source port of the packet
     *
     * @return
     */
    public int getSrcPortNum() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(this.srcPortNum);
        buffer.flip();
        return buffer.getInt();
    }
}
