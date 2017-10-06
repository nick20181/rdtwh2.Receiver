package cs255hw2.Receiver;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class FileHandlerReceiver {

    private File directory = new File("..");
    private FileOutputStream fos;
    private DataOutputStream dos;

    public FileHandlerReceiver() {

        try {
            this.fos = new FileOutputStream(this.directory + "\\Received.data");
            this.dos = new DataOutputStream(this.fos);
        } catch (FileNotFoundException ex) {
            System.out.println("error");
        }
    }

    public String getDirectoryPath() {
        return this.directory.getAbsolutePath();
    }

    public void construct(byte[] pckt) {

        try {
            this.dos.write(pckt);
            
        } catch (FileNotFoundException ex) {
            System.out.println("error");
            ex.printStackTrace();
        } catch (IOException io) {
            System.out.println("error2");
        }

    }

}
