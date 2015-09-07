package guisanboot.data;

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
import java.util.ArrayList;
import java.util.regex.Pattern;
import mylib.tool.Check;

public class GetIScsiSessionObj extends NetworkRunning {
    private ArrayList<ISCSISessionObj> objs = new ArrayList<ISCSISessionObj>();
    private ISCSISessionObj curSession = null;
    private int lineNum = 0;
    
    public void parser( String line ){
        int tid=-1;
        
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1);
        if( s1 == null || s1.equals("") ) return;
        
        if( s1.startsWith("initiator-name") ||
            s1.startsWith("---------------")
        ){
            // do nothing
        }else{
            lineNum ++;
            
            try{
// iqn.1991-05.com.microsoft:vm-2003-cn-zjj 20.20.1.10      20.20.1.144     32777 rw         
                String[] list = Pattern.compile("\\s+").split( s1,-1 );
                curSession = new ISCSISessionObj();
                curSession.setInitor( list[0] );
                
                boolean isInitorIPOk = Check.ipCheck( list[1] );
                boolean isTgtIPOk = Check.ipCheck( list[2] );
                boolean isTidOk = true;
                try{
                    tid = Integer.parseInt( list[3] );
                }catch(Exception ex){
                    isTidOk = false;
                }
                
                if( isInitorIPOk && isTgtIPOk && isTidOk ){
                    curSession.setInitor_ip( list[1] );
                    curSession.setTgt_ip( list[2] );
                    curSession.setTid( tid );
                    curSession.setRight( list[4] );
                    objs.add( curSession );
SanBootView.log.debug(getClass().getName(),curSession.prtMe() );                     
                }
            }catch(Exception ex){
SanBootView.log.debug(getClass().getName(), ex.getMessage() );
            }
        }
    }

    public GetIScsiSessionObj(String cmd,Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetIScsiSessionObj( String cmd ){
        super( cmd );
    }

    public boolean getSessionListOk(){
        return ( objs.size() == lineNum );
    }
    
    public boolean updateSessionlist() {
SanBootView.log.info( getClass().getName()," get iscsi session cmd: "+this.getCmdLine());
        try{
            objs.clear();
            curSession = null;
            lineNum = 0;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," get iscsi session cmd retcode: "+getRetCode()  );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," get iscsi session cmd errmsg: "+getErrMsg()  );            
        }
        return isOk;
    }
    
    public ISCSISessionObj getFromCacheOnTid( int tid,String ip ){
        int size = objs.size();
        for( int i=0;i<size;i++ ){
            ISCSISessionObj obj = objs.get(i);
            if( ( obj.getTid() == tid ) && !obj.getInitor_ip().equals( ip ) ){
                return obj;
            }
        }
        
        return null;
    }
    
    public ArrayList<ISCSISessionObj> getAll(){
        int size = objs.size();
        ArrayList<ISCSISessionObj> ret  = new ArrayList<ISCSISessionObj>( size );
        for( int i=0;i<size;i++ ){
            ret.add( objs.get(i) );
        }
        return ret;
    }
}
