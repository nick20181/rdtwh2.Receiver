
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
    private PrintStream outGoing;
    private BufferedReader bufferInput;

    public SenderClient() {
        try {
            this.currentSocket = new Socket("10.15.1.21", 4466);
            this.incoming = new DataInputStream(this.currentSocket.getInputStream());
            this.outGoing = new PrintStream(this.currentSocket.getOutputStream());
            InputStreamReader ir = new InputStreamReader(this.currentSocket.getInputStream());
            this.bufferInput = new BufferedReader(ir);
        } catch (UnknownHostException e) {
            // System.out.println("SH UHE");
        } catch (IOException ex) {
            // System.out.println("SH IOE");
        }
        this.outGoing.println("Test");
    }

    public static void main(String[] args) throws IOException {
        new SenderClient();
    }
}
