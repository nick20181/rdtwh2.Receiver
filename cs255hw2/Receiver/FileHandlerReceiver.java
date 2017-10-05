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
        int fileLength = 0;
        File downloadFolder = new File("..");
        System.out.println(downloadFolder.getAbsolutePath());
        
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(downloadFolder + "\\" + "testing.txt");
            
            for (int i = 0; i != pckt.length; i++) {
                fileLength = pckt[i].length + fileLength;
            }
            byte[] target = new byte[fileLength];
            ByteBuffer buffer = ByteBuffer.allocate(fileLength);
            for (int i = 0; i != pckt.length; i++) {
                System.out.println(i);
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
