/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255hw2.shared;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

/**
 *
 * @author nicholas.bohm
 */
public class PacketRewrite {

    private DataHandlerRewrite dataHandler = new DataHandlerRewrite();
    private byte[] seqNum = new byte[4];
    private byte[] checkSum = new byte[4];
    private byte[] srcPortNum = new byte[4];
    private byte[] desPortNum = new byte[4];
    private byte[] dataLength = new byte[4];
    private byte[] payload = new byte[1004];

    public PacketRewrite(int seqNum, int srcPort, int desPort, int checkSum, byte[] payLoad) {
        fillHeader(this.seqNum, seqNum);
        fillHeader(this.srcPortNum, srcPort);
        fillHeader(this.desPortNum, desPort);
        fillHeader(this.checkSum, checkSum);
        fillHeader(this.dataLength, payLoad.length);
        if (payLoad.length == 1004) {
            this.payload = payLoad;
        } else {
            this.payload = paddPayLoad(payLoad);
        }
        fillHeader(this.checkSum, this.createChecksum());

    }

    public PacketRewrite(byte[] pckt) {
        breakPacket(pckt);
        this.payload = new byte[this.dataHandler.byteToInt(getDataLength())];
        extractPayload(pckt);
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
            buffer.get(this.srcPortNum);
            buffer.clear();
            
        }
        if (currentByte <= 8) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
            buffer.get(this.desPortNum);
            buffer.clear();
            
        }
        if (currentByte <= 12) {
            for (int i = 0; i != 4; i++) {
                buffer.put(pcktContents[currentByte]);
                currentByte++;
            }
            buffer.flip();
            buffer.get(this.checkSum);
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
        ByteBuffer buffer = ByteBuffer.allocate(this.dataHandler.byteToInt(getDataLength()));
        int start = 20;
        for (int i = 0; i != dataHandler.byteToInt(getDataLength()); i++) {

            buffer.put(pcktContents[start]);
            start++;
        }
        buffer.flip();
        buffer.get(this.payload);
    }

    public byte[] makePckt() {
        byte[] toReturn = new byte[1024];
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(this.seqNum);
        buffer.put(this.srcPortNum);
        buffer.put(this.desPortNum);
        buffer.put(this.checkSum);
        buffer.put(this.dataLength);
        buffer.put(this.payload);
        buffer.flip();
        buffer.get(toReturn);
        return toReturn;
    }

    private int createChecksum() {
        CRC32 crc32 = new CRC32();
        crc32.update(this.makePckt());
        return (int) crc32.getValue();

    }

    public byte[] paddPayLoad(byte[] beforePadd) {
        ByteBuffer buffer = ByteBuffer.allocate(1004);
        int counter = 0;
        int toAdd = (1004 - beforePadd.length);

        byte[] toReturn = new byte[1004];
        byte padding = 0;

        buffer.put(beforePadd);
        for (int i = 0; i != (toAdd); i++) {
            buffer.put(padding);
            counter++;
        }
        buffer.flip();
        buffer.get(toReturn);
        return toReturn;
    }

    public void fillHeader(byte[] target, int dataToConvert) {
        if (target == this.seqNum) {
            this.seqNum = dataHandler.intToByte(dataToConvert, 4);
        } else if (target == this.srcPortNum) {
            this.srcPortNum = dataHandler.intToByte(dataToConvert, 4);
        } else if (target == this.desPortNum) {
            this.desPortNum = dataHandler.intToByte(dataToConvert, 4);
        } else if (target == this.checkSum) {
            this.checkSum = dataHandler.intToByte(dataToConvert, 4);
        } else if (target == this.dataLength) {
            this.dataLength = dataHandler.intToByte(dataToConvert, 4);
        } else {
            this.payload = dataHandler.intToByte(dataToConvert, 1004);
        }
    }

    public byte[] getSeqNum() {
        return this.seqNum;
    }

    public byte[] getSrcPort() {
        return this.srcPortNum;
    }

    public byte[] getDesPort() {
        return this.desPortNum;
    }

    public byte[] getCheckSum() {
        return this.checkSum;
    }

    public byte[] getDataLength() {
        return this.dataLength;
    }

    public byte[] getPayload() {
        return this.payload;
    }
}
