package cs255hw2.Receiver;

import java.io.*;

import java.net.*;


public class RecieverClient {


    private Socket currentSocket;
    private DataInputStream incoming;
    private PrintStream outGoing;
    private byte[] toReturn;
    

    public RecieverClient() {
        try {
            this.currentSocket = new Socket("10.15.1.21", 4467);
            this.incoming = new DataInputStream(this.currentSocket.getInputStream());
            this.outGoing = new PrintStream(this.currentSocket.getOutputStream());
            InputStreamReader ir = new InputStreamReader(this.currentSocket.getInputStream());
        } catch (UnknownHostException e) {
            // System.out.println("SH UHE");
        } catch (IOException ex) {
            // System.out.println("SH IOE");
        }
        
        byte[] test = new byte[1024];
        try {
            this.incoming.read(test);
        } catch (IOException ex) {
            System.out.println("error");
        }
        toReturn = test;
        
    }
    
    public byte[] getToReturn() {
        return this.toReturn;
    }

    public static void main(String[] args) throws IOException {
        new RecieverClient();
    }
}
