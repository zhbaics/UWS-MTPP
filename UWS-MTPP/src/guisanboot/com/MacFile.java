/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * @author zjj
 */
public class MacFile {
    
    public static ArrayList<String> macList(String filePath) throws FileNotFoundException, IOException{
        ArrayList macList = new ArrayList();
        BufferedReader br = new BufferedReader(new FileReader( filePath ));
        String data = br.readLine();
        while( data != null ){
            macList.add( data );
            data = br.readLine();
        }
        return macList;
    }

    public String createMacFile(String filePath){
        File file = new File( filePath );
        if( !file.exists() ){
            try {
                file.createNewFile();
            } catch (IOException ex){
                
            }
        }
        return filePath;
    }

    public void writeMac(String filePath, String mac){
        RandomAccessFile randomFile = null;
        try {
            randomFile = new RandomAccessFile(filePath, "rw");
            long fileLength =  randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes("\n" + mac);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(randomFile != null){
                try {
                    randomFile.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
