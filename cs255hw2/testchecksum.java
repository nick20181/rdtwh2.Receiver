package cs255hw2;
import java.io.*;
import java.util.*;
import java.util.zip.*;
/**
 *
 * @author Nicholas Bohm <your.name at your.org>
 */
public class testchecksum {
    
    
    public testchecksum(){
        File checkSumFile = new File("C:\\Users\\nick201\\Desktop\\checksumtest.txt");
        File checkSumFile1 = new File("C:\\Users\\nick201\\Desktop\\AddonPedsVars.xml");
        byte[] filebytes =  new byte[(int)checkSumFile.length()];
        byte[] filebytes1 =  new byte[(int)checkSumFile1.length()];
        Adler32 test = new Adler32();
           test.update(filebytes);
           System.out.println(test.getValue());
        //System.out.println(filebytes.length);
        //System.out.println(filebytes1.length);
        //System.out.println(checkSumFile.getName());
    }
    
    
}
