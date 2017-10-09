/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs255hw2.Sender;

import cs255hw2.Sender.*;
import cs255hw2.shared.*;

/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class SenderRewrite {

    public SenderRewrite() {
        String ip = "10.15.1.21";
        int srcPort = 4466;
        String fileName = "checksumtest.txt";
        int payLoadSize = 1004;

        ClientRewrite client = new ClientRewrite(ip, srcPort);

        DataHandlerRewrite dataHandler = new DataHandlerRewrite();
        dataHandler.setDirectory("C:\\Users\\nick201\\Desktop\\toSend");
        int fileSize = payLoadSize /(int)dataHandler.getFileToSend(fileName).length();
        for(int i = 0; i < fileSize; i++){
        byte[] currentPayload = dataHandler.FileToByte(fileName, payLoadSize);
        
        }
    }
}
