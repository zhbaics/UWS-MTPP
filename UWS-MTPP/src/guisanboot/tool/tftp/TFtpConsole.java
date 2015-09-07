/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package guisanboot.tool.tftp;

import guisanboot.ui.SanBootView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import mylib.net.tftp.TFTP;
import mylib.net.tftp.TFTPClient;

/**
 *
 * @author Administrator
 */
public class TFtpConsole {
    private String server_ip;
    private String localFileName;
    private String remoteFileName;
    private TFTPClient tftp;
    private int timeout = 5*60;  // 超时时间
    private boolean isTimeOut = false;

    public TFtpConsole(
        String server_ip,
        String localFileName,
        String remoteFileName
    ){
        this.server_ip = server_ip;
        this.localFileName = localFileName;
        this.remoteFileName = remoteFileName;    
    }
    
    public boolean open(){
        // Create our TFTP instance to handle the file transfer.
        tftp = new TFTPClient(); 
        // We want to timeout if a response takes longer than 'timeout' seconds
        tftp.setDefaultTimeout( timeout * 1000 );
        
        try{
            tftp.open();
        }catch ( SocketException e ){
SanBootView.log.error( getClass().getName(),"could not open local UDP socket.");        
            return false;
        }
        
        return true;
    }
    
    public boolean recive(){
        boolean recOk = true;
        boolean closed = false;        
        FileOutputStream output = null;
        
        File localFile = new File( getLocalFileName() );
        // If file exists, don't overwrite it.
        if ( localFile.exists() ){
SanBootView.log.warning( getClass().getName(),getLocalFileName() + " already exists."  );
            if( !localFile.delete() ){
SanBootView.log.error( getClass().getName(),getLocalFileName() + " failed to delete file which already exists."  );
                return false;
            }
        }
        
        // Try to open local file for writing
        try{
            output = new FileOutputStream( localFile );
        }catch ( IOException e ){
            tftp.close();
SanBootView.log.error(getClass().getName(),"Could not open local file for writing:" + getLocalFileName()  );
            return false;
        }
        
        // Try to receive remote file via TFTP
        try{
SanBootView.log.info(getClass().getName(),"rec file from tftp server: "+ server_ip );            
            tftp.receiveFile( getRemoteFileName(), TFTP.BINARY_MODE, output, server_ip  );
        }catch ( UnknownHostException e ){
            recOk = false;
SanBootView.log.error( getClass().getName(),"Could not resolve hostname: "+ server_ip );
        }catch( SocketTimeoutException ex ){
            isTimeOut = true;
            recOk = false;
SanBootView.log.error(getClass().getName(),"SocketTimeoutException occured while receiving file: "+ getRemoteFileName() );
        }catch ( IOException e ){
            recOk = false;
SanBootView.log.error( getClass().getName(),"I/O exception occurred while receiving file: "+ getRemoteFileName()  );            
        }
        
        // Close local socket and output file
        tftp.close();
        try{
            output.close();
            closed = true;
        }catch ( IOException e ){
SanBootView.log.error( getClass().getName(),"Error closing file." );
            closed = false;
        }
        
        if (!closed ){
SanBootView.log.warning( getClass().getName(),"Error closing file." );
        }
        
        return recOk;
    }
    
    public boolean put(){
        boolean putOk = true;
        boolean closed = false;        
        // We're sending a file
        FileInputStream input = null;
        
        // Try to open local file for reading
        try{
            input = new FileInputStream( getLocalFileName() );
        }catch( IOException e ){
            tftp.close();
SanBootView.log.error(getClass().getName(),"Could not open local file for reading:" + getLocalFileName()  );
            return false;
        }
        
        // Try to send local file via TFTP
        try{
            tftp.sendFile( getRemoteFileName(), TFTP.BINARY_MODE, input,server_ip  );
        }catch( UnknownHostException e ){
            putOk = false;
SanBootView.log.error( getClass().getName(),"Could not resolve hostname: "+ server_ip );       
        }catch( SocketTimeoutException ex ){
            isTimeOut = true;
            putOk = false;
SanBootView.log.error(getClass().getName(),"SocketTimeoutException occured while putting file: "+ this.getRemoteFileName() );
        }catch( IOException e ){
            putOk = false;
SanBootView.log.error( getClass().getName(),"I/O exception occurred while putting file: " + this.getRemoteFileName()  );           
        }
        
        // Close local socket and input file
        tftp.close();
        try{
            input.close();
            closed = true;
        }catch (IOException e){
            closed = false;
SanBootView.log.error( getClass().getName(),"Error closing file." );
        }
        
        if( !closed ){
SanBootView.log.warning( getClass().getName(),"Error closing file." );
        }
        
        return putOk;
    }
    
    public void deleteLocalFile(){
        File localFile = new File( getLocalFileName() );
        if( localFile != null ){
            localFile.delete();
        }
    }

    public boolean isTimeout(){
        return this.isTimeOut;
    }

    public String getLocalFileName() {
        return localFileName;
    }

    public void setLocalFileName(String localFileName) {
        this.localFileName = localFileName;
    }

    public String getRemoteFileName() {
        return remoteFileName;
    }

    public void setRemoteFileName(String remoteFileName) {
        this.remoteFileName = remoteFileName;
    }
}
