package guisanboot.data;

/**
 * Title:        Odysys backup system
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      odysys
 * @author
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import java.util.regex.Pattern;
import guisanboot.ui.SanBootView;

public class GetPoolInfo extends NetworkRunning {
    private long total = -1L;
    private long avail = -1L;
    private long vused = -1L;
    
    public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1 ); 
        
        try{
            String[] list = Pattern.compile("\\s+").split( s1,-1 );
            int indx = list[1].indexOf("="); // capacity=52386369536
            String totalStr = list[1].substring(indx+1);
            total = Long.parseLong( totalStr );
          
            indx = list[2].indexOf("="); // freecapaciy=48949469184
            String availStr = list[2].substring( indx+1 );
            avail= Long.parseLong( availStr);
            
            indx = list[3].indexOf("="); // visual_used=14176947584
            String vusedStr = list[3].substring( indx+1 );
            vused = Long.parseLong( vusedStr );
        }catch(Exception ex){
SanBootView.log.debug(getClass().getName(),ex.getMessage() );
        }
    }

    public GetPoolInfo(String cmd,Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetPoolInfo( String cmd ){
        super( cmd );
    }

    public boolean getPoolInfo( String cmdLine ) {
        setCmdLine( cmdLine );
SanBootView.log.info( getClass().getName(), " get poolinfo cmd: "+this.getCmdLine() );
        this.total = -1L;
        this.avail = -1L;
        this.vused = -1L;
        
        try{
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get poolinfo cmd retcode: "+getRetCode()); 
        
        if( getRetCode() == AbstractNetworkRunning.OK ){
            if( this.total == -1L || this.avail == -1L || this.vused == -1L ){
SanBootView.log.error( getClass().getName(), " get poolinfo cmd errmsg: " + getErrMsg() );                 
                return false;
            }else{
                return true;
            }
        }else{
SanBootView.log.error( getClass().getName(), " get poolinfo cmd errmsg: " + getErrMsg() );      
            return false;
        }
    }
    
    public long getTotalCap(){
        return total;
    }
    public long getAvailCap(){
        return avail;
    }
    public long getVUsed(){
        return vused;
    }
}
