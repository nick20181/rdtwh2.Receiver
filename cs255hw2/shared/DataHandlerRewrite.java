/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255hw2.shared;

import java.io.DataOutputStream;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicholas.bohm
 */
public class DataHandlerRewrite {

    private File directory;
    private FileOutputStream fos;
    private FileInputStream fis;
    private ByteArrayOutputStream baos;

    //this.fos = new FileOutputStream(this.directory + fileName);
    public DataHandlerRewrite() {

    }
    
    public void ByteToFile(byte[] data){
        try {
            this.fos = new FileOutputStream(this.directory + "\\retrived.data");
            this.baos = new ByteArrayOutputStream();
            this.baos.write(data);
            this.baos.writeTo(fos);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] FileToByte(String fileName, int size) {
        int counter = 0;
        byte[][] store = new byte[size][1];
        try {
            this.fis = new FileInputStream(this.directory + "\\" + fileName);
            for (int i = 0; i < store.length; i++) {
                if (this.fis.read(store[i], 0, 1) != -1) {
                    counter++;
                }
            }
            byte[] toReturn = new byte[counter];
            for (int i = 0; i < counter; i++) {
                toReturn[i] = store[i][0];
            }
            System.out.println(" ");
            return toReturn;
        } catch (FileNotFoundException ex) {
            System.out.println("error in FileToByte - ex");
        } catch (IOException io) {
            System.out.println("error in FileToByte - io");
        }
        return null;
    }

    public int byteToInt(byte[] convert) {
        ByteBuffer buffer = ByteBuffer.allocate(convert.length);
        for (int i = 0; i != convert.length; i++) {
            buffer.put(convert[i]);
        }
        buffer.flip();
        return buffer.getInt();
    }

    public byte[] intToByte(int convert, int toAllocate) {
        byte[] dst = new byte[toAllocate];
        ByteBuffer buffer = ByteBuffer.allocate(toAllocate);
        buffer.putInt(convert);
        buffer.flip();
        buffer.get(dst);
        return dst;
    }

    public void setDirectory(String path) {
        this.directory = new File(path);
    }
}
