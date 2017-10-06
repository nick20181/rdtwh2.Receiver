package cs255hw2.Sender;

import java.io.*;

import java.net.*;

/**
 *
 * @author admin.dakota
 */
public class SenderClient {

    private Socket currentSocket;
    private DataInputStream incoming;

    public SenderClient() {
        try {
            this.currentSocket = new Socket("10.15.1.21", 4466);
            this.incoming = new DataInputStream(this.currentSocket.getInputStream());
            InputStreamReader ir = new InputStreamReader(this.currentSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.out.println("SH UHE");
        } catch (IOException ex) {
            System.out.println("SH IOE");
        }
    }

    public void sendCommand(byte[] pckt) {
        try {
            this.currentSocket.getOutputStream().write(pckt);
        } catch (IOException ex) {
            System.out.println("SH IOE");
        }
    }

    public byte[] getPckt() {
        byte[] store = null;
        try {
            store = new byte[1024];
            this.incoming.read(store, 0, 1024);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return store;
    }

    public void closeSocket() {
        try {
            this.currentSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public Socket retrieveSocket(){
        return this.currentSocket;
    }

    public int getPort() {
        return this.currentSocket.getPort();
    }

}
