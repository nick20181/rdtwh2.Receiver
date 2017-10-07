package cs255hw2.Sender;

import java.io.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 * @author Dakota Vanwormer 
 * 
 */
public class FileHandler {

    private File directory;
    private FileOutputStream fos;
    private DataOutputStream dos;

    /**
     * recives file handler constructor
     *
     * @param pathFile
     */
    public FileHandler(String pathFile) {
        this.directory = new File(pathFile);
        try {
            this.fos = new FileOutputStream(this.directory + "\\Received.data");
            this.dos = new DataOutputStream(this.fos);
        } catch (FileNotFoundException ex) {
            System.out.println("error");
        }
    }

    /**
     * Gets the directory path.
     *
     * @return
     */
    public String getDirectoryPath() {
        return this.directory.getAbsolutePath();
    }

    /**
     * Constructs the file from the pckts given.
     *
     * @param pckt
     */
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

    /**
     * This constructor is for the sender.
     */
    public FileHandler() {

    }

    /**
     * turns a file to a byte array.
     *
     * @param file
     * @return
     */
    private byte[] fileTobyte(File file) {

        byte[] toSend = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(toSend);
            fis.close();
            return toSend;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e1) {
            return null;
        }
    }

    /**
     * breaks up a file into several 1004 byte payloads
     *
     * @param File
     * @return
     */
    public byte[][] packetPayloadAssembler(File File) {
        byte[] FileContents = fileTobyte(File);

        int current = 0;
        int numberOfPckt;
        byte[][] toReturn;
        byte[] filler;
        //determines how many packets there will be
        if (FileContents.length % 1004 > 0) {
            numberOfPckt = (FileContents.length / 1004) + 1;
        } else {
            numberOfPckt = (FileContents.length / 1004);
        }
        //creates the return array
        toReturn = new byte[numberOfPckt][];
        //fills the return array
        for (int i = 0; i != numberOfPckt; i++) {
            if (current + 1004 > FileContents.length) {
                int counter = 0;
                filler = new byte[(FileContents.length - current)];
                while (current != (FileContents.length)) {
                    filler[counter] = FileContents[current];
                    counter++;
                    toReturn[i] = filler;
                    current++;
                }

            } else {
                int counter = 0;
                filler = new byte[1004];
                while (current != (1003 * (i + 1))) {
                    filler[counter] = FileContents[current];
                    counter++;
                    toReturn[i] = filler;
                    current++;
                }
            }
        }
        return toReturn;
    }

}
