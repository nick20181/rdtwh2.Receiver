package cs255hw2.Sender;

import java.io.*;

import java.net.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 * @author Dakota Vanwormer
 */
public class Client {

    private Socket currentSocket;
    private DataInputStream incoming;

    public Client() {
        try {
            this.currentSocket = new Socket("10.15.1.21", 4466);
            InputStreamReader ir = new InputStreamReader(this.currentSocket.getInputStream());
        } catch (UnknownHostException e) {
            System.out.println("Error Unkown Host Please check your ip and port!");
        } catch (IOException ex) {
            System.out.println("Error Unkown Host Please check your ip and port!");
        }
    }

    /**
     * sends data to the output stream of a socket.
     *
     * @param pckt
     */
    public void sendCommand(byte[] pckt) {
        try {
            this.currentSocket.getOutputStream().write(pckt);
        } catch (IOException ex) {
        }
    }

    /**
     * gets data from the input stream of the socket
     *
     * @return
     */
    public byte[] getPckt() {
        byte[] store = null;
        try {
            store = new byte[1024];
            this.incoming.read(store, 0, 1024);
        } catch (IOException ex) {
        }
        return store;
    }

    /**
     * closes the connection of the socket.
     */
    public void closeSocket() {
        try {
            this.currentSocket.close();
        } catch (IOException ex) {
        }
    }

    /**
     * retrieves the current socket
     *
     * @return
     */
    public Socket retrieveSocket() {
        return this.currentSocket;
    }

    /**
     * gets the port of the current socket
     *
     * @return
     */
    public int getPort() {
        return this.currentSocket.getPort();
    }

}
