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

public class GetIbootObjList extends NetworkRunning {
    private ArrayList<IBootObj> objs = new ArrayList<IBootObj>();
    private IBootObj curIbootObj = null;
    private boolean isStartup = false;
    
    public void parser( String line ){
        String s1 = line.trim();
SanBootView.log.debug(getClass().getName(),"========> "+ s1);
        
        if( s1.startsWith("ibenc table:") ||
            s1.startsWith("---------------")
        ){
            // do nothing
        }else if( s1.startsWith("#MAC_Address") ){
            isStartup = true;
        }else{
            try{
                String[] list = Pattern.compile("\\s+").split( s1,-1 );
                curIbootObj = new IBootObj();
                curIbootObj.mac = list[0];
                curIbootObj.ena = list[1];
                curIbootObj.hostIp = list[2];
                curIbootObj.hostName = list[3];
                curIbootObj.pip = list[4];
                curIbootObj.pport = list[5];
                curIbootObj.options = list[6];
                curIbootObj.rams = list[7];
                curIbootObj.unids = list[8];
                curIbootObj.uname = list[9];
                curIbootObj.password = list[10];
                curIbootObj.ini_name = list[11];
                curIbootObj.tgt_name = list[12];
                curIbootObj.living_days = list[13];
                objs.add( curIbootObj );
SanBootView.log.debug(getClass().getName(),curIbootObj.prtMe() );
            }catch(Exception ex){
SanBootView.log.debug(getClass().getName(), ex.getMessage() );
            }
        }
    }

    public GetIbootObjList(String cmd,Socket socket) throws IOException{
        super( cmd ,socket );
    }
  
    public GetIbootObjList( String cmd ){
        super( cmd );
    }

    public boolean updateIbootlist() {
SanBootView.log.info( getClass().getName()," get iboot list cmd: "+this.getCmdLine());
        try{
            objs.clear();
            curIbootObj = null;
            isStartup = false;
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName()," get iboot list cmd retcode: "+getRetCode()  );
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName()," get iboot list cmd errmsg: "+getErrMsg()  );            
        }
        return isOk;
    }
  
    public void addToCache(IBootObj obj){
        objs.add( obj );
    }

    public void removeFromCache(IBootObj obj){
        objs.remove( obj );
    }
    
    public IBootObj getFromVectorOnMac( String mac ){
        mac = mac.toUpperCase();
        int size = objs.size();
        for( int i=0;i<size;i++ ){
            IBootObj obj = objs.get(i);
            if( obj.mac.toUpperCase().equals( mac ) )
                return obj;
        }
        
        return null;
    }
    
    public boolean isStartIboot(){
        return isStartup;
    }
    
    public ArrayList<IBootObj> getAll(){
        int size = objs.size();
        ArrayList<IBootObj> ret  = new ArrayList<IBootObj>( size );
        for( int i=0;i<size;i++ ){
            ret.add( objs.get(i) );
        }
        return ret;
    }
}
