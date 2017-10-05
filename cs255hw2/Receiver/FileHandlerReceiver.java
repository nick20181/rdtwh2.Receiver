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

    public FileHandlerReceiver() {

    }

    public void construct(byte[][] pckt) {
        int counter = 0;
        File downloadFolder = new File("..");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(downloadFolder + "\\" + "testing.txt");
            for (int i = 0; i != pckt.length; i++) {
                counter = pckt[i].length + counter;
            }
            byte[] target = new byte[counter];
            ByteBuffer buffer = ByteBuffer.allocate(counter);
            for (int i = 0; i != pckt.length; i++) {
                buffer.put(pckt[i]);
            }
            buffer.flip();
            buffer.get(target);
            fos.write(target);
        } catch (FileNotFoundException ex) {
            System.out.println("error");
            ex.printStackTrace();
        } catch (IOException io) {
            System.out.println("error2");
        }

    }

}
