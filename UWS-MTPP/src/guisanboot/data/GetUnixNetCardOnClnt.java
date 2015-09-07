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
import java.util.*;
import java.util.regex.*;
import guisanboot.ui.*;

public class GetUnixNetCardOnClnt extends NetworkRunning {
    private UnixNetCard curCard = null;
    private Vector<UnixNetCard> cache = new Vector<UnixNetCard>();
    
    public void parser(String line)  {
        if( line == null || line.equals("") ) return;
SanBootView.log.debug(getClass().getName(),"========> "+ line );         
        try{
            String[] list = Pattern.compile(";").split( line,-1 );
            curCard = new UnixNetCard();
            curCard.netInterface = list[0];
            curCard.mac = list[1];
            for( int i=2; i<list.length; i++,i++){
                if( list[i].equals("") ) break;
                
                BindIPAndMask binder = new BindIPAndMask();
                binder.ip = list[i];
                try{
                    binder.mask = list[i+1];
                    curCard.ipList.addElement( binder );
                }catch(Exception ex){
                }
            }
            cache.addElement( curCard );
        }catch(Exception ex){
            curCard = null;
        }
    }

    public GetUnixNetCardOnClnt( String cmd,Socket socket ) throws IOException {
        super( cmd,socket );
    }
    
    public GetUnixNetCardOnClnt( String cmd ){
        super( cmd );
    }

    public boolean realDo(){
SanBootView.log.info( getClass().getName(), " get unix netcard (confile) cmd: "+ getCmdLine() ); 
        try{
            curCard = null;
            cache.removeAllElements();
            
            run();
        }catch(Exception ex){
            setExceptionErrMsg( ex );
            setExceptionRetCode( ex );
        }
SanBootView.log.info( getClass().getName(), " get unix netcard (confile) retcode: "+ getRetCode() ); 
        boolean isOk = ( getRetCode() == AbstractNetworkRunning.OK );
        if( !isOk ){
SanBootView.log.error( getClass().getName(), " get unix netcard (confile) errmsg: "+ getErrMsg() ); 
        }
        return isOk;
    }
    
    public ArrayList<UnixNetCard> getAllNetCard(){
        ArrayList<UnixNetCard> ret = new ArrayList<UnixNetCard>();
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            ret.add( cache.elementAt(i) );
        }
        
        return ret;
    }
    
    public String getAllContent( String mac ){
        UnixNetCard card;
        boolean isFirst = true;
        
        StringBuffer buf = new StringBuffer();
        int size = cache.size();
        for( int i=0; i<size; i++ ){
            card = cache.elementAt(i);
            if( isFirst ){
                buf.append( card.prtMe(  card.mac.toUpperCase().equals( mac.toUpperCase() ) ) );
                isFirst = false;
            }else{
                buf.append( "\n" + card.prtMe(  card.mac.toUpperCase().equals( mac.toUpperCase() ) ) );
            }
        }
        
        return buf.toString();
    }
}
