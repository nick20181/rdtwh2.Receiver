package cs255hw2.Sender;

import java.io.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class FileHandlerSender {

        public FileHandlerSender() {

        }

        public byte[] fileTobyte(File file) {

            byte[] toSend = new byte[(int) file.length()];
            try {
                FileInputStream fis = new FileInputStream(file);
                fis.read(toSend);
                fis.close();
                return toSend;
            } catch (FileNotFoundException e) {
                System.out.println("File Not Found.");
                e.printStackTrace();
                return null;
            } catch (IOException e1) {
                System.out.println("Error Reading The File.");
                e1.printStackTrace();
                return null;
            }
        }

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
                    while (current != (FileContents.length - 1)) {
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

        public byte[][] packetPayloadAssembler(byte[] FileContents) {
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
                    while (current != (FileContents.length - 1)) {
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
