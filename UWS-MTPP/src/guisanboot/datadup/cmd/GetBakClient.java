package guisanboot.datadup.cmd;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import guisanboot.ui.SanBootView;
import java.io.*;
import java.net.*;
import java.util.Vector;
import guisanboot.datadup.data.BackupClient; 
import guisanboot.data.*;

public class GetBakClient extends NetworkRunning {
    private Vector<BackupClient> nodes = new Vector<BackupClient>();
    private BackupClient curClient = null;
    
    public void parser(String line){
SanBootView.log.debug( getClass().getName()," =====> "+ line);        
        String s1 = line.trim();
        int index = s1.indexOf("=");
        if( index>0){
          String value = s1.substring( index+1 ).trim();
          if( s1.startsWith( BackupClient.CLNTID) ){
            try{
                int id = Integer.parseInt( value );
                curClient.setID( id );
            }catch(Exception ex){
                curClient.setID( -1 );
            }
          }else if( s1.startsWith( BackupClient.CLNTNAME )){
            curClient.setHostName( value );
          }else if( s1.startsWith( BackupClient.CLNTIP)){
            curClient.setIP( value );
          }else if( s1.startsWith( BackupClient.CLNTMACHINE)){
            curClient.setMachineType( value );
          }else if(s1.startsWith( BackupClient.CLNTPORT )){
            try{
                int port = Integer.parseInt( value );
                curClient.setPort( port );
            }catch(Exception ex){
                curClient.setPort( -1 );
            }
          }else if(s1.startsWith( BackupClient.CLNTOS )){
              curClient.setOsType( value );
          }else if(s1.startsWith( BackupClient.CLNTSTATUS )){
              curClient.setStatus( value );
          }else if(s1.startsWith( BackupClient.CLNTUUID ) ){
              curClient.setUUID( value );
          }else if(s1.startsWith( BackupClient.CLNTACCT) ){
              try{
                  int uid = Integer.parseInt( value );
                  curClient.setAcctID( uid);
              }catch(Exception ex){
                  curClient.setAcctID( 0 );
              }
              nodes.addElement( curClient );
          }
        }else{
          if( s1.startsWith( BackupClient.CLNTRECFLAG )){
            curClient = new BackupClient();
          }
        }
    }
    
    public GetBakClient(String cmd,Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetBakClient( String cmd ){
        super( cmd );
    }

    public boolean updateClient( ) {
SanBootView.log.info( getClass().getName(), " get bak clnt cmd: " +this.getCmdLine()  );
        try{
            nodes.removeAllElements();
            curClient = null;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get bak clnt cmd retcode: " + getRetCode() );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get bak clnt cmd errmsg: " + getErrMsg() );            
        }
        return isOk;
    }
  
    public void addClientToVector(BackupClient cli){
        nodes.addElement( cli );
    }

    public void removeClientFromVector(BackupClient cli){
        nodes.removeElement( cli );
    }

    public BackupClient getClientFromVector( long id ){
        int size = nodes.size();
        for( int i=0;i<size;i++ ){
            BackupClient client = nodes.elementAt(i);
            if( client.getID() == id )
                return client;
        }
        
        return null;
    }

    public BackupClient getClientFromVectorOnID( String id ){
        int size = nodes.size();
        for( int i=0;i<size;i++ ){
            BackupClient client = nodes.elementAt(i);
            if( id.equals( client.getID()+"" ) )
                return client;
        }
        
        return null;
    }
    
    public BackupClient getClientFromVectorOnIP( String ip ){
        int size = nodes.size();
        for( int i=0;i<size;i++ ){
            BackupClient client = nodes.elementAt(i);
            if( client.getIP().equals( ip ) )
                return client;
        }
        
        return null;
    }
     
    public BackupClient getClientFromVectorOnName(String name){
        int size = nodes.size();
        for( int i=0;i<size;i++){
            BackupClient client = nodes.elementAt(i);
            if( client.getHostName().equals( name ) )
                return client;
        }
        return null;
    }

    public Vector<BackupClient> getAllBackupClient(){
        int size = nodes.size();
        Vector<BackupClient> list  = new Vector<BackupClient>( size );
        for( int i=0;i<size;i++ ){
            list.addElement( nodes.elementAt(i) );
        }
        return list;
    }
    
    public BackupClient getBkClntOnUUID( String uuid ){
        BackupClient client;
        
        int size = nodes.size();
        for( int i=0; i<size; i++ ){
            client = nodes.elementAt(i);
            if( client.getUUID().equals(uuid) ){
                return client;
            }
        }
        
        return null;
    }

    public BackupClient getBkClntOnUUIDAndIP( String uuid,String ip ){
        BackupClient client;

        int size = nodes.size();
        for( int i=0; i<size; i++ ){
            client = nodes.elementAt(i);
            if( client.getUUID().equals(uuid) && client.getIP().equals( ip ) ){
                return client;
            }
        }

        return null;
    }
    
    public BackupClient getBkClntForRestOriDisk( String ip,int port ){
        BackupClient client;
        
        int size = nodes.size();
        for( int i=0; i<size; i++ ){
            client = nodes.elementAt(i);
            if( client.isForRstOriDisk() ){
                if( client.getIP().equals(ip) && ( client.getPort() == port ) ){
                    return client;
                }
            }
        }
        
        return null;
    }
}
